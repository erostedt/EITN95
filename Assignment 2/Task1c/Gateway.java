import java.util.*;
import java.io.*;

import static java.util.Objects.isNull;

//It inherits Proc so that we can use time and the signal names without dot notation


class Gateway extends Proc{

	//The random number generator is started:
	// Gateway position
	int x;
	int y;
	double T_p;
	// Statistics
	int totalTransmissions = 0;
	int failedTransmissions = 0;
	int successfulTransmissions = 0;
	int nbOutOfReach = 0;

	boolean occupied = false;
	boolean overlap = false;

	public Gateway(int x, int y, double T_p){
		this.x = x;
		this.y = y;
		this.T_p = T_p;
	}
	// What to do when a signal arrives
	public void TreatSignal(Signal x){
		switch (x.signalType){
			case START_TRANSMISSION: {
				totalTransmissions++;
				if (!occupied){
					occupied = true;
				} else{
					overlap = true;
				}
				double t = time + T_p;
				SignalList.SendSignal(END_TRANSMISSION, this,this, t);
				SignalList.SendSignal(END_TRANSMISSION, this, x.sender, t);
			} break;
			case END_TRANSMISSION: {
				if(!overlap){
					successfulTransmissions++;
				} else{
					failedTransmissions++;
				}
				if (totalTransmissions == successfulTransmissions + failedTransmissions + nbOutOfReach) {
					overlap = false;
					occupied = false;
				}
			} break;
			case OUT_OF_REACH: {
				totalTransmissions++;
				nbOutOfReach++;
			} break;
		}
	}
}
