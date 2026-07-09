package com.taskboard.repository;

import com.taskboard.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for Task entity.
 * v3: extends JpaSpecificationExecutor to support dynamic search/filter queries
 * combined with pagination and sorting.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
}
