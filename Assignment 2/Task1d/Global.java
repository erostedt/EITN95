import java.util.*;

public class Global{
	public static final int ARRIVAL = 1, TRANSMIT = 2, FAILED = 3, OUT_OF_REACH = 4; 
	public static double time = 0;
	static ArrayList<Sensor> transmitting = new ArrayList<>();
	static Random rnd = new Random();

}
