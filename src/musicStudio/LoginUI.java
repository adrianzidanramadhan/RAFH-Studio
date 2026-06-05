package musicStudio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginUI extends JFrame {

    private final Color sidebarColor = new Color(92, 64, 51);
    private final Color sidebarHover = new Color(120, 85, 68);
    private final Color bgColor = new Color(245, 245, 245);
    private final Color primaryAccent = new Color(224, 153, 94);

    public LoginUI() {

        setTitle("RAFH Studio");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        buildUI();

        setVisible(true);
    }

    private void buildUI() {

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(bgColor);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(420, 320));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220,220,220),
                        1,
                        true
                ),
                new EmptyBorder(30,30,30,30)
        ));

        JLabel iconLabel = new JLabel("🎵");
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("RAFH STUDIO");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Studio & Instrument Rental");
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton customerBtn =
                createModernButton(
                        "🎸 Masuk Sebagai Customer",
                        primaryAccent
                );

        JButton adminBtn =
                createModernButton(
                        "⚙️ Login Admin",
                        sidebarColor
                );

        customerBtn.addActionListener(e -> {

            new StudioUI();

            dispose();
        });

        adminBtn.addActionListener(e -> {

            showAdminLogin();
        });

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));

        card.add(title);
        card.add(Box.createVerticalStrut(5));

        card.add(subtitle);
        card.add(Box.createVerticalStrut(35));

        card.add(customerBtn);
        card.add(Box.createVerticalStrut(15));

        card.add(adminBtn);

        root.add(card);

        add(root);
    }

    private JButton createModernButton(
            String text,
            Color color) {

        JButton btn = new JButton(text);

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        50
                )
        );

        btn.setBackground(color);
        btn.setForeground(Color.WHITE);

        btn.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        Color hoverColor = color.darker();

        btn.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e) {

                        btn.setBackground(
                                hoverColor
                        );
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e) {

                        btn.setBackground(
                                color
                        );
                    }
                });

        return btn;
    }

    void showAdminLogin() {

        JTextField txtUser =
                new JTextField();

        JPasswordField txtPass =
                new JPasswordField();

        JPanel panel = new JPanel(
                new GridLayout(
                        2,
                        2,
                        10,
                        10
                )
        );

        panel.add(new JLabel("Username"));
        panel.add(txtUser);

        panel.add(new JLabel("Password"));
        panel.add(txtPass);

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Login Admin",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if(result ==
                JOptionPane.OK_OPTION) {

            boolean success =
                    AdminDAO.login(
                            txtUser.getText(),
                            new String(
                                    txtPass.getPassword()
                            )
                    );

            if(success) {

                new AdminDashboardUI();

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Username atau Password salah!",
                        "Login Gagal",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}