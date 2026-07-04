package musicStudio.component;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {

    private Color backgroundColor;
    private int radius;

    public RoundedPanel() {
        this(Color.WHITE, 20);
    }

    public RoundedPanel(Color bg) {
        this(bg, 20);
    }

    public RoundedPanel(Color bg, int radius) {

        this.backgroundColor = bg;
        this.radius = radius;

        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(backgroundColor);

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                radius,
                radius
        );

        super.paintComponent(g2);

        g2.dispose();
    }

}