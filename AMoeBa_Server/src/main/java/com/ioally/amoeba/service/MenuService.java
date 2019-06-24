package com.ioally.amoeba.service;

import com.ioally.amoeba.dto.MenuInfoDto;

import java.sql.SQLException;
import java.util.List;

public interface MenuService {

    /**
     * 查询所有可用菜单
     *
     * @return 返回所有的菜单数据
     */
    List<MenuInfoDto> allMenu() throws SQLException;

    /**
     * 查询当前用户可以看到的菜单
     *
     * @param userName 用户名（工号）
     * @return
     * @throws SQLException
     */
    List<MenuInfoDto> getMenu(String userName) throws SQLException;

}
