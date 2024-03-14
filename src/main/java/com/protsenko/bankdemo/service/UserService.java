package com.protsenko.bankdemo.service;

import com.protsenko.bankdemo.dto.request.UserFilterDto;
import com.protsenko.bankdemo.dto.request.UserRegisterDto;
import com.protsenko.bankdemo.dto.request.UserSendMoneyToUserDto;
import com.protsenko.bankdemo.entity.Email;
import com.protsenko.bankdemo.entity.PersonData;
import com.protsenko.bankdemo.entity.Phone;
import com.protsenko.bankdemo.entity.User;
import com.protsenko.bankdemo.exception.HttpCustomException;
import com.protsenko.bankdemo.repository.EmailRepository;
import com.protsenko.bankdemo.repository.PhoneRepository;
import com.protsenko.bankdemo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService
{
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final EmailRepository emailRepository;
    @Autowired
    @Qualifier("passwordEncoder")
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @PersistenceContext
    private final EntityManager entityManager;
    @Value("${bank.max_value_coeff}")
    private BigDecimal coeffMaxValue;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userWrapper = findUserByUsername(username);
        if(userWrapper.isEmpty())
            throw new UsernameNotFoundException(String.format("Пользователь с username = %s не найден", username));
        User user = userWrapper.get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), Collections.emptyList());
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(long id)
    {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username)
    {
        return userRepository.findUserByUsername(username);
    }

    @Transactional
    public User create(UserRegisterDto userRegisterDto)
    {
        //Свободен ли username
        Optional<User> userWrapper = userRepository.findUserByUsernameWithOptimisticForce(userRegisterDto.getUsername());
        userWrapper.ifPresent((u) -> {throw new HttpCustomException(HttpStatus.BAD_REQUEST, "username уже занят");});


        //Закреплен ли такой email
        Optional<Email> emailWrapper = emailRepository.findByEmail(userRegisterDto.getEmail());
        emailWrapper.ifPresent((e) -> {throw new HttpCustomException(HttpStatus.BAD_REQUEST, "email уже занят");});

        //Закреплен ли такой телефон
        Optional<Phone> phoneWrapper = phoneRepository.findByPhone(userRegisterDto.getPhone());
        phoneWrapper.ifPresent((p) -> {throw new HttpCustomException(HttpStatus.BAD_REQUEST, "телефон уже занят");});


        PersonData personData = new PersonData(userRegisterDto.getFirstName(),userRegisterDto.getLastName(),userRegisterDto.getSurname(), userRegisterDto.getBirthday());
        User user = User.builder()
                                    .username(userRegisterDto.getUsername())
                                    .password(bCryptPasswordEncoder.encode(userRegisterDto.getPassword()))
                                    .money(userRegisterDto.getMoney())
                                    .startMoneyOnDeposit(userRegisterDto.getMoney())
                                    .personData(personData)
                                    .build();
        Email email = new Email(userRegisterDto.getEmail(),user);
        Phone phone = new Phone(userRegisterDto.getPhone(),user);

        userRepository.save(user);
        emailRepository.save(email);
        phoneRepository.save(phone);
        return user;
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void addPhonesToUser(String[] phone, String username)
    {
        Optional<User> userWrapper = userRepository.findUserByUsernameWithOptimisticForce(username);
        userWrapper.orElseThrow(() -> new HttpCustomException(HttpStatus.BAD_REQUEST, String.format("Пользователь не найден")));
        Arrays.stream(phone).forEach(p ->
        {
            p = p.trim();
            Optional<Phone> wrapperPhone = phoneRepository.findByPhone(p);
            wrapperPhone.ifPresent(
                    phoneEntity -> {
                        throw new HttpCustomException(HttpStatus.BAD_REQUEST, String.format("Телефон %s уже занят",phoneEntity.getPhone()));
                    }
            );
            phoneRepository.save(new Phone(p, userWrapper.get()));
        });
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void addEmailsToUser(String[] emails, String username)
    {
        Optional<User> userWrapper = userRepository.findUserByUsernameWithOptimisticForce(username);
        userWrapper.orElseThrow(() -> new HttpCustomException(HttpStatus.BAD_REQUEST, String.format("Пользователь не найден")));
        Arrays.stream(emails).forEach(e ->
        {
            e = e.trim();
            Optional<Email> wrapperEmail = emailRepository.findByEmail(e);
            wrapperEmail.ifPresent(
                    emailEntity -> {
                        throw new HttpCustomException(HttpStatus.BAD_REQUEST, String.format("Email %s уже занят",emailEntity.getEmail()));
                    }
            );
            emailRepository.save(new Email(e, userWrapper.get()));
        });
    }

    @Transactional
    public void detachPhoneFromUser(String phone, String username)
    {
        phone = phone.trim();
        Optional<User> userWrapper = userRepository.findUserByUsernameWithOptimisticForce(username);
        userWrapper.orElseThrow(() -> new HttpCustomException(HttpStatus.BAD_REQUEST, String.format("Пользователь не найден")));

        Optional<Phone> phoneWrapper = phoneRepository.findByPhone(phone);
        final String p = phone;
        phoneWrapper.orElseThrow(() -> new HttpCustomException(HttpStatus.BAD_REQUEST, String.format("Телефон %s не найден", p)));

        //Если телефон закреплен за другим пользователем - ошибка
        if(!Objects.equals(phoneWrapper.get().getUser().getUsername(),username))
            throw new HttpCustomException(HttpStatus.BAD_REQUEST);

        if(userRepository.countPhoneFromUser(username) > 1)
            phoneRepository.delete(phoneWrapper.get());
        else
            throw new HttpCustomException(HttpStatus.BAD_REQUEST, "Нельзя отвязать последний телефон");
    }

    @Transactional
    public void detachEmailFromUser(String email, String username)
    {
        email = email.trim();
        Optional<User> userWrapper = userRepository.findUserByUsernameWithOptimisticForce(username);
        userWrapper.orElseThrow(() -> new HttpCustomException(HttpStatus.BAD_REQUEST, String.format("Пользователь не найден")));

        Optional<Email> emailWrapper = emailRepository.findByEmail(email);
        final String e = email;
        emailWrapper.orElseThrow(() -> new HttpCustomException(HttpStatus.BAD_REQUEST, String.format("Email %s не найден", e)));

        //Если email закреплен за другим пользователем - ошибка
        if(!Objects.equals(emailWrapper.get().getUser().getUsername(),username))
            throw new HttpCustomException(HttpStatus.BAD_REQUEST);

        if(userRepository.countEmailFromUser(username) > 1)
            emailRepository.delete(emailWrapper.get());
        else
            throw new HttpCustomException(HttpStatus.BAD_REQUEST, "Нельзя отвязать последний email");
    }

    @Transactional(readOnly = true)
    public List<User> searchUsers(UserFilterDto userFilterDto, int pageSize, int page)
    {
        var queryBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = queryBuilder.createQuery();
        Root<User> root = query.from(User.class);
        //Join<User,Phone> join1 = root.fetch(Phone.class);
        Predicate predicate = queryBuilder.isTrue(queryBuilder.literal(true)); // Predicate predicate = queryBuilder.conjunction(); - эквивалент?
        if(userFilterDto.getPhone() != null)
        {
            Join<User, Phone> phones = root.join("phones");
            queryBuilder.and(predicate = queryBuilder.like(phones.get("phone"),userFilterDto.getPhone()));
        }
        if(userFilterDto.getBirthday() != null)
        {
            queryBuilder.and(predicate = queryBuilder.and(predicate, queryBuilder.greaterThanOrEqualTo(root.join("personData").get("birthday"),userFilterDto.getBirthday())));
        }
        if(userFilterDto.getEmail() != null)
        {
            Join<User, Email> join = root.join("emails");
            queryBuilder.and(predicate, queryBuilder.like(queryBuilder.lower( join.get("email")), userFilterDto.getEmail().toLowerCase()));
        }

        query.where(predicate);
        query.select(root).distinct(true).orderBy(queryBuilder.asc(root.get("username")));

        Query typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(pageSize);
        typedQuery.setFirstResult((page - 1) * pageSize);
        List<User> users = typedQuery.getResultList();
        return users;
    }

    public List<String> findAllUsername()
    {
        return userRepository.getAllUsername();
    }

    @Transactional
    public void payForDeposit(String username, BigDecimal percent)
    {
        Optional<User> userWrapper = userRepository.findByUsernamePessimicticWrite(username);
        if(userWrapper.isEmpty())
            return;
        User user = userWrapper.get();
        BigDecimal newMoney = user.getMoney().multiply(percent.add(BigDecimal.ONE));
        if(newMoney.compareTo(user.getStartMoneyOnDeposit().multiply(coeffMaxValue)) > 0)
        {
            user.setMoney(user.getStartMoneyOnDeposit().multiply(coeffMaxValue));
        }
        else
        {
            user.setMoney(newMoney);
        }
    }

    @Transactional
    public void sendMoney(UserSendMoneyToUserDto userSendMoneyToUserDto)
    {
        Optional<User> fromUserWrapper = userRepository.findByUsernamePessimicticWrite(userSendMoneyToUserDto.from());
        Optional<User> destUserWrapper = userRepository.findByUsernamePessimicticWrite(userSendMoneyToUserDto.dest());

        if(fromUserWrapper.isEmpty() || destUserWrapper.isEmpty())
            throw new HttpCustomException(HttpStatus.BAD_REQUEST, "Пользователь не найден");

        if(fromUserWrapper.get().getMoney().compareTo(userSendMoneyToUserDto.money()) >= 0)
        {
            fromUserWrapper.get().setMoney(fromUserWrapper.get().getMoney().subtract(userSendMoneyToUserDto.money()));

            destUserWrapper.get().setMoney(destUserWrapper.get().getMoney().add(userSendMoneyToUserDto.money()));
        }

        fromUserWrapper.get().setMoney(fromUserWrapper.get().getMoney().add(userSendMoneyToUserDto.money()));
    }
}
