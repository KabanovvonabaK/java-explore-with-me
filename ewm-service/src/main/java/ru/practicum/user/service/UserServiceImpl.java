package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserDto create(UserDto userDto) {
        log.info("UserRepository.class create() {}", userDto);
        return UserMapper.objectToDto(repository.save(UserMapper.dtoToObject(userDto)));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size) {
        log.info("UserRepository.class getUsers() ids {}, from {}, size {}", ids, from, size);
        if (from < 0 || size <= 0) {
            throw new BadRequestException("from should be equal or greater 0, size should be greater than 0");
        }
        if (ids == null || ids.isEmpty()) {
            return repository.findAll(PageRequest.of(from / size, size))
                    .stream()
                    .map(UserMapper::objectToDto)
                    .collect(Collectors.toList());
        } else {
            return repository.findAllByIdIn(ids, PageRequest.of(from / size, size))
                    .stream()
                    .map(UserMapper::objectToDto)
                    .collect(Collectors.toList());
        }
    }

    public void delete(int id) {
        log.info("UserRepository.class delete() id {}", id);
        findUserById(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public User findUserById(int id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("No user with id %d", id)));
    }
}