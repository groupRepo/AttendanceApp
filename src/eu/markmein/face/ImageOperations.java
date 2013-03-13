package eu.markmein.face;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

public class ImageOperations {
	/*
	 * There is 4 different haar cascade files included in OpenCV, 
	 * we can decide at later stage which one will give us best result.
	 */
	private final static String HAAR_CASCADE[] = {"prog/cascade_files/haarcascade_frontalface_default.xml", "prog/cascade_files/haarcascade_frontalface_alt.xml",
			"prog/cascade_files/haarcascade_frontalface_alt2.xml", "prog/cascade_files/haarcascade_frontalface_alt_tree.xml"};
	/**
	 * Function takes in the image that is then searched to find the faces that it contains.
	 * @param aImage - buffered image
	 * @return FaceFrame[] - array of rectangle
	 */
	public static ArrayList<FaceFrame> getFacesCoords(IplImage aImage){
		IplImage orgImg = aImage;
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
	public static IplImage markFaces(IplImage aImage, ArrayList<FaceFrame> aFaces){
		IplImage orgImg = aImage;

		//Draw a rectangle around each detected face.
		for(FaceFrame f : aFaces){
			cvRectangle(orgImg, cvPoint(f.getA(), f.getB()), cvPoint(f.getA() + f.getWidth(), 
					f.getB() + f.getHeight()), CvScalar.RED, 1, CV_AA, 0);
		}
		return orgImg;
	}
	public static IplImage prepareImageForRecognition(IplImage aImage){
		IplImage orgImg = aImage;
		IplImage grayImg = IplImage.create(orgImg.width(), orgImg.height(), IPL_DEPTH_8U, 1);
		
		IplImage tempImg = IplImage.create(200, 200, IPL_DEPTH_8U, 1);
		IplImage normalisedImg = IplImage.create(200, 200, IPL_DEPTH_8U, 1);
		
		cvCvtColor(orgImg, grayImg, CV_BGR2GRAY);
		cvResize(grayImg, tempImg, CV_INTER_AREA);
		cvEqualizeHist(tempImg, normalisedImg);
		return normalisedImg;
	}
	public static IplImage BuffImg2IplImg(BufferedImage aImage){
		return IplImage.createFrom(aImage, CV_LOAD_IMAGE_GRAYSCALE);
	}
	public static BufferedImage IplImg2BuffImage(IplImage aImage){
		return aImage.getBufferedImage();
	}
	public static ArrayList<IplImage> extractFaces(IplImage aImage, ArrayList<FaceFrame> aFaces){
		ArrayList<IplImage> a = new ArrayList<IplImage>();
		
		for(FaceFrame f : aFaces){
			cvSetImageROI(aImage, f.getCvRect());
			IplImage temp = cvCreateImage(cvGetSize(aImage), aImage.depth(), aImage.nChannels());
			cvCopy(aImage, temp, null);
			cvResetImageROI(aImage);
			temp = prepareImageForRecognition(temp);
			a.add(temp);
		}
		return a;
	}
	public static IplImage loadImageFromFile(String aPath){
		return cvLoadImage(aPath);
	}
	public static void writeImageToFile(String aPath, IplImage aImage){
		cvSaveImage(aPath, aImage);
	}
}
