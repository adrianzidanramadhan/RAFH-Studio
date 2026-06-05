package musicStudio;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudioManagementUI extends JFrame {

    JTable table;
    DefaultTableModel model;

    JTextField txtNama = new JTextField();
    JTextField txtHarga = new JTextField();
    JTextField txtSearch = new JTextField();

    JComboBox<String> cbStatus =
            new JComboBox<>(
                    new String[]{
                        "Tersedia",
                        "Maintenance",
                        "Tidak Aktif"
                    });

    Color primaryColor = new Color(92, 64, 51);
    Color accentColor = new Color(224, 153, 94);
    Color bgColor = new Color(245, 245, 245);

    public StudioManagementUI() {

        setTitle("Manajemen Studio");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buildUI();

        loadData();

        setVisible(true);
    }

    void buildUI() {

        setLayout(new BorderLayout());
        getContentPane().setBackground(bgColor);

        //---------------- HEADER ----------------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(primaryColor);
        header.setPreferredSize(new Dimension(0, 70));

        JLabel title =
                new JLabel("  🎵 Manajemen Studio");

        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif",
                Font.BOLD, 22));

        header.add(title, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        //---------------- FORM ----------------
        JPanel formPanel = new JPanel(new GridBagLayout());

        formPanel.setBackground(bgColor);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nama Studio"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtNama, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Harga/Jam"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtHarga, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Status"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(cbStatus, gbc);

        //---------------- BUTTON PANEL ----------------

        JPanel buttonPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT));

        buttonPanel.setBackground(bgColor);

        JButton btnTambah =
                createButton(
                        "Tambah",
                        new Color(40, 167, 69));

        JButton btnUpdate =
                createButton(
                        "Update",
                        new Color(255, 193, 7));

        JButton btnDelete =
                createButton(
                        "Hapus",
                        new Color(220, 53, 69));

        JButton btnRefresh =
                createButton(
                        "Refresh",
                        accentColor);

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        //---------------- SEARCH ----------------

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT));

        searchPanel.setBackground(bgColor);

        txtSearch.setPreferredSize(
                new Dimension(200, 30));

        JButton btnSearch =
                createButton(
                        "Search",
                        accentColor);

        searchPanel.add(
                new JLabel("Cari Studio :"));

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        add(searchPanel, BorderLayout.SOUTH);

        //---------------- TABLE ----------------

        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Nama");
        model.addColumn("Harga/Jam");
        model.addColumn("Status");

        table = new JTable(model);

        table.setRowHeight(30);

        table.getTableHeader().setFont(
                new Font("SansSerif",
                        Font.BOLD,
                        13));

        JScrollPane scroll =
                new JScrollPane(table);

        add(scroll, BorderLayout.CENTER);

        //---------------- EVENT ----------------

        btnTambah.addActionListener(
                e -> tambahData());

        btnUpdate.addActionListener(
                e -> updateData());

        btnDelete.addActionListener(
                e -> hapusData());

        btnRefresh.addActionListener(
                e -> {
                    loadData();
                    clearForm();
                });

        btnSearch.addActionListener(
                e -> searchData());

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    int row =
                            table.getSelectedRow();

                    if (row != -1) {

                        txtNama.setText(
                                model.getValueAt(
                                        row,
                                        1).toString());

                        txtHarga.setText(
                                model.getValueAt(
                                        row,
                                        2).toString());

                        cbStatus.setSelectedItem(
                                model.getValueAt(
                                        row,
                                        3).toString());
                    }
                });
    }

    JButton createButton(
            String text,
            Color color) {

        JButton btn =
                new JButton(text);

        btn.setBackground(color);
        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR));

        return btn;
    }

    void loadData() {

        model.setRowCount(0);

        ArrayList<Studio> studios =
                StudioDAO.getAllStudio();

        for (Studio s : studios) {

            model.addRow(new Object[]{
                s.getId(),
                s.getName(),
                s.getPricePerHour(),
                s.getStatus()
            });
        }
    }

    void tambahData() {

        StudioDAO.insert(
                txtNama.getText(),
                Integer.parseInt(
                        txtHarga.getText()),
                cbStatus
                        .getSelectedItem()
                        .toString()
        );

        loadData();

        clearForm();

        JOptionPane.showMessageDialog(
                this,
                "Studio berhasil ditambahkan");
    }

    void updateData() {

        int row =
                table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih data terlebih dahulu");

            return;
        }

        int id =
                Integer.parseInt(
                        model.getValueAt(
                                row,
                                0)
                                .toString());

        StudioDAO.update(
                id,
                txtNama.getText(),
                Integer.parseInt(
                        txtHarga.getText()),
                cbStatus
                        .getSelectedItem()
                        .toString()
        );

        loadData();

        clearForm();

        JOptionPane.showMessageDialog(
                this,
                "Studio berhasil diupdate");
    }

    void hapusData() {

        int row =
                table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih data terlebih dahulu");

            return;
        }

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Yakin ingin menghapus studio ini?",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION);

        if (confirm ==
                JOptionPane.YES_OPTION) {

            int id =
                    Integer.parseInt(
                            model.getValueAt(
                                    row,
                                    0)
                                    .toString());

            StudioDAO.delete(id);

            loadData();

            clearForm();
        }
    }

    void searchData() {

        String keyword =
                txtSearch.getText()
                        .toLowerCase();

        model.setRowCount(0);

        for (Studio s :
                StudioDAO.getAllStudio()) {

            if (s.getName()
                    .toLowerCase()
                    .contains(keyword)) {

                model.addRow(
                        new Object[]{
                            s.getId(),
                            s.getName(),
                            s.getPricePerHour(),
                            s.getStatus()
                        });
            }
        }
    }

    void clearForm() {

        txtNama.setText("");
        txtHarga.setText("");

        cbStatus.setSelectedIndex(0);

        table.clearSelection();
    }
}