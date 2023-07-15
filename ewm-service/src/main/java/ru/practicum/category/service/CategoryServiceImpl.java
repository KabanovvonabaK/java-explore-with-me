package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    public CategoryDto create(CategoryDto dto) {
        log.info("CategoryService.class create() {}", dto);
        return CategoryMapper.toDto(repository.save(CategoryMapper.toObject(dto)));
    }

    public CategoryDto update(CategoryDto dto, int id) {
        log.info("CategoryService.class update() id {} with {}", id, dto);
        Category category = CategoryMapper.toObject(findById(id));
        category.setName(dto.getName());
        return CategoryMapper.toDto(repository.save(category));
    }

    public void delete(int id) {
        log.info("CategoryService.class delete() id {}", id);
        findById(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CategoryDto findById(int id) {
        log.info("CategoryService.class findById() id {}", id);
        return CategoryMapper.toDto(repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("No category with id %d", id))));
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAll(Integer from, Integer size) {
        log.info("CategoryService.class getAll() from {} size {}", from, size);
        return repository.findAll(PageRequest.of(from / size, size))
                .stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}