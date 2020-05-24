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

	public void TreatSignal(Signal x) {
		// If sensor can reach the gateway, send an arrival
		if (isReachable) {
			SignalList.SendSignal(ARRIVAL, this, this.gateway, time);
		} 
		// If sensor cannot reach the gateway, send a out of reach signal.
		else {
			SignalList.SendSignal(OUT_OF_REACH, this, this.gateway, time);
		}
		// Schedule new arrival to this sensor.
		SignalList.SendSignal(ARRIVAL, this,this, time - Math.log(rnd.nextDouble()) * ts);
	}

	private boolean isReachable(Gateway g){
		// checks if this sensor can reach the gateway.
		return Math.pow(this.x - g.x, 2) +  Math.pow(this.y - g.y, 2) <= Math.pow(this.r, 2);
	}

}