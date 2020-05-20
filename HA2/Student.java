

public class Student extends Proc{
    int id;
    int x,y;
    int destination_x,destination_y;
    int veloicty;
    int[] frendship = new int[size];
    int[] direction = new int[2];
    Boolean talking = false; 

    public Student(int veloicty,int x,int y,int id,int[] diretion,int destination_x, int destination_y){
        this.veloicty = veloicty;
        this.x = x;
        this.y = y;
        this.id = id;
        this.direction = diretion;
        this.destination_x = destination_x;
        this.destination_y = destination_y;
        room[x][y]++;
    }

	@Override
	public void TreatSignal(Signal s) {
		switch(s.signalType){

            case Moving:
                if(!talking){
                    if(room[x][y] == 2){
                        //find who else is here
                        int i = 0;
                        Student friend;
                        do{
                            friend = friendList[i];
                            i++;
                        }while(friend.equals(this) || (friend.x != this.x && friend.y != this.y));

                        talking = true;
                        friend.talking = true;
                        
                        frendship[friend.id] += 60;
                        friend.frendship[id] += 60;
                        if(friendMatrix[friend.id][id] == 0){
                            friendMatrix[friend.id][id] = 1;
                            friendMatrix[id][friend.id] = 1; 
                        }

                        SignalList.SendSignal(StartMovingAgain, s.destination, time + 60,s.destination_x,s.destination_y);
                        SignalList.SendSignal(StartMovingAgain, friend, time + 60,destination_x,destination_y);
                        break;
                    }
                    if(x == destination_x && y == destination_y){
                        int direction[] = chosedirection(x, y);
                        SignalList.SendSignal(Moving, this, time+timespent(direction),direction[0],direction[1]);
                        break;
                    }else{
                        if(IsInWall(x+direction[0],y+direction[1])){
                            //Manhatan stepsleft
                            int stepsleft;
                            if(Math.abs(direction[0]) > 0 && Math.abs(direction[1]) > 0){
                                stepsleft = (int) ((int) Math
                                        .sqrt(Math.abs(x - destination_x) + Math.abs(y - destination_y))
                                        / Math.sqrt(x));
                            }
                            stepsleft = Math.abs(x-destination_x) + Math.abs(y-destination_y);
                            int direction[] = chosedirection(x,y);
                            this.direction = direction;
                            room[x][y]--;
                            room[x+direction[0]][y+direction[1]]++;
                            SignalList.SendSignal(Moving, this, time+timespent(direction), x+stepsleft*direction[0],y+stepsleft*direction[1]);
                            break;
                        }else{
                            room[x][y]--;
                            room[x+direction[0]][y+direction[1]]++;
                            SignalList.SendSignal(Moving, this, time+timespent(direction), destination_x, destination_y);
                        }
                }
            }break;
            
            case StartMovingAgain:
                talking = false;
                SignalList.SendSignal(Moving, this, time, destination_x,destination_y);
        }
		
    }
    

    private double timespent(int[] direction){
        if(Math.abs(direction[0])+Math.abs(direction[0]) == 2){
            return Math.sqrt(2)/veloicty;
        }else{
            return 1/veloicty;
        }
    }


    

}