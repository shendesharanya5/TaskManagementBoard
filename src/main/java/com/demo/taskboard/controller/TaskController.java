package com.demo.taskboard.controller;

import com.demo.taskboard.entity.Task;
import com.demo.taskboard.service.TaskService;

/**
 * REST controller exposing CRUD + search endpoints for Task.
 * v2: works with TaskDTO; each Task belongs to a Project.
 * v3: adds assignment, status/priority/dueDate, search/filter/pagination/sorting.
 */
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.createTask(taskDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskDTO> partialUpdateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.partialUpdateTask(id, taskDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * v3: Combined search + filter + pagination + sorting endpoint.
     * Example: GET /api/v1/tasks/search?keyword=login&status=IN_PROGRESS&priority=HIGH
     *          &projectId=1&assignedUserId=2&dueBefore=2026-12-31&page=0&size=10&sort=dueDate,asc
     */
    @GetMapping("/search")
    public ResponseEntity<Page<TaskDTO>> searchTasks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long assignedUserId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueBefore,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueAfter,
            Pageable pageable
    ) {
        return ResponseEntity.ok(taskService.searchTasks(
                keyword, status, priority, projectId, assignedUserId, dueBefore, dueAfter, pageable));
    }
}
