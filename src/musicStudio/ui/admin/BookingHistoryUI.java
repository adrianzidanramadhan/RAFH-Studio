package musicStudio.ui.admin;

import musicStudio.dao.BookingHistoryDAO;
import musicStudio.util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BookingHistoryUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch = new JTextField();

    public BookingHistoryUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        buildUI();
        loadData();
    }

    private void buildUI() {
        JPanel rootPanel = new JPanel(new BorderLayout(20, 20));
        rootPanel.setBackground(Theme.BACKGROUND);
        rootPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header Title
        JLabel titleLabel = new JLabel("📋 Riwayat Seluruh Booking Studio & Instrument");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Theme.TEXT);
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel Utama (Tabel + Search Bar)
        rootPanel.add(createMainContent(), BorderLayout.CENTER);

        add(rootPanel, BorderLayout.CENTER);
    }

    private JPanel createMainContent() {
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBackground(Theme.BACKGROUND);

        // --- 1. SEARCH BAR DI ATAS TABEL ---
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchBar.setBackground(Theme.BACKGROUND);

        styleTextField(txtSearch);
        txtSearch.setPreferredSize(new Dimension(250, 35));

        JButton btnSearch = new JButton("🔍 Cari");
        styleButtonSmall(btnSearch, Theme.ACCENT);

        JButton btnRefresh = new JButton("🔄 Refresh Data");
        styleButtonSmall(btnRefresh, Theme.SIDEBAR);

        btnSearch.addActionListener(e -> searchData());
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadData();
        });

        searchBar.add(new JLabel("Cari (Nama / Tanggal) :"));
        searchBar.add(txtSearch);
        searchBar.add(btnSearch);
        searchBar.add(btnRefresh);

        contentPanel.add(searchBar, BorderLayout.NORTH);

        // --- 2. TABLE PANEL (CARD STYLE) ---
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Theme.CARD);
        tableContainer.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1, true));

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel hanya untuk dibaca (Read-Only)
            }
        };

        // Buat kolom tabel yang rapi
        model.addColumn("ID");
        model.addColumn("Item / Ruangan");
        model.addColumn("Tanggal");
        model.addColumn("Jam");
        model.addColumn("Durasi");
        model.addColumn("Total Biaya");

        table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(32);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Styling Table Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(Theme.SIDEBAR);
        header.setForeground(Theme.WHITE);
        header.setPreferredSize(new Dimension(0, 40));

        // Perataan Kolom (ID, Tanggal, Jam, Durasi di Tengah)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.WHITE);

        tableContainer.add(scroll, BorderLayout.CENTER);
        contentPanel.add(tableContainer, BorderLayout.CENTER);

        return contentPanel;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                new EmptyBorder(5, 8, 5, 8)
        ));
    }

    private void styleButtonSmall(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Theme.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 15, 35));

        Color hoverColor = color.darker();
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(hoverColor); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });
    }

    public void loadData() {
        model.setRowCount(0);
        ArrayList<String> historyList = BookingHistoryDAO.getHistory();

        for (String record : historyList) {
            // Memecah teks dari format: "3 | Studio VIP | 2026-06-05 | 21:00:00 | 2 Jam | Rp 100000"
            String[] parts = record.split("\\s*\\|\\s*");
            
            if (parts.length >= 6) {
                // Memodifikasi angka nominal agar ada pemisah ribuan
                String biayaRaw = parts[5].replaceAll("[^0-9]", "");
                String biayaFormatted = parts[5];
                try {
                    long nominal = Long.parseLong(biayaRaw);
                    biayaFormatted = "Rp " + String.format("%,d", nominal);
                } catch (Exception ignored) {}

                model.addRow(new Object[]{
                        parts[0], // ID
                        parts[1], // Item/Studio
                        parts[2], // Tanggal
                        parts[3], // Jam
                        parts[4], // Durasi
                        biayaFormatted // Total Biaya
                });
            } else {
                // Jika format berbeda, masukkan utuh ke kolom kedua
                model.addRow(new Object[]{"-", record, "-", "-", "-", "-"});
            }
        }
    }

    private void searchData() {
        String keyword = txtSearch.getText().toLowerCase().trim();
        model.setRowCount(0);

        for (String record : BookingHistoryDAO.getHistory()) {
            if (record.toLowerCase().contains(keyword)) {
                String[] parts = record.split("\\s*\\|\\s*");
                if (parts.length >= 6) {
                    model.addRow(new Object[]{parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]});
                }
            }
        }
    }
}