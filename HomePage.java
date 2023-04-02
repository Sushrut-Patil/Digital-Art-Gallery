import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.AttributedCharacterIterator;
import javax.swing.*;


public class HomePage extends JFrame implements ActionListener {
    int counter;
    JPanel panel,buttonspanel1,artpanel,buttonspanel2;

    private  JLabel title,imageLabel;
    private JLabel ArtName,Artist,Artype,Dimensions,Ratings,Availability,Price,Descriptions;
    private JLabel VArtName,VArtist,VArtype,VDimensions,VRatings,VAvailability,VPrice,VDescriptions;

    String username,ValArtName,ValArtist,ValArtype,ValDimensions,ValRatings,ValAvailability,ValPrice,ValDescriptions;
    private JButton Left,Right,BuyArt,SellArt,Myaccount,AddToCart, AddtoWishList,Mycart,Exit;
    HomePage(String username,int Counter) {
        super("Digital Art Gallery Home Page");
        this.username = username;

        title = new JLabel("Digital Art Gallery ",SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        title.setForeground(Color.RED);
        title.setBounds(500,0,500,50);
        add(title);

        panel = new JPanel();
        panel.setBounds(10,40,1200,750);
        try {

        ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File("src/Images/sample.jpeg")));
        Image scaleImage = imageIcon.getImage().getScaledInstance(1200, 750,Image.SCALE_SMOOTH);
        imageLabel = new JLabel(new ImageIcon(scaleImage));
        panel.add(imageLabel);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        Left = new JButton("<<<");
        Left.setForeground(Color.BLACK);
        Left.setBounds(10,760,70,31);
        Left.setBorderPainted(false);
        Left.setFocusPainted( false );
        add(Left);

        Right = new JButton(">>>");
        Right.setForeground(Color.BLACK);
        Right.setBounds(1140,760,70,31);
        Right.setBorderPainted(false);
        Right.setFocusPainted( false );
        add(Right);

        artpanel = new JPanel(new GridLayout(9,2,5,5));
        artpanel.setBounds(1220,50,300,500);
        artpanel.setBackground(Color.CYAN);

        ArtName = new JLabel(" Art Name :");
        artpanel.add(ArtName);
        VArtName = new JLabel(ValArtName);
        artpanel.add(VArtName);

        Artist = new JLabel(" Artist Name :");
        artpanel.add(Artist);
        VArtist = new JLabel(ValArtist);
        artpanel.add(VArtist);

        Artype = new JLabel(" Art-Type :");
        artpanel.add(Artype);
        VArtype = new JLabel(ValArtype);
        artpanel.add(VArtype);

        Dimensions = new JLabel(" Dimensions : ");
        artpanel.add(Dimensions);
        VDimensions = new JLabel(ValDimensions);
        artpanel.add(VDimensions);

        Ratings = new JLabel(" Ratings: ");
        artpanel.add(Ratings);
        VRatings = new JLabel(ValRatings);
        artpanel.add(VRatings);

        Price = new JLabel(" Price : ");
        artpanel.add(Price);
        VPrice = new JLabel(ValPrice);
        artpanel.add(VPrice);

        Availability = new JLabel(" Availability : ");
        artpanel.add(Availability);
        VAvailability = new JLabel(ValAvailability);
        artpanel.add(VAvailability);

        Descriptions = new JLabel(" Description : ");
        artpanel.add(Descriptions);
        VDescriptions = new JLabel(ValDescriptions);
        artpanel.add(VDescriptions);





        BuyArt = new JButton("Buy This Art");
        AddToCart = new JButton("Add To Cart");
        AddtoWishList = new JButton("Add to WishList");
        buttonspanel1 = new JPanel(new GridLayout(3,1,10,10));
        buttonspanel1.add(AddtoWishList);
        buttonspanel1.add(AddToCart);
        buttonspanel1.add(BuyArt);
        buttonspanel1.setBounds(1220,560,300,200);

        Myaccount = new JButton("My Account");
        Myaccount.addActionListener(this);
        Mycart = new JButton("My Cart");
        SellArt = new JButton("Sell Art");
        SellArt.addActionListener(this);
        Exit = new JButton("Exit");
        Exit.addActionListener(this);

        buttonspanel2 = new JPanel(new GridLayout(1,4,30,0));
        buttonspanel2.add(Myaccount);
        buttonspanel2.add(Mycart);
        buttonspanel2.add(SellArt);
        buttonspanel2.add(Exit);
        buttonspanel2.setBounds(200,800,800,30);
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
            new ArtSubmissionPage(username,counter);
            setVisible(false);
        } else if (e.getActionCommand().equals("My Account")) {

        } else if (e.getActionCommand().equals("Exit")) {
            System.exit(0);

        }
    }
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/DigitalArtGallery", "root", "root");
    }
    public void FetchDetails(int counter) {


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            String sql = " ";

            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();
            resultSet.next();
            System.out.println(resultSet.getInt(1));

            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }


    }
}