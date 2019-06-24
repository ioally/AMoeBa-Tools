package com.ioally.amoeba.controller;

import com.ioally.amoeba.api.MenuApi;
import com.ioally.amoeba.dto.ResponseDto;
import com.ioally.amoeba.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping(value = MenuApi.PATH)
public class MenuController implements MenuApi {

    @Autowired
    private MenuService menuService;

    /**
     * 查询所有可用菜单
     *
     * @return 返回所有的菜单数据
     */
    @Override
    public ResponseDto allMenu() throws SQLException {
        return ResponseDto.newInstance(menuService.allMenu());
    }

    /**
     * 根据用户的权限查询指定的菜单
     *
     * @param param userName-用户名称（工号）
     * @return
     * @throws SQLException
     */
    @Override
    public ResponseDto getMenu(@RequestBody Map<String, String> param) throws SQLException {
        return ResponseDto.newInstance(menuService.getMenu(param.get("userName")));
    }
}
