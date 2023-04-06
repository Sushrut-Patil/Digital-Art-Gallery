
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.Objects;


//TODO Adding Action Listener to Remaining buttons
public class HomePage extends JFrame implements ActionListener {

    JPanel panel,buttonspanel1,artpanel,buttonspanel2;

    String ValArtID,ValArtName,ValArtist,ValArtype,ValDimensions,ValRatings,ValAvailability,ValPrice,ValDescriptions;
    private final JButton Left;
    private final JButton Right;

    HomePage() {
        super("Digital Art Gallery Home Page");


        JLabel title = new JLabel("Digital Art Gallery ", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        title.setForeground(Color.RED);
        title.setBounds(500,0,500,50);
        add(title);

        FetchDetails(Main.getCounter());

        panel = new JPanel();
        panel.setBounds(10,40,1200,750);
        try {
        ImageIcon imageIcon = FetchArt(Main.getCounter());
        Image scaleImage = imageIcon.getImage().getScaledInstance(1200, 750,Image.SCALE_FAST);
        JLabel imageLabel = new JLabel(new ImageIcon(scaleImage));
        panel.add(imageLabel);
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Left = new JButton("<<<");
        Left.addActionListener(this);
        Left.setForeground(Color.BLACK);
        Left.setBounds(10,760,70,31);
        Left.setBorderPainted(false);
        Left.setFocusPainted( false );
        add(Left);

        Right = new JButton(">>>");
        Right.addActionListener(this);
        Right.setForeground(Color.BLACK);
        Right.setBounds(1140,760,70,31);
        Right.setBorderPainted(false);
        Right.setFocusPainted( false );
        add(Right);

        artpanel = new JPanel(new GridLayout(9,2));
        artpanel.setBounds(1220,50,300,550);
        artpanel.setBackground(Color.WHITE);

        JLabel art_Id = new JLabel(" Art ID :");
        artpanel.add(art_Id);
        JLabel VArtID = new JLabel(ValArtID);
        artpanel.add(VArtID);

        JLabel artName = new JLabel(" Art Name :");
        artpanel.add(artName);
        JLabel VArtName = new JLabel(ValArtName);
        artpanel.add(VArtName);

        JLabel artist = new JLabel(" Artist Name :");
        artpanel.add(artist);
        JLabel VArtist = new JLabel(ValArtist);
        artpanel.add(VArtist);

        JLabel artype = new JLabel(" Art-Type :");
        artpanel.add(artype);
        JLabel VArtype = new JLabel(ValArtype);
        artpanel.add(VArtype);

        JLabel dimensions = new JLabel(" Dimensions : ");
        artpanel.add(dimensions);
        JLabel VDimensions = new JLabel(ValDimensions);
        artpanel.add(VDimensions);

        JLabel ratings = new JLabel(" Ratings: ");
        artpanel.add(ratings);
        JLabel VRatings = new JLabel(ValRatings);
        artpanel.add(VRatings);

        JLabel price = new JLabel(" Price : ");
        artpanel.add(price);
        JLabel VPrice = new JLabel(ValPrice);
        artpanel.add(VPrice);

        JLabel availability = new JLabel(" Availability : ");
        artpanel.add(availability);
        JLabel VAvailability = new JLabel(ValAvailability);
        Font f = VAvailability.getFont();
        artpanel.add(VAvailability);

        JTextArea descriptions = new JTextArea(" Description : ");
        descriptions.setOpaque(false);
        descriptions.setEditable(false);
        descriptions.setFont(f);
        artpanel.add(descriptions);
        JTextArea VDescriptions = new JTextArea(ValDescriptions);
        VDescriptions.setLineWrap(true);
        VDescriptions.setWrapStyleWord(true);
        VDescriptions.setOpaque(false);
        VDescriptions.setEditable(false);
        VDescriptions.setFont(f);
        artpanel.add(VDescriptions);


        JButton buyArt = new JButton("Buy This Art");
        JButton addToCart = new JButton("Add To Cart");
        JButton addtoWishList = new JButton("Add to WishList");
        buttonspanel1 = new JPanel(new GridLayout(3,1,10,10));
        buttonspanel1.add(addtoWishList);
        buttonspanel1.add(addToCart);
        buttonspanel1.add(buyArt);
        buttonspanel1.setBounds(1220,640,300,150);

        JButton myaccount = new JButton("My Account");
        myaccount.addActionListener(this);
        JButton searchArt = new JButton("Search Art");
        JButton mycart = new JButton("My Cart");
        JButton sellArt = new JButton("Sell Art");
        sellArt.addActionListener(this);
        JButton exit = new JButton("Exit");
        exit.addActionListener(this);

        buttonspanel2 = new JPanel(new GridLayout(1,5,30,0));
        buttonspanel2.add(myaccount);
        buttonspanel2.add(searchArt);
        buttonspanel2.add(mycart);
        buttonspanel2.add(sellArt);
        buttonspanel2.add(exit);
        buttonspanel2.setBounds(150,800,900,30);
        add(panel);
        add(buttonspanel1);
        add(buttonspanel2);
        add(artpanel);

        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Sell Art")) {
            new ArtSubmissionPage();
            setVisible(false);
        } else if (e.getActionCommand().equals("My Account")) {
                new MyAccount();
        } else if (e.getActionCommand().equals("Exit")) {
            System.exit(0);

        } else if (e.getSource()==Right) {
            Main.setCounter(Main.getCounter()+1);
            if (Main.getCounter()>Main.Upper) {
                Main.setCounter(Main.Lower);
                new HomePage();
                setVisible(false);
            }else {
                new HomePage();
                setVisible(false);
            }
        } else if (e.getSource()==Left) {
            Main.setCounter(Main.getCounter()-1);

            if (Main.getCounter()<Main.Lower){
                Main.setCounter(Main.Upper);
                new HomePage();
                setVisible(false);
            } else {
                new HomePage();
                setVisible(false);
            }

        }
    }

    public ImageIcon FetchArt(int counter) throws SQLException, IOException, ClassNotFoundException {

        Connection connection;
        PreparedStatement statement;
        ResultSet resultSet;
        Blob blob = null;

        connection = Main.getConnection();
        String query = "Select image from Art where art_id = (?)";
        statement = connection.prepareStatement(query);
        statement.setInt(1, counter);

        resultSet = statement.executeQuery();


        if (resultSet.next()) {
            blob = resultSet.getBlob("image");
        }

        try (ObjectInputStream is = new ObjectInputStream(Objects.requireNonNull(blob).getBinaryStream())) {
            return (ImageIcon) is.readObject();
        }
    }
    public void FetchDetails(int counter) {


        Connection connection;
        PreparedStatement statement;
        ResultSet resultSet;
        try {
            connection = Main.getConnection();
            String sql = "Select Art_id,Art_Name,Artist_Name,Art_type,Height,Width,Price,Availability,Description,Ratings" +
                    " from ArtDetails where Art_Id = (?)";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, counter);
            resultSet = statement.executeQuery();
            resultSet.next();
            ValArtID = String.valueOf(resultSet.getInt(1));
            ValArtName = resultSet.getString(2);
            ValArtist = resultSet.getString(3);
            ValArtype = resultSet.getString(4);
            ValDimensions = resultSet.getString(5) + " x " + resultSet.getString(6);
            ValPrice = String.valueOf(resultSet.getInt(7));
            ValAvailability = resultSet.getString(8);
            ValDescriptions = resultSet.getString(9);
            ValRatings = String.valueOf(resultSet.getInt(10));

            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }


    }
}