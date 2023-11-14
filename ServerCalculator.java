import java.io.*;
import java.net.*;
import java.util.*;

public class ServerCalculator {
    static int portaDelServer = 5000;

    public static <T> Stack<T> reverseStack(Stack<T> stack) {
        Stack<T> reversedStack = new Stack<T>();

        while (!stack.empty()) {
            reversedStack.push(stack.pop());
        }

        return reversedStack;
    }

    public static void elabora(Socket clientSocket) {
        try {
            BufferedReader inputStreamClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter outputStreamClient = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()),
                    true);

            System.out.println("Ожидание сообщения от клиента...");
            String messaggioClient = inputStreamClient.readLine();

            System.out.println("Сообщение получено: " + messaggioClient);

            Double rispostaClient = 0.0;
            Stack<Double> numeri = new Stack<Double>();
            Stack<Character> operandi = new Stack<Character>();

            char operando;
            Double num;

            String[] parsedElements = messaggioClient.split("(?<=[-+*^/\\(\\)])|(?=[-+*^/\\(\\)])");

            numeri.push(Double.parseDouble(parsedElements[0]));

            for (int i = 1; i <= parsedElements.length - 2; i += 2) {
                try {
                    operando = parsedElements[i].toCharArray()[0];
                    num = Double.parseDouble(parsedElements[i + 1]);

                    if (operando == '*') {
                        numeri.push(numeri.pop() * num);
                    } else if (operando == '/') {
                        numeri.push(numeri.pop() / num);
                    } else if (operando == '^') {
                        numeri.push(Math.pow(numeri.pop(), num));
                    } else {
                        numeri.push(num);
                        operandi.push(operando);
                    }
                } catch (Exception e) {
                    System.out.println("Плохо отформатированное выражение.");
                }
            }

            numeri = reverseStack(numeri);
            operandi = reverseStack(operandi);

            rispostaClient = numeri.pop();

            while (!numeri.empty() && !operandi.empty()) {
                operando = operandi.pop();
                num = numeri.pop();
                if (operando == '+') {
                    rispostaClient += num;
                } else if (operando == '-') {
                    rispostaClient -= num;
                }
            }

            System.out.println("Отправка ответа клиенту...");
            outputStreamClient.println(rispostaClient);

            System.out.println("Ответ успешно отправлен.");

        } catch (Exception e) {
            System.err.println("Ошибка при обработке данных: " + e.getMessage());
        }

    }

    public static void main(String args[]) {

        try (ServerSocket serverSocket = new ServerSocket(portaDelServer)) {
            System.out.println("Сервер доступен и прослушивает порт: " + portaDelServer + " , IP:"
                    + serverSocket.getLocalSocketAddress());

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    String indirizzoIPclient = clientSocket.getRemoteSocketAddress().toString();

                    System.out.println("Подключен к клиенту, IP: " + indirizzoIPclient);

                    elabora(clientSocket);

                    System.out.println("Закрытие клиентского соединения.");
                    clientSocket.close();
                } catch (Exception e) {
                    System.err.println("Ошибка при подключении к клиенту: " + e.getMessage());
                }

            }

        } catch (Exception e) {
            System.err.println("Ошибка при выполнении кода сервера" + e.getMessage());
        }
    }
}