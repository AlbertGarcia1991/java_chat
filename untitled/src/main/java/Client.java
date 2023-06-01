import javax.swing.*;
import java.awt.*;

public class Client extends JFrame {
    private static final long SerialVersionUID = 1L;

    private JPanel contentPane;
    private JScrollPane scroll;
    private Integer port;
    private String name, address;
    public Client(String name, String address, int port) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.name = name;
        this.address = address;
        this.port = port;
        createWindow();
    }

    private void createWindow() {
        contentPane = new JPanel();
        setSize(1000, 600);

        JTextArea display = new JTextArea();
        display.setBounds(10, 10, 980, 500);
        JScrollPane scroll = new JScrollPane(display);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(10, 10, 980, 500);

        JButton btn = new JButton("Send");

        contentPane.add(btn);
        contentPane.add(scroll);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
