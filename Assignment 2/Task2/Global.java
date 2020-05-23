import java.util.Random;

public class Global{
	public static final int FIND_OTHER_STUDENT = 1, MOVE = 2;
	public static double time = 0;
	public static final int numStudents = 20;
	public static boolean[][] pairs = new boolean[numStudents][numStudents];
	static Random rnd = new Random();
	public static int numPairs = 0;
}
