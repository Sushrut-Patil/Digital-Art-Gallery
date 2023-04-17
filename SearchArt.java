import javax.management.Query;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.EventListener;


public class SearchArt extends JFrame implements ActionListener {

    private final JRadioButton PriceLabel,AvailabilityLabel,ArtTypeLabel,HeightLabel,WidthLabel,YesButton,NoButton;
    private final JSlider PriceSlider,HeightSlider,WidthSlider;
    private JComboBox<String> Art_TypesBox;
    JTable Table;
    private ButtonGroup FilterGroup;

    public SearchArt() {
        super("Search Art Page");

        JLabel title = new JLabel("Digital Art Gallery ", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 40));
        title.setForeground(Color.RED);
        title.setBounds(0,10,500,100);
        add(title);

        //Page Title
        JLabel pagetitle = new JLabel("Art Search Page", SwingConstants.CENTER);
        pagetitle.setFont(new Font("Verdana", Font.PLAIN, 20));
        pagetitle.setForeground(Color.BLACK);
        pagetitle.setBounds(125,100,250,50);
        add(pagetitle);

        JPanel ArtFilter = new JPanel(new GridLayout(4,2,4,4));
        ArtFilter.setBounds(10,200,400,500);
        ArtFilter.setBackground(Color.green);
        add(ArtFilter);

        FilterGroup = new ButtonGroup();

        PriceLabel = new JRadioButton("Select Price");
        ArtFilter.add(PriceLabel);
        PriceLabel.setActionCommand("Price");
        FilterGroup.add(PriceLabel);
        PriceLabel.setSelected(true);

        PriceSlider = new JSlider(0,100,10);
        PriceSlider.setPaintTrack(true);
        PriceSlider.setPaintTicks(true);
        PriceSlider.setPaintLabels(true);
        PriceSlider.setMajorTickSpacing(50);
        PriceSlider.setMinorTickSpacing(10);
        ArtFilter.add(PriceSlider);

        ArtTypeLabel = new JRadioButton("Select Art Type");
        ArtFilter.add(ArtTypeLabel);
        ArtTypeLabel.setActionCommand("Art_Type");
        FilterGroup.add(ArtTypeLabel);

        String[] listarttype = { "Painting", "AI-Art", "Photography", "Sketch", "Sculptor","Photo Painting","Digital Art", "Others" };
        Art_TypesBox = new JComboBox<>(listarttype);
        ArtFilter.add(Art_TypesBox);

        JPanel dimensionsLabel = new JPanel(new GridLayout(2, 1));
        HeightLabel = new JRadioButton("Height");
        HeightLabel.setActionCommand("Height");
        WidthLabel = new JRadioButton("Width");
        WidthLabel.setActionCommand("Width");
        dimensionsLabel.add(HeightLabel);
        dimensionsLabel.add(WidthLabel);
        ArtFilter.add(dimensionsLabel);
        FilterGroup.add(HeightLabel);
        FilterGroup.add(WidthLabel);

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


        AvailabilityLabel = new JRadioButton("Select Availability");
        AvailabilityLabel.setActionCommand("Availability");
        ArtFilter.add(AvailabilityLabel);
        FilterGroup.add(AvailabilityLabel);
        YesButton = new JRadioButton("Yes");
        NoButton = new JRadioButton("No");

        ButtonGroup Availability = new ButtonGroup();
        Availability.add(YesButton);
        Availability.add(NoButton);
        JPanel Buttons = new JPanel(new GridLayout(1,2));
        Buttons.add(YesButton);
        Buttons.add(NoButton);
        ArtFilter.add(Buttons);

        JButton search = new JButton("Search");
        search.addActionListener(this);
        search.setBounds(80,720,100,30);
        add(search);

        JButton cancelbutton = new JButton("Cancel");
        cancelbutton.addActionListener(this);
        cancelbutton.setBounds(240,720,100,30);
        add(cancelbutton);

        JPanel Panel = new JPanel(new GridLayout(5,5,5,5));
        Panel.setBounds(500,250,1000,500);
        Panel.setBackground(Color.cyan);
        add(Panel);

        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);


    }
    public interface ListSelectionListener extends EventListener {
        
    }
    private String QueryGenrator() {
        String sql = "SELECT ART_ID,ART_NAME,ART_TYPE,HEIGHT,WIDTH,PRICE,AVAILABILITY FROM ARTDETAILS WHERE ";
        if (FilterGroup.getSelection().getActionCommand().equals("Price")) {
            sql += "Price >= " + PriceSlider.getValue();
        } else if (FilterGroup.getSelection().getActionCommand().equals("Art_Type")) {
            sql += "ART_TYPE = " + Art_TypesBox.getSelectedItem();
        } else if (FilterGroup.getSelection().getActionCommand().equals("Height")) {
            sql += "HEIGHT >= " + PriceSlider.getValue();
        }
        return sql;
    }
    public void DetailsTable()  {

        String[] columnNames = {"ART ID", "ART NAME", "ART TYPE", "HEIGHT", "WIDTH", "PRICE", "AVAILABILITY"};
        JTable Table = new JTable((Object[][]) FetchDetails(QueryGenrator()), columnNames);
        Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Table.setDefaultEditor(Object.class, null);
        Table.doLayout();
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane TableContainer = new JScrollPane(Table);
        TableContainer.setBounds(500,50,1000,200);
        add(TableContainer);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(FilterGroup.getSelection().getActionCommand());
        if (e.getActionCommand().equals("Search")) {
            DetailsTable();
            System.out.println();
        } else if (e.getActionCommand().equals("Cancel")) {
            new HomePage();
            dispose();
        }
    }



}
