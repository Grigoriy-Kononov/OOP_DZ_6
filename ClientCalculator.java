import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientCalculator {
    public static void main(String[] args) {
        System.out.println("Подключение к серверу...");
        try (Socket connessioneServer = new Socket("localhost", 5000)) {
            String IPServer = connessioneServer.getRemoteSocketAddress().toString();
            String IPLocale = connessioneServer.getLocalAddress().toString();
            System.out.println("Установленное соединение с сервером:\nIP удаленный сервер: " + IPServer
                    + "\nIP-клиент: " + IPLocale);

            BufferedReader streamInput = new BufferedReader(new InputStreamReader(connessioneServer.getInputStream()));
            PrintWriter invioDati = new PrintWriter(new OutputStreamWriter(connessioneServer.getOutputStream()), true);
            
            InputClient espressioneInput = new InputClient();
            InputClient espressioneInput1 = espressioneInput;
            System.out.println("Отправка выражения на сервер...");
            invioDati.println(espressioneInput1);

            System.out.println("Ожидание ответа сервера...");
            String rispostaServer = streamInput.readLine();

            System.out.println("Получен ответ сервера: " + rispostaServer);

            connessioneServer.close();
            
        } catch (Exception e) {

            System.err.println("Ошибка при подключении к серверу: " + e.getMessage());
        }
    }


}