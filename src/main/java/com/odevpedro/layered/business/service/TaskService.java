package com.odevpedro.layered.business.service;

import com.odevpedro.layered.business.domain.Task;
import com.odevpedro.layered.persistence.entity.TaskEntity;
import com.odevpedro.layered.persistence.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    @Transactional
    public Task create(String title) {
        // regra simples (exemplo): não aceita vazio
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("title must not be blank");
        }

        TaskEntity saved = repository.save(TaskEntity.builder()
                .title(title.trim())
                .done(false)
                .build());

        return new Task(saved.getId(), saved.getTitle(), saved.isDone());
    }

    @Transactional(readOnly = true)
    public List<Task> list() {
        return repository.findAll().stream()
                .map(e -> new Task(e.getId(), e.getTitle(), e.isDone()))
                .toList();
    }
}
