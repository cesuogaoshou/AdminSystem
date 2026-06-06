package com.example.admin.module.dept;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeptServiceTest {

    @Mock
    private DeptMapper deptMapper;

    @InjectMocks
    private DeptService deptService;

    @Test
    void treeShouldBuildDeptHierarchy() {
        LocalDateTime now = LocalDateTime.now();
        List<Dept> depts = List.of(
                new Dept(1L, 0L, "总公司", "CEO", "10000000000", 1, 1, now, now),
                new Dept(2L, 1L, "技术部", "CTO", "10000000001", 2, 1, now, now),
                new Dept(3L, 1L, "产品部", "CPO", "10000000002", 3, 1, now, now)
        );
        when(deptMapper.findAll()).thenReturn(depts);

        List<DeptVO> tree = deptService.tree();

        assertThat(tree).hasSize(1);
        assertThat(tree.getFirst().name()).isEqualTo("总公司");
        assertThat(tree.getFirst().children())
                .extracting(DeptVO::name)
                .containsExactly("技术部", "产品部");
    }

    @Test
    void treeShouldReturnEmptyListWhenNoDeptExists() {
        when(deptMapper.findAll()).thenReturn(List.of());

        List<DeptVO> tree = deptService.tree();

        assertThat(tree).isEmpty();
    }
}