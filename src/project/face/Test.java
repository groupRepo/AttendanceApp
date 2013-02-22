package project.face;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacv.cpp.opencv_contrib.*;
import static com.googlecode.javacv.cpp.opencv_contrib.createEigenFaceRecognizer;
import static com.googlecode.javacv.cpp.opencv_contrib.createFisherFaceRecognizer;
import static com.googlecode.javacv.cpp.opencv_contrib.createLBPHFaceRecognizer;
import com.googlecode.javacv.cpp.opencv_contrib.FaceRecognizer;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvArrArray;
import com.googlecode.javacv.cpp.opencv_core.MatVector;

import project.face.FaceOperations;

public class Test {

	  public static void main(String[] args) throws Exception {

		FaceRecogniser f = new FaceRecogniser(3);
		/*f.addPerson("Sam", "./studentImgs/R00072205/");
		f.addPerson("Jakub", "./studentImgs/R00068252/");
		f.addPerson("Neil", "./studentImgs/R00076373/");
		f.train();
		f.saveTrainingData("./studentImgs/");*/
		f.loadTrainingDate("./studentImgs/");
		System.out.println(f.recognise(cvLoadImage("4.jpg", CV_LOAD_IMAGE_GRAYSCALE)));
	  }
}