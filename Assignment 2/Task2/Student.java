import java.lang.*;
import java.util.*;
import java.io.*;

//It inherits Proc so that we can use time and the signal names without dot notation

class Student extends Proc {
	Floor floor;
	int index;
	List<Integer> pos;
	int steps;
	int maxSteps;
	int[] dir;
	double vel;
	List<Integer> timeSpent = new ArrayList<>(Collections.nCopies(20, 0));


	public Student(Floor floor, int index, double vel, List<Integer> pos){
		this.floor = floor;
		this.index = index;
		this.vel = vel;
		this.pos = pos;
		this.steps = 0;
		this.maxSteps = slump.nextInt(10) + 1;
		this.dir = getDirection(this);
	}

	// What to do when a signal arrives
	public void TreatSignal(Signal x) {
		if (x.signalType == CHECK_FOR_FRIEND) {
			Tile currentTile = this.floor.tiles[this.pos.get(0)][this.pos.get(1)];

			if (currentTile.students.size() >= 2 ){
				// There are more than one student on the tile
				if (this.equals(currentTile.students.get(0))){
					// First person in list
					Student friend = currentTile.students.get(1);
					// Update how long the two people has been talking
					this.timeSpent.set(friend.index, this.timeSpent.get(friend.index) + 60);
					friend.timeSpent.set(this.index, friend.timeSpent.get(this.index) + 60);

					// Update who knows who
					if (!friends[this.index][friend.index]){
						met++;
						friends[this.index][friend.index] = true;
						friends[friend.index][this.index] = true;
					}
				}
				else if (this.equals(currentTile.students.get(1))){
					// Second person in the list
					SignalList.SendSignal(MOVE, this, time + 60);
					SignalList.SendSignal(MOVE, currentTile.students.get(0), time + 60);
				}
				else {
					// Not number one or two in the list, keep moving
					double t = move(this);
					SignalList.SendSignal(CHECK_FOR_FRIEND, this, time + t);
					//SignalList.SendSignal(MOVE, this, time);
				}
			}
			else {
				// Only one on the tile, keep moving
				double t = move(this);
				SignalList.SendSignal(CHECK_FOR_FRIEND, this, time + t);
			}
		}
		else if (x.signalType == MOVE){
			double t = move(this);
			SignalList.SendSignal(CHECK_FOR_FRIEND, this, time + t);
		}
	}


	private double move(Student s){
		if(outOfBounds(s.pos.get(0) + s.dir[0], s.pos.get(1) + s.dir[1]) || s.steps >= s.maxSteps) {
			// If we move outside, get a new direction, new maximum steps and reset the step counter
			s.dir = getDirection(s);
			s.steps = 0;
			s.maxSteps = slump.nextInt(10) + 1;
		}
		List<Integer> newPos = new ArrayList<>(Arrays.asList(s.pos.get(0) + s.dir[0], s.pos.get(1) + s.dir[1]));
		s.floor.updateFloor(s, newPos);
		s.pos = newPos;
		s.steps++;

		// Returns the time it takes to move to the square depending on diagonal or straight move
		if (Math.abs(s.dir[0]) == Math.abs(s.dir[1])){
			return Math.sqrt(2)/s.vel;
		} else {
			return 1.0/s.vel;
		}
	}


	private boolean outOfBounds(int x, int y){
		return x > 19 || x < 0 || y > 19 || y < 0;
	}


	private int[] getDirection(Student s){
		int[] d = new int[2];
		do{
			d[0] = slump.nextInt(3) - 1;
			d[1] = slump.nextInt(3) - 1;
		} while ((d[0] == 0 && d[1] == 0) || (outOfBounds(s.pos.get(0) + d[0], s.pos.get(1) + d[1])));
		return d;
	}
}
