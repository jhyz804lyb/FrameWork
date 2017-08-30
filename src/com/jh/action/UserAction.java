package com.jh.action;

import com.jh.Interceptor.*;
import com.jh.entity.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("user")
public class UserAction
{
    @RequestMapping(value = "adduser")
    @Json
    public Match name(@Find Match match)
    {
        return match;
    }

    @RequestMapping(value = "MatchCount")
    @Json
    public List<MatchCount> getMatchCount(@Find List<MatchCount> matchs)
    {
        return matchs;
    }
}
