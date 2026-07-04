package musicStudio.ui.admin;

import musicStudio.dao.InstrumentDAO;
import musicStudio.model.Instrument;
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

public class InstrumentManagementUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtNama = new JTextField();
    private JTextField txtHarga = new JTextField();
    private JTextField txtStok = new JTextField();
    private JTextField txtSearch = new JTextField();

    public InstrumentManagementUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        buildUI();
        loadData();
    }

    private void buildUI() {
        JPanel rootPanel = new JPanel(new BorderLayout(20, 20));
        rootPanel.setBackground(Theme.BACKGROUND);
        rootPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header Title Halaman
        JLabel titleLabel = new JLabel("🎸 Manajemen Data Instrument");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Theme.TEXT);
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        // Kiri: Form Panel (Card Style)
        rootPanel.add(createFormPanel(), BorderLayout.WEST);

        // Kanan: Table & Search Panel
        rootPanel.add(createTablePanel(), BorderLayout.CENTER);

        add(rootPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(310, 0));
        card.setBackground(Theme.CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel formTitle = new JLabel("Form Input Instrument");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        formTitle.setForeground(Theme.TEXT);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        styleTextField(txtNama);
        styleTextField(txtHarga);
        styleTextField(txtStok);

        card.add(formTitle);
        card.add(Box.createVerticalStrut(15));

        card.add(createFormRow("Nama Instrument:", txtNama));
        card.add(Box.createVerticalStrut(12));

        card.add(createFormRow("Harga Sewa (Rp):", txtHarga));
        card.add(Box.createVerticalStrut(12));

        card.add(createFormRow("Stok Tersedia:", txtStok));
        card.add(Box.createVerticalStrut(25));

        // Tombol Aksi
        JButton btnTambah = createModernButton("➕ Tambah Instrument", Theme.ACCENT);
        JButton btnUpdate = createModernButton("✏️ Update Data", Theme.SIDEBAR);
        JButton btnDelete = createModernButton("🗑️ Hapus Instrument", Theme.DANGER);
        JButton btnClear  = createModernButton("🧹 Reset Form", Theme.GRAY);

        btnTambah.addActionListener(e -> tambahData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> hapusData());
        btnClear.addActionListener(e -> clearForm());

        card.add(btnTambah);
        card.add(Box.createVerticalStrut(8));
        card.add(btnUpdate);
        card.add(Box.createVerticalStrut(8));
        card.add(btnDelete);
        card.add(Box.createVerticalStrut(8));
        card.add(btnClear);

        card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel createFormRow(String labelText, JComponent component) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        row.setBackground(Theme.CARD);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(Theme.TEXT);

        row.add(label, BorderLayout.NORTH);
        row.add(component, BorderLayout.CENTER);
        return row;
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                new EmptyBorder(5, 8, 5, 8)
        ));
    }

    private JPanel createTablePanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(0, 15));
        rightPanel.setBackground(Theme.BACKGROUND);

        // Search Bar di Atas Tabel
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchBar.setBackground(Theme.BACKGROUND);

        styleTextField(txtSearch);
        txtSearch.setPreferredSize(new Dimension(220, 35));

        JButton btnSearch = new JButton("🔍 Cari");
        styleButtonSmall(btnSearch, Theme.ACCENT);

        JButton btnRefresh = new JButton("🔄 Refresh Semua");
        styleButtonSmall(btnRefresh, Theme.SIDEBAR);

        btnSearch.addActionListener(e -> searchData());
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadData();
            clearForm();
        });

        searchBar.add(new JLabel("Cari Nama :"));
        searchBar.add(txtSearch);
        searchBar.add(btnSearch);
        searchBar.add(btnRefresh);

        rightPanel.add(searchBar, BorderLayout.NORTH);

        // Table Panel (Card Style)
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Theme.CARD);
        tableContainer.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1, true));

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("ID");
        model.addColumn("Nama Instrument");
        model.addColumn("Harga Sewa");
        model.addColumn("Stok");

        table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(32);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Table Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(Theme.SIDEBAR);
        header.setForeground(Theme.WHITE);
        header.setPreferredSize(new Dimension(0, 40));

        // Center Alignment untuk Kolom ID dan Stok
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setMaxWidth(90);

        // Selection Listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtNama.setText(model.getValueAt(row, 1).toString());
                txtHarga.setText(model.getValueAt(row, 2).toString().replaceAll("[^0-9]", ""));
                txtStok.setText(model.getValueAt(row, 3).toString());
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.WHITE);

        tableContainer.add(scroll, BorderLayout.CENTER);
        rightPanel.add(tableContainer, BorderLayout.CENTER);

        return rightPanel;
    }

    private JButton createModernButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setBackground(color);
        btn.setForeground(Theme.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color hoverColor = color.darker();
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(hoverColor); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });

        return btn;
    }

    private void styleButtonSmall(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Theme.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 15, 35));
    }

    private void loadData() {
        model.setRowCount(0);
        ArrayList<Instrument> list = InstrumentDAO.getAllInstrument();

        for (Instrument i : list) {
            model.addRow(new Object[]{
                    i.getId(),
                    i.getName(),
                    "Rp " + String.format("%,d", i.getPrice()),
                    i.getStock()
            });
        }
    }

    private void tambahData() {
        if (!validateInput()) return;

        try {
            InstrumentDAO.insert(
                    txtNama.getText().trim(),
                    Integer.parseInt(txtHarga.getText().trim()),
                    Integer.parseInt(txtStok.getText().trim())
            );
            loadData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Instrument berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menambah data instrument!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateInput()) return;

        try {
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            InstrumentDAO.update(
                    id,
                    txtNama.getText().trim(),
                    Integer.parseInt(txtHarga.getText().trim()),
                    Integer.parseInt(txtStok.getText().trim())
            );
            loadData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Data instrument berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate data instrument!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus instrumen ini?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(model.getValueAt(row, 0).toString());
                InstrumentDAO.delete(id);
                loadData();
                clearForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data instrument!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchData() {
        String keyword = txtSearch.getText().toLowerCase().trim();
        model.setRowCount(0);

        for (Instrument i : InstrumentDAO.getAllInstrument()) {
            if (i.getName().toLowerCase().contains(keyword)) {
                model.addRow(new Object[]{
                        i.getId(),
                        i.getName(),
                        "Rp " + String.format("%,d", i.getPrice()),
                        i.getStock()
                });
            }
        }
    }

    private boolean validateInput() {
        if (txtNama.getText().trim().isEmpty() || txtHarga.getText().trim().isEmpty() || txtStok.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom form harus diisi!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(txtHarga.getText().trim());
            Integer.parseInt(txtStok.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Kolom Harga dan Stok harus berupa angka bulat valid tanpa huruf atau simbol!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtNama.setText("");
        txtHarga.setText("");
        txtStok.setText("");
        table.clearSelection();
    }
}