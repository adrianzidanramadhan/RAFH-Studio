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
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminDashboardUI extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Label untuk statistik di halaman Dashboard Overview
    private JLabel lblStudio;
    private JLabel lblInstrument;
    private JLabel lblBooking;
    private JLabel lblRevenue;

    // Label untuk Jam Digital (Thread 1)
    private JLabel lblClock;

    public AdminDashboardUI() {
        setTitle("RAFH Studio - Admin Dashboard");
        setSize(1280, 720);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        buildUI();
        
        loadStatistics(); // Muat data awal
        startClockThread(); // -> JALANKAN THREAD JAM DIGITAL

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

        JLabel lblLogo = new JLabel("👑 RAFH ADMIN");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblLogo.setForeground(Theme.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(lblLogo);
        sidebar.add(Box.createVerticalStrut(35));

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

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);

        root.add(sidebar, BorderLayout.WEST);

        // --- 2. CONTENT PANEL (Kanan) ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BACKGROUND);

        JPanel dashboardOverview = createDashboardOverviewPanel();
        StudioManagementUI studioPanel = new StudioManagementUI();
        InstrumentManagementUI instrumentPanel = new InstrumentManagementUI();
        BookingHistoryUI bookingPanel = new BookingHistoryUI();

        contentPanel.add(dashboardOverview, "MENU_DASHBOARD");
        contentPanel.add(studioPanel, "MENU_STUDIO");
        contentPanel.add(instrumentPanel, "MENU_INSTRUMENT");
        contentPanel.add(bookingPanel, "MENU_BOOKING");

        cardLayout.show(contentPanel, "MENU_DASHBOARD");
        root.add(contentPanel, BorderLayout.CENTER);

        // --- 3. EVENT LISTENER ---
        btnDashboard.addActionListener(e -> {
            loadStatistics();
            cardLayout.show(contentPanel, "MENU_DASHBOARD");
        });

        btnStudio.addActionListener(e -> cardLayout.show(contentPanel, "MENU_STUDIO"));
        btnInstrument.addActionListener(e -> cardLayout.show(contentPanel, "MENU_INSTRUMENT"));

        btnBooking.addActionListener(e -> {
            bookingPanel.loadData();
            cardLayout.show(contentPanel, "MENU_BOOKING");
        });

        // -> IMPLEMENTASI THREAD 2: REFRESH DENGAN SWINGWORKER
        btnRefresh.addActionListener(e -> {
            btnRefresh.setEnabled(false);
            btnRefresh.setText("⏳  Memuat...");

            // Pekerja Background Thread
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    // Simulasi delay sedikit (500ms) agar terasa proses load-nya
                    Thread.sleep(500); 
                    return null;
                }

                @Override
                protected void done() {
                    loadStatistics(); // Update label di GUI setelah selesai
                    btnRefresh.setEnabled(true);
                    btnRefresh.setText("🔄  Refresh Statistik");
                    JOptionPane.showMessageDialog(AdminDashboardUI.this, "Statistik berhasil diperbarui!", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            };
            worker.execute(); // Jalankan thread pekerja!
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this, "Apakah Anda yakin ingin logout?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
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

        // Header dengan Judul & Jam Digital
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BACKGROUND);
        header.setBorder(new EmptyBorder(30, 35, 15, 35));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Theme.TEXT);
        header.add(title, BorderLayout.WEST);

        // Label untuk menampung jam real-time
        lblClock = new JLabel("⏰ Memuat waktu...");
        lblClock.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblClock.setForeground(Theme.GRAY);
        header.add(lblClock, BorderLayout.EAST);

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

    // --- IMPLEMENTASI THREAD 1: JAM DIGITAL REAL-TIME ---
    private void startClockThread() {
        Thread clockThread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy  |  HH:mm:ss");
            while (true) {
                try {
                    String currentTime = sdf.format(new Date());
                    // Gunakan SwingUtilities.invokeLater agar aman mengupdate teks GUI dari background thread
                    SwingUtilities.invokeLater(() -> {
                        if (lblClock != null) {
                            lblClock.setText("⏰  " + currentTime);
                        }
                    });
                    Thread.sleep(1000); // Tidur 1 detik sebelum update waktu lagi
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        clockThread.setDaemon(true); // Thread otomatis mati jika aplikasi ditutup
        clockThread.start();
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
            public void mouseEntered(MouseEvent e) { btn.setBackground(Theme.SIDEBAR_HOVER); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(Theme.SIDEBAR); }
        });

        return btn;
    }
}