import java.util.*;
import java.lang.Math;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int nbrInQueue1 = 0, nbrInQueue2 = 0, nbrAccumulated = 0, nbrMeasurements = 0, finishedCostumers = 0;
	public final double MEAN_SERVICE_TIME = 1, ARRIVAL_TIME = 1.1;
	public double accumulatedTime = 0;
	ArrayList<Double> times = new ArrayList<Double>();	//Arraylist which holds arrival times of costumers.

	Random rnd = new Random(); // This is just a random number generator

	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVAL_Q1 :
				arrivalQ1();
				break;
			case ARRIVAL_Q2 :
				arrivalQ2();
				break;
			case READY_Q1:
				readyQ1();
				break;
			case READY_Q2:
				readyQ2();
				break;
			case MEASURE:
				measure();
				break;
		}
	}
	
	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.
	
	private void arrivalQ1(){
		/**
		 * Inserts new costumer time in list. If queue is empty, schedules new departure from queue 1.
		 * Increments queue size and schedules new arrival.
		 */
		times.add(time);
		if (nbrInQueue1 == 0) {
			insertEvent(READY_Q1, time - Math.log(rnd.nextDouble())*MEAN_SERVICE_TIME);
		}
		nbrInQueue1++;
		insertEvent(ARRIVAL_Q1, time - Math.log(rnd.nextDouble())*ARRIVAL_TIME);
	}
	private void arrivalQ2(){
		/**
		 * Schedules new departure from queue 2 if queue is empty, increments number in queue 2.
		 */
		if (nbrInQueue2 == 0) {
			// If this is the first person in line, schedule a "Ready"
			insertEvent(READY_Q2, time - Math.log(rnd.nextDouble())*MEAN_SERVICE_TIME);
		}
		nbrInQueue2++;
	}
	
	private void readyQ1(){
		/**
		 * Decrements size of queue 1, schedules arrival to queue 2 and if there are still people in queue 1,
		 * schedules new departure.
		 */
		nbrInQueue1--;
		insertEvent(ARRIVAL_Q2, time);
		if (nbrInQueue1 > 0) {
			insertEvent(READY_Q1, time - Math.log(rnd.nextDouble())*MEAN_SERVICE_TIME);
		}
	} 
		
	private void readyQ2(){
		/**
		 * Increments accumulated time of costumer time spent in queues. 
		 * Decrements size of queue 2 and if still people in queue 2, schedules a new departure.
		 * Lastly increments total number of costumers that have exited the queue system.
		 */
		accumulatedTime += (time - times.remove(0));
		nbrInQueue2--;
		if (nbrInQueue2 > 0){
			// If there is still someone left in the queue, schedule a new "Ready"
			insertEvent(READY_Q2,time - Math.log(rnd.nextDouble())*MEAN_SERVICE_TIME);
		}
		finishedCostumers++;
	}
	
	private void measure(){
		/**
		 * Updates measurements and schedules new.
		 */
		nbrAccumulated += nbrInQueue1 + nbrInQueue2;
		nbrMeasurements++;
		insertEvent(MEASURE, time - Math.log(rnd.nextDouble())*5);
	}
}