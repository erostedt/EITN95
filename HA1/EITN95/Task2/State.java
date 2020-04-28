import java.util.*;
import java.lang.Math;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int subTask;		// Simulation changes for the different subtasks.
	public int nbrOfAInQueue = 0, nbrAccumulated = 0, nbrMeasurements = 0, nbrOfBInQueue = 0;	//Variables for measurements.
	public double SERVICE_TIME_A = 0.002, SERVICE_TIME_B = 0.004, d = 1;	// Variables for simulation.

	State(int subTask){
		/**
		 * Assigns subtask.
		 */
		this.subTask = subTask;
	}

	Random rnd = new Random(); // This is just a random number generator

	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVAL_A :
				arrivalA();
				break;
			case ARRIVAL_B:
				arrivalB();
				break;
			case READY_A:
				readyA();
				break;
			case READY_B:
				readyB();
				break;
			case MEASURE:
				measure();
				break;
		}
	}
	
	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.
	
	private void arrivalA(){
		/**
		 * If queue is empty, schedules a new ready_A (job finished of type A). Increases number of A jobs in the queue
		 * and schedules a new Job A arrival.
		 */
		if (nbrOfAInQueue + nbrOfBInQueue == 0) {
			insertEvent(READY_A, time + SERVICE_TIME_A);
		}
		nbrOfAInQueue++;
		insertEvent(ARRIVAL_A, time - Math.log(rnd.nextDouble())/150);
	} 

	private void arrivalB(){
		/**
		 * Schedules a new ready_B (job finished of type B) if queue is empty.
		 * increases number of B jobs in queue.
		 */
		if (nbrOfAInQueue + nbrOfBInQueue == 0) {
			// If the current one is the first one in line, schedule a "Ready_B"
			insertEvent(READY_B, time + SERVICE_TIME_B);
		}
		nbrOfBInQueue++;
	}
	
	private void readyA() {
		/**
		 * Decrements number of A jobs and schedule arrival of B Job in accordance to subtask.
		 * Inserts ready event using a helper function.
		 */
		nbrOfAInQueue--;
		if(subTask == 2){
			insertEvent(ARRIVAL_B, time - Math.log(rnd.nextDouble())*d);
		}
		else{
			insertEvent(ARRIVAL_B, time + d);
		}
		insertReady();
	}

	private void readyB() {
		/**
		 * Decrements number of B Jobs in queue and inserts new ready event using helper function.
		 */
		nbrOfBInQueue--;
		insertReady();
	}

	private void insertReady() {
		/**
		 * Schedule ready event in accordance to subtasks (depending on priority of jobs).
		 */
		if (subTask != 3) {
			if (nbrOfBInQueue > 0) {
				insertEvent(READY_B, time + SERVICE_TIME_B);
			} else if (nbrOfAInQueue > 0) {
				insertEvent(READY_A, time + SERVICE_TIME_A);
			}
		} else {
			if (nbrOfAInQueue > 0) {
				insertEvent(READY_A, time + SERVICE_TIME_A);
			} else if (nbrOfBInQueue > 0) {
				insertEvent(READY_B, time + SERVICE_TIME_B);
			}
		}
	}
	
	private void measure(){
		/**
		 * Updates measurements and schedules new measurement.
		 */
		nbrAccumulated += nbrOfAInQueue + nbrOfBInQueue;
		nbrMeasurements++;
		insertEvent(MEASURE, time + 0.1);
	}
}