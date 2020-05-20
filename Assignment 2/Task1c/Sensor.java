
import java.util.*;

//It inherits Proc so that we can use time and the signal names without dot notation

class Sensor extends Proc {
	Gateway gateway;
	int x, y;
	double r;
	double t_s;
	double lb = 1.5;
	double ub = 2.5;
	static List<int[]> allPos = new ArrayList<>();
	List<Sensor> currentlyTransmitting = new ArrayList<>();
	boolean inRange;

	public Sensor(Gateway g, double r, double t_s, int x, int y){
		this.gateway = g;
		this.r = r;
		this.t_s = t_s;
		this.x = x;
		this.y = y;
		this.inRange = checkDistance(g);
	}

	//The random number generator is started:
	static Random slump = new Random();

	// What to do when a signal arrives
	public void TreatSignal(Signal x) {
		if (x.signalType == START_TRANSMISSION) {
			double wait = 0;
			if (transmissionDetected()) {
				wait = lb + (ub - lb)*slump.nextDouble();
			}
			if (inRange) {
				SignalList.SendSignal(START_TRANSMISSION, this, this.gateway, time + wait);
			} else {
				SignalList.SendSignal(OUT_OF_REACH, this, this.gateway, time + wait);
			}
			SignalList.SendSignal(END_TRANSMISSION, this, this, time + gateway.T_p + wait);
			SignalList.SendSignal(ADD_TO_CURRENTLY_TRANSMITTING, this, this, time + wait);
			SignalList.SendSignal(START_TRANSMISSION, this, this, time + getSleep() + wait);
		}
		else if (x.signalType == END_TRANSMISSION){
			currentlyTransmitting.remove(this);
		}
		else if (x.signalType == ADD_TO_CURRENTLY_TRANSMITTING){
			currentlyTransmitting.add(this);
		}
	}

	public static int[] generatePosition(){
		int[] pos = new int[2];
		do {
			pos[0] = slump.nextInt(10000);
			pos[1] = slump.nextInt(10000);
		} while(allPos.contains(pos));
		allPos.add(pos);
		return pos;
	}

	private boolean checkDistance(Gateway g){
		return Math.hypot(this.x - g.x, this.y - g.y) <= this.r;
	}

	private boolean checkDistance(Sensor s){
		return Math.hypot(this.x - s.x, this.y - s.y) <= this.r;
	}

	private double getSleep(){
		return -Math.log(slump.nextDouble())*t_s;
	}

	private boolean transmissionDetected(){
		for(Sensor s : currentlyTransmitting){
			if (checkDistance(s)){
				return true;
			}
		}
		return false;
	}
}