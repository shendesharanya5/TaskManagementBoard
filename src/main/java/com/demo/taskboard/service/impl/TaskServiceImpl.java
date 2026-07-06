package com.taskboard.service.impl;

import com.taskboard.dto.TaskDTO;
import com.taskboard.entity.Project;
import com.taskboard.entity.Task;
import com.taskboard.entity.TaskPriority;
import com.taskboard.entity.TaskStatus;
import com.taskboard.entity.User;
import com.taskboard.exception.ResourceNotFoundException;
import com.taskboard.mapper.TaskMapper;
import com.taskboard.repository.ProjectRepository;
import com.taskboard.repository.TaskRepository;
import com.taskboard.repository.UserRepository;
import com.taskboard.service.TaskService;
import com.taskboard.util.TaskSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Task service implementation.
 * v2: works with TaskDTO and resolves the associated Project by id.
 * v3: also resolves assignedUser, and supports search/filter/pagination/sorting.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Project project = findProjectOrThrow(taskDTO.getProjectId());
        User assignedUser = resolveUser(taskDTO.getAssignedUserId());
        Task task = taskMapper.toEntity(taskDTO, project, assignedUser);
        Task saved = taskRepository.save(task);
        log.info("Task [{}] created in Project [{}]", saved.getId(), project.getId());
        return taskMapper.toDTO(saved);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        return taskMapper.toDTO(findTaskOrThrow(id));
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task existing = findTaskOrThrow(id);
        Project project = findProjectOrThrow(taskDTO.getProjectId());
        existing.setTitle(taskDTO.getTitle());
        existing.setDescription(taskDTO.getDescription());
        existing.setCompleted(taskDTO.getCompleted());
        existing.setProject(project);
        existing.setAssignedUser(resolveUser(taskDTO.getAssignedUserId()));
        if (taskDTO.getStatus() != null) existing.setStatus(taskDTO.getStatus());
        if (taskDTO.getPriority() != null) existing.setPriority(taskDTO.getPriority());
        existing.setDueDate(taskDTO.getDueDate());
        return taskMapper.toDTO(taskRepository.save(existing));
    }

    @Override
    public TaskDTO partialUpdateTask(Long id, TaskDTO taskDTO) {
        Task existing = findTaskOrThrow(id);
        if (taskDTO.getTitle() != null) existing.setTitle(taskDTO.getTitle());
        if (taskDTO.getDescription() != null) existing.setDescription(taskDTO.getDescription());
        if (taskDTO.getCompleted() != null) existing.setCompleted(taskDTO.getCompleted());
        if (taskDTO.getProjectId() != null) existing.setProject(findProjectOrThrow(taskDTO.getProjectId()));
        if (taskDTO.getAssignedUserId() != null) existing.setAssignedUser(resolveUser(taskDTO.getAssignedUserId()));
        if (taskDTO.getStatus() != null) existing.setStatus(taskDTO.getStatus());
        if (taskDTO.getPriority() != null) existing.setPriority(taskDTO.getPriority());
        if (taskDTO.getDueDate() != null) existing.setDueDate(taskDTO.getDueDate());
        return taskMapper.toDTO(taskRepository.save(existing));
    }

    @Override
    public void deleteTask(Long id) {
        Task existing = findTaskOrThrow(id);
        taskRepository.delete(existing);
        log.info("Task [{}] deleted", id);
    }

    @Override
    public Page<TaskDTO> searchTasks(String keyword, TaskStatus status, TaskPriority priority,
                                      Long projectId, Long assignedUserId,
                                      LocalDate dueBefore, LocalDate dueAfter, Pageable pageable) {
        var spec = TaskSpecification.filterBy(keyword, status, priority, projectId, assignedUserId, dueBefore, dueAfter);
        return taskRepository.findAll(spec, pageable).map(taskMapper::toDTO);
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    private Project findProjectOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
    }

    private User resolveUser(Long userId) {
        if (userId == null) return null;
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
}
