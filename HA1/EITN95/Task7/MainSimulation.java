import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that should be used
    	// Some events must be put in the event list at the beginning

		// The main simulation loop
		double accumulatedTime = 0;
		int runs = 1000;
		for (int run = 0; run < runs; run++) {
			actState.brokenComps = new boolean[5];
			actState.scheduleBreakDowns();
			while (!actState.allBrokenDown(actState.brokenComps)) {
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
			}
			accumulatedTime += time;
		}
    	// Printing the result of the simulation, in this case a mean value

		System.out.println("Average life length of system: " + accumulatedTime/runs);
	}
}