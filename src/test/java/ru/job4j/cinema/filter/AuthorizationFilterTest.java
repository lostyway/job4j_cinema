package ru.job4j.cinema.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class AuthorizationFilterTest {
    private AuthorizationFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        filter = new AuthorizationFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        session = mock(HttpSession.class);
    }


    @Test
    public void whenUriIsPermittedThenDoFilterCalled() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/users/login");

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void whenUriIsNotPermittedThenRedirectToLogin() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/buyTicket");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getContextPath()).thenReturn("");

        filter.doFilter(request, response, chain);

        verify(response, times(1)).sendRedirect("/users/login");
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    public void whenUriIsNotPermittedButUserLoginIn() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/buyTicket");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new Object());

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }
}