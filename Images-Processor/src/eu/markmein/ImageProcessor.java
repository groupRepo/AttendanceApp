package eu.markmein;

import java.io.File;

public class ImageProcessor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length != 2){
			System.err.println("Usage: Image-Processor <imagesDir> <destDir>");
		}
		else{
			File srcDir = new File(args[0]);
			File listOfPhotos[] = srcDir.listFiles();

			for(int i = 0; i < listOfPhotos.length; i++){
				Runnable r = new Standardise(i, listOfPhotos[i].getAbsolutePath(), args[1] + "/");
				Thread t = new Thread(r);
				t.start();
			}
		}
	}
}
