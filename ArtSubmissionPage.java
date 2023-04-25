import Utils.CustomButtons;
import Utils.CustomLabel;
import Utils.MainTitle;
import Utils.PageTitles;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class ArtSubmissionPage extends JFrame implements ActionListener {

    private final JButton ImagePreviewbutton;
    private final JFileChooser fileChooser;
    private final JTextField ArtNameField,ArtistField,PriceField;
    private final JTextArea DescriptionsField;
    private final JComboBox<String> ArttypeField;
    JPanel ImagePreviewPanel = new JPanel(),HeigthWidthPanel;
    Integer Height=0,Width=0;
    private File SelectedFile;

    public ArtSubmissionPage() {
        super("Art Submission Page");

        //Main Title
        JLabel title = new MainTitle();
        title.setBounds(50,10,500,100);
        add(title);

        //Page Title
        JLabel pagetitle = new PageTitles("Art Submission Page");
        pagetitle.setBounds(185,100,250,50);
        add(pagetitle);

        JLabel imagePreviewLabel = new PageTitles("Image Preview");
        imagePreviewLabel.setBounds(950,10,200,50);
        add(imagePreviewLabel);

        JButton submitButton = new CustomButtons("Submit",Color.GREEN);
        submitButton.addActionListener(this);
        submitButton.setBounds(100,700,100,30);
        add(submitButton);

        ImagePreviewbutton = new CustomButtons("Image Preview",Color.BLUE);
        ImagePreviewbutton.addActionListener(this);
        ImagePreviewbutton.setBounds(230,700,150,30);
        add(ImagePreviewbutton);

        JButton cancelbutton = new CustomButtons("Cancel",Color.red);
        cancelbutton.addActionListener(this);
        cancelbutton.setBounds(400,700,100,30);
        add(cancelbutton);

        fileChooser = new JFileChooser();
        fileChooser.setBounds(700,50,700,300);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images Filter", "jpg", "jpeg","png");
        fileChooser.setFileFilter(filter);


        JPanel submissionpanel = new JPanel(new GridLayout(5, 2, 5, 5));
        submissionpanel.setBounds(100,200,400,300);
        submissionpanel.setBackground(Color.YELLOW);

        JLabel artName = new CustomLabel(" Art Name :");
        submissionpanel.add(artName);
        ArtNameField = new JTextField();
        submissionpanel.add(ArtNameField);

        JLabel artist = new CustomLabel(" Artist Name :");
        submissionpanel.add(artist);
        ArtistField = new JTextField();
        submissionpanel.add(ArtistField);

        JLabel artype = new CustomLabel(" Art-Type :");
        submissionpanel.add(artype);
        String[] listarttype = { "Painting", "AI-Art", "Photography", "Sketch", "Sculptor","Photo Painting","Digital Art", "Others" };
        ArttypeField = new JComboBox<>(listarttype);

        submissionpanel.add(ArttypeField);


        JLabel price = new CustomLabel(" Price : ");
        submissionpanel.add(price);
        PriceField = new JTextField();
        submissionpanel.add(PriceField);


        JLabel descriptions = new CustomLabel(" Description : ");
        submissionpanel.add(descriptions);
        DescriptionsField = new JTextArea();
        DescriptionsField.setLineWrap(true);
        submissionpanel.add(DescriptionsField);
        add(submissionpanel);

        HeigthWidthPanel = new JPanel(new GridLayout(2,2,5,5));
        HeigthWidthPanel.setBounds(100,500,400,120);
        HeigthWidthPanel.setBackground(Color.YELLOW);
        HeightWidth();


        add(HeigthWidthPanel);
        add(ImagePreviewPanel);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

    }
    public void HeightWidth() {

        HeigthWidthPanel.removeAll();
        JLabel height = new CustomLabel(" Height : ");
        HeigthWidthPanel.add(height);
        JLabel heightLabel = new CustomLabel(String.valueOf(Height));
        HeigthWidthPanel.add(heightLabel);

        JLabel width = new CustomLabel(" Width : ");
        HeigthWidthPanel.add(width);
        JLabel widthLabel = new CustomLabel(String.valueOf(Width));
        HeigthWidthPanel.add(widthLabel);

        HeigthWidthPanel.revalidate();
        HeigthWidthPanel.repaint();
    }
    public void ArtPreview() {

        ImageIcon imageIcon = null;
        try {
            System.out.println(SelectedFile);
            imageIcon = new ImageIcon(ImageIO.read(SelectedFile));
            Image scaleImage = imageIcon.getImage().getScaledInstance(imageIcon.getIconWidth() / 2, imageIcon.getIconHeight() / 2, Image.SCALE_SMOOTH);

            ImagePreviewPanel.removeAll();
            ImagePreviewPanel.add(new JLabel(new ImageIcon(scaleImage)));

            ImagePreviewPanel.setBounds(650, 50, imageIcon.getIconWidth() / 2, imageIcon.getIconHeight() / 2);
            ImagePreviewPanel.revalidate();
            ImagePreviewPanel.repaint();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Height = Objects.requireNonNull(imageIcon).getIconWidth();
        Width = Objects.requireNonNull(imageIcon).getIconHeight();
        HeightWidth();
    }
    public boolean ArtSubmission(String ArtName,String ArtistName,String ArtType,Integer Height,Integer Width,Integer Price,String Description) {
        Connection connection;
        PreparedStatement statement1,statement2;
        int num;
        try {
            connection = Main.getConnection();

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
            statement2.setString(1,Main.getUsername());
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
                ArtPreview();
            } else if (Returnval == JFileChooser.CANCEL_OPTION) {
                    fileChooser.cancelSelection();

            }
        } else if(e.getActionCommand().equals("Cancel")) {
            new HomePage();
            dispose();
        } else if (e.getActionCommand().equals("Submit") && SelectedFile!=null) {

            String ArtName = ArtNameField.getText();
            String ArtistName = ArtistField.getText();
            String ArtType = (String) ArttypeField.getSelectedItem();
            Integer Price = Integer.valueOf(PriceField.getText());
            String Description = DescriptionsField.getText();

            if (ArtName.isEmpty() || ArtistName.isEmpty() || Height.equals(0) || Width.equals(0) || Price.equals(0) || SelectedFile ==null) {

                JOptionPane.showMessageDialog(this, "Please enter all fields");

            } else if (ArtSubmission(ArtName,ArtistName,ArtType,Height,Width,Price,Description)) {

                JOptionPane.showMessageDialog(this, "Art Submission successful");
                Main.ArtCounter();
                Main.setCounter(Main.Upper);
                new HomePage();
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Please Enter Correct Details");
            }
        }
    }
}




