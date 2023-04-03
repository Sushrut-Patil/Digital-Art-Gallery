import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;

public class ArtSubmissionPage extends JFrame implements ActionListener {
    int counter;
    private final JButton ImagePreviewbutton;
    private final JFileChooser fileChooser;
    private final JTextField ArtNameField,ArtistField,HeightField,WidthField,PriceField;

    private final JTextArea DescriptionsField;
    private final JComboBox<String> ArttypeField;
    private final JPanel ImagePreviewPanel;
    private File SelectedFile;
    private final String username;
    public ArtSubmissionPage(String username,int counter) {
        super("Art Submission Page");
        this.username = username;
        this.counter = counter;

        //Main Title
        JLabel title = new JLabel("Digital Art Gallery ", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 40));
        title.setForeground(Color.RED);
        title.setBounds(50,10,500,100);
        add(title);

        //Page Title
        JLabel pagetitle = new JLabel("Art Submission Page", SwingConstants.CENTER);
        pagetitle.setFont(new Font("Verdana", Font.PLAIN, 20));
        pagetitle.setForeground(Color.BLACK);
        pagetitle.setBounds(185,100,250,50);
        add(pagetitle);

        JLabel imageChooseLabel = new JLabel("Image Chooser", SwingConstants.CENTER);
        imageChooseLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        imageChooseLabel.setForeground(Color.BLACK);
        imageChooseLabel.setBounds(950,10,200,50);
        add(imageChooseLabel);

        JLabel imagePreviewLabel = new JLabel("Image Preview", SwingConstants.CENTER);
        imagePreviewLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        imagePreviewLabel.setForeground(Color.BLACK);
        imagePreviewLabel.setBounds(950,330,200,50);
        add(imagePreviewLabel);

        ImagePreviewPanel = new JPanel();
        ImagePreviewPanel.setBounds(650,380,800,450);
        add(ImagePreviewPanel);


        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        submitButton.setBounds(100,700,100,30);
        add(submitButton);

        ImagePreviewbutton = new JButton("Image Preview");
        ImagePreviewbutton.addActionListener(this);
        ImagePreviewbutton.setBounds(240,700,120,30);
        add(ImagePreviewbutton);

        JButton cancelbutton = new JButton("Cancel");
        cancelbutton.addActionListener(this);
        cancelbutton.setBounds(400,700,100,30);
        add(cancelbutton);

        fileChooser = new JFileChooser();
        fileChooser.setBounds(700,50,700,300);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images Filter", "jpg", "jpeg","png");
        fileChooser.setFileFilter(filter);
        add(fileChooser);


        JPanel submissionpanel = new JPanel(new GridLayout(7, 2, 5, 5));
        submissionpanel.setBounds(100,200,400,400);
        submissionpanel.setBackground(Color.YELLOW);

        JLabel artName = new JLabel(" Art Name :");
        submissionpanel.add(artName);
        ArtNameField = new JTextField();
        submissionpanel.add(ArtNameField);

        JLabel artist = new JLabel(" Artist Name :");
        submissionpanel.add(artist);
        ArtistField = new JTextField();
        submissionpanel.add(ArtistField);

        JLabel artype = new JLabel(" Art-Type :");
        submissionpanel.add(artype);
        String[] listarttype = { "Painting", "AI-Art", "Photography", "Sketch", "Pixel Art","Photo Painting", "Others" };
        ArttypeField = new JComboBox<>(listarttype);

        submissionpanel.add(ArttypeField);

        JLabel height = new JLabel(" Height : ");
        submissionpanel.add(height);
        HeightField = new JTextField();
        submissionpanel.add(HeightField);

        JLabel width = new JLabel(" Width : ");
        submissionpanel.add(width);
        WidthField = new JTextField();
        submissionpanel.add(WidthField);


        JLabel price = new JLabel(" Price : ");
        submissionpanel.add(price);
        PriceField = new JTextField();
        submissionpanel.add(PriceField);


        JLabel descriptions = new JLabel(" Description : ");
        submissionpanel.add(descriptions);
        DescriptionsField = new JTextArea();
        DescriptionsField.setLineWrap(true);
        submissionpanel.add(DescriptionsField);
        add(submissionpanel);

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
    public boolean ArtSubmission(String ArtName,String ArtistName,String ArtType,Integer Height,Integer Width,Integer Price,String Description) {
        Connection connection;
        PreparedStatement statement1,statement2;
        int num;
        try {
            connection = getConnection();


            ImageIcon icon = new ImageIcon(SelectedFile.getAbsolutePath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try(ObjectOutputStream os = new ObjectOutputStream(baos)) {
                os.writeObject(icon);
            }
            String sql1 = "Insert Into Art (image) Values (?)";
            statement1 = connection.prepareStatement(sql1);
            statement1.setBlob(1, new ByteArrayInputStream(baos.toByteArray()));

            String sql2 = "Insert Into ArtDetails " +
                    "(Owner,Art_Name,Artist_name,Art_type,Height,Width,Price,Description) " +
                    "Values (?,?,?,?,?,?,?,?)";
            statement2 = connection.prepareStatement(sql2);
            statement2.setString(1,username);
            statement2.setString(2,ArtName);
            statement2.setString(3,ArtistName);
            statement2.setString(4,ArtType);
            statement2.setInt(5,Height);
            statement2.setInt(6,Width);
            statement2.setInt(7,Price);
            statement2.setString(8,Description);

            num = statement1.executeUpdate();
            if (num==0){
                return true;
            }
            num = statement2.executeUpdate();
            if (num!=0) {
                return true;
            }
            statement1.close();
            statement2.close();
            connection.close();

        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==ImagePreviewbutton) {
            int Returnval = fileChooser.showOpenDialog(this);
            if(Returnval == JFileChooser.APPROVE_OPTION) {
                this.SelectedFile = fileChooser.getSelectedFile();
                System.out.println(SelectedFile);
                ArtPreview();
            } else if (Returnval == JFileChooser.CANCEL_OPTION) {
                    //TODO : Working of Cancel button in File Chooser
                    //TODO : Refreshing Image Viewer Panel
            }
        } else if(e.getActionCommand().equals("Cancel")) {
            new HomePage(username,counter);
            setVisible(false);
        } else if (e.getActionCommand().equals("Submit")) {

            String ArtName = ArtNameField.getText();
            String ArtistName = ArtistField.getText();
            String ArtType = (String) ArttypeField.getSelectedItem();
            Integer Height = Integer.valueOf(HeightField.getText());
            Integer Width = Integer.valueOf(WidthField.getText());
            Integer Price = Integer.valueOf(PriceField.getText());
            String Description = DescriptionsField.getText();

            if (ArtName.isEmpty() || ArtistName.isEmpty() || Height.equals(0) || Width.equals(0) || Price.equals(0)) {
                JOptionPane.showMessageDialog(this, "Please enter all fields");
            } else if (ArtSubmission(ArtName,ArtistName,ArtType,Height,Width,Price,Description)) {
                JOptionPane.showMessageDialog(this, "Art Submission successful");
                new HomePage(username,counter);
                //TODO Return to Page of Newly Inserted Art
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Please Enter Correct Details");
            }
        }
    }
}
