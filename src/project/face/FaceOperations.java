package project.face;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.googlecode.javacpp.SizeTPointer;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

public class FaceOperations {
	/*
	 * There is 4 different haar cascade files included in OpenCV, 
	 * we can decide at later stage which one will give us best result.
	 */
	private final static String HAAR_CASCADE[] = {"cascade_files/haarcascade_frontalface_default.xml", "cascade_files/haarcascade_frontalface_alt.xml",
			"cascade_files/haarcascade_frontalface_alt2.xml", "cascade_files/haarcascade_frontalface_alt_tree.xml"};
	/**
	 * Function takes in the image that is then searched to find the faces that it contains.
	 * @param aImage - buffered image
	 * @return FaceFrame[] - array of rectangle
	 */
	public static ArrayList<FaceFrame> getFacesCoords(BufferedImage aImage){
		IplImage orgImg = IplImage.createFrom(aImage);
		ArrayList<FaceFrame> result = new ArrayList<FaceFrame>();
		/*
		 * Convert original image to gray scale.
		 * IPL_DEPTH_8U - 8bit unsigned image
		 * 1 color channel ?
		 * CV_BGR2GRAY - conversion color space converts RGB to grey scale
		 */
		IplImage grayImg = IplImage.create(orgImg.width(), orgImg.height(), IPL_DEPTH_8U, 1);
		cvCvtColor(orgImg, grayImg, CV_BGR2GRAY);
		
		//Haar cascade classifier to be used for face detection
		CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad(HAAR_CASCADE[2]));
		
		/*
		 * OpenCV uses CvMemStorage for storing growing data structures.
		 * In our case this will be the list of faces.
		 */
		CvMemStorage storage = CvMemStorage.create();
		
		//Detect the faces on the image using grey image and provided cascade file.
		CvSeq faces = cvHaarDetectObjects(grayImg, cascade, storage, 1.1, 3, 0);

		//Convert detected faces to FaceFrame objects.
		for (int i = 0; i < faces.total(); i++) {
			result.add(new FaceFrame(new CvRect(cvGetSeqElem(faces, i))));
		}
		return result;
	}
	public static BufferedImage markFaces(BufferedImage aImage, ArrayList<FaceFrame> aFaces){
		IplImage orgImg = IplImage.createFrom(aImage);

		//Draw a rectangle around each detected face.
		for(FaceFrame f : aFaces){
			cvRectangle(orgImg, cvPoint(f.getA(), f.getB()), cvPoint(f.getA() + f.getWidth(), 
					f.getB() + f.getHeight()), CvScalar.RED, 1, CV_AA, 0);
		}
		return orgImg.getBufferedImage();
	}
	public static BufferedImage prepareImageForRecognition(BufferedImage aImage){
		IplImage orgImg = IplImage.createFrom(aImage);
		IplImage grayImg = IplImage.create(orgImg.width(), orgImg.height(), IPL_DEPTH_8U, 1);
		IplImage temp = IplImage.create(100, 100, IPL_DEPTH_8U, 1);
		cvCvtColor(orgImg, grayImg, CV_BGR2GRAY);
		cvResize(grayImg, temp, CV_INTER_AREA);
		
		return temp.getBufferedImage();
	}
}
