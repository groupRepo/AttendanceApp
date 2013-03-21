package eu.markmein;
import eu.markmein.face.FaceRecogniser;

public class Trainer {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		if(args.length < 2){
			System.err.println("Usage model-trainel <moduleOfferingId> <studentNumber> ...");
		}
		else{
			String saveFolder = "classes/" + args[0] + "/";
			System.out.println("Students: " + (args.length -1));
			FaceRecogniser f = new FaceRecogniser(args.length -1);
			int i;
			for(i = 1; i < args.length; i++){

				String name = args[i]; 
				System.out.println("Adding student: " + name + " students/" + i);
				f.addPerson(name, "students/" + args[i] + "/");
			}
			f.train();
			f.saveTrainingData(saveFolder);
		}
	}
}
