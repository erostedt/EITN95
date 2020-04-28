import java.util.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc implements Comparable<QS>{
	public int nbrInQueue = 0, nbrAccumulated = 0;
	ArrayList<Double> times = new ArrayList<>();	// Arraylist with arrival times.
	public double accumulatedTime = 0;
	Random rnd = new Random();

	public void TreatSignal(Signal x){
		switch (x.signalType){
			case ARRIVAL:{
				arrival();
			} break;

			case READY:{
				ready();
			} break;
		}
	}

	public void arrival(){
		/**
		 * Adds arrival time to list. If queue is empty, schedule a new departure. 
		 * Increment queue size.
		 */
		times.add(time);
		if (nbrInQueue == 0){
			SignalList.SendSignal(READY, this, time - Math.log(rnd.nextDouble())*0.5);
		}
		nbrInQueue++;
	}

	public void ready(){
		/**
		 * Updates accumulated time in queue. Decrements queue, increments accumulated number of items and if there are still
		 * items in the queue, schedules a new departure.
		 */
		accumulatedTime += (time - times.remove(0));
		nbrInQueue--;
		nbrAccumulated++;
		if (nbrInQueue > 0){
			SignalList.SendSignal(READY, this, time - Math.log(rnd.nextDouble())*0.5);
		}
	}
	@Override
	public int compareTo(QS other){
		/**
		 * Compare function. Used when comparing size of queues.
		 */
		if(this.nbrInQueue < other.nbrInQueue){
			return -1;
		}
		else if (this.nbrInQueue > other.nbrInQueue) {
			return 1;
		}
		else{
			return 0;
		}
	}
}