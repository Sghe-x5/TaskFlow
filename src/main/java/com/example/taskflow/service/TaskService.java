package com.example.taskflow.service;

import com.example.taskflow.model.Task;
import com.example.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;
import com.example.taskflow.exception.TaskNotFoundException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Получить список всех задач
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Получить задачу по ID
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));
    }

    // Создать новую задачу
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Обновить существующую задачу
    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setCompleted(updatedTask.isCompleted());
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found."));
    }

    // Удалить задачу по ID
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task with ID " + id + " not found.");
        }
        taskRepository.deleteById(id);
    }

    // Получить задачи по статусу выполнения
    public List<Task> getTasksByCompletion(boolean completed) {
        return taskRepository.findByCompleted(completed);
    }

    public List<Task> getTasksByUsername(String username) {
        return taskRepository.findByUsername(username);
    }

    public Task getTaskByIdAndUsername(Long id, String username) {
        return taskRepository.findByIdAndUsername(id, username)
                .orElseThrow(() -> new TaskNotFoundException("Task not found for user: " + username));
    }

    public void deleteTaskByIdAndUsername(Long id, String username) {
        Task task = getTaskByIdAndUsername(id, username);
        taskRepository.delete(task);
    }

}
