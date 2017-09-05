package com.jh.utils;

import java.io.*;

/**
 * @author liyabin
 * @date 2017-09-01下午 2:45
 */
public class ReadByteUtil
{
    public static byte[] getByte(String filePath) throws FileNotFoundException
    {
        File file =new File(filePath);
        FileInputStream inputStream =new FileInputStream(file);

       // inputStream.r
       //BufferedOutputStream in = new BufferedInputStream()
     return null;
    }
    public static void readByBufferedReader(String fileName) {
            try {
                File file = new File(fileName);
                // 读取文件，并且以utf-8的形式写出去
                BufferedReader bufread;
                String read;
                bufread = new BufferedReader(new FileReader(file));
                while ((read = bufread.readLine()) != null) {
                    System.out.println(read);
                }
                bufread.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
}
