import java.util.*;
import java.io.*;
import java.io.FileReader;
import java.io.BufferedReader;

//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global {

	public static void main(String[] args) throws IOException {

		// The signal list is started and actSignal is declared. actSignal is the latest signal that has been fetched from the
		// signal list in the main loop below.
		int worldSize = 10000;
		int[] numSensors = {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000};
		double[] throughputs = new double[10];
		Signal actSignal;
		new SignalList();

		boolean create = false;
		if (create){
			ArrayList<Integer> newPos;
			ArrayList< ArrayList<Integer> > positions;
			for (int n : numSensors) {
				createFiles(n, 1, 4000, 7000, 5000, 5000);

				positions = new ArrayList<>();
				for(int i = 0; i < n; i++){
					newPos = new ArrayList<>(2);
					newPos.add(rnd.nextInt(worldSize));
					newPos.add(rnd.nextInt(worldSize));
					do {
						newPos.set(0, rnd.nextInt(worldSize));
						newPos.set(1, rnd.nextInt(worldSize));
					} while(positions.contains(newPos));
					positions.add(newPos);
					writePosition(n, newPos.get(0), newPos.get(1));
				}
			}
		}
		else{

			ArrayList< ArrayList<Double> > allPackageLosses = new ArrayList<>();
			ArrayList<Double[]> confidenceIntervals = new ArrayList<>(10);
			
			for(int i = 0; i < 10; i++){
				confidenceIntervals.add(new Double[3]);
			}

			
			int maxiter = 20;
			for(int run = 0; run < maxiter; run++) {

				ArrayList<Double> thisRunPackageLosses = new ArrayList<>();

				for (int numSensorIdx = 0; numSensorIdx < numSensors.length; numSensorIdx++) {
					int fName = numSensors[numSensorIdx];
						try {
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
							throughputs[numSensorIdx] = (double)gateway.nbrSuccesful / time;
							double packageLoss = (double) gateway.nbrFailed / (gateway.totalAttempts - gateway.nbrOutOfReach);
							thisRunPackageLosses.add(packageLoss);

						} catch (IOException e) {
							java.lang.System.exit(0);
						}
					time = 0;
				}
				System.out.println(run);
				allPackageLosses.add(thisRunPackageLosses);
				if (run > 1){
					for (int i = 0; i < 10; i++) {
						confidenceIntervals.set(i, confidenceInterval.getConfidenceInterval(allPackageLosses, i));
					}
					if (confidenceInterval.anyOverlap(confidenceIntervals)){
						break;
					}
				}
			}
			for (int i = 0; i < 10; i++) {
				writeConfInts(confidenceIntervals.get(i), numSensors[i]);
				writeThroughput(throughputs[i], numSensors[i]);
			}
		}
	}

	public static void createFiles(int numSensors, double Tp, double ts, double radius, int gatewayX, int gatewayY){
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

		String path = Integer.toString(numSensors);
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

	public static void writePosition(int n, int x, int y){
		writer(String.valueOf(n), String.format("x=%d", x));
		writer(String.valueOf(n), String.format("y=%d", y));
	}

	public static void writeConfInts(Double[] confInt, int n){
		String path = "Confidence Intervals";
		writer(path, String.format("Confidence Interval for n = %d", n));
		writer(path, String.format("Mean: %f", confInt[1]));
		writer("Confidence Intervals", String.format("[%f, %f]", confInt[0], confInt[2]));
	}

	public static void writeThroughput(double throughput, int n){
		String path = "Throughputs";
		writer(path, String.format("n = %d: %f", n, throughput));
	}
}