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
		double delay = 0;
		// See if this signal would get overlapped, if so, delay transmission attempt 
		if(overlappingSignal()){
			delay = getDelay(1, 5);
		}
		// If sensor can reach sensor, send arrival signal
		if (isReachable) {
		SignalList.SendSignal(ARRIVAL, this, this.gateway, time + delay);
		}
		// If sensor cannot reach gateway, send out of range signal.
		else {
			SignalList.SendSignal(OUT_OF_REACH, this, this.gateway, time + delay);
		}
		// Generate new signal.
		SignalList.SendSignal(ARRIVAL, this,this, time - Math.log(rnd.nextDouble()) * ts + delay);
		
	}

	private boolean isReachable(Gateway g){
		// Check if sensor can reach gateway.
		return Math.pow(this.x - g.x, 2) +  Math.pow(this.y - g.y, 2) <= Math.pow(this.r, 2);
	}

	private boolean overlapsWithOther(Sensor sensor1, Sensor sensor2){
		// check if sensor 1 can overlap with sensor 2
		return Math.pow(sensor1.x - sensor2.x, 2) +  Math.pow(sensor1.y - sensor2.y, 2) <= Math.pow(sensor1.r, 2);
	}

	private boolean overlappingSignal(){
		// Checks any other signal would overlap with the signal that this sensor would like to send.
		for (Sensor sensor: transmitting){
			if (overlapsWithOther(this, sensor)){
				return true;
			}
		}
		return false;
	}

	private double getDelay(double lowerBound, double upperBound){
		// gets random delay.
		return lowerBound + (upperBound - lowerBound) * rnd.nextDouble();
	}

}