package com.jh.vilidata;

import com.jh.entity.*;
import com.jh.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liyabin
 * @date 2017-08-31下午 7:45
 */
@Component
public class Vilidata
{
    @Autowired
    UserService userService;

    @Autowired
    FaceService faceService;

    public Vilidata()
    {
    }

    /**
     * 校验用户信息的的方法
     *
     * @param user
     * @return
     */
    public boolean checkUserInfo(Object user)
    {
        if (!(user instanceof User)) return false;
        User result = (User) user;
        boolean b = userService.checkUserName(result.getUsername());
        return b;
    }

    /**
     * 校验用户信息的的方法
     *
     * @param face
     * @return
     */
    public boolean checkFaceInfo(Object face)
    {
        if (!(face instanceof FaceInfo)) return false;
        FaceInfo result = (FaceInfo) face;
        return faceService.checkFace(result);
    }

    /**
     * 校验用户信息的的方法
     *
     * @param menu
     * @return
     */
    public boolean checkMenuInfo(Object menu)
    {
        if (!(menu instanceof Menu)) return false;
        Menu result = (Menu) menu;
        return !(result.getLevel() != 1 && result.getParentId() == null);
    }
}
