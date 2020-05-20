
import java.util.List;
import java.util.ArrayList;
import java.lang.Integer;
import java.util.Random;

//It inherits Proc so that we can use time and the signal names without dot notation

class Sensor extends Proc {
	Gateway gateway;
	int x, y;
	double r;
	double t_s;
	static List<List<Integer>> allPos = new ArrayList<>();
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
		if (inRange) {
			SignalList.SendSignal(START_TRANSMISSION, this, this.gateway, time);
		} else {
			SignalList.SendSignal(OUT_OF_REACH, this, this.gateway, time);
		}
		SignalList.SendSignal(START_TRANSMISSION, this,this, time + getSleep());
	}

	public static List<Integer> generatePosition(){
		List<Integer> pos = new ArrayList<>();
		do {
			pos.add(slump.nextInt(10000));
			pos.add(slump.nextInt(10000));
		} while(allPos.contains(pos));
		allPos.add(pos);
		return pos;
	}

	private boolean checkDistance(Gateway g){
		return Math.hypot(this.x - g.x, this.y - g.y) <= this.r;
	}

	private double getSleep(){
		return -Math.log(slump.nextDouble())*t_s;
	}

}