package com.jh.action;

import com.Native.NativeInteface;
import com.jh.Interceptor.*;
import com.jh.entity.*;
import com.jh.utils.*;
import faceDemo.FaceUtil;
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


    // @RequestMapping(value = "CompareFaceImage")
    @ResponseBody
    public Map<String, String> CompareFace(String image)
    {
        Map<String, String> map = new HashMap<String, String>();
        NativeInteface inteface = new NativeInteface();
        if (StringUtils.isNull(image))
        {
            map.put("success", "false");
            map.put("msg", "比对失败，未捕捉到图片");
            return map;
        }
        image = image.substring(image.indexOf(",") + 1);
        Base64ImageUtil.GenerateImage(image, "D:\\Jquery插件\\temp\\1.jpg");
        String result = inteface.CompareFaceImage("D:\\Jquery插件\\temp\\1.jpg");
        if (!StringUtils.isNull(result))
        {
            map.put("success", "true");
            map.put("msg", "已经找到人脸特征，将在右上方打开该图片！");
            map.put("imagePath", result);
        }
        else
        {
            map.put("success", "false");
            map.put("msg", "人脸库中没有找到对比结果！");
        }
        return map;
    }

    @RequestMapping(value = "getImage")
    public void getImage(String imagePath, HttpServletResponse response) throws IOException
    {
        if (StringUtils.isNull(imagePath)) return;
        else
        {
            OutputStream out = response.getOutputStream();
            response.setContentType("image/jpg");
            File file = new File(imagePath);
            FileInputStream outStream = new FileInputStream(file);
            byte[] temp = new byte[1024];
            int len = 0;
            while ((len = outStream.read(temp)) != -1)
            {
                out.write(temp, 0, len);
            }
            outStream.close();
            out.flush();
        }
    }

    @RequestMapping(value = "initImage")
    @ResponseBody
    public Map<String, String> uplodaImage1(String image) throws IOException
    {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNull(image))
        {
            map.put("success", "false");
            map.put("msg", "入库失败，未得到图片");
            return map;
        }
        image = image.substring(image.indexOf(",") + 1);
        Base64ImageUtil.GenerateImage(image, "D:\\tempImg\\1.jpg");
        boolean result = FaceUtil.addImg("D:\\tempImg\\1.jpg");
        if (result)
        {
            map.put("success", "true");
            map.put("msg", "人脸入库成功！提示：现阶段一个人只能入库一张人脸图片。" + "当前采样人脸总数：" + FaceUtil.faceImgSize());
        }
        else
        {
            map.put("success", "false");
            map.put("msg", "人脸入库失败，人脸库中已经存在该人脸特征！");
        }
        return map;
    }


    @RequestMapping(value = "CompareFaceImage")
    @ResponseBody
    public Map<String, String> CompareFace1(String image) throws IOException
    {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNull(image))
        {
            map.put("success", "false");
            map.put("msg", "比对失败，未捕捉到图片");
            return map;
        }
        image = image.substring(image.indexOf(",") + 1);
        Base64ImageUtil.GenerateImage(image, "D:\\tempImg\\1.jpg");
        List<Map<String, String>> maps = FaceUtil.compareAll("D:\\tempImg\\1.jpg");
        boolean faceExist = FaceUtil.isFaceExist("D:\\tempImg\\1.jpg", 60.0);
        if (maps != null)
        {
            for (Map<String, String> msp : maps)
            {
                System.out
                        .println("图片路径------------" + msp.get("imagePath") + "--------------相似度：" + msp.get("result"));
            }
        }
        if (faceExist)
        {
            map.put("success", "true");
            map.put("msg", "已经找到人脸特征，将在右上方打开该图片！");
            map.put("imagePath", maps.get(0).get("imagePath"));
        }
        else
        {
            map.put("success", "false");
            map.put("msg", "人脸库中没有找到对比结果！");
        }
        return map;
    }

    @RequestMapping(value = "addFace")
    @Json
    public FaceInfo addFaceImg(@Add FaceInfo faceInfo) throws IOException
    {
        return faceInfo;
    }
}
