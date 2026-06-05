package musicStudio;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InstrumentManagementUI extends JFrame {

    JTable table;

    JTextField txtNama = new JTextField();
    JTextField txtHarga = new JTextField();
    JTextField txtStok = new JTextField();

    DefaultTableModel model;

    public InstrumentManagementUI() {

        setTitle("Manajemen Instrument");

        setSize(700, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buildUI();

        loadData();

        setVisible(true);
    }

    void buildUI() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Nama");
        model.addColumn("Harga");
        model.addColumn("Stok");

        table = new JTable(model);

        JScrollPane scroll =
                new JScrollPane(table);

        add(scroll, BorderLayout.CENTER);

        JPanel form =
                new JPanel(new GridLayout(4, 2));

        form.add(new JLabel("Nama"));
        form.add(txtNama);

        form.add(new JLabel("Harga"));
        form.add(txtHarga);

        form.add(new JLabel("Stok"));
        form.add(txtStok);

        JButton btnTambah =
                new JButton("Tambah");

        JButton btnUpdate =
                new JButton("Update");

        form.add(btnTambah);
        form.add(btnUpdate);

        add(form, BorderLayout.NORTH);

        JButton btnDelete =
                new JButton("Hapus");

        add(btnDelete, BorderLayout.SOUTH);

        btnTambah.addActionListener(e -> tambahData());

        btnUpdate.addActionListener(e -> updateData());

        btnDelete.addActionListener(e -> hapusData());

        table.getSelectionModel().addListSelectionListener(e -> {

            int row = table.getSelectedRow();

            if (row != -1) {

                txtNama.setText(
                        model.getValueAt(row, 1).toString());

                txtHarga.setText(
                        model.getValueAt(row, 2).toString());

                txtStok.setText(
                        model.getValueAt(row, 3).toString());
            }

        });

    }

    void loadData() {

        model.setRowCount(0);

        ArrayList<Instrument> list =
                InstrumentDAO.getAllInstrument();

        for (Instrument i : list) {

            model.addRow(new Object[]{
                i.getId(),
                i.getName(),
                i.getPrice(),
                i.getStock()
            });

        }

    }

    void tambahData() {

        InstrumentDAO.insert(
                txtNama.getText(),
                Integer.parseInt(txtHarga.getText()),
                Integer.parseInt(txtStok.getText())
        );

        loadData();

        clearForm();
    }

    void updateData() {

        int row = table.getSelectedRow();

        if (row == -1) {
            return;
        }

        int id =
                Integer.parseInt(
                        model.getValueAt(row, 0).toString());

        InstrumentDAO.update(
                id,
                txtNama.getText(),
                Integer.parseInt(txtHarga.getText()),
                Integer.parseInt(txtStok.getText())
        );

        loadData();

        clearForm();
    }

    void hapusData() {

        int row = table.getSelectedRow();

        if (row == -1) {
            return;
        }

        int id =
                Integer.parseInt(
                        model.getValueAt(row, 0).toString());

        InstrumentDAO.delete(id);

        loadData();

        clearForm();
    }

    void clearForm() {

        txtNama.setText("");
        txtHarga.setText("");
        txtStok.setText("");

    }
}