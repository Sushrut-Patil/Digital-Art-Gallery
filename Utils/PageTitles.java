package Utils;

import javax.swing.*;
import java.awt.*;

public class PageTitles extends JLabel{
    public PageTitles (String Text) {
        setText(Text);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(new Font("Verdana", Font.PLAIN, 20));
        setForeground(Color.BLACK);
    }

}
