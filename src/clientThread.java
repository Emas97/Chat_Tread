import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class clientThread extends Thread {
    private BufferedReader is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;
    private String name = null;
    private String name2 = null;
    private static int nr;

    public clientThread(Socket clientSocket, clientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
        nr++;
    }

    private boolean goodName(String name) {
        for (int i = 0; i < nr; i++) {
            if (name.equals(threads[i].getNameT())) {
                return true;
            }
        }
        return false;
    }

    public String getNameT() {
        return name;
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;
        try {

            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os = new PrintStream(clientSocket.getOutputStream());
            os.println("SUBMITNAME");
            name2 = is.readLine().trim();

            if (2 <= nr) {
                while (goodName(name2)) {
                    os.println("OTHERNAME");
                    name2 = is.readLine().trim();
                }
            }

            this.name = name2;
            os.println("NAMEACCEPTED Witaj, " + name + " aby opuścić czat napisz EXIT. Żeby napisać do konkretnego użytkownika");
            os.println("NAMEACCEPTED napisz @ i podaj login osoby do której chcesz napisać np.@TADEK.");
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("MESSAGE *** Nowy użytkownik " + name + " podłączył się do czatu  !!! ***");
                }
            }
            while (true) {
                String line = is.readLine();
                if (line.startsWith("EXIT")) {
                    nr--;
                    break;
                }
                if (line.startsWith("@")) {
                    String[] words = line.split("\\s+");
                    String named = words[0].substring(1);
                    for (int i = 0; i < nr; i++) {
                        if ((threads[i].getNameT()).toUpperCase().equals(named)) {
                            threads[i].os.println("MESSAGE " + line);
                        }
                    }
                } else {
                    for (int i = 0; i < maxClientsCount; i++) {
                        if (threads[i] != null) {
                            threads[i].os.println("MESSAGE <" + name + ">" + line);
                        }
                    }
                }
            }
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("MESSAGE *** Użytkownik " + name + " opuścił czat !!! ***");
                }
            }
            os.println("MESSAGE *** Do zobaczenia " + name + " następnym razem ***");

            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }
            ;
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {

        }finally {
            System.out.println("masny");
            System.out.println("sss");
            System.out.println("kajak");
        }
    }
}
