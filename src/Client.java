import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try (Scanner scan = new Scanner(System.in)) {

            while (true) {
                System.out.println("Enter an integer number (or 'exit' to quit): ");
                String input = scan.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                int num;
                try {
                    num = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter an integer.");
                    continue;
                }

                // Connect to Dictionary
                try (Socket dictSocket = new Socket("localhost", 5000);
                     PrintWriter dictOut = new PrintWriter(dictSocket.getOutputStream(), true);
                     BufferedReader dictIn = new BufferedReader(new InputStreamReader(dictSocket.getInputStream()))) {
                    dictOut.println("GET_SERVER");
                    String serverInfo = dictIn.readLine();
                    String[] parts = serverInfo.split(" ");
                    String serverIp = parts[0];
                    int serverPort = Integer.parseInt(parts[1]);
                    // Connect to Server
                    try (Socket serverSocket = new Socket(serverIp, serverPort);
                         PrintWriter serverOut = new PrintWriter(serverSocket.getOutputStream(), true);
                         BufferedReader serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()))) {

                        serverOut.println("IS_PRIME " + num);
                        String isPrimeResponse = serverIn.readLine();
                        System.out.println("Server response: " + isPrimeResponse);

                        serverOut.println("NEXT_PRIME " + num);
                        String nextPrimeResponse = serverIn.readLine();
                        System.out.println("Server response: " + nextPrimeResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
