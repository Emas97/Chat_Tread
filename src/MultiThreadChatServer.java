import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadChatServer {
    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static final int maxClientsCount = 5;
    private static final clientThread[] threads = new clientThread[maxClientsCount];

    public static void main(String[] args) {
        int portNumber = 2222;
        if (args.length < 1) {
            System.out.println("cink dd");
            System.out.println("smoki");
            System.out.println("Usage: java MultiThreadChatServer <portNumber>\nNow using port number" + portNumber);
        } else {
            portNumber = Integer.valueOf(args[0]).intValue();
        }
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }

        while(true) {
                try {
                    clientSocket = serverSocket.accept();
                    int i = 0;
                    for (i = 0; i < maxClientsCount; i++) {
                        if (threads[i] == null) {
                            (threads[i] = new clientThread(clientSocket, threads)).start();
                            break;
                        }
                    }

                    if (i == maxClientsCount) {
                        PrintStream os = new PrintStream(clientSocket.getOutputStream());
                        os.println("BUSY Serwer zajęty, spróbuj później");
                        os.close();
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
        }
    }
}
