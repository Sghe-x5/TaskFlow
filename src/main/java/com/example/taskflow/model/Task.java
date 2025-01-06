package com.example.taskflow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data // Аннотация Lombok для генерации геттеров, сеттеров и других методов
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    private String description;

    private boolean completed;

    @Column(nullable = false) // Поле username обязательно для каждой задачи
    private String username; // Имя пользователя, связанное с задачей
}
