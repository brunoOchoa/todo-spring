package br.com.brunoochoa.todolist.task;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("")
    public TaskModel create(@RequestBody TaskModel taskModel, @AuthenticationPrincipal Jwt jwt) {
        // Extrai claims do JWT
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");
        System.out.println("Usuário do JWT: " + username);
        System.out.println("Email do JWT: " + email);
        System.out.println("Nome do JWT: " + name);
        // Não existe senha no JWT, apenas claims públicas
        // Se quiser associar o userId, pode usar o username/email para buscar no banco
        taskModel.setSlayer(username);
        return this.taskRepository.save(taskModel);
    }
}
