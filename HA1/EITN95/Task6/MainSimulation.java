import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that should be used
    	// Some events must be put in the event list at the beginning

		// The main simulation loop
		double accumulatedTimeOverAll = 0;
		int nbrOfDays = 1000;
		int startOfDay = 9;
		for (int day = 0; day < nbrOfDays; day++) {
			insertEvent(ARRIVAL, 0);
			while (!actState.endOfDay) {
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
			}
			accumulatedTimeOverAll += time;
			actState.endOfDay = false;
		}
		// Converts measurements into hours and minutes.
		double endTime = accumulatedTimeOverAll/nbrOfDays;
		int endHour = startOfDay + (int) endTime;
		int endMinute = ((int) (endTime * 60)) % 60;
		String endMinuteString;
		if (endMinute < 10){
			endMinuteString = "0" + Integer.toString(endMinute);
		}
		else{
			endMinuteString = Integer.toString(endMinute);
		}

		double averageTime = (double) actState.accumulatedTime/actState.nbrAccumulated;
		int averageTimeHour = (int) averageTime;
		int averageTimeMinutes = ((int) (averageTime * 60)) % 60;

		System.out.println("Average time when the work is done: " + endHour + ":" + endMinuteString + ".");
		System.out.println("Average from arrival of a prescription until it has been filled in: " + averageTimeHour + "h " + averageTimeMinutes + "m.");
	}
}