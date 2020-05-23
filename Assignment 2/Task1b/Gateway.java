class Gateway extends Proc{

	int x, y;
	double Tp;
	boolean occupied;

	int nbrArrivals = 0, nbrFinished = 0, nbrFailed = 0, nbrSuccesful = 0, nbrOutOfReach = 0, totalAttempts = 0;

	public Gateway(int x, int y, double Tp){
		this.x = x;
		this.y = y;
		this.Tp = Tp;
		occupied = false;
	}
	// What to do when a signal arrives
	public void TreatSignal(Signal x){
		switch (x.signalType){

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

			case FAILED: {
				nbrFinished++;
				nbrFailed++;
				if(nbrArrivals == nbrFinished){
					occupied = false;
				}
			} break;


			case OUT_OF_REACH: {
				nbrOutOfReach++;
				totalAttempts++;
			} break;
		}
	}
}
