package project.face;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

import java.io.PushbackInputStream;

import com.googlecode.javacv.cpp.opencv_contrib.*;
import static com.googlecode.javacv.cpp.opencv_contrib.createEigenFaceRecognizer;
import static com.googlecode.javacv.cpp.opencv_contrib.createFisherFaceRecognizer;
public class Rec {
	public static void main(String args[]){
		
		//Eigen faces recogniser
		FaceRecognizer f = createEigenFaceRecognizer(1, 10000);
		
		MatVector images = new MatVector();
		images.capacity(6);
		int id[] = new int[6];
		System.out.println(images.isNull());
		CvArr c = cvLoadImage("1.jpg", CV_LOAD_IMAGE_GRAYSCALE);
		System.out.println("1");
		images.put(c);
		System.out.println("2");
		id[0] = 0;
		c = cvLoadImage("1_1.jpg", CV_LOAD_IMAGE_GRAYSCALE);
		images.put(c);
		id[1] = 0;
		c = cvLoadImage("1_1.jpg", CV_LOAD_IMAGE_GRAYSCALE);
		images.put(c);
		id[2] = 0;
		/*images.put(3, cvLoadImageM("2.jpg", CV_LOAD_IMAGE_GRAYSCALE));
		id[3] = 1;
		images.put(4, cvLoadImageM("2_2.jpg", CV_LOAD_IMAGE_GRAYSCALE));
		id[4] = 1;
		images.put(5, cvLoadImageM("2_2.jpg", CV_LOAD_IMAGE_GRAYSCALE));
		id[5] = 1*/
	
		System.out.println(images.position(1).toString());
		f.train(images, id);
		
		CvMat input = cvLoadImageM("3.jpg", CV_LOAD_IMAGE_GRAYSCALE);
		int label[] = new int[4];
		double prediction[] = new double[4];
		f.predict(input, label, prediction);
		
		System.out.println("Predicted label: " + label);
		System.out.println("Prediction: " + prediction);
	}
}
