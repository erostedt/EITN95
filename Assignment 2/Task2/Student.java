//It inherits Proc so that we can use time and the signal names without dot notation

class Student extends Proc {
	Hall hall;
	int id, x, y, steps, maxSteps;
	int[] dir;
	double vel;
	int[] timeWithEach;
	


	public Student(Hall hall, int id, int x, int y, double vel){
		this.hall = hall;
		this.id = id;
		this.x = x;
		this.y = y;
		this.dir = getDir(x, y);
		this.vel = vel;

		timeWithEach = new int[numStudents];
		for (int i = 0; i < numStudents; i++){
			timeWithEach[0] = 0;
		}
	}

	// What to do when a signal arrives
	public void TreatSignal(Signal x) {
		if (x.signalType == FIND_OTHER_STUDENT) {
			Tile currentTile = this.hall.tiles[this.x][this.y];

			if (currentTile.students.size() >= 2 ){
				// There are more than one student on the tile
				if (this.equals(currentTile.students.get(0))){
					// First person in list
					Student other = currentTile.students.get(1);
					// Update how long the two people has been talking
					this.timeWithEach[other.id] += 60;
					other.timeWithEach[this.id] += 60;

					// Update who knows who
					if (!pairs[this.id][other.id]){
						numPairs = numPairs + 2;
						pairs[this.id][other.id] = true;
						pairs[other.id][this.id] = true;
					}
				}
				else if (this.equals(currentTile.students.get(1))){
					// Second person in the list
					SignalList.SendSignal(MOVE, this, time + 60);
					SignalList.SendSignal(MOVE, currentTile.students.get(0), time + 60);
				}
				else {
					// Not number one or two in the list, keep moving
					SignalList.SendSignal(MOVE, this, time);
				}
			}
			else {
				// Only one on the tile, keep moving
				SignalList.SendSignal(MOVE, this, time);
			}
		}
		else if (x.signalType == MOVE){
			double timeToMove = move(this);
			SignalList.SendSignal(FIND_OTHER_STUDENT, this, time + timeToMove);
		}
	}


	private double move(Student student){
		if(outside(student.x + student.dir[0], student.y + student.dir[1]) || student.maxSteps <= student.steps) {
			// If we move outside, get a new direction, new maximum steps and reset the step counter
			student.dir = getDir(student.x, student.y);
			student.steps = 0;
			student.maxSteps = rnd.nextInt(10) + 1;
		}
		int[] newPos = {student.x + student.dir[0], student.y + student.dir[1]};
		double dist = Math.sqrt(Math.pow(student.dir[0], 2) + Math.pow(student.dir[1], 2));
		student.hall.moveStudent(student, newPos);
		student.x = newPos[0];
		student.y = newPos[1];
		student.steps++;

		// Returns the time it takes to move to the square depending on diagonal or straight move
		return dist / student.vel;
	}


	private boolean outside(int x, int y){
		return x > hall.size - 1 || x < 0 || y > hall.size - 1 || y < 0;
	}


	private int[] getDir(int x, int y){
		int[] dir = new int[2];
		do{
			dir[0] = rnd.nextInt(3) - 1;
			dir[1] = rnd.nextInt(3) - 1;
		} while ((dir[0] == 0 && dir[1] == 0) || (outside(x + dir[0], y + dir[1])));
		return dir;
	}
}
