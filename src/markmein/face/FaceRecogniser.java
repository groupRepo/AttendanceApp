package eu.markmein.face;

import com.googlecode.javacv.cpp.opencv_contrib.FaceRecognizer;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.javacv.cpp.opencv_contrib.createLBPHFaceRecognizer;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;


public class FaceRecogniser {
	/*
	 * Name of training data file
	 */
	private final String TR_DATA_FILE = "TRData.xml";
	/*
	 * Name of names data file
	 */
	private final String TR_NAMES_FILE = "TRNames.bin";
	/*
	 * number of sample pictures per person
	 */
	private final int FACES_PER_PERSON = 8;
	
	/*
	 * capacity of the recogniser
	 */
	private int personLimit = 0;
	/*
	 * counter used when adding people to the recogniser
	 */
	private int personCount = 0;
	/*
	 * Instance of opencv FaceRecognizer.
	 */
	private FaceRecognizer recogniser;
	/*
	 * boolen value show if recogniser has learned all faces
	 */
	private boolean trained = false;
	/*
	 * opencv vector used to store training images
	 */
	private MatVector trainingImages;
	/*
	 * labels array is used  by opencv, each picture has its 
	 * own id, which is returned when comparison is done.
	 */
	private int labels[];
	/*
	 * hashmap holds the unique names, of people
	 * names are associated with integers stored in labels array
	 */
	private Map<Integer, String> names;
	/*
	 * counters to keep track of where we are with adding new images
	 */
	int trainingImagesC, labelsC = 0;
	
	/*
	 * Constructors
	 */
	public FaceRecogniser() {
		initialSetUp();
	}
	public FaceRecogniser(String aPath){
		initialSetUp();
		loadTrainingDate(aPath);
	}
	public FaceRecogniser(int aPersonCount){
		initialSetUp();
		initialiseRecogniser(aPersonCount);
		
	}
	private void initialSetUp(){
		recogniser = createLBPHFaceRecognizer();
		//recogniser = createLBPHFaceRecognizer(2,16,8,8, 120);
		//recogniser.set("threshold",90.5);
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
	public ArrayList<String> recogniseMany(ArrayList<IplImage> aFaces){
		ArrayList<String> rNames = new ArrayList<String>();
		if(trained){
			//recognise
			for(IplImage f : aFaces){
				String tempName = null;
				tempName = recognise(f);
				// TODO to be tested !!!!! May need to be changed
				if(tempName != null && !rNames.contains(tempName)){
					rNames.add(tempName);
				}
			}
			
		}
		return rNames;
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
	@SuppressWarnings("unchecked")
	public void loadTrainingDate(String aPath){
		recogniser.load(aPath + TR_DATA_FILE);
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(aPath + TR_NAMES_FILE));
			names = (Map<Integer, String>) is.readObject();
			is.close();
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
