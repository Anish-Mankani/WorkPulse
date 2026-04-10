package com.workpulse.controller;

import com.workpulse.dto.TaskRequest;
import com.workpulse.dto.TaskResponse;
import com.workpulse.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(taskRequest, email));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/submit")
    public ResponseEntity <TaskResponse> submitTask (@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.submitTask(id));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity <List<TaskResponse>> getTaskByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity <List<TaskResponse>> getAllTask() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> deleteTaskByUser(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/approve")
    public ResponseEntity <TaskResponse> approveTask(@PathVariable UUID id) {
       TaskResponse approvedTask = taskService.approveTask(id);
        return ResponseEntity.ok(approvedTask);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/reject")
    public ResponseEntity <TaskResponse> rejectTask(@PathVariable UUID id) {
        TaskResponse rejectedTask = taskService.rejectTask(id);
        return ResponseEntity.ok(rejectedTask);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity <TaskResponse> completeTask(@PathVariable UUID id) {
        TaskResponse completedTask = taskService.completeTask(id);
        return ResponseEntity.ok(completedTask);
    }

    @GetMapping("/pending")
    public ResponseEntity <List<TaskResponse>> getPendingTask() {
       List<TaskResponse> pendingTasks = taskService.getPendingTask();
        return ResponseEntity.ok(pendingTasks);
    }

    @GetMapping("/completed")
    public ResponseEntity <List<TaskResponse>> getCompleteTask() {
        List<TaskResponse> completeTasks = taskService.getCompleteTask();
        return ResponseEntity.ok(completeTasks);
    }

}
