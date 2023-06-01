import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.io.Serial;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends JDialog {
    @Serial
    private static final long serialVersionUID = 1L;

    private final JTextField txtName;
    private final JTextField txtIPAddress;
    private final JTextField txtPort;
    public Login() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setResizable(false);
        setTitle("Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 320);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblName = new JLabel("Name:", SwingConstants.CENTER);
        lblName.setBounds(175, 10, 50, 15);
        contentPane.add(lblName);

        txtName = new JTextField();
        txtName.setBounds(100, 25, 200, 30);
        txtName.setHorizontalAlignment(JTextField.CENTER);
        contentPane.add(txtName);
        txtName.setColumns(10);

        JLabel lblIPAddress = new JLabel("IP Address:", SwingConstants.CENTER);
        lblIPAddress.setBounds(150, 60, 100, 15);
        contentPane.add(lblIPAddress);

        txtIPAddress = new JTextField();
        txtIPAddress.setBounds(100, 75, 200, 28);
        txtIPAddress.setHorizontalAlignment(JTextField.CENTER);
        contentPane.add(txtIPAddress);
        txtIPAddress.setColumns(10);

        JLabel lblPort = new JLabel("Port:", SwingConstants.CENTER);
        lblPort.setBounds(175, 110, 50, 15);
        contentPane.add(lblPort);

        txtPort = new JTextField();
        txtPort.setBounds(100, 125, 200, 28);
        txtPort.setHorizontalAlignment(JTextField.CENTER);
        ((AbstractDocument)txtPort.getDocument()).setDocumentFilter(new DocumentFilter(){
            final Pattern regEx = Pattern.compile("\\d*");

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches()){
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
        contentPane.add(txtPort);
        txtPort.setColumns(10);

        JLabel lblLanguage = new JLabel("Language:", SwingConstants.CENTER);
        lblLanguage.setBounds(175, 160, 50, 15);
        contentPane.add(lblLanguage);

        String[] languages = {"Spanish", "English"};
        JComboBox<String> cmbLanguage = new JComboBox<>(languages);
        cmbLanguage.setBounds(100, 175, 200, 28);
        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        cmbLanguage.setRenderer(listRenderer);

        contentPane.add(cmbLanguage);

        JLabel lblError = new JLabel("", SwingConstants.CENTER);
        lblError.setBounds(50, 210, 300, 16);
        lblError.setForeground(Color.RED);
        contentPane.add(lblError);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(150, 240, 100, 28);
        // Anonymous class -> We can also create a class ona different file, but since only is used here, is easier and
        // shorter to declare the class as argument
        btnLogin.addActionListener(arg0 -> {

            String name = txtName.getText();
            if (name.length() == 0) {
                lblError.setText("Name cannot be empty");
                return;
            }
            else {
                lblError.setText("");
            }
            String address = txtIPAddress.getText();
            if (address.length() == 0) {
                lblError.setText("IP Address cannot be empty");
                return;
            }
            else {
                lblError.setText("");
            }
            String strPort = txtPort.getText();
            if (strPort.length() == 0) {
                lblError.setText("Port cannot be empty");
                return;
            }
            else {
                lblError.setText("");
            }
            int port = Integer.parseInt(strPort);

            login(name, address, port);
        });
        contentPane.add(btnLogin);

        dispose();
        new Client("A", "address", 888);
    }

    private void login(String name, String address, int port) {
        dispose();
        new Client(name, address, port);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
