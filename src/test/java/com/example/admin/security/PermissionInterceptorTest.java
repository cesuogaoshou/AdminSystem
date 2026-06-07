package com.example.admin.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class PermissionInterceptorTest {

    private final PermissionInterceptor permissionInterceptor = new PermissionInterceptor();

    @AfterEach
    void tearDown() {
        CurrentUserContext.clear();
    }

    @Test
    void preHandleShouldPassWhenHandlerIsNotMethod() throws Exception {
        boolean result = permissionInterceptor.preHandle(
                mock(HttpServletRequest.class),
                new MockHttpServletResponse(),
                new Object()
        );

        assertThat(result).isTrue();
    }

    @Test
    void preHandleShouldPassWhenMethodHasNoPermissionAnnotation() throws Exception {
        boolean result = permissionInterceptor.preHandle(
                mock(HttpServletRequest.class),
                new MockHttpServletResponse(),
                handlerMethod("noPermission")
        );

        assertThat(result).isTrue();
    }

    @Test
    void preHandleShouldPassWhenCurrentUserHasPermission() throws Exception {
        CurrentUserContext.set(new CurrentUser(
                1L,
                "admin",
                List.of("sys:user:list")
        ));

        boolean result = permissionInterceptor.preHandle(
                mock(HttpServletRequest.class),
                new MockHttpServletResponse(),
                handlerMethod("needUserListPermission")
        );

        assertThat(result).isTrue();
    }

    @Test
    void preHandleShouldRejectWhenCurrentUserMissing() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = permissionInterceptor.preHandle(
                mock(HttpServletRequest.class),
                response,
                handlerMethod("needUserListPermission")
        );

        assertThat(result).isFalse();
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentAsString()).contains("\"code\":403");
        assertThat(response.getContentAsString()).contains("无权限");
    }

    @Test
    void preHandleShouldRejectWhenCurrentUserLacksPermission() throws Exception {
        CurrentUserContext.set(new CurrentUser(
                1L,
                "admin",
                List.of("sys:role:list")
        ));
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = permissionInterceptor.preHandle(
                mock(HttpServletRequest.class),
                response,
                handlerMethod("needUserListPermission")
        );

        assertThat(result).isFalse();
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentAsString()).contains("\"code\":403");
        assertThat(response.getContentAsString()).contains("无权限");
    }

    private HandlerMethod handlerMethod(String methodName) throws NoSuchMethodException {
        Method method = TestController.class.getDeclaredMethod(methodName);
        return new HandlerMethod(new TestController(), method);
    }

    private static final class TestController {

        @SuppressWarnings("unused")
        void noPermission() {
        }

        @RequirePermission("sys:user:list")
        void needUserListPermission() {
        }
    }
}
