import java.util.Scanner;

public class InputClient {

    public String InputClient() {
        
        Scanner inputStreamReader = new Scanner(System.in);
        System.out.println("Введите выражение для вычисления: ");
        String espressioneInput = inputStreamReader.nextLine();
        inputStreamReader.close();
        return espressioneInput;

    }
}
