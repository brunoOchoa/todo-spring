package br.com.brunoochoa.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.brunoochoa.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.equals("/tasks")) {

            var autorizathion = request.getHeader("Authorization");
            var authEncoded = autorizathion.substring("Basic ".length()).trim();
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);

            String auth = new String(authDecode);
            var usernamePassword = auth.split(":");
            var username = usernamePassword[0];
            var password = usernamePassword[1];
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            var user = userRepository.findByUsername(username);
            System.out.println("User: " + user);
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Usuario nao encontrado");
                System.out.println("Usuario nao existe");
                return;
            }

            var passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            if (!passwordVerified.verified) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Senha invalida");
                System.out.println("Senha invalida");
                return;
            }

            // Usuário e senha corretos
            request.setAttribute("idUser", user.getId());
            // filterChain.doFilter(request, response);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Usuario e senha validos, tarefa criada!");
            System.out.println("Usuario e senha validos, tarefa criada!");

        } else {
            filterChain.doFilter(request, response);
        }

    }

}