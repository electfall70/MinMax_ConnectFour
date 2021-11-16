//Pol Lozano Llorens - polo0976
import java.util.Scanner;

public class InputHandler {
    private Scanner input = new Scanner(System.in);

    public int readInt(int defaultVal) {
        return tryParse(input.nextLine(), defaultVal);
    }

    public int tryParse(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
    
    public void close() {
        input.close();
    }
}
