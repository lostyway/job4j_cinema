package ru.job4j.cinema.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class AuthorizationFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var uri = request.getRequestURI();
        if (isAlwaysPermitted(uri)) {
            chain.doFilter(request, response);
            return;
        }
        var userLoggedIn = request.getSession().getAttribute("user") != null;
        if (!userLoggedIn) {
            var loginPageUrl = request.getContextPath() + "/users/login";
            response.sendRedirect(loginPageUrl);
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isAlwaysPermitted(String uri) {
        return uri.endsWith("/users/register")
                || uri.endsWith("/users/login")
                || uri.startsWith("/js")
                || uri.startsWith("/css")
                || uri.startsWith("/files")
                || uri.startsWith("/photo")
                || uri.endsWith("/cinema/list")
                || uri.endsWith("/")
                || uri.endsWith("/index")
                || uri.startsWith("/cinema/schedule/sessionTimes")
                || uri.startsWith("/cinema/schedule/sessions_by_time")
                || uri.startsWith("/cinema/one/")
                || uri.startsWith("/cinema/schedule/sessionsByTime");
    }
}
