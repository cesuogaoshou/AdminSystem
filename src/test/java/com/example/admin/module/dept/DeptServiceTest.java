package com.example.admin.module.dept;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.admin.common.BusinessException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
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

    @Test
    void createShouldInsertDeptWithDefaults() {
        DeptSaveRequest request = new DeptSaveRequest(
                null, "研发部", "研发负责人", "10000000003", null, null
        );

        deptService.create(request);

        verify(deptMapper).insert(org.mockito.ArgumentMatchers.argThat(dept ->
                dept.parentId().equals(0L)
                        && dept.name().equals("研发部")
                        && dept.leader().equals("研发负责人")
                        && dept.phone().equals("10000000003")
                        && dept.sortOrder().equals(0)
                        && dept.status().equals(1)
        ));
    }

    @Test
    void updateShouldUpdateDeptWhenDeptExists() {
        LocalDateTime now = LocalDateTime.now();
        Dept existing = new Dept(
                2L, 1L, "技术部", "CTO", "10000000001",
                2, 1, now, now
        );
        DeptSaveRequest request = new DeptSaveRequest(
                1L, "研发部", "研发负责人", "10000000003", 4, 1
        );
        when(deptMapper.findById(2L)).thenReturn(existing);

        deptService.update(2L, request);

        verify(deptMapper).findById(2L);
        verify(deptMapper).update(org.mockito.ArgumentMatchers.argThat(dept ->
                dept.id().equals(2L)
                        && dept.parentId().equals(1L)
                        && dept.name().equals("研发部")
                        && dept.leader().equals("研发负责人")
                        && dept.phone().equals("10000000003")
                        && dept.sortOrder().equals(4)
                        && dept.status().equals(1)
        ));
    }

    @Test
    void updateShouldThrowBusinessExceptionWhenDeptNotFound() {
        DeptSaveRequest request = new DeptSaveRequest(
                1L, "研发部", "研发负责人", "10000000003", 4, 1
        );
        when(deptMapper.findById(99L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.update(99L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("部门不存在");
    }

    @Test
    void updateShouldThrowBusinessExceptionWhenParentIsSelf() {
        DeptSaveRequest request = new DeptSaveRequest(
                2L, "研发部", "研发负责人", "10000000003", 4, 1
        );

        assertThatThrownBy(() -> deptService.update(2L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("上级部门不能是自己");
    }

    @Test
    void deleteShouldDeleteDeptWhenNoChildAndNoUser() {
        LocalDateTime now = LocalDateTime.now();
        Dept existing = new Dept(
                3L, 1L, "产品部", "CPO", "10000000002",
                3, 1, now, now
        );
        when(deptMapper.findById(3L)).thenReturn(existing);
        when(deptMapper.countByParentId(3L)).thenReturn(0);
        when(deptMapper.countUsersByDeptId(3L)).thenReturn(0);

        deptService.delete(3L);

        verify(deptMapper).deleteById(3L);
    }

    @Test
    void deleteShouldThrowBusinessExceptionWhenDeptNotFound() {
        when(deptMapper.findById(99L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.delete(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("部门不存在");
    }

    @Test
    void deleteShouldThrowBusinessExceptionWhenDeptHasChildren() {
        LocalDateTime now = LocalDateTime.now();
        Dept existing = new Dept(
                1L, 0L, "总公司", "CEO", "10000000000",
                1, 1, now, now
        );
        when(deptMapper.findById(1L)).thenReturn(existing);
        when(deptMapper.countByParentId(1L)).thenReturn(2);

        assertThatThrownBy(() -> deptService.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("存在子部门，不能删除");
    }

    @Test
    void deleteShouldThrowBusinessExceptionWhenDeptHasUsers() {
        LocalDateTime now = LocalDateTime.now();
        Dept existing = new Dept(
                2L, 1L, "技术部", "CTO", "10000000001",
                2, 1, now, now
        );
        when(deptMapper.findById(2L)).thenReturn(existing);
        when(deptMapper.countByParentId(2L)).thenReturn(0);
        when(deptMapper.countUsersByDeptId(2L)).thenReturn(1);

        assertThatThrownBy(() -> deptService.delete(2L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("部门下存在用户，不能删除");
    }
}