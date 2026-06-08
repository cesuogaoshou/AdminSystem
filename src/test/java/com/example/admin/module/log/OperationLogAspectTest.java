package com.example.admin.module.log;

import com.example.admin.security.CurrentUser;
import com.example.admin.security.CurrentUserContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationLogAspectTest {

    @Mock
    private LogPublisher logPublisher;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @AfterEach
    void tearDown() {
        CurrentUserContext.clear();
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void aroundShouldSaveSuccessOperationLog() throws Throwable {
        CurrentUserContext.set(new CurrentUser(1L, "admin", List.of("sys:user:add")));
        bindRequest("POST", "/api/users", "127.0.0.1");
        OperationLog operationLog = operationLog("createUser");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"test_user"});
        when(joinPoint.proceed()).thenReturn("ok");
        OperationLogAspect aspect = new OperationLogAspect(logPublisher);

        Object result = aspect.around(joinPoint, operationLog);

        assertThat(result).isEqualTo("ok");

        ArgumentCaptor<SysLog> captor = ArgumentCaptor.forClass(SysLog.class);
        verify(logPublisher).publish(captor.capture());
        SysLog savedLog = captor.getValue();
        assertThat(savedLog.username()).isEqualTo("admin");
        assertThat(savedLog.module()).isEqualTo("用户管理");
        assertThat(savedLog.operation()).isEqualTo("新增用户");
        assertThat(savedLog.method()).isEqualTo("POST");
        assertThat(savedLog.requestUrl()).isEqualTo("/api/users");
        assertThat(savedLog.requestIp()).isEqualTo("127.0.0.1");
        assertThat(savedLog.params()).contains("test_user");
        assertThat(savedLog.result()).contains("ok");
        assertThat(savedLog.duration()).isNotNegative();
        assertThat(savedLog.status()).isOne();
        assertThat(savedLog.errorMsg()).isNull();
    }

    @Test
    void aroundShouldSaveFailedOperationLogAndRethrowException() throws Throwable {
        CurrentUserContext.set(new CurrentUser(1L, "admin", List.of("sys:user:add")));
        bindRequest("POST", "/api/users", "127.0.0.1");
        OperationLog operationLog = operationLog("createUser");
        IllegalStateException exception = new IllegalStateException("create failed");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"test_user"});
        when(joinPoint.proceed()).thenThrow(exception);
        OperationLogAspect aspect = new OperationLogAspect(logPublisher);

        assertThatThrownBy(() -> aspect.around(joinPoint, operationLog))
                .isSameAs(exception);

        ArgumentCaptor<SysLog> captor = ArgumentCaptor.forClass(SysLog.class);
        verify(logPublisher).publish(captor.capture());
        SysLog savedLog = captor.getValue();
        assertThat(savedLog.status()).isZero();
        assertThat(savedLog.errorMsg()).isEqualTo("create failed");
        assertThat(savedLog.result()).isNull();
    }

    private void bindRequest(String method, String requestUri, String remoteAddr) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, requestUri);
        request.setRemoteAddr(remoteAddr);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    private OperationLog operationLog(String methodName) throws NoSuchMethodException {
        return method(methodName).getAnnotation(OperationLog.class);
    }

    private Method method(String methodName) throws NoSuchMethodException {
        return TestController.class.getDeclaredMethod(methodName, String.class);
    }

    private static final class TestController {

        @OperationLog(module = "用户管理", operation = "新增用户")
        void createUser(String username) {
        }
    }
}
