import java.net.*;
import java.io.*;

public class Server {
    private static boolean isPrime(int x) {
        if (x <= 1) return false;
        for (int i = 2; i <= Math.sqrt(x); i++) {
            if (x % i == 0) return false;
        }
        return true;
    }

    private static int nextPrime(int x) {
        int num = x;
        while (true) {
            num++;
            if (isPrime(num)) return num;
        }
    }

    public static void main(String[] args) {
        try {
            int port = 4100;
            System.out.println("Enter server port number: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            port = Integer.parseInt(reader.readLine());

            // Register with Dictionary
            try (Socket dictSocket = new Socket("localhost", 5000);
                 PrintWriter dictOut = new PrintWriter(dictSocket.getOutputStream(), true);
                 BufferedReader dictIn = new BufferedReader(new InputStreamReader(dictSocket.getInputStream()))) {

                dictOut.println("REGISTER localhost " + port);
                System.out.println(dictIn.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ServerHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ServerHandler implements Runnable {
        private Socket socket;

        ServerHandler(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String request;
                while ((request = in.readLine()) != null) {
                    System.out.println("Received request: " + request);
                    String[] parts = request.split(" ");
                    int num = Integer.parseInt(parts[1]);

                    if (parts[0].equals("IS_PRIME")) {
                        boolean result = isPrime(num);
                        out.println("IS_PRIME " + result);
                        System.out.println("Sent response: IS_PRIME " + result);
                    } else if (parts[0].equals("NEXT_PRIME")) {
                        int result = nextPrime(num);
                        out.println("NEXT_PRIME " + result);
                        System.out.println("Sent response: NEXT_PRIME " + result);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    System.out.println("Socket closed.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
