package br.com.brunoochoa.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class MinhaPrimeiraController {

    @GetMapping("n")
    public String hello() {
        return "Hello, World!";
    }
}
