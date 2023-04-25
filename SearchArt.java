import Utils.BaseFrame;
import Utils.CustomButtons;
import Utils.MainTitle;
import Utils.PageTitles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


public class SearchArt extends BaseFrame implements ActionListener {

    private final JSlider PriceSlider,HeightSlider,WidthSlider;
    private final JComboBox<String> Art_TypesBox;
    private final JButton ImagePreviewbutton;
    private final ButtonGroup FilterGroup;
    private final ButtonGroup Availability;
    private final CustomButtons Addtocartbutton;
    JTable Table;
    JButton search,cancelbutton;
    JPanel ImagePanel;
    JScrollPane TableContainer;

    public SearchArt() {
        super("Art Search Page");

        JLabel title = new MainTitle();
        title.setBounds(0,10,500,100);
        add(title);

        //Page Title
        JLabel pagetitle = new PageTitles("Art Search Page");
        pagetitle.setBounds(125,100,250,50);
        add(pagetitle);

        JPanel ArtFilter = new JPanel(new GridLayout(4,2,4,4));
        ArtFilter.setBounds(10,200,400,500);
        ArtFilter.setBackground(Color.green);
        add(ArtFilter);

        FilterGroup = new ButtonGroup();

        JRadioButton priceLabel = new JRadioButton("Select Price");
        ArtFilter.add(priceLabel);
        priceLabel.setActionCommand("Price");
        FilterGroup.add(priceLabel);
        priceLabel.setSelected(true);

        PriceSlider = new JSlider(0,100,50);
        PriceSlider.setPaintTrack(true);
        PriceSlider.setPaintTicks(true);
        PriceSlider.setPaintLabels(true);
        PriceSlider.setMajorTickSpacing(50);
        PriceSlider.setMinorTickSpacing(10);
        ArtFilter.add(PriceSlider);

        JRadioButton artTypeLabel = new JRadioButton("Select Art Type");
        ArtFilter.add(artTypeLabel);
        artTypeLabel.setActionCommand("Art_Type");
        FilterGroup.add(artTypeLabel);

        String[] listarttype = { "Painting", "AI-Art", "Photography", "Sketch", "Sculptor","Photo Painting","Digital Art", "Others" };
        Art_TypesBox = new JComboBox<>(listarttype);
        ArtFilter.add(Art_TypesBox);

        JPanel dimensionsLabel = new JPanel(new GridLayout(2, 1));
        JRadioButton heightLabel = new JRadioButton("Height");
        heightLabel.setActionCommand("Height");
        JRadioButton widthLabel = new JRadioButton("Width");
        widthLabel.setActionCommand("Width");
        dimensionsLabel.add(heightLabel);
        dimensionsLabel.add(widthLabel);
        ArtFilter.add(dimensionsLabel);
        FilterGroup.add(heightLabel);
        FilterGroup.add(widthLabel);

        JPanel dimensionGroup = new JPanel(new GridLayout(2, 1));
        ArtFilter.add(dimensionGroup);

        HeightSlider = new JSlider(0,5000,2500);
        HeightSlider.setPaintTrack(true);
        HeightSlider.setPaintTicks(true);
        HeightSlider.setPaintLabels(true);
        HeightSlider.setMajorTickSpacing(1000);
        HeightSlider.setMinorTickSpacing(500);

        WidthSlider = new JSlider(0,5000,2500);
        WidthSlider.setPaintTrack(true);
        WidthSlider.setPaintTicks(true);
        WidthSlider.setPaintLabels(true);
        WidthSlider.setMajorTickSpacing(1000);
        WidthSlider.setMinorTickSpacing(500);

        dimensionGroup.add(HeightSlider);
        dimensionGroup.add(WidthSlider);


        JRadioButton availabilityLabel = new JRadioButton("Select Availability");
        availabilityLabel.setActionCommand("Availability");
        ArtFilter.add(availabilityLabel);
        FilterGroup.add(availabilityLabel);

        JRadioButton yesButton = new JRadioButton("Yes");
        yesButton.setSelected(true);
        yesButton.setActionCommand("YES");
        JRadioButton noButton = new JRadioButton("No");
        noButton.setActionCommand("NO");

        Availability = new ButtonGroup();
        Availability.add(yesButton);
        Availability.add(noButton);

        JPanel Buttons = new JPanel(new GridLayout(1,2));
        Buttons.add(yesButton);
        Buttons.add(noButton);
        ArtFilter.add(Buttons);

        search = new CustomButtons("Search",Color.blue);
        search.addActionListener(this);
        search.setBounds(80,720,100,30);
        add(search);

        cancelbutton = new CustomButtons("Cancel",Color.red);
        cancelbutton.addActionListener(this);
        cancelbutton.setBounds(240,720,100,30);
        add(cancelbutton);

        ImagePreviewbutton = new CustomButtons("Image Preview",Color.BLUE);
        ImagePreviewbutton.addActionListener(this);
        ImagePreviewbutton.setBounds(750,260,200,30);
        add(ImagePreviewbutton);

        Addtocartbutton = new CustomButtons("Add to Cart",Color.BLUE);
        Addtocartbutton.addActionListener(this);
        Addtocartbutton.setBounds(1050,260,200,30);
        add(Addtocartbutton);

        ImagePanel = new JPanel();
        ImagePanel.setBounds(500,300,1000,500);
        add(ImagePanel);
        DetailsTable();

        revalidate();
        repaint();
    }
    private void ViewImage() throws SQLException, IOException, ClassNotFoundException {

        ImagePanel.removeAll();
        Main.setCounter((Integer) Table.getValueAt(Table.getSelectedRow(),0));
        ImageIcon imageIcon = StaticMethods.FetchArt(Main.getCounter());
        Image scaleImage = imageIcon.getImage().getScaledInstance(1000, 500, Image.SCALE_FAST);
        JLabel imageLabel = new JLabel(new ImageIcon(scaleImage));
        ImagePanel.add(imageLabel);
        ImagePanel.revalidate();
        ImagePanel.repaint();
    }
    //Method to Generate Query based on Selected Filter by User
    private String QueryGenerator() {
        String sql = "SELECT ART_ID,ART_NAME,ART_TYPE,HEIGHT,WIDTH,PRICE,AVAILABILITY FROM ARTDETAILS WHERE ";
        if (FilterGroup.getSelection().getActionCommand().equals("Price")) {
            sql += "Price <= " + PriceSlider.getValue();
        } else if (FilterGroup.getSelection().getActionCommand().equals("Art_Type")) {
            sql += "ART_TYPE = \"" + Art_TypesBox.getSelectedItem() + "\"";
        } else if (FilterGroup.getSelection().getActionCommand().equals("Height")) {
            sql += "HEIGHT <= " + HeightSlider.getValue();
        } else if (FilterGroup.getSelection().getActionCommand().equals("Width")) {
                sql += "WIDTH <= " + WidthSlider.getValue();
        } else if (FilterGroup.getSelection().getActionCommand().equals("Availability")) {
            sql += "AVAILABILITY = \""+ Availability.getSelection().getActionCommand()+"\"";
        }
        return sql;
    }
    //Method to Display Details in JTable
    public void DetailsTable()  {

        //Display Searched Art in JTable
        String[] columnNames = {"ART ID", "ART NAME", "ART TYPE", "HEIGHT", "WIDTH", "PRICE", "AVAILABILITY"};
        Table = new JTable((Object[][]) FetchDetails(QueryGenerator()), columnNames);
        Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Table.setDefaultEditor(Object.class, null);
        Table.doLayout();
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableContainer = new JScrollPane(Table);
        TableContainer.setBounds(500,50,1000,200);
        add(TableContainer);
    }
    private void UpdateTable() {

        Table = null;
        remove(TableContainer);
        DetailsTable();
        revalidate();
        repaint();
    }
    public Object FetchDetails(String sql) {
        Connection connection;
        PreparedStatement statement;
        ResultSet rs;
        ArrayList<Object[]> dataList = new ArrayList<>();
        try {
            connection = Main.getConnection();
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();

            while (rs.next()){
                Object[] object = new Object[]{rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getString(7)};
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
    public static boolean AddtoCart(int id) {
        boolean flag = false;
        Connection connection;
        PreparedStatement statement,statement1;
        ResultSet rs;
        try {
            connection = Main.getConnection();
            String sql = "SELECT COUNT(USERNAME) FROM CART WHERE USERNAME = (?) AND ART_ID = (?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1,Main.getUsername());
            statement.setInt(2,id);
            rs = statement.executeQuery();
            rs.next();
            System.out.println(rs.getInt(1));
            if (rs.getInt(1)==0){
                String sql2 = "INSERT INTO CART (USERNAME,ART_ID) VALUES (?,?)";
                statement1 = connection.prepareStatement(sql2);
                statement1.setString(1,Main.getUsername());
                statement1.setInt(2,id);
                int  num = statement1.executeUpdate();
                if (num!=0) {
                    flag = true;
                }
                statement1.close();
            }
            rs.close();
            statement.close();
            connection.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource()==search) {
            UpdateTable();
        } else if (e.getSource()==cancelbutton) {
            new HomePage();
            dispose();
        } else if (e.getSource()==ImagePreviewbutton) {
            if (Table.getSelectedRow()!=-1) {
                try {
                    ViewImage();
                } catch (SQLException | IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Select Art_Id");
            }

        } else if (e.getSource()==Addtocartbutton) {
            if(Table.getSelectedRow()!=-1) {

                if (AddtoCart((Integer) Table.getValueAt(Table.getSelectedRow(),0))){
                    JOptionPane.showMessageDialog(this, "Added to Cart Successfully");
                } else {
                    JOptionPane.showMessageDialog(this, "Art Already present in Cart");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Art Already present in Cart");
            }

        }
    }
}
