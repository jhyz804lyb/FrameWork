package faceDemo;

import com.jh.utils.StringUtils;
import org.bytedeco.javacpp.opencv_core.*;
import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.*;
import java.util.*;

import static org.bytedeco.javacpp.helper.opencv_imgproc.cvCalcHist;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.opencv.highgui.Highgui.CV_LOAD_IMAGE_GRAYSCALE;

/**
 * Created by kqw on 2016/9/9.
 * FaceUtil
 */
public final class FaceUtil
{
    static
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try
        {
            FaceUtil.initCreateFaces();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static final String TAG = "FaceUtil";

    /**
     * 特征保存
     *
     * @param image    Mat
     * @param rect     人脸信息
     * @param fileName 文件名字
     * @return 保存是否成功
     */
    public static boolean saveImage(Mat image, Rect rect, String fileName)
    {
        // 原图置灰
        Mat grayMat = new Mat();
        Imgproc.cvtColor(image, grayMat, Imgproc.COLOR_BGR2GRAY);
        // 把检测到的人脸重新定义大小后保存成文件
      //  Mat sub = grayMat.submat(rect);
       // Mat mat = new Mat();
       // Imgproc.resize(sub, mat, rect.size());
        return Highgui.imwrite(fileName, grayMat);
    }

    /**
     * 删除特征
     *
     * @param fileName 特征文件
     * @return 是否删除成功
     */
    public static boolean deleteImage(String fileName)
    {
        // 文件名不能为空
        if (!StringUtils.isNull(fileName))
        {
            return false;
        }
        // 文件路径不能为空
        String path = fileName;
        if (path != null)
        {
            File file = new File(path);
            return file.exists() && file.delete();
        }
        else
        {
            return false;
        }
    }

    /**
     * 提取特征
     *
     * @param fileName 文件名
     * @return 特征图片
     */
    public static Mat getImage(String fileName)
    {
        String filePath = fileName;
        if (!StringUtils.isNull(fileName))
        {
            return null;
        }
        else
        {
            return Highgui.imread(fileName);
        }
    }

    /**
     * 特征对比
     *
     * @param fileName1 人脸特征
     * @param fileName2 人脸特征
     * @return 相似度
     */
    public static double compare(String fileName1, String fileName2)
    {
        try
        {
            String pathFile1 = getFilePath(fileName1);
            String pathFile2 = getFilePath(fileName2);
            if (pathFile1 == null || pathFile2 == null) return 0.00;
            IplImage image1 = cvLoadImage(pathFile1, CV_LOAD_IMAGE_GRAYSCALE);
            IplImage image2 = cvLoadImage(pathFile2, CV_LOAD_IMAGE_GRAYSCALE);
            if (null == image1 || null == image2)
            {
                return -1;
            }

            int l_bins = 256;
            int hist_size[] = {l_bins};
            float v_ranges[] = {0, 255};
            float ranges[][] = {v_ranges};

            IplImage imageArr1[] = {image1};
            IplImage imageArr2[] = {image2};
            CvHistogram Histogram1 = CvHistogram.create(1, hist_size, CV_HIST_ARRAY, ranges, 1);
            CvHistogram Histogram2 = CvHistogram.create(1, hist_size, CV_HIST_ARRAY, ranges, 1);
            cvCalcHist(imageArr1, Histogram1, 0, null);
            cvCalcHist(imageArr2, Histogram2, 0, null);
            cvNormalizeHist(Histogram1, 100.0);
            cvNormalizeHist(Histogram2, 100.0);
            // 参考：http://blog.csdn.net/nicebooks/article/details/8175002
            double c1 = cvCompareHist(Histogram1, Histogram2, CV_COMP_CORREL) * 100;
            double c2 = cvCompareHist(Histogram1, Histogram2, CV_COMP_INTERSECT);
            return (c1 + c2) / 2;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * 获取人脸特征路径
     *
     * @param fileName 人脸特征的图片的名字
     * @return 路径
     */
    private static String getFilePath(String fileName)
    {
        if (StringUtils.isNull(fileName))
        {
            return null;
        }
        if (fileName.indexOf(".") == -1 && (fileName.indexOf("\\") == -1 || fileName.indexOf("/") == -1))
        {
            return Cost.baseImageUrl + fileName + ".jsp";
        }
        return fileName;
    }

    /**
     * 图片对比
     *
     * @param fileName
     * @return
     */
    public static List<Map<String, String>> compareAll(String fileName) throws IOException
    {
        String pathFile1 = getFilePath(fileName);
        createFaceImg(new File(fileName),Cost.TEMP_IMG);
        List<Map<String, String>> result = new ArrayList<>();
        File file = new File(Cost.TEMP_IMG_URL);
        if (file.exists() && file.isDirectory())
        {
            for (File item : file.listFiles())
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("imagePath", item.getAbsolutePath());
                double compare = compare(fileName, item.getAbsolutePath());
                map.put("result", String.valueOf(compare));
                result.add(map);
            }
        }
        return sort(result);
    }

    /**
     * 图片入库
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static boolean addImg(String filePath) throws IOException
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        org.opencv.objdetect.CascadeClassifier faceDetector = new org.opencv.objdetect.CascadeClassifier(
                "D:\\openCvJDK\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalface_alt.xml");

        Mat image = Highgui
                .imread("D:\\faceImg\\1.jpg");
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        if (faceDetections.toArray().length > 0)
        {
            Integer retult = faceImgSize();
            if (retult == -1)
            {
                File file = new File(Cost.baseImageUrl);
                file.createNewFile();
                retult = 0;
            }
            Highgui.imwrite(Cost.baseImageUrl + "\\" + retult + ".jpg", image);
        }
        return false;
    }

    /**
     * 得到人脸库中的人脸照片个数
     *
     * @return
     */
    public static Integer faceImgSize()
    {
        File file = new File(Cost.baseImageUrl);
        if (file.exists() && file.isDirectory())
        {
            return file.listFiles().length;
        }
        return -1;
    }

    /**
     * 按得分排序
     *
     * @param listSize
     * @return
     */
    private static List<Map<String, String>> sort(List<Map<String, String>> listSize)
    {
        if (listSize == null) return null;
        for (int i = 0; i < listSize.size() - 1; i++)
        {
            for (int j = 0; j < listSize.size() - i - 1; j++)
            {
                Map<String, String> temp1 = listSize.get(j);
                Map<String, String> temp2 = listSize.get(j + 1);
                if (Double.parseDouble(temp1.get("result")) < Double.parseDouble(temp2.get("result")))
                {
                    listSize.set(j + 1, temp1);
                    listSize.set(j, temp2);
                }
            }
        }
        return listSize;
    }

    /**
     * 图片中的人脸是否已经在人脸库中
     *
     * @param filePath
     * @param maxScore
     * @return
     */
    public static boolean isFaceExist(String filePath, Double maxScore) throws IOException
    {
        List<Map<String, String>> maps = compareAll(filePath);
        maps = sort(maps);
        for (Map<String, String> map : maps)
        {
            if (Double.parseDouble(map.get("result")) > maxScore)
            {
                return true;
            }
        }
        return false;
    }

    public static String createFaceImg(String faceImg) throws IOException
    {
        File file = new File(Cost.TEMP_IMG_URL);
        if (!file.exists())
        {
            file.createNewFile();
        }
        Date time = new Date();
        String fileName =
                Cost.TEMP_IMG_URL + "\\" + String.valueOf(time.getTime()) + "-" + (int) Math.random() * 10000 + ".jpg";
        //加载人脸算法
        CascadeClassifier faceDetector = new CascadeClassifier(Cost.FACE_DATA_CONFIG);
        Mat image = Highgui.imread(faceImg);
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        for (Rect rect : faceDetections.toArray())
        {
            Mat roi_img = new Mat(image, rect);
            Mat tmp_img = new Mat();
            roi_img.copyTo(tmp_img);
            Highgui.imwrite(fileName, tmp_img);
        }
        return faceDetections.toArray().length > 0 ? fileName : null;
    }

    public static void initCreateFaces() throws IOException
    {
        File file = new File(Cost.baseImageUrl);
        if (file.exists() && file.isDirectory())
        {
            File file3 = new File(Cost.TEMP_IMG_URL);
            if(file3.exists() && file3.isDirectory()&&file3.listFiles().length>0) return ;
            for (File item : file.listFiles())
            {
                if (item.isFile())
                    createFaceImg(item);
            }
        }
    }

    /**
     * 识别照片中的人脸，并生成行的人脸图保存在默认路径
     * @param file
     * @throws IOException
     */
    public static void createFaceImg(File file) throws IOException
    {
        createFaceImg(file, Cost.TEMP_IMG_URL);
    }

    /**
     * 识别照片中的人脸，并生成行的人脸图保存在指定路径
     * @param file 照片文件
     * @param basePath 生成人脸后照片的保存路径
     * @throws IOException
     */
    public static void createFaceImg(File file, String basePath) throws IOException
    {
        String fileName = basePath + "\\" + file.getName();
        //加载人脸算法
        CascadeClassifier faceDetector = new CascadeClassifier(Cost.FACE_DATA_CONFIG);
        //生成图片
        Mat image = Highgui.imread(file.getAbsolutePath());
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        for (Rect rect : faceDetections.toArray())
        {
            Mat roi_img = new Mat(image, rect);
            Mat tmp_img = new Mat();
            roi_img.copyTo(tmp_img);
            saveImage(tmp_img, rect, fileName);
        }
    }
}
