package Utils;

import javax.swing.*;
import java.awt.*;

public class MainTitle extends JLabel {
    public MainTitle() {
        setText("Digital Art Gallery ");
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setFont(new Font("Verdana",Font.BOLD, 40));
        setForeground(Color.RED);
    }
}
