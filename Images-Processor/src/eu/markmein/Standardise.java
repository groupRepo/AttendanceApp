package eu.markmein;

import java.util.ArrayList;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import eu.markmein.face.FaceFrame;
import eu.markmein.face.ImageOperations;

public class Standardise implements Runnable {
	
	private String inPath, outPath;
	int id;
	public Standardise(int aId, String aInPath, String aOutPath) {
		id = aId;
		inPath = aInPath;
		outPath = aOutPath;
	}
	public void run() {
		System.out.println("Processing: " + inPath + "-" + outPath + id + ".jpg");
		IplImage image = ImageOperations.loadImageFromFile(inPath);
		ArrayList<FaceFrame> faces = ImageOperations.getFacesCoords(image);
		ArrayList<IplImage> extracts = ImageOperations.extractFaces(image, faces);
		ImageOperations.writeImageToFile(outPath + id + ".jpg", extracts.get(0));
	}
}
