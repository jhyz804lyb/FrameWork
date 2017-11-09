package com.jh.action;

import com.Native.NativeInteface;
import com.jh.Interceptor.*;
import com.jh.entity.*;
import com.jh.utils.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("user")
public class UserAction
{
    File file;

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


    @RequestMapping(value = "findMatch")
    @Json
    public Match findMatch(@Find Match match)
    {
        return match;
    }

    @RequestMapping(value = "findMatchs")
    @Json
    @Page
    public List<Match> findMatch(@Find List<Match> matchs)
    {
        return matchs;
    }

    @RequestMapping(value = "add")
    @Json
    public User addMatch(@Add User matchs)
    {

        return matchs;
    }

    @RequestMapping(value = "file")
    @Json
    public MatchCount getMatchCount3(HttpServletRequest request)
    {
        System.out.print(request.getParameter("image"));
        String result = request.getParameter("image");
        result = result.substring(result.indexOf(",") + 1);
        System.out.print(result);
        Base64ImageUtil.GenerateImage(result, "D:\\Jquery插件\\temp\\1.jpg");
        return new MatchCount();
    }

    //   @RequestMapping(value = "initImage")
    @ResponseBody
    public Map<String, String> uplodaImage(String image)
    {
        Map<String, String> map = new HashMap<String, String>();
        NativeInteface inteface = new NativeInteface();
        if (StringUtils.isNull(image))
        {
            map.put("success", "false");
            map.put("msg", "入库失败，未得到图片");
            return map;
        }
        int faceNum = inteface.getFaceNum();
        image = image.substring(image.indexOf(",") + 1);
        Base64ImageUtil.GenerateImage(image, "D:\\Jquery插件\\temp\\1.jpg");
        inteface.FaceInitImage("D:\\Jquery插件\\temp\\1.jpg");
        int faceNum2 = inteface.getFaceNum();
        if (faceNum2 > faceNum)
        {
            map.put("success", "true");
            map.put("msg", "人脸入库成功！提示：现阶段一个人只能入库一张人脸图片。" + "当前采样人脸总数：" + faceNum2);
        }
        else
        {
            map.put("success", "false");
            map.put("msg", "人脸入库失败，人脸库中已经存在该人脸特征！");
        }
        return map;
    }

    @RequestMapping(value = "addFace")
    @Json
    public FaceInfo addFaceImg(@Add FaceInfo faceInfo) throws IOException
    {
        return faceInfo;
    }

    @RequestMapping(value = "excelFile")
    @ExportExcel(titleList = {"比赛名称","比赛时间","状态","主队","客队","结果"}
            ,fieldList = {"matchName","matchTime","state","homeTeam","guestTeam","result"},fileName = "框架导出测试")
    @Page
    public List<Match> exportFile(@Find List<Match> matchs)
    {
        return matchs;
    }
}
