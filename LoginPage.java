import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class LoginPage extends JFrame implements ActionListener {

    JPanel panel;

    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginPage() {

        super("Login Page");
//        To Set Background Image to JFrame
        try {
            setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src/Images/LoginPage.JPEG")))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel title = new JLabel("Digital Art Gallery ", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 40));
        title.setForeground(Color.RED);
        title.setBounds(500,100,500,100);
        add(title);

        JLabel pagetitle = new JLabel("Login Page", SwingConstants.CENTER);
        pagetitle.setFont(new Font("Verdana", Font.PLAIN, 20));
        pagetitle.setForeground(Color.BLACK);
        pagetitle.setBounds(650,250,200,50);
        add(pagetitle);

        panel = new JPanel(new GridLayout(2,2,0,10));
        panel.setBounds(550,350,400,80);
        panel.setOpaque(false);
        add(panel);

        JLabel usernameLabel = new JLabel("Username:", SwingConstants.CENTER);
        panel.add(usernameLabel);
        usernameField = new JTextField();
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password :", SwingConstants.CENTER);
        panel.add(passwordLabel);
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginbutton = new JButton("Login");
        loginbutton.setBackground(Color.BLUE);
        loginbutton.setForeground(Color.WHITE);
        loginbutton.setBounds(700,500,100,30);
        add(loginbutton);
        loginbutton.addActionListener(this);

        JButton toregisterpage = new JButton("Don't have Account?Create One!!");
        toregisterpage.setForeground(Color.BLUE);
        toregisterpage.setBounds(620,550,250,30);
        add(toregisterpage);
        toregisterpage.addActionListener(this);

//        Frame Settings
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

    }
//   Login Function
    private boolean LoginFunction(String username,String Password) {
        boolean loggedIn = false;
        Connection connection;
        PreparedStatement statement;
        ResultSet resultSet;

        try {
//            Connects to Database
            connection = Main.getConnection();

            String sql = "SELECT * FROM users WHERE username = ? AND password = MD5(?)";

            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, Password);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                loggedIn = true;
            }
//            Close Connection, ResultSet and Statement
            resultSet.close();
            statement.close();
            connection.close();

        }catch (SQLException e) {
                e.printStackTrace();
        }
        return loggedIn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (e.getActionCommand().equals("Login")) {
            if (username.isEmpty() || password.isEmpty() ) {
                JOptionPane.showMessageDialog(this, "Please enter all fields");
            } else if (LoginFunction(username,password)) {

                JOptionPane.showMessageDialog(this, "Login Successful");
//                On Successful Login new HomePage Opens
                Main.setUsername(username);
                Main.RandomCounter();
                new HomePage();
                dispose();

            } else{
                JOptionPane.showMessageDialog(this, "Please Enter correct Password or Username");
                }
            }
        else if (e.getActionCommand().equals("Don't have Account?Create One!!")) {
                    new RegistrationPage();
                    dispose();

        }

    }
}
