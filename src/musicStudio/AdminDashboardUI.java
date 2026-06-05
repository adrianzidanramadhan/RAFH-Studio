package musicStudio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminDashboardUI extends JFrame {

    Color sidebarColor = new Color(92, 64, 51);
    Color sidebarHover = new Color(120, 85, 68);
    Color bgColor = new Color(245, 245, 245);
    Color primaryAccent = new Color(224, 153, 94);

    JLabel lblStudio;
    JLabel lblInstrument;
    JLabel lblBooking;
    JLabel lblRevenue;

    public AdminDashboardUI() {

        setTitle("RAFH Studio - Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        loadStatistics();

        setVisible(true);
    }

    private void buildUI() {

        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );

        sidebar.setBackground(sidebarColor);
        sidebar.setPreferredSize(
                new Dimension(240, 0)
        );

        sidebar.setBorder(
                new EmptyBorder(25,15,25,15)
        );

        JLabel brand =
                new JLabel("RAFH ADMIN");

        brand.setForeground(Color.WHITE);

        brand.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        brand.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        sidebar.add(brand);
        sidebar.add(Box.createVerticalStrut(20));

        JButton btnStudio =
                createSidebarButton(
                        "🏢 Kelola Studio"
                );

        JButton btnInstrument =
                createSidebarButton(
                        "🎸 Kelola Instrument"
                );

        JButton btnBooking =
                createSidebarButton(
                        "📋 Riwayat Booking"
                );

        JButton btnRefresh =
                createSidebarButton(
                        "🔄 Refresh Statistik"
                );

        JButton btnLogout =
                createSidebarButton(
                        "🚪 Logout"
                );

        btnStudio.addActionListener(e -> {
            new StudioManagementUI();
        });

        btnInstrument.addActionListener(e -> {
            new InstrumentManagementUI();
        });

        btnBooking.addActionListener(e -> {
            showBookingHistory();
        });

        btnRefresh.addActionListener(e -> {
            loadStatistics();
        });

        btnLogout.addActionListener(e -> {

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Logout sekarang?",
                            "Konfirmasi",
                            JOptionPane.YES_NO_OPTION
                    );

            if(confirm ==
                    JOptionPane.YES_OPTION){

                new LoginUI();

                dispose();
            }
        });

        sidebar.add(btnStudio);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(btnInstrument);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(btnBooking);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(btnRefresh);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(btnLogout);

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JButton createSidebarButton(
            String text
    ) {

        JButton btn =
                new JButton(text);

        btn.setMaximumSize(
                new Dimension(210,45)
        );

        btn.setForeground(Color.WHITE);
        btn.setBackground(sidebarColor);

        btn.setBorderPainted(false);
        btn.setFocusPainted(false);

        btn.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setBorder(
                new EmptyBorder(
                        10,15,10,15
                )
        );

        btn.addMouseListener(
                new java.awt.event.MouseAdapter() {

            public void mouseEntered(
                    java.awt.event.MouseEvent evt
            ) {
                btn.setBackground(
                        sidebarHover
                );
            }

            public void mouseExited(
                    java.awt.event.MouseEvent evt
            ) {
                btn.setBackground(
                        sidebarColor
                );
            }
        });

        return btn;
    }

    private JPanel createMainPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(bgColor);

        JLabel title =
                new JLabel(
                        "Dashboard Admin"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        JPanel header =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        header.setBackground(bgColor);
        header.setBorder(
                new EmptyBorder(
                        25,25,15,25
                )
        );

        header.add(title);

        panel.add(
                header,
                BorderLayout.NORTH
        );

        JPanel center =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                20,
                                20
                        )
                );

        center.setBackground(bgColor);

        center.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        25,
                        25
                )
        );

        lblStudio = new JLabel();
        lblInstrument = new JLabel();
        lblBooking = new JLabel();
        lblRevenue = new JLabel();

        center.add(
                createStatCard(
                        "Total Studio",
                        lblStudio
                )
        );

        center.add(
                createStatCard(
                        "Total Instrument",
                        lblInstrument
                )
        );

        center.add(
                createStatCard(
                        "Total Booking",
                        lblBooking
                )
        );

        center.add(
                createStatCard(
                        "Total Revenue",
                        lblRevenue
                )
        );

        panel.add(
                center,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel createStatCard(
            String title,
            JLabel valueLabel
    ) {

        JPanel card =
                new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        220,
                                        220
                                )
                        ),
                        new EmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );

        JLabel lblTitle =
                new JLabel(title);

        lblTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        18
                )
        );

        valueLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        36
                )
        );

        valueLabel.setForeground(
                primaryAccent
        );

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(20));
        card.add(valueLabel);

        return card;
    }

    private void loadStatistics() {

        try {

            int totalStudio =
                    StudioDAO
                            .getAllStudio()
                            .size();

            int totalInstrument =
                    InstrumentDAO
                            .getAllInstrument()
                            .size();

            int totalBooking =
                    BookingDAO
                            .getTotalBooking();

            int totalRevenue =
                    BookingDAO
                            .getTotalRevenue();

            lblStudio.setText(
                    String.valueOf(
                            totalStudio
                    )
            );

            lblInstrument.setText(
                    String.valueOf(
                            totalInstrument
                    )
            );

            lblBooking.setText(
                    String.valueOf(
                            totalBooking
                    )
            );

            lblRevenue.setText(
                    "Rp "
                    + totalRevenue
            );

        } catch(Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Gagal memuat statistik"
            );
        }
    }

    private void showBookingHistory() {

        JTextArea area =
                new JTextArea();

        area.setEditable(false);

        for(String s :
                BookingHistoryDAO
                        .getHistory()) {

            area.append(
                    s + "\n"
            );
        }

        JScrollPane sp =
                new JScrollPane(area);

        sp.setPreferredSize(
                new Dimension(
                        900,
                        500
                )
        );

        JOptionPane.showMessageDialog(
                this,
                sp,
                "Riwayat Booking",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}