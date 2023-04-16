package Utils;
import javax.swing.*;

public class BaseFrame extends JFrame {
    public BaseFrame(String Title) {

        super(Title);
        //Frame Settings
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

    }
}
