package com.jh.vilidata;

import com.jh.entity.User;
import com.jh.service.UserService;
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
}
