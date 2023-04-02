import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class ArtSubmissionPage extends JFrame implements ActionListener {
    int counter;
    private final JButton  submitButton,cancelbutton,ImagePreviewbutton;
    private  JLabel title,pagetitle,ImagePreviewLabel,ImageChooseLabel;
    private JFileChooser fileChooser;
    private JLabel ArtName,Artist,Artype,Height,Width,Price,Descriptions;
    private JTextField ArtNameField,ArtistField,HeightField,WidthField,PriceField;
    private JTextArea DescriptionsField;
    private JComboBox<String> ArttypeField;
    private JPanel Submissionpanel,ImagePreviewPanel;
    private File SelectedFile;
    private String username;
    public ArtSubmissionPage(String username,int counter) {
        super("Art Submission Page");
        this.username = username;
        this.counter = counter;
        username = this.username;
        //Main Title
        title = new JLabel("Digital Art Gallery ",SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 40));
        title.setForeground(Color.RED);
        title.setBounds(50,10,500,100);
        add(title);

        //Page Title
        pagetitle = new JLabel("Art Submission Page",SwingConstants.CENTER);
        pagetitle.setFont(new Font("Verdana", Font.PLAIN, 20));
        pagetitle.setForeground(Color.BLACK);
        pagetitle.setBounds(185,100,250,50);
        add(pagetitle);

        ImageChooseLabel = new JLabel("Image Chooser",SwingConstants.CENTER);
        ImageChooseLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        ImageChooseLabel.setForeground(Color.BLACK);
        ImageChooseLabel.setBounds(950,10,200,50);
        add(ImageChooseLabel);

        ImagePreviewLabel = new JLabel("Image Preview",SwingConstants.CENTER);
        ImagePreviewLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        ImagePreviewLabel.setForeground(Color.BLACK);
        ImagePreviewLabel.setBounds(950,330,200,50);
        add(ImagePreviewLabel);

        ImagePreviewPanel = new JPanel();
        ImagePreviewPanel.setBounds(650,380,800,450);
        add(ImagePreviewPanel);


        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        submitButton.setBounds(100,700,100,30);
        add(submitButton);

        ImagePreviewbutton = new JButton("Image Preview");
        ImagePreviewbutton.addActionListener(this);
        ImagePreviewbutton.setBounds(240,700,120,30);
        add(ImagePreviewbutton);

        cancelbutton = new JButton("Cancel");
        cancelbutton.addActionListener(this);
        cancelbutton.setBounds(400,700,100,30);
        add(cancelbutton);

        fileChooser = new JFileChooser();
        fileChooser.setBounds(700,50,700,300);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images Filter", "jpg", "jpeg","png");
        fileChooser.setFileFilter(filter);
        add(fileChooser);


        Submissionpanel = new JPanel(new GridLayout(7,2,5,5));
        Submissionpanel.setBounds(100,200,400,400);
        Submissionpanel.setBackground(Color.YELLOW);

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
        ArttypeField = new JComboBox<>(listarttype);

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
    public void ArtPreview() {
        try {
            System.out.println(SelectedFile);
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(SelectedFile));
            Image scaleImage = imageIcon.getImage().getScaledInstance(800, 450,Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaleImage));
            ImagePreviewPanel.add(imageLabel);
            ImagePreviewPanel.revalidate();
            ImagePreviewPanel.repaint();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/DigitalArtGallery", "root", "root");
    }
    public boolean ArtSubmission(String ArtName,String ArtistName,String ArtType,String Height,String Width,String Price,String Description) {
        boolean submitflag = false;
        Connection connection = null;
        PreparedStatement statement1,statement2 = null;
        int num;
        try {
            connection = getConnection();

            String sql1 = "Insert Into Art (image) Values (?)";
            statement1 = connection.prepareStatement(sql1);
            statement1.setString(1,SelectedFile.getAbsolutePath());

            String sql2 = "Insert Into ArtDetails " +
                    "(Owner,Art_Name,Artist_name,Art_type,Height,Width,Price,Description) " +
                    "Values (?,?,?,?,?,?,?,?)";
            statement2 = connection.prepareStatement(sql2);
            statement2.setString(1,username);
            statement2.setString(2,ArtName);
            statement2.setString(3,ArtistName);
            statement2.setString(4,ArtType);
            statement2.setString(5,Height);
            statement2.setString(6,Width);
            statement2.setString(7,Price);
            statement2.setString(8,Description);

            num = statement1.executeUpdate();
            if (num==0){
                return submitflag;
            }
            num = statement2.executeUpdate();
            if (num!=0) {
                submitflag = true;
            }
            statement1.close();
            statement2.close();
            connection.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return submitflag;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==ImagePreviewbutton) {
            int Returnval = fileChooser.showOpenDialog(this);
            if(Returnval == JFileChooser.APPROVE_OPTION) {
                this.SelectedFile = fileChooser.getSelectedFile();
                System.out.println(SelectedFile);
                ArtPreview();
            } else if (Returnval == fileChooser.CANCEL_OPTION) {
                    //TODO : Working of Cancel button in File Chooser
            }
        } else if(e.getActionCommand().equals("Cancel")) {
            new HomePage(username,counter);
            setVisible(false);
        } else if (e.getActionCommand().equals("Submit")) {

            String ArtName = ArtNameField.getText();
            String ArtistName = ArtistField.getText();
            String ArtType = (String) ArttypeField.getSelectedItem();
            String Height = HeightField.getText();
            String Width = WidthField.getText();
            String Price = PriceField.getText();
            String Description = DescriptionsField.getText();
            if (ArtName.isEmpty() || ArtistName.isEmpty() || Height.isEmpty() || Width.isEmpty() || Price.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all fields");
            } else if (ArtSubmission(ArtName,ArtistName,ArtType,Height,Width,Price,Description)) {
                JOptionPane.showMessageDialog(this, "Art Submission successful");
                new HomePage(username,1);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Please Enter Correct Details");
            }
        }
    }
}
