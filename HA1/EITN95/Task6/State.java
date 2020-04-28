import java.util.*;
import java.lang.Math;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int nbrInQueue = 0, nbrAccumulated = 0;
	public double accumulatedTime = 0;
	public boolean endOfDay = false;
	ArrayList<Double> times = new ArrayList<>();	// List that holds arrival times.

	Random rnd = new Random(); // This is just a random number generator

	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType) {
			case ARRIVAL:
				arrival();
				break;
			case READY:
				ready();
				break;
		}
	}
	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.
	
	private void arrival(){
		/**
		 * Inserts a new time in the arrival times list. If queue is empty, schedules a new departure.
		 * Increments size of queue and, if the pharmacist still takes prescriptions, schedules a new arrival. 
		 */
		times.add(time);
		if (nbrInQueue == 0) {
			insertEvent(READY, time + (1+rnd.nextDouble())/6);
		}
		nbrInQueue++;

		if (time <= 8) {
			insertEvent(ARRIVAL, time - Math.log(rnd.nextDouble())/4);
		}
	}
	
	private void ready(){
		/**
		 * Updates measurement variables, decrements queue size. Schedules a new departure if queue is not empty.
		 * If Queue is empty and the pharmacist have stopped taken prescriptions, then the workday is over.
		 */
		accumulatedTime += (time - times.remove(0));
		nbrInQueue--;
		nbrAccumulated++;

		if (nbrInQueue > 0) {
			insertEvent(READY, time + (1+rnd.nextDouble())/6);
		}
		if (time > 8 && nbrInQueue == 0){
			endOfDay = true;
		}
	}
}