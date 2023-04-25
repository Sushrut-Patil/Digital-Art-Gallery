import Utils.BaseFrame;
import Utils.CustomButtons;
import Utils.MainTitle;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS;

public class MyCart extends BaseFrame implements ActionListener {
    private final JButton ReturnHome;
    private final JButton BuyThis;
    private JTable Table;
    public MyCart() {
        super("My Cart");

        //Main Title of Application
        JLabel title = new MainTitle();
        title.setBounds(500, 20, 500, 100);
        add(title);

        //Page Title


        DetailsTable();

        JPanel ButtonsPanel = new JPanel(new GridLayout(1,1,10,10));
        ButtonsPanel.setBounds(600,600,300,30);
        add(ButtonsPanel);
        BuyThis = new CustomButtons("BUY THIS",Color.BLUE);
        BuyThis.addActionListener(this);
        ButtonsPanel.add(BuyThis);
        ReturnHome = new CustomButtons(" HOME ",Color.RED);
        ReturnHome.addActionListener(this);
        ButtonsPanel.add(ReturnHome);

        revalidate();
        repaint();


    }
    public void DetailsTable() {

        String[] columnNames = {"ART ID", "ART NAME", "ART TYPE", "HEIGHT", "WIDTH", "PRICE", "AVAILABILITY","RATING"};
        Table = new JTable((Object[][]) FetchArtDetails(), columnNames);
        Table.setFont(new Font("Verdana", Font.PLAIN, 14));
        Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Table.setDefaultEditor(Object.class, null);
        Table.setRowSelectionInterval(0,0);
        Table.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
        Table.doLayout();
        JScrollPane TableContainer = new JScrollPane(Table);
        TableContainer.setBounds(100, 250, getWidth()-250, 300);
        add(TableContainer);
    }
    public Object FetchArtDetails() {
        Connection connection;
        PreparedStatement statement;
        ResultSet rs;
        ArrayList<Object[]> dataList = new ArrayList<>();
        try {
            connection = Main.getConnection();
            String sql = "SELECT A.ART_ID,A.ART_NAME,A.ART_TYPE,A.HEIGHT,A.WIDTH,A.PRICE,A.AVAILABILITY,A.RATINGS " +
                    "FROM ARTDETAILS as A " +
                    "INNER JOIN CART as C on C.ART_ID = A.ART_ID " +
                    "WHERE C.USERNAME = ?";
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
                        rs.getString(7),
                        rs.getInt(8)};
                dataList.add(object);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==BuyThis) {

            if (Table.getSelectedRow()!=-1){
                Main.setCounter((Integer) Table.getValueAt(Table.getSelectedRow(),0));
                new BuyArt();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please Select Art");
            }
        } else if (e.getSource()==ReturnHome) {
            new HomePage();
            dispose();
        }
    }
}
