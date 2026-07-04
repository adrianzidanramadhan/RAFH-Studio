package musicStudio.component;

import musicStudio.util.FontUtil;
import musicStudio.util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SidebarButton extends JButton {

    public SidebarButton(String text) {

        super(text);

        setMaximumSize(new Dimension(220, 45));

        setAlignmentX(Component.LEFT_ALIGNMENT);

        setHorizontalAlignment(SwingConstants.LEFT);

        setFont(FontUtil.normal());

        setForeground(Color.WHITE);

        setBackground(Theme.SIDEBAR);

        setFocusPainted(false);

        setBorderPainted(false);

        setContentAreaFilled(false);

        setOpaque(true);

        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setBorder(new EmptyBorder(10,20,10,20));

        addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(Theme.SIDEBAR_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(Theme.SIDEBAR);
            }

        });

    }

}