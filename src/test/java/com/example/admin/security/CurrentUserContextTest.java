package com.example.admin.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CurrentUserContextTest {

    @AfterEach
    void tearDown() {
        CurrentUserContext.clear();
    }

    @Test
    void currentUserShouldHoldUserInfoAndPermissions() {
        CurrentUser currentUser = new CurrentUser(
                1L,
                "admin",
                List.of("sys:user:list", "sys:role:list")
        );

        assertThat(currentUser.userId()).isEqualTo(1L);
        assertThat(currentUser.username()).isEqualTo("admin");
        assertThat(currentUser.permissions()).containsExactly("sys:user:list", "sys:role:list");
    }

    @Test
    void contextShouldSetGetAndClearCurrentUser() {
        CurrentUser currentUser = new CurrentUser(
                1L,
                "admin",
                List.of("sys:user:list")
        );

        CurrentUserContext.set(currentUser);

        assertThat(CurrentUserContext.get()).isEqualTo(currentUser);

        CurrentUserContext.clear();

        assertThat(CurrentUserContext.get()).isNull();
    }
}
