package com.workpulse.repository;

import com.workpulse.model.Task;
import com.workpulse.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByCreatedById(UUID userId);
    List<Task> findByStatus(TaskStatus status);
    }
