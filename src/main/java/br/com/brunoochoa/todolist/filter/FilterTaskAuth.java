package br.com.brunoochoa.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

        var autorizathion = request.getHeader("Authorization");
        var authEncoded = autorizathion.substring("Basic ".length()).trim();
        byte[] authDecode = Base64.getDecoder().decode(authEncoded);

        String auth = new String(authDecode);
        var usernamePassword = auth.split(":");
        var username = usernamePassword[0];
        var password = usernamePassword[1];
        var user = userRepository.findByUsername(username);
        System.out.println("User: " + user);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Usuario nao encontrado");
            System.out.println("Usuario nao existe");
            return;
        } else {
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.getWriter().write("Usuario existe");
            System.out.println("Usuario existe");
            return;
        }

        // filterChain.doFilter(request, response);
    }

}