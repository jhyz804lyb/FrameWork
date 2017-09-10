package com.jh.action.test;

import com.jh.Interceptor.*;
import com.jh.entity.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author liyabin
 * @date 2017-08-21上午 11:42
 */
@Controller
@RequestMapping("log")
public class MatchAction {

    @RequestMapping("addlog")
    @Json
    public Log addLog(@Add Log log) {
    return log;
    }

    @RequestMapping("listas")
    @Json
    @Page
    public List<Log> addLog(@Find List<Log> log) {
    return log;
    }
}
