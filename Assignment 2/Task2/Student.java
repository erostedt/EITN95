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

	public void TreatSignal(Signal x) {
		/**
		 * Treats the signal
		 */

		if (x.signalType == FIND_OTHER_STUDENT) {
			Tile currentTile = this.hall.tiles[this.x][this.y];

			// We shall only investigate further if there are at least two people on tile
			if (currentTile.students.size() >= 2 ){

				// If this person is the first to the tile.
				if (this.equals(currentTile.students.get(0))){

					Student other = currentTile.students.get(1);
					// Update the first person and the second persons times and update that they have met
					this.timeWithEach[other.id] += 60;
					other.timeWithEach[this.id] += 60;

					if (!pairs[this.id][other.id]){
						numPairs = numPairs + 2;
						pairs[this.id][other.id] = true;
						pairs[other.id][this.id] = true;
					}
				}
				// If this person is the second to the tile we schedule that this student and the student which this student talks to
				// to move.
				else if (this.equals(currentTile.students.get(1))){

					SignalList.SendSignal(MOVE, this, time + 60);
					SignalList.SendSignal(MOVE, currentTile.students.get(0), time + 60);
				}
				else {
					// If this student was not one of the two first, then this student moves.
					SignalList.SendSignal(MOVE, this, time);
				}
			}
			else {
				// If this students is alone on the tile, s/he should move.
				SignalList.SendSignal(MOVE, this, time);
			}
		}
		else if (x.signalType == MOVE){
			// Moves and look for other student when arriving.
			double timeToMove = move(this);
			SignalList.SendSignal(FIND_OTHER_STUDENT, this, time + timeToMove);
		}
	}

	private double move(Student student){
		// Move the student and returns the time it takes to walk.
		if(outside(student.x + student.dir[0], student.y + student.dir[1]) || student.maxSteps <= student.steps) {
			// If outside or student have reached maxSteps, get new dir, set steps in that dir to zero and get new maxSteps
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

		return dist / student.vel;
	}


	private boolean outside(int x, int y){
		// Checks if outside the board
		return x > hall.size - 1 || x < 0 || y > hall.size - 1 || y < 0;
	}


	private int[] getDir(int x, int y){
		// Get new dir, cannot be (0, 0) since then we get stuck.
		int[] dir = new int[2];
		do{
			dir[0] = rnd.nextInt(3) - 1;
			dir[1] = rnd.nextInt(3) - 1;
		} while ((dir[0] == 0 && dir[1] == 0) || (outside(x + dir[0], y + dir[1])));
		return dir;
	}
}
