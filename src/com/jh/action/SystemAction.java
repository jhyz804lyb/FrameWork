package com.jh.action;

import com.jh.Interceptor.*;
import com.jh.entity.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author liyabin
 * @date 2017/11/16
 */
@Controller
@RequestMapping("system")
public class SystemAction
{
    public SystemAction()
    {
    }

    @RequestMapping("addMenu")
    @Json
    public Menu addMenu(@Add Menu menu)
    {
        return menu;
    }

    @RequestMapping("menuList")
    public String getMenus(@Find List<Menu> menus,HttpServletRequest request)
    {
        request.setAttribute("menus",menus);
        return "/UIFrameWork/common/menu";
    }
}
