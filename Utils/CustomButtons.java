package Utils;

import javax.swing.*;
import java.awt.*;

public class CustomButtons extends JButton {
    public CustomButtons(String Text,Color color) {
        setFont(new Font("Verdana", Font.PLAIN, 14));
        setText(Text);
        setForeground(Color.WHITE);
        setBackground(color);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
    }
}
