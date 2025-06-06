package br.com.brunoochoa.todolist.task;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("")
    public TaskModel create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        UUID idUser = (UUID) request.getAttribute("idUser");
        taskModel.setUserId(idUser);
        System.out.println("Request received: " + request.getAttribute("idUser"));
        return this.taskRepository.save(taskModel);
    }
}
