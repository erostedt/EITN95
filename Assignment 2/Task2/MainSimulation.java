import java.util.*;
import java.io.*;

//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global {

	public static void main(String[] args) throws IOException {

		// When -1, enter if-statement below to give each student a velocity from U(1,7)
		double[] vels = {2.0, 4.0, -1.0};

		ArrayList<ArrayList<Double>> allRunTimes = new ArrayList<>();

		ArrayList<Double[]> confidenceIntervals = new ArrayList<>(3);

		for(int i = 0; i < confidenceIntervals.size(); i++){
			confidenceIntervals.add(new Double[3]);
		}

		int maxiter = 100;
		for (int run = 0; run < maxiter; run++) {
			ArrayList<Double> runTimes = new ArrayList<>();
			for (double vel : vels) {
				Signal actSignal;
				new SignalList();
				pairs = new boolean[20][20];
				Hall hall = new Hall();

				List<Student> students = new ArrayList<>();

				for (int id = 0; id < numStudents; id++) {

					Student student;
					int rndX = rnd.nextInt(hall.size);
					int rndY = rnd.nextInt(hall.size);
					if (vel == -1) {
						// vel ~ U(1,7)
						student = new Student(hall, id, rndX, rndY, 1 + 6*rnd.nextDouble());
					}
					else{
						student = new Student(hall, id, rndX, rndY, vel);
					}
					students.add(student);
					hall.tiles[rndX][rndY].students.add(student);
					SignalList.SendSignal(MOVE, student, time);

				}

				while (numPairs != 380) {
					actSignal = SignalList.FetchSignal();
					time = actSignal.arrivalTime;
					actSignal.destination.TreatSignal(actSignal);

				}
				// reset
				runTimes.add(time);
				numPairs = 0;
				time = 0;
				
				
				for(Student student: students){
					for(Student other: students){
						writer(String.valueOf(vel), String.valueOf(student.timeWithEach[other.id]), false);
					}
				}
				writer(String.valueOf(vel), "", true);
			}

			allRunTimes.add(runTimes);
			if (allRunTimes.size() > 1) {
				for (int i = 0; i < confidenceIntervals.size(); i++) {
					confidenceIntervals.set(i, confidenceInterval.getConfidenceInterval(allRunTimes, i));
				}
				if(!confidenceInterval.anyOverlap(confidenceIntervals)){
					break;
				}
			}
		}
		for (Double[] confidenceInterval : confidenceIntervals) {
			System.out.println("Mean time: " + confidenceInterval[1]);
			System.out.println(confidenceInterval[0] + ", " + confidenceInterval[2]);
		}
	}

	public static void writer(String path, String s, boolean lineBreak) {
		/**
		 * Writes a string to a file.
		 * @param f File to write to.
		 * @param s string to be written on the file.
		 */
		try {
			FileWriter fw = new FileWriter(path, true);
			if(lineBreak){
				fw.write(System.lineSeparator());
			}
			else{
				fw.write(s);
				fw.write(" ");
			}
			fw.close();
		} catch (IOException ex) {
			System.err.println("Could not write to file.");
		}
	}
}

