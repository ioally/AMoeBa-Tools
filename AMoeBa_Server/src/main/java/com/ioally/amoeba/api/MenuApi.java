package com.ioally.amoeba.api;

import com.ioally.amoeba.dto.ResponseDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.SQLException;
import java.util.Map;

public interface MenuApi {
    String PATH = AMoeBaApi.PATH + "/menu";

    /**
     * 查询所有可用菜单
     *
     * @return 返回所有的菜单数据
     */
    @RequestMapping(path = "allMenu", method = {RequestMethod.POST, RequestMethod.GET})
    ResponseDto allMenu() throws SQLException;

    /**
     * 根据用户的权限查询指定的菜单
     *
     * @param param userName-用户名称（工号）
     * @return
     * @throws SQLException
     */
    @RequestMapping(path = "getMenu", method = {RequestMethod.POST})
    ResponseDto getMenu(Map<String, String> param) throws SQLException;

}
