package project.face;

import com.googlecode.javacv.cpp.opencv_contrib.FaceRecognizer;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.javacv.cpp.opencv_contrib.createLBPHFaceRecognizer;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;


public class FaceRecogniser {
	private final int FACES_PER_PERSON = 10;
	private int personLimit = 0;
	private int personCount = 0;
	
	private FaceRecognizer recogniser;
	private boolean trained = false;
	private MatVector trainingImages;
	private int labels[];
	private Map<Integer, String> names;
	//counters to keep track of where we are with adding new images
	int trainingImagesC, labelsC = 0;
	
	FaceRecogniser() {
		recogniser = createLBPHFaceRecognizer();
		recogniser.set("threshold", 85.0);
	}
	FaceRecogniser(int aPersonCount){
		recogniser = createLBPHFaceRecognizer();
		recogniser.set("threshold", 85.0);
		initialiseRecogniser(aPersonCount);
		
	}
	public boolean isTrained(){
		return trained;
	}
	public void initialiseRecogniser(int aPersonCount){
		personLimit = aPersonCount;
		trainingImages = new MatVector(FACES_PER_PERSON * personLimit);
		labels = new int[FACES_PER_PERSON * personLimit];
		names = Collections.synchronizedMap(new HashMap<Integer, String>(aPersonCount));
	}
	public void addPerson(String aName, String aPhotoDirectoryPath){
		names.put(labelsC, aName);
		for(int i = 0; i < FACES_PER_PERSON; i++){
			trainingImages.put(trainingImagesC, cvLoadImage(aPhotoDirectoryPath + i + ".jpg"));
			labels[trainingImagesC] = labelsC;
			trainingImagesC++;
		}
		personCount++;
		labelsC++;
	}
	public boolean train(){
		if(personCount == personLimit){
			recogniser.train(trainingImages, labels);
			trained = true;
		}
		return trained;
	}
	public String recognise(IplImage aImage){
		String name = null;
		if(trained){
			int result = recogniser.predict((CvArr) aImage);
			if(names.containsKey(Integer.valueOf(result))){
				name = (String) names.get(Integer.valueOf(result));
			}
		}
		return name;
	}
	
}
