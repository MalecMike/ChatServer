import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class ServerGUI extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ServerLogic serverLogic;

    public ServerGUI(){
        userText = new JTextField();
        userText.setEditable(true);
        userText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    serverLogic.getServerWorker().sendToAllServerMessage(userText.getText());
                    userText.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        add(userText, BorderLayout.SOUTH);
        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow));
        setSize(500,250);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        serverLogic = new ServerLogic(3333,this);
        run();
    }
    public void setCanType(boolean canType) {
        //this.canType = canType;
    }
    public void setChatWindow(String msg) {
        chatWindow.append(msg);
    }
    public void run() {
        try {
            serverLogic.startRunning();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}