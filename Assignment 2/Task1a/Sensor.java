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
		// If can reach the gateway send an arrival
		if (isReachable) {
			SignalList.SendSignal(ARRIVAL, this, this.gateway, time);
		}
		// If cannot reach the gateway, send out of range 
		else {
			SignalList.SendSignal(OUT_OF_REACH, this, this.gateway, time);
		}
		// Schedule new Arrival
		SignalList.SendSignal(ARRIVAL, this,this, time - Math.log(rnd.nextDouble()) * ts);
	}

	// Check if sensor can reach gateway.
	private boolean isReachable(Gateway g){
		return Math.pow(this.x - g.x, 2) +  Math.pow(this.y - g.y, 2) <= Math.pow(this.r, 2);
	}

}