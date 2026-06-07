package com.example.admin.module.menu;

import com.example.admin.common.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final MenuMapper menuMapper;

    public MenuService(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    public List<MenuVO> tree() {
        List<Menu> menus = menuMapper.findAll();

        return menus.stream()
                .filter(menu -> menu.parentId() == 0)
                .map(menu -> toTreeNode(menu, menus))
                .toList();
    }

    public void create(MenuSaveRequest request) {
        Menu menu = new Menu(
                null,
                request.parentId() == null ? 0L : request.parentId(),
                request.name(),
                request.type(),
                request.path(),
                request.component(),
                request.permission(),
                request.icon(),
                request.sortOrder() == null ? 0 : request.sortOrder(),
                request.visible() == null ? 1 : request.visible(),
                null,
                null
        );

        menuMapper.insert(menu);
    }

    public void update(Long id, MenuSaveRequest request) {
        if (id.equals(request.parentId())) {
            throw new BusinessException(400, "上级菜单不能是自己");
        }

        Menu existing = menuMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "菜单不存在");
        }

        Menu menu = new Menu(
                existing.id(),
                request.parentId() == null ? 0L : request.parentId(),
                request.name(),
                request.type(),
                request.path(),
                request.component(),
                request.permission(),
                request.icon(),
                request.sortOrder() == null ? existing.sortOrder() : request.sortOrder(),
                request.visible() == null ? existing.visible() : request.visible(),
                existing.createTime(),
                existing.updateTime()
        );

        menuMapper.update(menu);
    }

    public void delete(Long id) {
        Menu existing = menuMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "菜单不存在");
        }

        if (menuMapper.countByParentId(id) > 0) {
            throw new BusinessException(400, "存在子菜单，不能删除");
        }

        if (menuMapper.countRolesByMenuId(id) > 0) {
            throw new BusinessException(400, "菜单已分配给角色，不能删除");
        }

        menuMapper.deleteById(id);
    }

    private MenuVO toTreeNode(Menu menu, List<Menu> allMenus) {
        List<MenuVO> children = allMenus.stream()
                .filter(child -> menu.id().equals(child.parentId()))
                .map(child -> toTreeNode(child, allMenus))
                .toList();

        return new MenuVO(
                menu.id(),
                menu.parentId(),
                menu.name(),
                menu.type(),
                menu.path(),
                menu.component(),
                menu.permission(),
                menu.icon(),
                menu.sortOrder(),
                menu.visible(),
                children
        );
    }
}
