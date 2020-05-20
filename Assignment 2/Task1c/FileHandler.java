import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static String path;

    public static void createConfigFile(int n, double r, double t_s, double T_p, int gateway_x, int gateway_y) throws IOException {
        path = "config:n=" + n + ":r=" + r + ":T_p=" + T_p + ":t_s=" + t_s;

        writeToFile("Parameters");
        writeToFile("n=" + n);
        writeToFile("r=" + r);
        writeToFile("T_p=" + T_p);
        writeToFile("t_s=" + t_s);

        // Write Gateway position to file
        writeToFile("Gateway Position");
        writeToFile(gateway_x + "," + gateway_y);

        // Create all sensors and write their positions to file
        writeToFile("Sensor Positions");
        for (int i = 0; i < n; i++) {
            int[] pos = Sensor.generatePosition();
            writeToFile(pos[0] + "," + pos[1]);
        }
    }

    public void readConfigFile(String path){

    }

    public static void writeToFile(String line) throws IOException{
        FileWriter write = new FileWriter(path, true);
        PrintWriter print_line = new PrintWriter(write);

        print_line.printf( "%s" + "%n" , line);
        print_line.close();
        write.close();
    }

    public static void writeOutputFile(){

    }
}
