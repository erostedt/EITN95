public class GlobalSimulation{
	
	// This class contains the definition of the events that shall take place in the
	// simulation. It also contains the global time, the event list and also a method
	// for insertion of events in the event list. That is just for making the code in
	// MainSimulation.java and State.java simpler (no dot notation is needed).
	
	public static final int COMP_1 = 0, COMP_2 = 1, COMP_3 = 2, COMP_4 = 3, COMP_5 = 4; // The events, add or remove if needed!
	public int comps[] = {COMP_1, COMP_2, COMP_3, COMP_4, COMP_5};
	public static double time = 0; // The global time variable
	public static EventListClass eventList = new EventListClass(); // The event list used in the program
	public static void insertEvent(int type, double TimeOfEvent){  // Just to be able to skip dot notation
		eventList.InsertEvent(type, TimeOfEvent);
	}
}