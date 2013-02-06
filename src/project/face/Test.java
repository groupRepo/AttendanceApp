package project.face;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import project.face.FaceOperations;

public class Test {
	
	/*
	 * Grrrrrrr!!!! 
	 */

	  public static void main(String[] args) throws Exception {
		  BufferedImage img = ImageIO.read(new File("2.jpg"));
		  ArrayList<FaceFrame> frames = new ArrayList<FaceFrame>();
		  frames = FaceOperations.getFacesCoords(img);
		  int i = 20;
		  for(FaceFrame f: frames){
			 ImageIO.write(FaceOperations.prepareImageForRecognition(img.getSubimage(f.getA(), f.getB(), f.getWidth(), f.getHeight())), "jpg", new File(i + ".jpg"));
			 i++;
		 }
		ImageIO.write(FaceOperations.markFaces(img, FaceOperations.getFacesCoords(img)),
				 "jpg", new File("out.jpg"));
	  }
}