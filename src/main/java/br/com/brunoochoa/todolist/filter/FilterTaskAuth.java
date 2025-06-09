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
            System.out.println("FilterTaskAuth: /tasks endpoint accessed");
            var authorization = request.getHeader("Authorization");
            System.out.println("Authorization header: " + authorization);
            if (authorization == null || !authorization.startsWith("Basic ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authorization header missing or invalid");
                return;
            }
            try {
                var authEncoded = authorization.substring("Basic ".length()).trim();
                byte[] authDecode = Base64.getDecoder().decode(authEncoded);
                String auth = new String(authDecode);
                var usernamePassword = auth.split(":");
                if (usernamePassword.length != 2) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Invalid auth format");
                    return;
                }
                var username = usernamePassword[0];
                var password = usernamePassword[1];
                System.out.println("Username: " + username);
                var user = userRepository.findByUsername(username);
                if (user == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("Usuario nao encontrado");
                    return;
                }
                var passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (!passwordVerified.verified) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Senha invalida");
                    return;
                }
                request.setAttribute("idUser", user.getId());
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Erro ao processar autenticação");
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }

}