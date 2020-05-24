import java.util.*;
import java.io.*;
import java.io.FileReader;
import java.io.BufferedReader;


public class MainSimulation extends Global {

	public static void main(String[] args) throws IOException {

		int worldSize = 10000;
		int[] radiusSize = {6000, 7000, 8000, 9000, 10000, 11000};
		int _n = 2000;
		Signal actSignal;
		new SignalList();

		//Set to true if you want to create new config files
		boolean create = false;
		if (create){
			ArrayList<Integer> newPos;
			ArrayList< ArrayList<Integer> > positions;
			for (int r : radiusSize) {
				createFiles(_n, 1, 4000, r, 5000, 5000);

				positions = new ArrayList<>();
				for(int i = 0; i < r; i++){
					newPos = new ArrayList<>(2);
					newPos.add(rnd.nextInt(worldSize));
					newPos.add(rnd.nextInt(worldSize));
					do {
						newPos.set(0, rnd.nextInt(worldSize));
						newPos.set(1, rnd.nextInt(worldSize));
					} while(positions.contains(newPos));
					positions.add(newPos);
					writePosition(r, newPos.get(0), newPos.get(1));
				}
			}
		}
		else{
			ArrayList< ArrayList<Double> > allPackageLosses = new ArrayList<>();
			ArrayList<Double[]> confidenceIntervals = new ArrayList<>(6);
			
			for(int i = 0; i < 6; i++){
				confidenceIntervals.add(new Double[3]);
			}

			double[] throughputs = new double[6];
			int maxiter = 20;
			for(int run = 0; run < maxiter; run++) {
				ArrayList<Double> thisRunPackageLosses = new ArrayList<>();
				for (int radiusIdx = 0; radiusIdx < radiusSize.length; radiusIdx++) {
					double fName = radiusSize[radiusIdx];
						try {
							// Read config file
							FileReader _reader = new FileReader(String.valueOf(fName));
							BufferedReader reader = new BufferedReader(_reader);

							int n = Integer.parseInt(reader.readLine().split("=")[1]);
							double T_p = Double.parseDouble(reader.readLine().split("=")[1]);
							double t_s = Double.parseDouble(reader.readLine().split("=")[1]);
							double r = Double.parseDouble(reader.readLine().split("=")[1]);
							int gx = Integer.parseInt(reader.readLine().split("=")[1]);
							int gy = Integer.parseInt(reader.readLine().split("=")[1]);

							Gateway gateway = new Gateway(gx, gy, T_p);

							Sensor[] sensors = new Sensor[n];

							for (int i = 0; i < n; i++) {
								int sx = Integer.valueOf(reader.readLine().split("=")[1]);
								int sy = Integer.valueOf(reader.readLine().split("=")[1]);
								sensors[i] = new Sensor(gateway, r, t_s, sx, sy);

								SignalList.SendSignal(ARRIVAL, sensors[i], sensors[i], time + - Math.log(rnd.nextDouble()) * sensors[i].ts);
							}
							reader.close();
							_reader.close();

							// This is the main loop
							while (time < 10000) {
								actSignal = SignalList.FetchSignal();
								time = actSignal.arrivalTime;
								actSignal.destination.TreatSignal(actSignal);
							}
							throughputs[radiusIdx] = (double)gateway.nbrSuccesful / time;
							double packageLoss = (double) gateway.nbrFailed / (gateway.totalAttempts - gateway.nbrOutOfReach);
							thisRunPackageLosses.add(packageLoss);

						} catch (IOException e) {
							java.lang.System.exit(0);
						}
				System.out.println(run);
					time = 0;
				}
				// Update confidence intervals and check for overlap.
				allPackageLosses.add(thisRunPackageLosses);
				if (run > 1){
					for (int i = 0; i < 6; i++) {
						confidenceIntervals.set(i, confidenceInterval.getConfidenceInterval(allPackageLosses, i));
					}
					if (confidenceInterval.anyOverlap(confidenceIntervals)){
						break;
					}
				}
			}
			// Write throughput to file
			for (int i = 0; i < 6; i++) {
				writeThroughput(throughputs[i], radiusSize[i]);
			}
		}
	}

	public static void createFiles(int numSensors, double Tp, double ts, double radius, int gatewayX, int gatewayY){
		// Creates config file.
		String[] s = new String[6];
		s[0] = Integer.toString(numSensors);
		s[1] = Double.toString(Tp);
		s[2] = Double.toString(ts);
		s[3] = Double.toString(radius);
		s[4] = Integer.toString(gatewayX);
		s[5] = Integer.toString(gatewayY);

		String[] name = new String[6];
		name[0] = "n=";
		name[1] = "Tp=";
		name[2] = "ts=";
		name[3] = "r=";
		name[4] = "gx=";
		name[5] = "gy=";

		String path = Double.toString(radius);
		for (int i = 0; i < 6; i++){
			String str = name[i] + s[i];
			writer(path, str);
		}
	}

	public static void writer(String path, String s) {
		/**
		 * Writes a string to a file.
		 * @param f File to write to.
		 * @param s string to be written on the file.
		 */
		try {
			FileWriter fw = new FileWriter(path, true);
				fw.write(s);
				fw.write(System.lineSeparator());
				fw.close();
		} catch (IOException ex) {
			System.err.println("Could not write to file.");
		}
	}

	public static void writePosition(double r, int x, int y){
		// Writes position to config file
		writer(String.valueOf(r), String.format("x=%d", x));
		writer(String.valueOf(r), String.format("y=%d", y));
	}

	public static void writeThroughput(double throughput, int r){
		// Write throughput to file
		String path = "Throughputs";
		writer(path, String.format("r = %d: %f", r, throughput));
	}
}