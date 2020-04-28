import java.util.*;

class State extends GlobalSimulation{

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	boolean[] brokenComps;	// boolean array.
	Random rnd = new Random(); // This is just a random number generator

	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		breakDown(x.eventType);
	}


	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.

	private void breakDown(int comp){
		switch (comp){
			case COMP_1:
				brokenComps[comp] = true;
				brokenComps[COMP_2] = true;
				brokenComps[COMP_5] = true;
				break;
			case COMP_3:
				brokenComps[comp] = true;
				brokenComps[COMP_4] = true;
				break;
			default:
				brokenComps[comp] = true;
				break;
		}
	}

	public boolean allBrokenDown(boolean[] brokenComps){
		/**
		 * Checks if all elements in array are true.
		 * @param brokenComps boolean array with elements that are true if component is broken and false if not broken.
		 * @return True if all components are broken, false else.
		 */
		for (boolean comp: brokenComps){
			if (comp == false){
				return false;
			}
		}
		return true;
	}

	public void scheduleBreakDowns(){
		/**
		 * Schecules breakdowns for all components.
		 */
		for (int comp: comps){
			insertEvent(comp, 1 + 4*rnd.nextDouble());	
		} 
	}
}