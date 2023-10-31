package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserSubscriptionDto;
import ru.practicum.entity.User;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;

import javax.validation.ValidationException;
import java.util.List;

import static ru.practicum.exception.type.ExceptionType.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findByIds(List<Long> ids) {
        log.info("searching for users with ids in: {}", ids);
        List<User> users = userRepository.findByIdIn(ids);

        log.info("found {} users", users.size());
        return userMapper.mapToDtos(users);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> find(Integer from, Integer size) {
        log.info("searching for users from = {}, size = {}", from, size);
        int page = from != 0 ? from / size : from;
        Pageable pageable = PageRequest.of(page, size);
        List<User> users = userRepository.findAll(pageable).getContent();

        return userMapper.mapToDtos(users);
    }

    @Override
    public UserDto create(UserDto dto) {
        String email = dto.getEmail();

        if (userRepository.findByEmailEquals(email).isPresent()) {
            throw new IllegalStateException(String.format(EMAIL_ALREADY_EXISTS.getValue(), email));
        }
        User user = userRepository.save(userMapper.mapToEntity(dto));

        log.info("new user has been created. id = {}, email = {}", user.getId(), user.getEmail());
        return userMapper.mapToDto(user);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.getValue(), id)));

        userRepository.delete(user);
        log.info("user with id = {} has been deleted", id);
    }

    @Override
    public UserSubscriptionDto subscribe(Long subscriberId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.getValue(), userId)));
        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.getValue(), subscriberId)));

        if (userRepository.findByIdAndSubscriptionsContaining(subscriberId, user).isPresent()) {
            throw new ValidationException(String.format(ALREADY_SUBSCRIBED.getValue(), subscriberId, userId));
        }
        subscriber.getSubscriptions().add(user);
        User saved = userRepository.save(subscriber);

        user.getSubscribers().add(subscriber);
        userRepository.save(user);

        log.info("userId = {} is now subscribed on userId = {}", subscriberId, userId);
        return userMapper.mapToSubscriptionDto(saved);
    }

    @Override
    public UserSubscriptionDto unsubscribe(Long subscriberId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.getValue(), userId)));
        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.getValue(), subscriberId)));

        if (userRepository.findByIdAndSubscriptionsContaining(subscriberId, user).isEmpty()) {
            throw new ValidationException(String.format(NOT_SUBSCRIBED.getValue(), subscriberId, userId));
        }
        subscriber.removeSubscription(user);
        User saved = userRepository.save(subscriber);

        log.info("userId = {} has unsubscribed from userId = {}", subscriberId, userId);
        return userMapper.mapToSubscriptionDto(saved);
    }

}
