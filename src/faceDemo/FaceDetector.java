package faceDemo;

import org.opencv.core.Core;

public class FaceDetector
{

    public static void main(String[] args)
    {
       System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
/*        System.out.println("init");
        CascadeClassifier faceDetector = new CascadeClassifier(
                "D:\\openCvJDK\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalface_alt.xml");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("\nRunning FaceDetector");
        Mat image = Highgui
                .imread("D:\\faceImg\\1.jpg");
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        for (Rect rect : faceDetections.toArray())
        {
            Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }
        String filename = "ouput3.png";
        System.out.println(String.format("Writing %s", filename));
        Highgui.imwrite(filename, image);*/
        System.out.println(  FaceUtil.compare("D:\\faceImg\\4.jpg", "D:\\faceImg\\12.jpg"));
    }
}