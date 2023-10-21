package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.UserDto;
import ru.practicum.entity.User;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;

import javax.validation.ValidationException;
import java.util.List;

import static ru.practicum.exception.type.ExceptionType.EMAIL_ALREADY_EXISTS;
import static ru.practicum.exception.type.ExceptionType.USER_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public List<UserDto> findByIds(List<Long> ids) {
        log.info("searching for users with ids in: {}", ids);
        List<User> users = userRepository.findUsersByIdIn(ids);

        log.info("found {} users", users.size());
        return userMapper.mapToDtos(users);
    }

    @Override
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

        if (userRepository.findUserByEmailEquals(email).isPresent()) {
            throw new ValidationException(String.format(EMAIL_ALREADY_EXISTS.getValue(), email));
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

}
