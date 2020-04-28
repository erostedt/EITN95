import java.util.*;

class Dispatcher extends Proc{

	int totalJobs = 0, nbrAccumulated = 0, nbrMeasurements = 0;
	int subTask;
	int nbrQueues;
	ArrayList<QS> queues = new ArrayList<>();	//Arraylist which holds all the queues.
	Random rnd = new Random();

	Dispatcher(int subTask, int nbrQueues){
		/**
		 * Assigns the subtask and number of queues.
		 */
		this.subTask = subTask;
		this.nbrQueues = nbrQueues;
	}

	// What to do when a signal arrives
	public void TreatSignal(Signal x){
		switch (x.signalType){
			case ARRIVAL: {
				arrival();
			} break;
			case MEASURE: {
				measure();
			} break;
		}
	}

	private void arrival(){
		/**
		 * Sends arrival signal to some destination in accordance to the helper function getDestination().
		 */
		SignalList.SendSignal(ARRIVAL, getDestination(), time);
	}

	private void measure(){
		/**
		 * Updates measurment variables and schedules new measurement.
		 */
		for (QS queue : queues){
			nbrAccumulated += queue.nbrInQueue;
		}
		nbrMeasurements++;
		SignalList.SendSignal(MEASURE, this, time + rnd.nextDouble()*5);
	}

	private Proc getDestination(){
		/**
		 * Returns which server to choose as destination in accordance to some strategi.
		 */
		if (subTask == 1) {
			// Choose random.
			return queues.get(rnd.nextInt(5));
		}
		else if (subTask == 2) {
			// Choose cyclically. 
			return queues.get(totalJobs++ % nbrQueues);
		}
		else{
			// Pick smallest queue, if tie, choose randomly out of the tied ones.
			ArrayList<QS> queuesCopy = (ArrayList<QS>) queues.clone();
			queuesCopy.sort((q1, q2) -> q1.compareTo(q2));

			int min = queuesCopy.get(0).nbrInQueue;
			int numEquals = 0;
			for (QS queue: queuesCopy){
				if (queue.nbrInQueue == min){
					numEquals++;
				}
				else{
					break;
				}
			}
			return queuesCopy.get(rnd.nextInt(numEquals));
		}
	}
}