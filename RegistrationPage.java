import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class RegistrationPage extends JFrame implements ActionListener {

    JPanel panel;

    private final JTextField NameField, emailField,usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmpasswordField;

    public RegistrationPage() {

        super("Registration Page");
        try {
            setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src/Images/RegistrationPage.JPEG")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel title = new JLabel("Digital Art Gallery ", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 40));
        title.setForeground(Color.RED);
        title.setBounds(500,100,500,100);
        add(title);

        JLabel pagetitle = new JLabel("Registration Page", SwingConstants.CENTER);
        pagetitle.setFont(new Font("Verdana", Font.PLAIN, 20));
        pagetitle.setForeground(Color.BLACK);
        pagetitle.setBounds(650,250,200,50);
        add(pagetitle);

        panel = new JPanel(new GridLayout(5,2,0,10));
        panel.setBounds(550,350,400,200);
        panel.setOpaque(false);
        add(panel);

        JLabel nameLabel = new JLabel("Name :", SwingConstants.CENTER);
        panel.add(nameLabel);
        NameField = new JTextField();
        panel.add(NameField);

        JLabel emailLabel = new JLabel("Email Id : ", SwingConstants.CENTER);
        panel.add(emailLabel);
        emailField = new JTextField();
        panel.add(emailField);

        JLabel usernameLabel = new JLabel("Username:", SwingConstants.CENTER);
        panel.add(usernameLabel);
        usernameField = new JTextField();
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password :", SwingConstants.CENTER);
        panel.add(passwordLabel);
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JLabel confirmpasswordlabel = new JLabel(" Confirmed Password :", SwingConstants.CENTER);
        panel.add(confirmpasswordlabel);
        confirmpasswordField = new JPasswordField();
        panel.add(confirmpasswordField);

        JButton registerbutton = new JButton("Register");
        registerbutton.setBackground(Color.BLUE);
        registerbutton.setForeground(Color.WHITE);
        registerbutton.setBounds(700,600,100,30);
        add(registerbutton);
        registerbutton.addActionListener(this);

        JButton tologinpage = new JButton("Already have Account?Login");
        tologinpage.setForeground(Color.BLUE);
        tologinpage.setBounds(650,650,200,30);
        add(tologinpage);
        tologinpage.addActionListener(this);
        
        
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }
    private boolean UsernameCheck(String username) {
        boolean duplicateusername = true;
        Connection connection;
        PreparedStatement statement;
        ResultSet resultSet;
        try {
            connection = Main.getConnection();
            String sql = "SELECT COUNT(username) FROM users WHERE username = ? ";

            statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            resultSet = statement.executeQuery();
            resultSet.next();

            if (resultSet.getInt(1)==0) {
                duplicateusername = false;
            }
            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return duplicateusername;
    }
    private boolean RegisterFunction(String name,String email_id,String username,String Password) {
        boolean Registerflag = false;
        Connection connection;
        PreparedStatement statement;
        int num;

        try {
            connection = Main.getConnection();

            String sql = "Insert Into Users (name,email,username,password) Values (?,?,?,MD5(?))";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, email_id);
            statement.setString(3, username);
            statement.setString(4, Password);

            num = statement.executeUpdate();
            if (num!=0) {
                Registerflag = true;
            }
            statement.close();
            connection.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return Registerflag;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String name = NameField.getText();
        String email_id = emailField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmpasswordField.getPassword());
        
        if (e.getActionCommand().equals("Register")) {
            if (name.isEmpty() || email_id.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all fields");
            } else if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match");
            } else if (UsernameCheck(username)) {
                JOptionPane.showMessageDialog(this, "Username Already Exist \n Please take Another Username");
            } else if (RegisterFunction(name,email_id,username,password)) {

                JOptionPane.showMessageDialog(this, "Registration successful");
                Main.setUsername(username);
                Main.RandomCounter();
                new HomePage();
                setVisible(false);



            }
        } else if (e.getActionCommand().equals("Already have Account?Login")) {
                    new LoginPage();
                    setVisible(false);

        }
    }
}


