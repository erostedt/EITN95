import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that should be used
    	// Some events must be put in the event list at the beginning

        insertEvent(ARRIVAL_Q1, 0);
		insertEvent(MEASURE, 5);


		// The main simulation loop
    	while (actState.nbrMeasurements < 1000){
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}
    	
    	// Printing the result of the simulation, in this case a mean value

		System.out.println("Probability that a costumer is rejected: " + (double)actState.nbrRejected/actState.totalArrivals);
		System.out.println("Mean number of costumers in Q2: " + (double)actState.nbrAccumulated/actState.nbrMeasurements);
	}
}