import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(final String[] args) throws IOException {
		Event actEvent;
		final int subTask = 3;
		final boolean writeToFile = true;
		State actState = new State(subTask, writeToFile); // The state that should be used
    	// Some events must be put in the event list at the beginning

        insertEvent(ARRIVAL_A, 0);
		insertEvent(MEASURE, 5);

		// The main simulation loop
    	while (actState.nbrMeasurements < 1000){
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}
    	System.out.println("Mean number of jobs in the buffer: " + (double)actState.nbrAccumulated/actState.nbrMeasurements);
	}
}