import java.util.Random;

public class Global {
	public static final int size = 20;
	public static final int Moving = 0, StartMovingAgain = 1;
	public static double time = 0;
	public static Random rand = new Random();
	public static int[][] room = new int[size][size];
	public static int[][] friendMatrix = new int[size][size]; //symmetric matrix
	public static Student[] friendList = new Student[size];

	public static int[] chosedirection(int x, int y){
        int dir_x, dir_y; 
        do{
            dir_x = rand.nextInt(3) -1; 
            dir_y = rand.nextInt(3) -1; 
        }while(dir_x == 0 && dir_y == 0 && IsInWall(dir_x, dir_y));
		int direction[] = { dir_x, dir_y };
		return direction;
	}

	public static boolean IsInWall(int dir_x, int dir_y) {
        return (dir_x < 0 || dir_x >= size || dir_y < 0 || dir_y >= size);
    }
}
