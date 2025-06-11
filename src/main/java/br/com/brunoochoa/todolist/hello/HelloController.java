package br.com.brunoochoa.todolist.hello;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(@AuthenticationPrincipal Jwt jwt) {
        String name = jwt.getClaimAsString("name");
        return "Hello, " + name + "!";
    }
}