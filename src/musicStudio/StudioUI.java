/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package musicStudio;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class StudioUI extends JFrame {

    ArrayList<Studio> studios = new ArrayList<>();
    ArrayList<Instrument> instruments = new ArrayList<>();

    DefaultListModel<String> orderModel = new DefaultListModel<>();
    JList<String> orderList = new JList<>(orderModel);

    JLabel totalLabel = new JLabel("Rp 0");

    JTextField hourField = new JTextField(5);
    JTextField searchField = new JTextField(15);

    Booking currentBooking;
    boolean alatOnlyMode = false;

    JPanel studioPanel;
    JPanel alatPanel;

    Color primary = new Color(33, 150, 243);

    public StudioUI() {
        setSize(950, 600); // 🔥 fix window awal
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        showMenu();
    }

    // ================= MENU AWAL =================
    void showMenu() {
        getContentPane().removeAll();

        JPanel panel = new JPanel(new GridLayout(3,1,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(100,200,100,200));

        JButton btn1 = new JButton("Studio + Alat");
        JButton btn2 = new JButton("Alat Saja");

        styleButton(btn1);
        styleButton(btn2);

        btn1.addActionListener(e -> {
            alatOnlyMode = false;
            buildUI();
        });

        btn2.addActionListener(e -> {
            alatOnlyMode = true;
            currentBooking = new Booking(null, 0);
            buildUI();
        });

        panel.add(new JLabel("Pilih Mode", SwingConstants.CENTER));
        panel.add(btn1);
        panel.add(btn2);

        add(panel);
        revalidate();
        repaint();
        setVisible(true);
    }

    // ================= UI UTAMA =================
    void buildUI() {

        getContentPane().removeAll();
        setLayout(new BorderLayout());

        setTitle("🎸 Music Studio Booking");

        // ===== HEADER + SEARCH =====
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(primary);

        JButton backBtn = new JButton("← Menu");
        styleButton(backBtn);
        backBtn.addActionListener(e -> showMenu());

        JLabel title = new JLabel("Music Studio System", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);

        JLabel date = new JLabel(LocalDate.now().toString());
        date.setForeground(Color.WHITE);

        header.add(backBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        header.add(date, BorderLayout.EAST);

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        topPanel.add(header, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ===== DATA =====
        studios.clear();
        instruments.clear();

        studios.add(new Studio("Studio A", 50000));
        studios.add(new Studio("Studio B", 75000));
        studios.add(new Studio("VIP", 100000));

        instruments.add(new Instrument("Gitar", 20000));
        instruments.add(new Instrument("Drum", 30000));
        instruments.add(new Instrument("Mic", 10000));

        // ===== PANEL UTAMA =====
        JPanel center = new JPanel(new GridLayout(1,2,10,10));
        center.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        studioPanel = new JPanel();
        studioPanel.setLayout(new BoxLayout(studioPanel, BoxLayout.Y_AXIS));

        alatPanel = new JPanel();
        alatPanel.setLayout(new BoxLayout(alatPanel, BoxLayout.Y_AXIS));

        center.add(new JScrollPane(studioPanel));
        center.add(new JScrollPane(alatPanel));

        add(center, BorderLayout.CENTER);

        refreshButtons();

        // ===== RIGHT PANEL =====
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("Pesanan"));

        right.add(new JScrollPane(orderList), BorderLayout.CENTER);

        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        right.add(totalLabel, BorderLayout.SOUTH);

        add(right, BorderLayout.EAST);

        // ===== BOTTOM =====
        JPanel bottom = new JPanel();

        bottom.add(new JLabel("Durasi:"));
        bottom.add(hourField);

        JButton removeBtn = new JButton("Kurangi");
        styleButton(removeBtn);
        removeBtn.addActionListener(e -> kurangiItem());

        bottom.add(removeBtn);

        add(bottom, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    // ================= FILTER =================
    void filterData() {
        String keyword = searchField.getText().toLowerCase();

        studioPanel.removeAll();
        alatPanel.removeAll();

        if (!alatOnlyMode) {
            for (Studio s : studios) {
                if (s.name.toLowerCase().contains(keyword)) {

                    String img = s.name.equals("Studio A") ? "studioA.png" :
                                 s.name.equals("Studio B") ? "studioB.png" :
                                 "studioVIP.png";

                    studioPanel.add(createCard(s.name, s.pricePerHour, img, () -> pilihStudio(s)));
                }
            }
        }

        for (Instrument i : instruments) {
            if (i.name.toLowerCase().contains(keyword)) {

                String img = i.name.equals("Gitar") ? "guitar.png" :
                             i.name.equals("Drum") ? "drum.png" :
                             "mic.png";

                alatPanel.add(createCard(i.name, i.price, img, () -> tambahAlat(i)));
            }
        }

        studioPanel.revalidate();
        alatPanel.revalidate();
        repaint();
    }

    void refreshButtons() {
        filterData();
    }

    // ================= CARD =================
    JPanel createCard(String name, int price, String imgName, Runnable action) {

        JPanel card = new JPanel(new BorderLayout(10,10));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setMaximumSize(new Dimension(400, 100));

        ImageIcon icon;
        try {
            icon = new ImageIcon(getClass().getResource("/assets/images/" + imgName));
        } catch (Exception e) {
            icon = new ImageIcon();
        }

        Image img = icon.getImage();
        if (img != null) {
            img = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        }

        JLabel imgLabel = new JLabel(new ImageIcon(img));
        imgLabel.setPreferredSize(new Dimension(90,90));

        JLabel text = new JLabel("<html><b>" + name + "</b><br>Rp" + price + "</html>");

        JButton btn = new JButton("Pilih");
        btn.setPreferredSize(new Dimension(80,40));
        styleButton(btn);
        btn.addActionListener(e -> action.run());

        JPanel rightPanel = new JPanel();
        rightPanel.add(btn);

        card.add(imgLabel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    void styleButton(JButton btn) {
        btn.setBackground(primary);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    // ================= LOGIC =================
    void pilihStudio(Studio s) {
        try {
            int jam = Integer.parseInt(hourField.getText());
            currentBooking = new Booking(s, jam);
            refreshList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Isi durasi dulu!");
        }
    }

    void tambahAlat(Instrument i) {
        if (currentBooking == null) {
            currentBooking = new Booking(null, getJam());
        }
        currentBooking.addInstrument(i);
        refreshList();
    }

    void kurangiItem() {
        int index = orderList.getSelectedIndex();
        if (index < 0) return;

        String text = orderModel.get(index);

        for (Instrument i : instruments) {
            if (text.contains(i.name)) {
                currentBooking.removeInstrument(i);
                break;
            }
        }

        refreshList();
    }

    int getJam() {
        try {
            return Integer.parseInt(hourField.getText());
        } catch (Exception e) {
            return 0;
        }
    }

    void refreshList() {
        orderModel.clear();

        if (currentBooking.studio != null) {
            orderModel.addElement("Studio: " + currentBooking.studio.name + " (" + currentBooking.hours + " jam)");
        }

        for (Instrument i : currentBooking.instruments.keySet()) {
            int qty = currentBooking.instruments.get(i);
            orderModel.addElement(i.name + " x" + qty);
        }

        updateTotal();
    }

    void updateTotal() {
        if (currentBooking != null) {
            totalLabel.setText("Rp " + currentBooking.getTotal());
        }
    }
}