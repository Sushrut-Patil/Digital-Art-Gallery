import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchArt extends JFrame implements ActionListener {



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

        JPanel ArtFilter = new JPanel(new GridLayout(5,2,10,10));
        ArtFilter.setBounds(0,100,400,400);
        ArtFilter.setBackground(Color.CYAN);

        JLabel PriceLabel = new JLabel("Select Price");
        ArtFilter.add(PriceLabel);

        JLabel ArtTypeLabel = new JLabel("Select Art Type");
        ArtFilter.add(ArtTypeLabel);

        JLabel DimensionsLabel = new JLabel("Select Dimensions");
        ArtFilter.add(DimensionsLabel);

        JLabel AvailabilityLabel = new JLabel("Select Availability");
        ArtFilter.add(AvailabilityLabel);

        add(ArtFilter);

        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }



}
