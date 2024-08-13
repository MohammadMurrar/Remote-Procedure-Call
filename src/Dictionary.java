import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private static List<ServerInfo> servers = new ArrayList<>();
    private static int currentIndex = 0;

    public static void main(String[] args) {
        int port = 5000; // Dictionary port
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Dictionary listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new DictionaryHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class DictionaryHandler implements Runnable {
        private Socket socket;

        DictionaryHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String request = in.readLine();
                if (request.startsWith("REGISTER")) {
                    String[] parts = request.split(" ");
                    String serverIp = parts[1];
                    int serverPort = Integer.parseInt(parts[2]);
                    servers.add(new ServerInfo(serverIp, serverPort));
                    out.println("Server registered.");
                } else if (request.equals("GET_SERVER")) {
                    if (!servers.isEmpty()) {
                        ServerInfo serverInfo = servers.get(currentIndex);
                        currentIndex = (currentIndex + 1) % servers.size();
                        out.println(serverInfo.ip + " " + serverInfo.port);
                    } else {
                        out.println("No servers available.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ServerInfo {
        String ip;
        int port;

        ServerInfo(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }
    }
}
