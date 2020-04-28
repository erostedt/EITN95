import java.util.*;
import java.io.*;

class State extends GlobalSimulation{

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	private final int N = 100, x = 10, lambda = 4, T = 4;
	public final int  M = 4000;
	public int busyServers = 0, nbrMeasurements = 0;
	
	Random rnd = new Random(); // This is just a random number generator

	File file = new File("N=" + N + "x=" + x + "lambda=" + lambda + "T=" + T + "M=" + M);

	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVAL:
				arrival();
				break;
			case READY:
				ready();
				break;
			case MEASURE:
				measure();
				break;
		}
	}


	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.

	private void arrival(){
		/**
		 * If there exist a non busy servers, increment the number of busy servers and schedule a new departure.
		 * Schedule a new arrival.
		 */
		if (busyServers < N) {
			busyServers++;
			insertEvent(READY, time + x);
		}
		insertEvent(ARRIVAL, time - Math.log(rnd.nextDouble())/lambda);
	}

	private void ready(){
		/**
		 * Decrements the number of busy servers.
		 */
		busyServers--;
	}

	private void measure(){
		/**
		 * Update measurements, write measurement to file and schedule new measurement.
		 */
		nbrMeasurements++;
		writer(file, nbrMeasurements + "," + busyServers);
		insertEvent(MEASURE, time + T);
	}

	public void writer(File f, String s) {
		/**
		 * Writes a string to a file.
		 * @param f File to write to.
		 * @param s string to be written on the file.
		 */
		try {
			FileWriter fw = new FileWriter(f, true);
			fw.write(s);
			fw.write(System.lineSeparator());
			fw.close();
		} catch (IOException ex) {
			System.err.println("Could not write to file.");
		}
	}
}