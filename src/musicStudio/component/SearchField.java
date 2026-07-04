package musicStudio.component;

import musicStudio.util.FontUtil;
import musicStudio.util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SearchField extends JTextField {

    public SearchField(int columns) {

        super(columns);

        setFont(FontUtil.normal());

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(8,10,8,10)
        ));

        setPreferredSize(new Dimension(250,38));

    }

}