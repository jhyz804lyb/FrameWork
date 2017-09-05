package com.Native;

public class TestCode {

	
	public static void main(String[] args) {
		 System.loadLibrary("interface");
		 //D:\test.jpg
		 NativeInteface test = new NativeInteface();
		 test.FaceInit();
		//System.out.println(test.ContrastFace("D:\\test1.jpg", "D:\\test.jpg"));
		//System.out.println(test.ContrastFace("D:\\test.jpg", "D:\\test.jpg"));
		//test.FaceDestroy();
		System.out.println(test.FindFace());
		System.out.println("");
		System.out.println(test.CompareFace());
		test.FaceDestroy();

	}

}
