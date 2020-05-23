//It inherits Proc so that we can use time and the signal names without dot notation

class Sensor extends Proc {
	Gateway gateway;
	int x, y;
	double r, ts;
	boolean isReachable;

	public Sensor(Gateway g, double r, double ts, int x, int y){
		this.gateway = g;
		this.r = r;
		this.ts = ts;
		this.x = x;
		this.y = y;
		this.isReachable = isReachable(g);
	}

	// What to do when a signal arrives
	public void TreatSignal(Signal x) {
		if (isReachable) {
			SignalList.SendSignal(ARRIVAL, this, this.gateway, time);
		} else {
			SignalList.SendSignal(OUT_OF_REACH, this, this.gateway, time);
		}
		SignalList.SendSignal(ARRIVAL, this,this, time - Math.log(rnd.nextDouble()) * ts);
	}

	private boolean isReachable(Gateway g){
		return Math.pow(this.x - g.x, 2) +  Math.pow(this.y - g.y, 2) <= Math.pow(this.r, 2);
	}

}