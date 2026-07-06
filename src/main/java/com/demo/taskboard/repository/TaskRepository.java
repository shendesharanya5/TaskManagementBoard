package com.demo.taskboard.repository;

import com.demo.taskboard.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public class TaskRepository {
    public interface TaskRepository
            extends JpaRepository<Task,Long> {

    }
}
