import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    //dodaje
    //addd
    //dddddaaasss
    String serverAddress;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("ChatByPrzemo");
    JTextField textField = new JTextField(50);
    JTextArea messageArea = new JTextArea(16, 50);

    public  saa(String serverAddress) {
        this.serverAddress = serverAddress;
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(this.textField, "South");
        frame.getContentPane().add(new JScrollPane(this.messageArea), "Center");
        frame.pack();

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChatClient.this.out.println(ChatClient.this.textField.getText());
                ChatClient.this.textField.setText("");
            }
        });
    }

    private String getName() {
        return JOptionPane.showInputDialog(this.frame, "Podaj swój login:", "Login", JOptionPane.PLAIN_MESSAGE);
    }

    private String getOtherName() {
        return JOptionPane.showInputDialog(this.frame, "Podaj inny login ten jest zajęty:", "Login", JOptionPane.PLAIN_MESSAGE);
    }
    private void run() throws IOException {
        try {
            Socket socket = new Socket(this.serverAddress, 2222);
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream(), true);

            while(this.in.hasNextLine()) {
                String line = this.in.nextLine();
                if (line.startsWith("BUSY")) {
                    messageArea.append(line.substring(5) + "\n");
                }else if (line.startsWith("SUBMITNAME")) {
                    out.println(this.getName());
                }else if (line.startsWith("OTHERNAME")) {
                    out.println(this.getOtherName());
                }else if (line.startsWith("NAMEACCEPTED")) {
                    messageArea.append(line.substring(13) + "\n");
                    textField.setEditable(true);
                }else if (line.startsWith("MESSAGE")) {
                    messageArea.append(line.substring(8) + "\n");
                }
            }
        } finally {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            in.close();
            out.close();
            frame.setVisible(false);
            frame.dispose();
        }

    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
        } else {
            /*ChatClient client = new ChatClient(args[0]);
            client.frame.setDefaultCloseOperation(3);
            client.frame.setVisible(true);
            client.run();*/
        }
    }

}
