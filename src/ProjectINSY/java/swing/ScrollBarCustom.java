
package ProjectINSY.java.swing;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollBar;

public class ScrollBarCustom extends JScrollBar {

    public ScrollBarCustom() {
        setUI(new ModernScrollBarUI());
        setUnitIncrement(30);
        setPreferredSize(new Dimension(8, 8));
        setForeground(new Color(25, 102, 24));
        setBackground(Color.WHITE);
    }
}
