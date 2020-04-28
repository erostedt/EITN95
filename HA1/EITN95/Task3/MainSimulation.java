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
    	
		System.out.println("Mean number of customers in queuing network: " + (double)actState.nbrAccumulated/actState.nbrMeasurements);
		System.out.println("Mean time a customer spends in the queuing network " + (double)actState.accumulatedTime/actState.finishedCostumers);
	}
}
