package musicStudio.component;

import musicStudio.util.FontUtil;
import musicStudio.util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardCard extends RoundedPanel {

    private JLabel iconLabel;
    private JLabel titleLabel;
    private JLabel valueLabel;

    public DashboardCard(String icon, String title, String value) {

        super(Theme.CARD, 25);

        setLayout(new BorderLayout());

        setBorder(new EmptyBorder(20,20,20,20));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        titleLabel = new JLabel(title);
        titleLabel.setFont(FontUtil.normal());

        top.add(iconLabel, BorderLayout.WEST);
        top.add(titleLabel, BorderLayout.CENTER);

        valueLabel = new JLabel(value);

        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setFont(FontUtil.title());
        valueLabel.setForeground(Theme.ACCENT);

        add(top, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);

        setPreferredSize(new Dimension(250,140));

    }

    public void setValue(String value){

        valueLabel.setText(value);

    }

    public void setTitle(String title){

        titleLabel.setText(title);

    }

}
}
