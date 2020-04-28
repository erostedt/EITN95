import java.util.*;
import java.lang.Math;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int nbrInQueue1 = 0, totalArrivals = 0, nbrRejected = 0;		// Variables for measurements.
	public int nbrInQueue2 = 0, nbrAccumulated = 0, nbrMeasurements = 0;	// Variables for measurements
	public final double MEAN_SERVICE_TIME_Q1 = 2.1, SERVICE_TIME_Q2 = 2.0, ARRIVAL_TIME = 1;	// Variables for simulation.


	Random rnd = new Random(); // This is just a random number generator

	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVAL_Q1 :
				arrivalQ1();
				break;
			case READY_Q1:
				readyQ1();
				break;
			case ARRIVAL_Q2 :
				arrivalQ2();
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
		 * Increases total arrivals and if first queue is empty, schedules a ready (departure) from the first queue.
		 * If the queue holds less than 10 people then the queue is increased, otherwise the person is rejected.
		 * Lastly we schedule a new arrival to this queue.
		 */
		totalArrivals++;
		if (nbrInQueue1 == 0) {
			insertEvent(READY_Q1, time - Math.log(rnd.nextDouble())*MEAN_SERVICE_TIME_Q1);
		}
		if (nbrInQueue1 < 10) {
			nbrInQueue1++;
		} else {
			nbrRejected++;
		}
		insertEvent(ARRIVAL_Q1, time + ARRIVAL_TIME);
	}
	private void arrivalQ2(){
		/**
		 * Increments the number in the second queue, and if the queue was previously empty,
		 * schedules a ready (departure) from the second queue.
		 */
		if (nbrInQueue2 == 0) {
			insertEvent(READY_Q2, time + SERVICE_TIME_Q2);
		}
		nbrInQueue2++;
	}
	
	private void readyQ1(){
		/**
		* Decreases size of first queue, schedules an arrival to the second queue
		* and if the first queue is not empty, schedules a new ready (depature) from the first queue.
		*/
		nbrInQueue1--;
		insertEvent(ARRIVAL_Q2, time);
		if (nbrInQueue1 > 0) {
			insertEvent(READY_Q1, time - Math.log(rnd.nextDouble())*MEAN_SERVICE_TIME_Q1);
		}
	} 
		
	private void readyQ2(){
		/**
		* Decreases size of second queue and if the queue is not empty, schedules a new ready (depature) from this queue.
		*/
		nbrInQueue2--;
		if (nbrInQueue2 > 0){
			insertEvent(READY_Q2, time + SERVICE_TIME_Q2);
		}
	}
	
	private void measure(){
		/**
		* Measures current queue size and updates the accumulated number and increments the number of measurments.
		* Lastly schedules a new measurement exponentially distributed with mean 5 seconds.
		*/
		nbrAccumulated += nbrInQueue2;
		nbrMeasurements++;
		insertEvent(MEASURE, time - Math.log(rnd.nextDouble())*5);
	}
}