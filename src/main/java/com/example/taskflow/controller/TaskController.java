package com.example.taskflow.controller;

import com.example.taskflow.model.Task;
import com.example.taskflow.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Получить все задачи текущего пользователя
    @GetMapping
    public List<Task> getAllTasks() {
        String currentUsername = getCurrentUsername();
        return taskService.getTasksByUsername(currentUsername);
    }

    // Получить задачу по ID текущего пользователя
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        String currentUsername = getCurrentUsername();
        return taskService.getTaskByIdAndUsername(id, currentUsername);
    }

    // Создать новую задачу для текущего пользователя
    @PostMapping
    public Task createTask(@Valid @RequestBody Task task) {
        String currentUsername = getCurrentUsername();
        task.setUsername(currentUsername); // Связь с пользователем
        return taskService.createTask(task);
    }

    // Обновить задачу текущего пользователя
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @Valid @RequestBody Task task) {
        String currentUsername = getCurrentUsername();
        task.setId(id);
        task.setUsername(currentUsername); // Убедимся, что задача связана с текущим пользователем
        return taskService.updateTask(id, task);
    }

    // Удалить задачу текущего пользователя
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        String currentUsername = getCurrentUsername();
        taskService.deleteTaskByIdAndUsername(id, currentUsername);
    }

    // Вспомогательный метод для получения текущего имени пользователя
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // Получение имени пользователя из токена
    }
}
