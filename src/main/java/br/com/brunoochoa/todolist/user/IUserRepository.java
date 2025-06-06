package br.com.brunoochoa.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {

    UserModel findByEmail(String email);

    UserModel findByUsername(String username);

}
