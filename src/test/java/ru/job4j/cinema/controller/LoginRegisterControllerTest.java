package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.cinema.exceptions.NotFoundException;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LoginRegisterControllerTest {
    private LoginRegisterController controller;
    private UserService userService;
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ConcurrentModel();
        userService = mock(UserService.class);
        controller = new LoginRegisterController(userService);
    }

    @Test
    public void whenGetLoginPageThanGetLoginPage() {
        var view = controller.getLoginPage();
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenGetRegisterPageThanGetRegisterPage() {
        var view = controller.getRegisterPage();
        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenGetLogOutPageThanGetLoginPageAndLogout() {
        HttpSession session = mock(HttpSession.class);
        var view = controller.makeLogout(session);
        verify(session).invalidate();
        assertThat(view).isEqualTo("redirect:/users/login");
    }

    @Test
    public void whenGetLogOutPageThanGetLoginPageAndLogoutRealMock() {
        MockHttpSession session = new MockHttpSession();
        var view = controller.makeLogout(session);
        assertThat(session.isInvalid()).isTrue();
        assertThat(view).isEqualTo("redirect:/users/login");
    }

    @Nested
    class LoginByEmailAndPasswordAndRegisterMethodsTest {
        private HttpServletRequest request;
        private User user;

        @BeforeEach
        public void setUp() {
            user = new User(1, "Yuri", "test@gmail.com", "password");
            request = new MockHttpServletRequest();
        }

        @Test
        public void whenLoginByEmailAndPasswordIsSuccessThanGetIndexPage() {
            when(userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(user);

            var view = controller.loginByEmailAndPassword(user, model, request);

            assertThat(request.getSession().getAttribute("user")).isEqualTo(user);
            assertThat(request.getSession().getAttribute("userId")).isEqualTo(user.getId());
            assertThat(view).isEqualTo("redirect:/index");
        }

        @Test
        public void whenLoginByEmailAndPasswordIsFailedByGlobalExceptionThanGetErrorPageAndMessage() {
            when(userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword())).thenThrow(new RuntimeException());

            var view = controller.loginByEmailAndPassword(user, model, request);

            assertThat(request.getSession().getAttribute("user")).isNull();
            assertThat(request.getSession().getAttribute("userId")).isNull();
            assertThat(model.getAttribute("error")).isEqualTo("Произошла непредвиденная ошибка. Попробуйте позже");
            assertThat(view).isEqualTo("errors/404");
        }

        @Test
        public void whenLoginByEmailAndPasswordIsFailedByNotFoundExceptionThanGetErrorPageAndMessage() {
            when(userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword())).thenThrow(new NotFoundException("Почта или пароль введены неверно"));

            var view = controller.loginByEmailAndPassword(user, model, request);

            assertThat(request.getSession().getAttribute("user")).isNull();
            assertThat(request.getSession().getAttribute("userId")).isNull();
            assertThat(model.getAttribute("error")).isEqualTo("Почта или пароль введены неверно");
            assertThat(view).isEqualTo("users/login");
        }

        @Test
        public void whenRegisterNewUserIsSuccessThanGetLoginPage() {
            when(userService.save(user)).thenReturn(user);

            var view = controller.registerNewUser(user, model);

            assertThat(view).isEqualTo("redirect:/users/login");
        }

        @Test
        public void whenRegisterNewUserIsFailedByGlobalExceptionThanGetErrorPageAndMessage() {
            when(userService.save(user)).thenThrow(new RuntimeException());

            var view = controller.registerNewUser(user, model);

            assertThat(view).isEqualTo("errors/404");
            assertThat(model.getAttribute("error")).isEqualTo("Произошла непредвиденная ошибка. Попробуйте позже");
        }

        @Test
        public void whenRegisterNewUserIsFailedByIllegalArgumentExceptionThanGetErrorPageAndMessage() {
            when(userService.save(user)).thenThrow(new IllegalArgumentException(String.format("Пользователь с почтой %s уже существует", user.getEmail())));

            var view = controller.registerNewUser(user, model);

            assertThat(view).isEqualTo("errors/404");
            assertThat(model.getAttribute("error")).isEqualTo(String.format("Пользователь с почтой %s уже существует", user.getEmail()));
        }
    }
}