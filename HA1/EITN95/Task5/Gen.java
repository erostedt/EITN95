
import java.util.*;

//It inherits Proc so that we can use time and the signal names without dot notation

class Gen extends Proc {

	//The random number generator is started:
	Random slump = new Random();

	//There are two parameters:
	public Proc sendTo;   //Where to send customers

	// What to do when a signal arrives
	public void TreatSignal(Signal x) {
		SignalList.SendSignal(ARRIVAL, sendTo, time);
		SignalList.SendSignal(READY, this, time + slump.nextDouble()*0.24);
	}
}