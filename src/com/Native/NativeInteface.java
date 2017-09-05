package com.Native;

import java.io.File;

public class NativeInteface
{
    static
    {
        System.loadLibrary("interface");
        NativeInteface inteface = new NativeInteface();
        inteface.FaceInit();
        //如果人脸库中没有图片
        if (inteface.getFaceNum() == 0)
        {
            //D:/faceImg/
            String baseFiles = "D:/faceImg/";
            File file = new File(baseFiles);
            if (file.isDirectory())
            {
                File[] files = file.listFiles();
                if (files != null && files.length > 0)
                {
                    for (File f : files)
                    {
                      inteface.FaceInitImage(f.getAbsolutePath());
                    }
                }
            }
        }
    }

    public native float ContrastFace(String imageUrl1, String imageUrl2);

    public native float ContrastFaceVideo(String imageUrl1, String videoUrl2);

    public native String ContrastFaceVideoByImgList(String fileUrl, String videoUrl);

    public native void FaceInit();

    public native void FaceDestroy();

    public native int FindFace();

    public native int getFaceNum();

    public native String CompareFace();

    /**
     * 初始化一张人脸照片到人脸库
     *
     * @return
     */
    public native void FaceInitImage(String imageUrl);

    /**
     * 用图片去比对人脸库的图片
     *
     * @return
     */
    public native String CompareFaceImage(String imageUrl);

    /**
     * 设置人脸库图片保存的路径
     *
     * @return
     */
    public native String setBaseFaceImageUrl();

}
