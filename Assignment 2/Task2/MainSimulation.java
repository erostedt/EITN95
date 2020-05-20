import java.util.*;
import java.io.*;
import java.lang.*;
import java.io.FileReader;
import java.io.BufferedReader;

//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global {

	public static void main(String[] args) throws IOException {

		// When -1, enter if-statement below to give each student a velocity from U(1,7)
		double[] velocities = {2.0, 4.0, -1.0};

		ArrayList<ArrayList<Double>> allTimes = new ArrayList<>();
		ArrayList<Double[]> CIs = new ArrayList<>(3);
		ArrayList<ArrayList<Integer>> levelOfAcquaint = new ArrayList<>();
		for(int i = 0; i < 3; i++){
			CIs.add(new Double[3]);
			levelOfAcquaint.add(new ArrayList<>());
		}

		boolean CIoverlap = true;
		while (CIoverlap) {
			ArrayList<Double> times = new ArrayList<>();
			for (double vel : velocities) {

				Signal actSignal;
				new SignalList();
				friends = new boolean[20][20];
				// Generate the floor
				Floor floor = new Floor(20);
				// Create all students

				List<Student> students = new ArrayList<>();

				for (int i = 0; i < 20; i++) {
					// Generate students at unique positions
					List<Integer> pos = generatePosition();
					Student s;
					if (vel == -1) {
						// vel ~ U(1,7)
						s = new Student(floor, i, 1 + slump.nextDouble()*6, pos);
					}
					else{
						s = new Student(floor, i, vel, pos);
					}
					students.add(s);
					SignalList.SendSignal(CHECK_FOR_FRIEND, s, time + 0.01*slump.nextDouble());

					// Add student to the floor
					floor.tiles[pos.get(0)][pos.get(1)].studentsOnTile += 1;
					floor.tiles[pos.get(0)][pos.get(1)].students.add(s);
				}

				// This is the main loop
				while (met != 190) {
					actSignal = SignalList.FetchSignal();
					time = actSignal.arrivalTime;
					actSignal.destination.TreatSignal(actSignal);

				}
				times.add(time);
				met = 0;
				time = 0;


				ArrayList<Integer> acquaintTimes;
				if (vel == 2.0){
					acquaintTimes = levelOfAcquaint.get(0);
				}
				else if (vel == 4.0){
					acquaintTimes = levelOfAcquaint.get(1);
				}
				else{
					acquaintTimes = levelOfAcquaint.get(2);
				}
				for (Student s : students){
					acquaintTimes.addAll(s.timeSpent);
				}
			}

			allTimes.add(times);
			if (allTimes.size() > 3) {
				for (int i = 0; i < 3; i++) {
					CIs.set(i, CI.calc95CI(allTimes, i));
				}
				CIoverlap = CI.overlappingCI(CIs);
			}
		}
		for (Double[] CI : CIs) {
			System.out.println("Mean time: " + CI[1]);
			System.out.println(CI[0] + ", " + CI[2]);
		}

		for (double vel : velocities){
			if (vel == 2.0){
				createLogFile("2.0", levelOfAcquaint.get(0));
			}
			else if (vel == 4.0){
				createLogFile("4.0", levelOfAcquaint.get(1));
			}
			else{
				createLogFile("U(1,7)", levelOfAcquaint.get(2));
			}
		}

	}


	private static List<Integer> generatePosition(){
		List<Integer> pos = new ArrayList<>();
		do {
			pos.add(slump.nextInt(20));
			pos.add(slump.nextInt(20));
		} while(allPos.contains(pos));
		allPos.add(pos);
		return pos;
	}

	private static void createLogFile(String vel, List<Integer> values) throws IOException{
		String path = "log:vel:" + vel;
		for (Integer v : values){
			writeToFile(Integer.toString(v), path);
		}
	}

	private static void writeToFile(String line, String path) throws IOException{
		FileWriter write = new FileWriter(path, true);
		PrintWriter print_line = new PrintWriter(write);

		print_line.printf( "%s" + "%n" , line);
		print_line.close();
		write.close();
	}
}

