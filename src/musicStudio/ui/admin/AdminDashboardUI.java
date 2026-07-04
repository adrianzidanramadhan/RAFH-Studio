package musicStudio.ui.admin;

import musicStudio.ui.customer.LoginUI;
import musicStudio.util.Theme;
import musicStudio.dao.BookingDAO;
import musicStudio.dao.StudioDAO;
import musicStudio.dao.InstrumentDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminDashboardUI extends JFrame {

    private JPanel contentPanel; // Panel kanan utama (CardLayout)
    private CardLayout cardLayout;

    // Label untuk statistik di halaman Dashboard Overview
    private JLabel lblStudio;
    private JLabel lblInstrument;
    private JLabel lblBooking;
    private JLabel lblRevenue;

    public AdminDashboardUI() {
        setTitle("RAFH Studio - Admin Dashboard");
        setSize(1280, 720);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen opsional
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        buildUI();
        loadStatistics(); // Muat data statistik saat pertama buka

        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BACKGROUND);

        // --- 1. SIDEBAR PANEL (Kiri) ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Theme.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(new EmptyBorder(30, 15, 30, 15));

        // Logo / Title Admin
        JLabel lblLogo = new JLabel("👑 RAFH ADMIN");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblLogo.setForeground(Theme.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(lblLogo);
        sidebar.add(Box.createVerticalStrut(35));

        // Daftar Tombol Sidebar
        JButton btnDashboard  = createSidebarButton("🏠  Dashboard Overview");
        JButton btnStudio     = createSidebarButton("🏢  Kelola Studio");
        JButton btnInstrument = createSidebarButton("🎸  Kelola Instrument");
        JButton btnBooking    = createSidebarButton("📋  Riwayat Booking");
        JButton btnRefresh    = createSidebarButton("🔄  Refresh Statistik");
        JButton btnLogout     = createSidebarButton("🚪  Logout");

        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnStudio);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnInstrument);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnBooking);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnRefresh);

        sidebar.add(Box.createVerticalGlue()); // Mendorong tombol logout ke bawah
        sidebar.add(btnLogout);

        root.add(sidebar, BorderLayout.WEST);

        // --- 2. CONTENT PANEL DENGAN CARDLAYOUT (Kanan) ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BACKGROUND);

        // Inisialisasi Seluruh Halaman (Panel)
        JPanel dashboardOverview = createDashboardOverviewPanel();
        StudioManagementUI studioPanel = new StudioManagementUI();
        InstrumentManagementUI instrumentPanel = new InstrumentManagementUI();
        BookingHistoryUI bookingPanel = new BookingHistoryUI(); // --> Panel Riwayat Booking Ditambahkan!

        // Daftarkan ke CardLayout dengan Key unik
        contentPanel.add(dashboardOverview, "MENU_DASHBOARD");
        contentPanel.add(studioPanel, "MENU_STUDIO");
        contentPanel.add(instrumentPanel, "MENU_INSTRUMENT");
        contentPanel.add(bookingPanel, "MENU_BOOKING"); // --> Didaftarkan ke CardLayout!

        // Tampilkan halaman pertama (Dashboard Overview)
        cardLayout.show(contentPanel, "MENU_DASHBOARD");

        root.add(contentPanel, BorderLayout.CENTER);

        // --- 3. EVENT LISTENER NAVIGASI TOMBOL SIDEBAR ---
        btnDashboard.addActionListener(e -> {
            loadStatistics(); // Update statistik saat balik ke dashboard
            cardLayout.show(contentPanel, "MENU_DASHBOARD");
        });

        btnStudio.addActionListener(e -> cardLayout.show(contentPanel, "MENU_STUDIO"));
        
        btnInstrument.addActionListener(e -> cardLayout.show(contentPanel, "MENU_INSTRUMENT"));

        // --> Navigasi ke Riwayat Booking (Bukan Popup Lagi!)
        btnBooking.addActionListener(e -> {
            bookingPanel.loadData(); // Otomatis refresh data terbaru sebelum dipanggil
            cardLayout.show(contentPanel, "MENU_BOOKING");
        });

        btnRefresh.addActionListener(e -> {
            loadStatistics();
            JOptionPane.showMessageDialog(this, "Statistik berhasil diperbarui!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Apakah Anda yakin ingin logout?",
                    "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginUI();
                dispose();
            }
        });

        add(root);
    }

    // --- HALAMAN DASHBOARD OVERVIEW ---
    private JPanel createDashboardOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(Theme.BACKGROUND);
        header.setBorder(new EmptyBorder(30, 35, 15, 35));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Theme.TEXT);
        header.add(title);

        panel.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 2, 25, 25));
        center.setBackground(Theme.BACKGROUND);
        center.setBorder(new EmptyBorder(10, 35, 35, 35));

        lblStudio = new JLabel("0");
        lblInstrument = new JLabel("0");
        lblBooking = new JLabel("0");
        lblRevenue = new JLabel("Rp 0");

        center.add(createStatCard("🏢 Total Studio", lblStudio));
        center.add(createStatCard("🎸 Total Instrument", lblInstrument));
        center.add(createStatCard("📋 Total Booking", lblBooking));
        center.add(createStatCard("💰 Total Revenue", lblRevenue));

        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.CARD);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitle.setForeground(Theme.GRAY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        valueLabel.setForeground(Theme.ACCENT);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(15));
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    // --- FUNGSI LOAD STATISTIK DARI DATABASE ---
    private void loadStatistics() {
        try {
            int totalStudio = StudioDAO.getAllStudio().size();
            int totalInstrument = InstrumentDAO.getAllInstrument().size();
            int totalBooking = BookingDAO.getTotalBooking();
            int totalRevenue = BookingDAO.getTotalRevenue();

            if (lblStudio != null) lblStudio.setText(String.valueOf(totalStudio));
            if (lblInstrument != null) lblInstrument.setText(String.valueOf(totalInstrument));
            if (lblBooking != null) lblBooking.setText(String.valueOf(totalBooking));
            if (lblRevenue != null) lblRevenue.setText("Rp " + String.format("%,d", totalRevenue));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- HELPER BUAT TOMBOL SIDEBAR MODERN ---
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setBackground(Theme.SIDEBAR);
        btn.setForeground(Theme.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(Theme.SIDEBAR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Theme.SIDEBAR);
            }
        });

        return btn;
    }
}