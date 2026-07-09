package com.demo.taskboard.service;

import com.demo.taskboard.entity.Task;
import com.demo.taskboard.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository repository;

    @Override
    public Task createTask(Task task) {
        return repository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Task updateTask(Task task) {
        return repository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        repository.deleteById(id);
    }
}
