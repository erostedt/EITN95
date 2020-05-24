class Gateway extends Proc{

	int x, y;
	double Tp;
	boolean occupied;

	//Measurements
	int nbrArrivals = 0, nbrFinished = 0, nbrFailed = 0, nbrSuccesful = 0, nbrOutOfReach = 0, totalAttempts = 0;

	public Gateway(int x, int y, double Tp){
		this.x = x;
		this.y = y;
		this.Tp = Tp;
		occupied = false;
	}
	public void TreatSignal(Signal x){
		switch (x.signalType){
			// If arriving signal, send to failed if gateway is occupied, else schedule transmition.
			case ARRIVAL: {
				nbrArrivals++;
				totalAttempts++;
				if(occupied){
					SignalList.SendSignal(FAILED, x.sender, this, time + Tp);
				}
				else{
					SignalList.SendSignal(TRANSMIT, x.sender, this, time + Tp);
					occupied = true;
				}
			} break;
			// Transmit is succesful if no other signal arrived during the transmition time.
			case TRANSMIT: {
				nbrFinished++;
				if(nbrArrivals != nbrFinished){
					nbrFailed++;
				}
				else{
					nbrSuccesful++;
					occupied = false;
				}
				
			} break;
			// If transmission faild we update measurements.
			case FAILED: {
				nbrFinished++;
				nbrFailed++;
				if(nbrArrivals == nbrFinished){
					occupied = false;
				}
			} break;
			// If the signal could not reach the gateway, update measurements.
			// This does not make to much sense to be in the gateway, but its simplest.
			case OUT_OF_REACH: {
				nbrOutOfReach++;
				totalAttempts++;
			} break;
		}
	}
}
