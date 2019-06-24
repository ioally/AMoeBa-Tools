package com.ioally.amoeba.service.impl;

import com.ioally.amoeba.dao.SqliteDao;
import com.ioally.amoeba.dto.MenuInfoDto;
import com.ioally.amoeba.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private SqliteDao sqliteDao;

    /**
     * 查询所有可用菜单
     *
     * @return 返回所有的菜单数据
     */
    @Override
    public List<MenuInfoDto> allMenu() throws SQLException {
        return sqliteDao.getMenuInfoDtos("0");
    }

    /**
     * 查询当前用户可以看到的菜单
     *
     * @param userName 用户名（工号）
     * @return
     * @throws SQLException
     */
    @Override
    public List<MenuInfoDto> getMenu(String userName) throws SQLException {
        if (StringUtils.isEmpty(userName)) {
            throw new RuntimeException("用户工号不能为空！");
        }
        String role = sqliteDao.getUserRole(userName);
        if (StringUtils.isNotEmpty(role)) {
            return sqliteDao.getMenuInfoDtos(role);
        }
        return null;
    }
}
