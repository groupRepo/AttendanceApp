package project.face;

import com.googlecode.javacv.cpp.opencv_contrib.FaceRecognizer;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.javacv.cpp.opencv_contrib.createLBPHFaceRecognizer;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;


public class FaceRecogniser {
	private final String TR_DATA_FILE = "TRData.xml";
	private final String TR_NAMES_FILE = "TRNames.bin";
	
	private final int FACES_PER_PERSON = 8;
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
		initialSetUp();
	}
	FaceRecogniser(String aPath){
		initialSetUp();
		loadTrainingDate(aPath);
	}
	FaceRecogniser(int aPersonCount){
		initialSetUp();
		initialiseRecogniser(aPersonCount);
		
	}
	private void initialSetUp(){
		recogniser = createLBPHFaceRecognizer();
		recogniser.set("threshold", 85.0);
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
			trainingImages.put(trainingImagesC, cvLoadImage(aPhotoDirectoryPath + i + ".jpg", CV_LOAD_IMAGE_GRAYSCALE));
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
	public void saveTrainingData(String aPath){
		recogniser.save(aPath + TR_DATA_FILE);
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(aPath + TR_NAMES_FILE));
			os.writeObject(names);
			os.close();
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void loadTrainingDate(String aPath){
		recogniser.load(aPath + "TR_Data.xml");
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(aPath + TR_NAMES_FILE));
			names = (Map<Integer, String>) is.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		trained = true;
	}
}
