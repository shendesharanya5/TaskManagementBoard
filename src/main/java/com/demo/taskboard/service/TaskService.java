package com.taskboard.service;

import com.taskboard.dto.TaskDTO;
import com.taskboard.entity.TaskPriority;
import com.taskboard.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    TaskDTO createTask(TaskDTO taskDTO);

    List<TaskDTO> getAllTasks();

    TaskDTO getTaskById(Long id);

    TaskDTO updateTask(Long id, TaskDTO taskDTO);

    TaskDTO partialUpdateTask(Long id, TaskDTO taskDTO);

    void deleteTask(Long id);

    /**
     * v3: search/filter tasks with pagination and sorting.
     * Every filter argument is optional (nullable).
     */
    Page<TaskDTO> searchTasks(String keyword,
                               TaskStatus status,
                               TaskPriority priority,
                               Long projectId,
                               Long assignedUserId,
                               LocalDate dueBefore,
                               LocalDate dueAfter,
                               Pageable pageable);
}
