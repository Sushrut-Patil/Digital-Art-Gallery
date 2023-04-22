import Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyAccount extends BaseFrame implements ActionListener {

    final JButton logout;
    final JButton returntohome;
    final JLabel ValUser_Id;
    final JLabel ValUsername;
    final JLabel ValName;
    final JLabel ValEmail;
    final JLabel ValArtOwned;
    private final CustomLabel ValBalance;
    int ArtsCount;

    public MyAccount() {
        super("My Account Page");

        //Main Title of Application
        JLabel title = new MainTitle();
        title.setBounds(500, 20, 500, 100);
        add(title);

        //Page Title
        JLabel pagetitle = new PageTitles(" MY ACCOUNT PAGE");
        pagetitle.setBounds(630, 120, 250, 50);
        add(pagetitle);

        JLabel ArtTabletitle = new PageTitles(" My Art Collection");
        ArtTabletitle.setBounds(900, 200, 200, 50);
        add(ArtTabletitle);

        //Function To Display ArtDetails Owned By User
        DetailsTable();

        JLabel UserDetails = new PageTitles(" User Details");
        UserDetails.setBounds(125, 200, 250, 50);
        add(UserDetails);

        //Panel to Display Users Details
        JPanel UserPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        UserPanel.setBounds(50, 250, 400, 300);
        UserPanel.setBackground(Color.PINK);
        add(UserPanel);

        JLabel User_Id = new CustomLabel(" User_ID");
        UserPanel.add(User_Id);
        ValUser_Id = new CustomLabel(" ");
        UserPanel.add(ValUser_Id);

        JLabel User_Name = new CustomLabel(" Username");
        UserPanel.add(User_Name);
        ValUsername = new CustomLabel(" ");
        UserPanel.add(ValUsername);

        JLabel Name = new CustomLabel(" Name");
        UserPanel.add(Name);
        ValName = new CustomLabel(" ");
        UserPanel.add(ValName);

        JLabel Email = new CustomLabel(" Email");
        UserPanel.add(Email);
        ValEmail = new CustomLabel(" ");
        UserPanel.add(ValEmail);

        JLabel ArtOwned = new CustomLabel(" Arts Owned");
        UserPanel.add(ArtOwned);
        ValArtOwned = new CustomLabel("");
        UserPanel.add(ValArtOwned);

        JLabel Balance = new CustomLabel(" Balance");
        UserPanel.add(Balance);
        ValBalance = new CustomLabel("");
        UserPanel.add(ValBalance);

        //Method to Fetch User Details from Database
        FetchUserDetails();

        //LogOut Button
        logout = new CustomButtons("LOG OUT", Color.RED);
        logout.addActionListener(this);
        logout.setBounds(200, 600, 100, 30);
        add(logout);

        //Return to HomePage
        returntohome = new CustomButtons("<<< HOME", Color.GREEN);
        returntohome.setBounds(10, 10, 150, 30);
        returntohome.setForeground(Color.BLACK);
        returntohome.addActionListener(this);
        add(returntohome);
    }

    public void DetailsTable() {

        String[] columnNames = {"ART ID", "ART NAME", "ART TYPE", "HEIGHT", "WIDTH", "PRICE", "AVAILABILITY"};
        JTable Table = new JTable((Object[][]) FetchArtDetails(), columnNames);
        Table.setFont(new Font("Verdana", Font.PLAIN, 14));
        Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Table.setDefaultEditor(Object.class, null);
        Table.doLayout();
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane TableContainer = new JScrollPane(Table);

        TableContainer.setBounds(500, 250, 1000, 300);
        add(TableContainer);
    }

    public Object FetchArtDetails() {
        Connection connection;
        PreparedStatement statement;
        ResultSet rs;
        ArrayList<Object[]> dataList = new ArrayList<>();
        ArtsCount = 0;
        try {
            connection = Main.getConnection();
            String sql = "SELECT ART_ID,ART_NAME,ART_TYPE,HEIGHT,WIDTH,PRICE,AVAILABILITY FROM ARTDETAILS WHERE OWNER = (?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, Main.getUsername());
            rs = statement.executeQuery();
            while (rs.next()) {
                Object[] object = new Object[]{rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getString(7)};
                dataList.add(object);
                ArtsCount++;
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Object[][] Data = new Object[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            Data[i] = dataList.get(i);
        }

        return Data;
    }

    private void FetchUserDetails() {

        Connection connection;
        PreparedStatement statement;
        ResultSet rs;
        try {
            connection = Main.getConnection();
            String sql = "SELECT USER_ID,NAME,EMAIL,BALANCE FROM USERS WHERE USERNAME = (?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, Main.getUsername());
            rs = statement.executeQuery();
            rs.next();

            ValUser_Id.setText(rs.getString(1));
            ValUsername.setText(Main.getUsername());
            ValName.setText(rs.getString(2));
            ValEmail.setText(rs.getString(3));
            ValArtOwned.setText(String.valueOf(ArtsCount));
            ValBalance.setText(String.valueOf(rs.getString(4)));

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(logout)) {
            int choice = JOptionPane.showConfirmDialog(this, "Do you want LogOut?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                new LoginPage();
                dispose();
            }
        } else if (e.getSource().equals(returntohome)) {
            new HomePage();
            dispose();
        }
    }
}
