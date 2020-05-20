import java.util.*;
import java.io.*;
import java.io.FileReader;
import java.io.BufferedReader;

//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global {

	public static void main(String[] args) throws IOException {

		// The signal list is started and actSignal is declared. actSignal is the latest signal that has been fetched from the
		// signal list in the main loop below.
		int[] n = {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000};
		double throughput = 0;
		Signal actSignal;
		new SignalList();
		/*
		for (int n_ : n) {
			FileHandler.createConfigFile(n_, 7000, 4000, 1, 5000, 5000);
		}
		*/
		boolean CIoverlap = true;

		ArrayList<ArrayList<Double>> packageLosses = new ArrayList<>();
		ArrayList<Double[]> CIs = new ArrayList<>(10);

		for(int i = 0; i <10; i++){
			CIs.add(new Double[3]);
		}

		while (CIoverlap) {
			ArrayList<Double> nPackageLosses = new ArrayList<>();
			for (int n_ : n) {

					try {
						FileReader fr = new FileReader(String.format("config:n=%d:r=7000.0:T_p=1.0:t_s=4000.0", n_));
						BufferedReader br = new BufferedReader(fr);

						System.out.println("Reading " + br.readLine() + "...");
						n_ = Integer.parseInt(br.readLine().split("=")[1]);
						double r = Double.parseDouble(br.readLine().split("=")[1]);
						double T_p = Double.parseDouble(br.readLine().split("=")[1]);
						double t_s = Double.parseDouble(br.readLine().split("=")[1]);


						System.out.println("Reading " + br.readLine() + "...");
						String[] gateway_pos = br.readLine().split(",");
						// Create gateway

						Gateway gateway = new Gateway(Integer.parseInt(gateway_pos[0]), Integer.parseInt(gateway_pos[1]), T_p);

						System.out.println("Reading " + br.readLine() + "...");
						// Create all sensors
						List<Sensor> sensors = new ArrayList<>();

						for (int i = 0; i < n_; i++) {
							String[] pos = br.readLine().split(",");
							sensors.add(new Sensor(gateway, r, t_s, Integer.parseInt(pos[0]), Integer.parseInt(pos[1])));

							//To start the simulation the first signals are put in the signal list
							SignalList.SendSignal(START_TRANSMISSION, sensors.get(i), sensors.get(i), time + 100*Sensor.slump.nextDouble());

						}
						br.close();
						fr.close();

						// This is the main loop
						while (time < 10000) {
							//long startTime = System.nanoTime();
							actSignal = SignalList.FetchSignal();
							time = actSignal.arrivalTime;
							actSignal.destination.TreatSignal(actSignal);
							//long endTime = System.nanoTime();
							//System.out.println(actSignal.destination + "," + actSignal.signalType + ": " + (endTime - startTime));
						}

						double packageLoss = (double) gateway.failedTransmissions / (gateway.totalTransmissions - gateway.nbOutOfReach);
						nPackageLosses.add(packageLoss);
						throughput = (double) (gateway.successfulTransmissions) / time;

					} catch (IOException e) {
						System.out.println("Couldn't read from config file, exiting.");
						java.lang.System.exit(0);
					}
					time = 0;
				//Finally the result of the simulation is printed below:
				System.out.println("Throughput for n=" + n_ + ":   " + throughput);

			}
			packageLosses.add(nPackageLosses);
			for (int i = 0; i < 10; i++) {
				CIs.set(i, CI.calc95CI(packageLosses, i));
			}
			CIoverlap = CI.overlappingCI(CIs);

		}
		for (Double[] CI : CIs) {
			System.out.println("Mean package loss: " + CI[1]);
			System.out.println(CI[0] + ", " + CI[2]);
		}
	}
}