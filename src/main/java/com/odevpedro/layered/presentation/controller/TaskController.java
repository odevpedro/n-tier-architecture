package com.odevpedro.layered.presentation.controller;

import com.odevpedro.layered.business.service.TaskService;
import com.odevpedro.layered.presentation.dto.CreateTaskRequest;
import com.odevpedro.layered.presentation.dto.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@RequestBody @Valid CreateTaskRequest request) {
        var task = service.create(request.title());
        return new TaskResponse(task.getId(), task.getTitle(), task.isDone());
    }

    @GetMapping
    public List<TaskResponse> list() {
        return service.list().stream()
                .map(t -> new TaskResponse(t.getId(), t.getTitle(), t.isDone()))
                .toList();
    }
}
