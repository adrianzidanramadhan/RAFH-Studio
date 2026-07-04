package musicStudio.component;

import musicStudio.util.FontUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RoundedButton extends JButton {

    private Color backgroundColor;

    public RoundedButton(String text, Color color) {

        super(text);

        backgroundColor = color;

        setFont(FontUtil.button());

        setForeground(Color.WHITE);

        setBackground(color);

        setFocusPainted(false);

        setBorderPainted(false);

        setContentAreaFilled(false);

        setOpaque(false);

        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setBorder(new EmptyBorder(10,20,10,20));

        addHoverEffect();
    }

    private void addHoverEffect() {

        addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {

                setBackground(backgroundColor.darker());

            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {

                setBackground(backgroundColor);

            }

        });

    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(getBackground());

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                18,
                18
        );

        super.paintComponent(g2);

        g2.dispose();

    }

}