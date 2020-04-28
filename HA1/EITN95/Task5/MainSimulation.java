import java.io.*;
//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global{

	public static void main(String[] args) throws IOException {

    	// The signal list is started and actSignal is declaree. actSignal is the latest signal that has been fetched from the
    	// signal list in the main loop below.

    	Signal actSignal;
    	new SignalList();
    	// Here process instances are created (two queues and one generator) and their parameters are given values.

		Gen generator = new Gen();
		int subTask = 1;
		int nbrQueues = 5;
		Dispatcher dispatcher = new Dispatcher(subTask, nbrQueues);
		for (int i = 0; i < nbrQueues; i++){
			dispatcher.queues.add(new QS());
		}
		generator.sendTo = dispatcher;

    	//To start the simulation the first signals are put in the signal list
    	SignalList.SendSignal(READY, generator, time);
    	SignalList.SendSignal(MEASURE, dispatcher, 5);

		// This is the main loop
    	while (time < 100000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}
    	double accumulatedTime = 0;
    	int nbrAccumulatedJobs = 0;
		for (QS queue : dispatcher.queues) {
			accumulatedTime += queue.accumulatedTime;
			nbrAccumulatedJobs += queue.nbrAccumulated;
		}

    	//Finally the result of the simulation is printed below:

    	System.out.println("Mean number of jobs in the queuing system: " + (double)dispatcher.nbrAccumulated/dispatcher.nbrMeasurements);
		System.out.println("Mean time per job: " + (double)accumulatedTime/nbrAccumulatedJobs);

	}
}