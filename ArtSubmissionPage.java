import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ArtSubmissionPage extends JFrame implements ActionListener {
    private JButton submitButton,cancelbutton;
    private final JLabel fileNameLabel,title,pagetitle;
    private JFileChooser fileChooser;
    private JLabel ArtName,Artist,Artype,Height,Width,Ratings,Availability,Price,Descriptions;
    private JTextField ArtNameField,ArtistField,HeightField,WidthField,AvailabilityField,PriceField;
    private JTextArea DescriptionsField;
    private JPanel Submissionpanel;
    private String username;
    public ArtSubmissionPage(String username) {
        super("Art Submission Page");
        username = this.username;
        title = new JLabel("Digital Art Gallery ",SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 40));
        title.setForeground(Color.RED);
        title.setBounds(500,10,500,100);
        add(title);

        pagetitle = new JLabel("Art Submission Page",SwingConstants.CENTER);
        pagetitle.setFont(new Font("Verdana", Font.PLAIN, 20));
        pagetitle.setForeground(Color.BLACK);
        pagetitle.setBounds(600,100,250,50);
        add(pagetitle);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        submitButton.setBounds(300,700,80,30);
        add(submitButton);

        cancelbutton = new JButton("Cancel");
        cancelbutton.addActionListener(this);
        cancelbutton.setBounds(400,700,80,30);
        add(cancelbutton);

        fileChooser = new JFileChooser();
        fileChooser.setBounds(700,150,500,500);
        add(fileChooser);

        fileNameLabel = new JLabel("No file selected");

        Submissionpanel = new JPanel(new GridLayout(9,2,5,5));
        Submissionpanel.setBounds(200,150,400,500);
        Submissionpanel.setBackground(Color.CYAN);

        ArtName = new JLabel(" Art Name :");
        Submissionpanel.add(ArtName);
        ArtNameField = new JTextField();
        Submissionpanel.add(ArtNameField);

        Artist = new JLabel(" Artist Name :");
        Submissionpanel.add(Artist);
        ArtistField = new JTextField();
        Submissionpanel.add(ArtistField);

        Artype = new JLabel(" Art-Type :");
        Submissionpanel.add(Artype);
        String[] listarttype = { "Painting", "AI-Art", "Photography", "Sketch", "Pixel Art","Photo Painting", "Others" };
        JComboBox<String> ArttypeField = new JComboBox<>(listarttype);

        Submissionpanel.add(ArttypeField);

        Height = new JLabel(" Height : ");
        Submissionpanel.add(Height);
        HeightField = new JTextField();
        Submissionpanel.add(HeightField);

        Width = new JLabel(" Width : ");
        Submissionpanel.add(Width);
        WidthField = new JTextField();
        Submissionpanel.add(WidthField);


        Price = new JLabel(" Price : ");
        Submissionpanel.add(Price);
        PriceField = new JTextField();
        Submissionpanel.add(PriceField);

        Availability = new JLabel(" Availability : ");
        Submissionpanel.add(Availability);
        AvailabilityField = new JTextField();
        Submissionpanel.add(AvailabilityField);

        Descriptions = new JLabel(" Description : ");
        Submissionpanel.add(Descriptions);
        DescriptionsField = new JTextArea();
        Submissionpanel.add(DescriptionsField);
        
        add(Submissionpanel);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            int result = fileChooser.showOpenDialog(this);

        }else if(e.getSource() == cancelbutton) {
            new HomePage(username);
            setVisible(false);
        }
    }
}
