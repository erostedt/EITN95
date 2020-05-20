import java.io.*;
//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global{

	public static void main(String[] args) throws IOException {

    	// The signal list is started and actSignal is declaree. actSignal is the latest signal that has been fetched from the
    	// signal list in the main loop below.

    	Signal actSignal;
    	new SignalList();
    	// Here process instances are created (two queues and one generator) and their parameters are given values.

		int veloicty = 2;
		for(int i = 0; i < size;i++){
			int x = rand.nextInt(20);
			int y = rand.nextInt(20);
			int[] diretion = new int[2];
			diretion = chosedirection(x,y);
			int steps = rand.nextInt(10)+1;
			Student s = new Student(veloicty, rand.nextInt(20), rand.nextInt(20), i, diretion, x+steps*diretion[0], y+steps*diretion[1]);
			friendList[i] = s;
			SignalList.SendSignal(Moving, s, time, x+steps*diretion[0], y+steps*diretion[1]);
		}
		int uniquefriends = 0;
		// This is the main loop
    	while (uniquefriends < 380){
			uniquefriends = 0;
			for (int c = 0; c < size; c++)
				uniquefriends += friendMatrix[0][c] + friendMatrix[size-1][c];
			for (int r = 0; r < size; r++)
				uniquefriends += friendMatrix[r][0] + friendMatrix[r][size-1];
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
			actSignal.destination.TreatSignal(actSignal);
			for (int c = 0; c < size; c++){
				for (int r = 0; r < size; r++){
					System.out.print(" " +room[r][c]);
				}
				System.out.print("\n");
			}
			System.out.println("------------------------------------------------------------------------------------------");

    	}

		System.out.println(time);

	}
}