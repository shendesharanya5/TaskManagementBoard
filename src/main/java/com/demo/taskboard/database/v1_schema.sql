-- ================================================
-- Task Management Board - v1 Schema
-- ================================================
CREATE DATABASE IF NOT EXISTS task_management_board;
USE task_management_board;

CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME
);

-- Sample data
INSERT INTO tasks (title, description, completed, created_at, updated_at) VALUES
('Set up project repository', 'Initialize Git repo and Spring Boot project', TRUE, NOW(), NOW()),
('Design database schema', 'Create ER diagram for tasks/projects/users', FALSE, NOW(), NOW()),
('Implement Task CRUD API', 'Build REST endpoints for Task entity', FALSE, NOW(), NOW());
