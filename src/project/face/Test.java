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
		//extract();
		//recognise();
		//recogniseMany(extractq());
		  
		FaceRecogniser f = new FaceRecogniser(3);
		/*f.addPerson("Sam", "./studentImgs/R00072205/");
		f.addPerson("Jakub", "./studentImgs/R00068252/");
		f.addPerson("Neil", "./studentImgs/R00076373/");
		f.train();
		f.saveTrainingData("./studentImgs/");*/
		f.loadTrainingDate("./studentImgs/");
		System.out.println(f.recognise(cvLoadImage("4.jpg", CV_LOAD_IMAGE_GRAYSCALE)));
	  }
public static void extract() throws IOException{
		  BufferedImage img = ImageIO.read(new File("a.jpg"));
		  ArrayList<FaceFrame> frames = new ArrayList<FaceFrame>();
		  frames = FaceOperations.getFacesCoords(img);
		  int i=20;
		  for(FaceFrame f: frames){
			 ImageIO.write(FaceOperations.prepareImageForRecognition(img.getSubimage(f.getA(), f.getB(), f.getWidth(), f.getHeight())), "jpg", new File(i + ".jpg"));
			 i++;
		 }
	
}
public static ArrayList<BufferedImage> extractq() throws IOException{
	ArrayList<BufferedImage> faces = new ArrayList<BufferedImage>();
	
		  BufferedImage img = ImageIO.read(new File("b.jpg"));
		  ArrayList<FaceFrame> frames = new ArrayList<FaceFrame>();
		  frames = FaceOperations.getFacesCoords(img);
		  for(FaceFrame f: frames){
			 faces.add(FaceOperations.prepareImageForRecognition(img.getSubimage(f.getA(), f.getB(), f.getWidth(), f.getHeight())));
		 }
		  return faces;
}
	
	/*
	  BufferedImage img = ImageIO.read(new File("1.jpg"));
	  ArrayList<FaceFrame> frames = new ArrayList<FaceFrame>();
	  frames = FaceOperations.getFacesCoords(img);
	  int i = 20;
	  for(FaceFrame f: frames){
		 ImageIO.write(FaceOperations.prepareImageForRecognition(img.getSubimage(f.getA(), f.getB(), f.getWidth(), f.getHeight())), "jpg", new File(i + ".jpg"));
		 i++;
	 }
  
	//ImageIO.write(FaceOperations.markFaces(img, FaceOperations.getFacesCoords(img)),
	//
}*/
public static void recognise(){
		
		//Local Binary Patterns Histograms recogniser has best performance on small sample spaces.
		FaceRecognizer f = createLBPHFaceRecognizer();
		//f.set("threshold", 90.0);
		
		int id[] = new int[24];
		MatVector images = new MatVector(24);
		
		int j = 0;
		
		for(int i = 1; i < 9; i++){
			images.put(j, cvLoadImage(i+".jpg", CV_LOAD_IMAGE_GRAYSCALE));
			id[j]=0;
			j++;
		}
		
		for(int i = 900; i < 908; i++){
			images.put(j, cvLoadImage(i+".jpg", CV_LOAD_IMAGE_GRAYSCALE));
			id[j]=1;
			j++;
		}
		for(int i = 700; i < 708; i++){
			images.put(j, cvLoadImage(i+".jpg", CV_LOAD_IMAGE_GRAYSCALE));
			id[j]=2;
			j++;
		}
		
		f.train(images, id);
		//f.load("nowy.xml");
		CvArr input = cvLoadImage("a.jpg", CV_LOAD_IMAGE_GRAYSCALE);
		int a = f.predict(input);
		//f.save("nowy.xml");
		String b;
		if(a==0){b="Sam";}
		else if(a==1){b="Jakub";}
		else if(a==2){b="Neil";}
		else{b="not recognised.";}
		System.out.println("Predicted Person: " + b);
		//System.out.println("Prediction: " + prediction.toString());
		
	}
public static void recogniseMany(ArrayList<BufferedImage>  bi){
	
	//Local Binary Patterns Histograms recogniser has best performance on small sample spaces.
	FaceRecognizer f = createLBPHFaceRecognizer();
	//f.set("threshold", 100.0);
	f.load("nowy.xml");
	
	for(BufferedImage i : bi){
		IplImage img = IplImage.createFrom(i);
		CvArr input = img;
		int a = f.predict(input);
		String b;
		if(a==0){b="Sam";}
		else if(a==1){b="Jakub";}
		else if(a==2){b="Neil";}
		else{b="not recognised.";}
		System.out.println("Predicted Person: " + b);
	}
	
	//System.out.println("Prediction: " + prediction.toString());
	
}

}