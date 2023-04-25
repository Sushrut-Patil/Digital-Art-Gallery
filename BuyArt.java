import Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class BuyArt extends BaseFrame implements ActionListener {
    Integer Price,Balance;
    String Owner,Availability;
    private final JButton Cancel;
    private final JButton Buy;
    public BuyArt() {
        super("Buy Art");

        JLabel title = new MainTitle();
        title.setBounds(500, 20, 500, 100);
        add(title);

        JLabel pagetitle = new PageTitles(" BUY ART PAGE");
        pagetitle.setBounds(630, 120, 250, 50);
        add(pagetitle);

        FetchArtDetails();
        FetchUserDetails();

        JPanel panel = new JPanel(new GridLayout(6,2,10,10));
        panel.setBounds(500, 300, 500, 300);
        panel.setBackground(Color.pink);
        add(panel);

        JLabel Art_IDLabel = new CustomLabel("Art Id");
        panel.add(Art_IDLabel);
        JLabel Art_IDVal = new CustomLabel(String.valueOf(Main.getCounter()));
        panel.add(Art_IDVal);

        JLabel PriceLabel = new CustomLabel("Price");
        panel.add(PriceLabel);
        JLabel PriceVal = new CustomLabel(String.valueOf(Price));
        panel.add(PriceVal);

        JLabel AvaiLabel = new CustomLabel("Availability");
        panel.add(AvaiLabel);
        JLabel AvailVal = new CustomLabel(Availability);
        panel.add(AvailVal);

        JLabel UsernameLabel = new CustomLabel("Username");
        panel.add(UsernameLabel);
        JLabel UsernameVal = new CustomLabel(String.valueOf(Main.getUsername()));
        panel.add(UsernameVal);

        JLabel BalanceLabel = new CustomLabel("Balance");
        panel.add(BalanceLabel);
        JLabel BalanceVal = new CustomLabel(String.valueOf(Balance));
        panel.add(BalanceVal);

        Buy = new CustomButtons(" BUY ",Color.BLUE);
        Buy.addActionListener(this);
        panel.add(Buy);
        Cancel = new CustomButtons("CANCEL",Color.red);
        Cancel.addActionListener(this);
        panel.add(Cancel);

        revalidate();
        repaint();
    }
    public void FetchArtDetails() {
        Connection connection;
        PreparedStatement statement;
        ResultSet resultSet;
        try {
            connection = Main.getConnection();
            String sql = "Select Owner,Price,Availability" +
                    " from ArtDetails where Art_Id = (?)";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, Main.getCounter());
            resultSet = statement.executeQuery();
            resultSet.next();

            Owner = resultSet.getString(1);
            Price = resultSet.getInt(2);
            Availability = resultSet.getString(3);

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void FetchUserDetails() {

        Connection connection;
        PreparedStatement statement;
        ResultSet rs;
        try {
            connection = Main.getConnection();
            String sql = "SELECT BALANCE FROM USERS WHERE USERNAME = (?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, Main.getUsername());
            rs = statement.executeQuery();
            rs.next();

            Balance = rs.getInt(1);

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==Buy) {
            if(Objects.equals(Owner, Main.getUsername())) {
                JOptionPane.showMessageDialog(this, "You Already Own This Art");
            } else if (Balance<Price) {
                JOptionPane.showMessageDialog(this, "Insufficient Balance");
            } else if (Objects.equals(Availability, "NO")) {
                JOptionPane.showMessageDialog(this, "THis Art is Not Available");
            } else {
                if(BuyUpdate()) {
                    JOptionPane.showMessageDialog(this, "Purchase Successful");
                    new HomePage();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Purchase UnSuccessful");
                }
            }
        } else if (e.getSource()==Cancel) {
            new HomePage();
            dispose();
        }
    }

    private boolean BuyUpdate() {
        boolean flag = false;
        Connection connection;
        PreparedStatement statement1,statement2;
        try {
            connection = Main.getConnection();
            String sql1 = "UPDATE USERS SET BALANCE = CASE " +
                    " WHEN USERNAME = (?) THEN BALANCE - (?)" +
                    " WHEN USERNAME = (?) THEN BALANCE + (?)" +
                    "END" +
                    " WHERE USERNAME = (?) OR USERNAME = (?)";
            statement1 = connection.prepareStatement(sql1);
            statement1.setString(1, Main.getUsername());
            statement1.setInt(2,Price);
            statement1.setString(3, Owner);
            statement1.setInt(4,Price);
            statement1.setString(5,Main.getUsername());
            statement1.setString(6,Owner);
            statement1.executeUpdate();

            String sql2 = "UPDATE ARTDETAILS " +
                    "SET OWNER = (?) , AVAILABILITY = (?) " +
                    "WHERE ART_ID = (?)";
            statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, Main.getUsername());
            statement2.setString(2,"NO");
            statement2.setInt(3,Main.getCounter());
            statement2.executeUpdate();

            statement1.close();
            statement2.close();
            connection.close();
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
