import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Global{
	public static final int CHECK_FOR_FRIEND = 1, MOVE = 2;
	public static double time = 0;
	public static boolean[][] friends = new boolean[20][20];
	static Random slump = new Random();
	public static int met = 0;

	// Keep track of all starting positions
	static List<List<Integer>> allPos = new ArrayList<>();

}
