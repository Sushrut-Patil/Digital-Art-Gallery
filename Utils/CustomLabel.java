
package Utils;

import javax.swing.*;
import java.awt.*;

public class CustomLabel extends JLabel {
    public CustomLabel(String Text) {
        setFont(new Font("Verdana", Font.PLAIN, 14));
        setText(" "+Text);
        setForeground(Color.BLACK);
        setHorizontalAlignment(CENTER);
        setHorizontalAlignment(CENTER);
    }
}
