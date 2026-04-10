package com.workpulse.service;

import com.workpulse.dto.TaskRequest;
import com.workpulse.dto.TaskResponse;
import com.workpulse.model.AppUser;
import com.workpulse.model.Task;
import com.workpulse.model.TaskStatus;
import com.workpulse.repository.AppUserRepository;
import com.workpulse.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final AppUserRepository appUserRepository;

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedBy() != null ? task.getCreatedBy().getName() : "System",
                task.getAssignedTo() != null ? task.getAssignedTo().getName() : "Unassigned",
                task.getCreatedAt()
        );
    }

    public TaskResponse createTask(TaskRequest request, String email) {
        AppUser createdBy = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        AppUser assignee = appUserRepository.findById(request.getAssignedToId())
                .orElseThrow(() -> new RuntimeException("Assignee not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.DRAFT);
        task.setCreatedBy(createdBy);
        task.setAssignedTo(assignee);

        return mapToResponse(taskRepository.save(task));
    }

    public TaskResponse submitTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with TaskId " + taskId));

        if (task.getStatus() != TaskStatus.DRAFT && task.getStatus() != TaskStatus.REJECTED) {
            throw new RuntimeException("Only Draft or Rejected tasks can be submitted.");
        }
        task.setStatus(TaskStatus.SUBMITTED);
        return mapToResponse(taskRepository.save(task));
    }

    public List<TaskResponse> getTasksByUser(UUID userId) {
        return taskRepository.findByCreatedById(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteTask(UUID taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task with ID " + taskId + " not found.");
        }
        taskRepository.deleteById(taskId);
    }

    public TaskResponse approveTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found."));

        if (task.getStatus() != TaskStatus.SUBMITTED) {
            throw new RuntimeException("Only submitted tasks can be approved.");
        }
        task.setStatus(TaskStatus.APPROVED);
        return mapToResponse(taskRepository.save(task));
    }

    public TaskResponse rejectTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found."));

        if (task.getStatus() != TaskStatus.SUBMITTED) {
            throw new RuntimeException("Only submitted tasks can be rejected.");
        }
        task.setStatus(TaskStatus.REJECTED);
        return mapToResponse(taskRepository.save(task));
    }

    public TaskResponse completeTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found."));

        if (task.getStatus() != TaskStatus.APPROVED) {
            throw new RuntimeException("Task must be APPROVED before it can be completed.");
        }
        task.setStatus(TaskStatus.COMPLETED);
        return mapToResponse(taskRepository.save(task));
    }

    public List<TaskResponse> getPendingTask() {
        return taskRepository.findByStatus(TaskStatus.SUBMITTED)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<TaskResponse> getCompleteTask() {
        return taskRepository.findByStatus(TaskStatus.COMPLETED)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}