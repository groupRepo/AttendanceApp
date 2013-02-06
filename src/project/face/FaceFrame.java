package project.face;

import com.googlecode.javacv.cpp.opencv_core.CvRect;

public class FaceFrame {
	private CvRect rect;
	
	FaceFrame(){}
	FaceFrame(int a, int b, int aWidth, int aHeigth){
		rect = new CvRect(a, b, aWidth, aHeigth);
	}
	FaceFrame(CvRect aRect){
		rect = aRect;
	}
	public int getA(){
		return rect.x();
	}
	public int getB(){
		return rect.y();
	}
	public int getWidth(){
		return rect.width();
	}
	public int getHeight(){
		return rect.height();
	}
	public CvRect getCvRect(){
		return rect;
	}
}
