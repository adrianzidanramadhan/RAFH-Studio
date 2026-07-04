package musicStudio.ui.customer;

import musicStudio.dao.AdminDAO;
import musicStudio.ui.admin.AdminDashboardUI;
import musicStudio.util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginUI extends JFrame {

    public LoginUI() {
        setTitle("RAFH Studio - Welcome");
        setSize(700, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        buildUI();

        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(Theme.BACKGROUND);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(420, 340));
        card.setBackground(Theme.CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1, true),
                new EmptyBorder(30, 35, 30, 35)
        ));

        JLabel iconLabel = new JLabel("🎵");
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("RAFH STUDIO");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Theme.TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Studio & Instrument Rental");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Theme.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton customerBtn = createModernButton(
                "🎸 Masuk Sebagai Customer",
                Theme.ACCENT,
                Theme.ACCENT.darker()
        );

        JButton adminBtn = createModernButton(
                "⚙️ Login Admin",
                Theme.SIDEBAR,
                Theme.SIDEBAR_HOVER
        );

        customerBtn.addActionListener(e -> {
            new StudioUI();
            dispose();
        });

        adminBtn.addActionListener(e -> showAdminLogin());

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(35));

        card.add(customerBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(adminBtn);

        root.add(card);
        add(root);
    }

    private JButton createModernButton(String text, Color baseColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setBackground(baseColor);
        btn.setForeground(Theme.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });

        return btn;
    }

    // --- POPUP LOGIN ADMIN KUSTOM YANG DIPERMAK ---
    private void showAdminLogin() {
        JDialog dialog = new JDialog(this, "🔐 Otorisasi Admin Studio", true);
        dialog.setSize(380, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Theme.CARD);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header Dialog
        JLabel lblHeader = new JLabel("Login Administrator");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblHeader.setForeground(Theme.TEXT);
        lblHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields
        JTextField txtUser = new JTextField();
        styleInputField(txtUser);

        JPasswordField txtPass = new JPasswordField();
        styleInputField(txtPass);

        // Row Username
        JPanel userRow = createInputRow("Username", txtUser);
        // Row Password
        JPanel passRow = createInputRow("Password", txtPass);

        // Panel Tombol
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(Theme.CARD);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton btnLogin = createModernButton("Masuk", Theme.SIDEBAR, Theme.SIDEBAR_HOVER);
        JButton btnCancel = createModernButton("Batal", Theme.GRAY, Theme.GRAY.darker());

        // Aksi Tombol Masuk
        Runnable doLogin = () -> {
            String username = txtUser.getText().trim();
            String password = new String(txtPass.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Username dan Password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success = AdminDAO.login(username, password);

            if (success) {
                dialog.dispose();
                new AdminDashboardUI();
                dispose(); // Tutup LoginUI
            } else {
                JOptionPane.showMessageDialog(dialog, "Username atau Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                txtPass.setText("");
            }
        };

        btnLogin.addActionListener(e -> doLogin.run());
        btnCancel.addActionListener(e -> dialog.dispose());
        
        // Tekan Enter di kolom password otomatis klik login
        txtPass.addActionListener(e -> doLogin.run());

        btnPanel.add(btnCancel);
        btnPanel.add(btnLogin);

        // Susun Komponen
        mainPanel.add(lblHeader);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(userRow);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(passRow);
        mainPanel.add(Box.createVerticalStrut(25));
        mainPanel.add(btnPanel);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private JPanel createInputRow(String label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(10, 5));
        row.setBackground(Theme.CARD);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(Theme.TEXT);
        lbl.setPreferredSize(new Dimension(75, 30));

        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private void styleInputField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                new EmptyBorder(5, 8, 5, 8)
        ));
    }
}