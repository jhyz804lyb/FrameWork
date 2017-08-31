package com.jh.action;

import com.jh.Interceptor.*;
import com.jh.entity.*;
import com.jh.utils.Base64ImageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserAction
{
    File file ;

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

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
    @RequestMapping(value = "MatchCount3")
    @Json
    public List<MatchCount> getMatchCount3(@Find List<MatchCount> matchs)
    {
        return matchs;
    }
    @RequestMapping(value = "file")
    @Json
    public MatchCount  getMatchCount3(HttpServletRequest request)
    {
        System.out.print(request.getParameter("image"));
        String result =request.getParameter("image");
        result = result.substring(result.indexOf(",")+1);
        System.out.print(result);
        Base64ImageUtil.GenerateImage(result,"D:\\Jquery插件\\temp\\1.jpg");
        return new  MatchCount();
    }
}
