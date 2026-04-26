/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package musicStudio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class StudioUI extends JFrame {

    ArrayList<Studio> studios = new ArrayList<>();
    ArrayList<Instrument> instruments = new ArrayList<>();
    
    // Fitur tambahan untuk deskripsi studio
    Map<String, String> studioFeatures = new HashMap<>();
    
    // Sistem Booking (Anti-Bentrok)
    // Format: "NamaStudio_Tanggal_Jam"
    ArrayList<String> bookedSchedules = new ArrayList<>();

    DefaultListModel<String> orderModel = new DefaultListModel<>();
    JList<String> orderList = new JList<>(orderModel);

    JLabel totalLabel = new JLabel("Rp 0");
    JTextField searchField = new JTextField(15);
    
    // Input Waktu & Penjadwalan
    JComboBox<String> dateCombo = new JComboBox<>();
    JComboBox<String> timeCombo = new JComboBox<>();
    JTextField hourField = new JTextField("1", 3); // Durasi

    Booking currentBooking;
    boolean alatOnlyMode = false;

    JPanel cardContainer;
    JLabel titleLabel;

    Color sidebarColor = new Color(92, 64, 51);
    Color sidebarHover = new Color(120, 85, 68);
    Color bgColor = new Color(245, 245, 245);
    Color primaryAccent = new Color(224, 153, 94);
    Color cardColor = Color.WHITE;

    public StudioUI() {
        setTitle("RAFH Studio & Instrument Rental");
        //setSize(1150, 750);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initDummyData();
        loadBookedSchedulesFromCSV(); // Memuat data yang sudah disewa dari CSV
        currentBooking = new Booking(null, 0);
        
        buildDashboardUI();
    }

    // ================= DATA INISIAL =================
    void initDummyData() {
        studios.add(new Studio("Studio A (Standard)", 50000));
        studios.add(new Studio("Studio B (Recording)", 75000));
        studios.add(new Studio("VIP Studio (Full Set)", 150000));

        // Menambahkan deskripsi fitur
        studioFeatures.put("Studio A (Standard)", "Fitur: 1 Gitar, 1 Drum, 1 Mic");
        studioFeatures.put("Studio B (Recording)", "Fitur: 2 Gitar, 1 Drum, Set Recording");
        studioFeatures.put("VIP Studio (Full Set)", "Fitur: Alat Lengkap + AC + Sound Engineer");

        instruments.add(new Instrument("Gitar Elektrik Fender", 40000));
        instruments.add(new Instrument("Gitar Akustik Yamaha", 20000));
        instruments.add(new Instrument("Drum Set Pearl", 80000));
        instruments.add(new Instrument("Mic Shure SM58", 15000));
        instruments.add(new Instrument("Bass Ibanez", 35000));
    }

    // ================= UI UTAMA (DASHBOARD) =================
    void buildDashboardUI() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createMainContent(), BorderLayout.CENTER);
        add(createOrderPanel(), BorderLayout.EAST);

        switchMenu(false);

        revalidate();
        repaint();
        setVisible(true);
    }

    // ================= SIDEBAR =================
    JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(sidebarColor);
        sidebar.setPreferredSize(new Dimension(220, 0));

        JLabel brand = new JLabel("RAFH Studio");
        brand.setFont(new Font("SansSerif", Font.BOLD, 22));
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);
        brand.setBorder(new EmptyBorder(30, 0, 30, 0));

        JButton btnStudio = createSidebarButton("🏠 Sewa Studio");
        JButton btnAlat = createSidebarButton("🎸 Sewa Alat Saja");
        JButton btnHistory = createSidebarButton("📜 Riwayat (CSV)");

        btnStudio.addActionListener(e -> { playSound("click.wav"); switchMenu(false); });
        btnAlat.addActionListener(e -> { playSound("click.wav"); switchMenu(true); });
        btnHistory.addActionListener(e -> { playSound("click.wav"); showHistory(); });

        sidebar.add(brand);
        sidebar.add(btnStudio);
        sidebar.add(btnAlat);
        sidebar.add(btnHistory);
        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(sidebarColor);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ================= MAIN CONTENT (Tengah) =================
    JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bgColor);
        header.setBorder(new EmptyBorder(20, 20, 20, 20));

        titleLabel = new JLabel("Menu Studio", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        JLabel dateLabel = new JLabel("📅 " + dateStr);
        dateLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));

        header.add(titleLabel, BorderLayout.WEST);
        header.add(dateLabel, BorderLayout.EAST);

        JPanel centerArea = new JPanel(new BorderLayout());
        centerArea.setBackground(bgColor);
        centerArea.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(bgColor);
        searchPanel.add(new JLabel("🔍 Cari: "));
        searchPanel.add(searchField);
        JButton btnSearch = new JButton("Search");
        styleButton(btnSearch, primaryAccent);
        btnSearch.addActionListener(e -> filterData());
        searchPanel.add(btnSearch);

        centerArea.add(searchPanel, BorderLayout.NORTH);

        cardContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        cardContainer.setBackground(bgColor);

        JScrollPane scrollPane = new JScrollPane(cardContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(bgColor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        centerArea.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(centerArea, BorderLayout.CENTER);

        return mainPanel;
    }

    // ================= RIGHT PANEL (Pesanan & Jadwal) =================
    JPanel createOrderPanel() {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(320, 0));
        orderPanel.setBackground(Color.WHITE);
        orderPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel orderTitle = new JLabel("Keranjang Pesanan");
        orderTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        orderPanel.add(orderTitle, BorderLayout.NORTH);

        orderList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane orderScroll = new JScrollPane(orderList);
        orderScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        orderPanel.add(orderScroll, BorderLayout.CENTER);

        // -- Area Penjadwalan & Checkout --
        JPanel bottomOrder = new JPanel();
        bottomOrder.setLayout(new BoxLayout(bottomOrder, BoxLayout.Y_AXIS));
        bottomOrder.setBackground(Color.WHITE);
        bottomOrder.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Setup ComboBox Tanggal (7 Hari ke depan)
        dateCombo.removeAllItems();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            dateCombo.addItem(today.plusDays(i).format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        }

        // Setup ComboBox Jam
        timeCombo.removeAllItems();
        for (int i = 9; i <= 22; i++) { // Buka jam 9 pagi s/d 10 malam
            timeCombo.addItem(String.format("%02d:00", i));
        }

        JPanel schedulePanel = new JPanel(new GridLayout(3, 2, 5, 5));
        schedulePanel.setBackground(Color.WHITE);
        schedulePanel.add(new JLabel("Tanggal:"));
        schedulePanel.add(dateCombo);
        schedulePanel.add(new JLabel("Jam Mulai:"));
        schedulePanel.add(timeCombo);
        schedulePanel.add(new JLabel("Durasi (Jam):"));
        schedulePanel.add(hourField);

        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        totalLabel.setForeground(primaryAccent);
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnKurangi = new JButton("🗑 Hapus Item");
        styleButton(btnKurangi, new Color(220, 53, 69));
        btnKurangi.addActionListener(e -> kurangiItem());

        JButton btnCheckout = new JButton("✅ Checkout");
        styleButton(btnCheckout, new Color(40, 167, 69));
        btnCheckout.addActionListener(e -> prosesCheckout());

        bottomOrder.add(schedulePanel);
        bottomOrder.add(Box.createVerticalStrut(10));
        bottomOrder.add(totalLabel);
        bottomOrder.add(Box.createVerticalStrut(10));
        bottomOrder.add(btnKurangi);
        bottomOrder.add(Box.createVerticalStrut(5));
        bottomOrder.add(btnCheckout);

        orderPanel.add(bottomOrder, BorderLayout.SOUTH);

        return orderPanel;
    }

    // ================= LOGIC TAMPILAN =================
    void switchMenu(boolean isAlatOnly) {
        this.alatOnlyMode = isAlatOnly;
        titleLabel.setText(isAlatOnly ? "Menu Tambahan Alat Musik" : "Menu Studio");
        searchField.setText("");
        filterData();
    }

    void filterData() {
        String keyword = searchField.getText().toLowerCase();
        cardContainer.removeAll();

        if (!alatOnlyMode) {
            for (Studio s : studios) {
                if (s.name.toLowerCase().contains(keyword)) {
                    String desc = studioFeatures.getOrDefault(s.name, "Fitur standar");
                    cardContainer.add(createModernCard(s.name, s.pricePerHour, desc, () -> pilihStudio(s)));
                }
            }
        } else {
            for (Instrument i : instruments) {
                if (i.name.toLowerCase().contains(keyword)) {
                    cardContainer.add(createModernCard(i.name, i.price, "Satuan", () -> tambahAlat(i)));
                }
            }
        }

        cardContainer.revalidate();
        cardContainer.repaint();
    }

    // ================= CARD DESIGN (Diperbarui dengan Deskripsi) =================
    JPanel createModernCard(String name, int price, String description, Runnable action) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(220, 280)); // Tinggi ditambah agar muat deskripsi

        JLabel imgLabel = new JLabel("Gambar", SwingConstants.CENTER);
        imgLabel.setOpaque(true);
        imgLabel.setBackground(new Color(230, 230, 230));
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imgLabel.setPreferredSize(new Dimension(190, 110));
        imgLabel.setMaximumSize(new Dimension(190, 110));

        JLabel nameLabel = new JLabel("<html><div style='text-align: center; width: 170px;'>" + name + "</div></html>");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(10, 0, 5, 0));
        
        //Label Deskripsi Fitur
        JLabel descLabel = new JLabel("<html><div style='text-align: center; width: 170px; font-size: 9px; color: gray;'>" + description + "</div></html>");
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setBorder(new EmptyBorder(0, 0, 5, 0));

        JLabel priceLabel = new JLabel("Rp " + price);
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        priceLabel.setForeground(primaryAccent);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnAdd = new JButton("+ Tambah");
        btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleButton(btnAdd, primaryAccent);
        btnAdd.addActionListener(e -> action.run());

        card.add(imgLabel);
        card.add(nameLabel);
        card.add(descLabel); // Masuk ke layout
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(btnAdd);

        return card;
    }

    void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
    }

    // ================= BUSINESS LOGIC & BOOKING =================
    void pilihStudio(Studio s) {
        // Jika sudah ada booking ganti studionya tapi pertahankan alat tambahannya
        int jam = getJam();
        if(currentBooking == null) {
            currentBooking = new Booking(s, jam);
        } else {
            currentBooking.studio = s;
            currentBooking.hours = jam;
        }
        playSound("select.wav");
        refreshList();
    }

    void tambahAlat(Instrument i) {
        if (currentBooking == null) {
            currentBooking = new Booking(null, getJam());
        }
        currentBooking.addInstrument(i);
        playSound("select.wav");
        refreshList();
    }

    void kurangiItem() {
        int index = orderList.getSelectedIndex();
        if (index < 0) return;

        String text = orderModel.get(index);
        
        if(text.contains("🏢")) {
            currentBooking.studio = null; // Hapus studio
        } else {
            for (Instrument i : instruments) {
                if (text.contains(i.name)) {
                    currentBooking.removeInstrument(i);
                    break;
                }
            }
        }
        refreshList();
    }

    int getJam() {
        try { return Integer.parseInt(hourField.getText()); } 
        catch (Exception e) { return 1; }
    }

    void refreshList() {
        orderModel.clear();
        if (currentBooking != null) {
            currentBooking.hours = getJam(); // Selalu sinkronkan dengan field jam saat refresh
            
            if (currentBooking.studio != null) {
                orderModel.addElement("🏢 " + currentBooking.studio.name + " (" + currentBooking.hours + " Jam)");
            }
            if (currentBooking.instruments != null) {
                for (Instrument i : currentBooking.instruments.keySet()) {
                    int qty = currentBooking.instruments.get(i);
                    orderModel.addElement("🎸 " + i.name + " x" + qty);
                }
            }
        }
        updateTotal();
    }

    void updateTotal() {
        if (currentBooking != null && (currentBooking.studio != null || !currentBooking.instruments.isEmpty())) {
            totalLabel.setText("Total: Rp " + currentBooking.getTotal());
        } else {
            totalLabel.setText("Total: Rp 0");
        }
    }

    // ================= CHECKOUT & JADWAL (CSV) =================
    void prosesCheckout() {
        if (currentBooking == null || (currentBooking.studio == null && currentBooking.instruments.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Keranjang kosong!");
            return;
        }

        String tanggal = (String) dateCombo.getSelectedItem();
        String jamMulai = (String) timeCombo.getSelectedItem();
        int durasi = getJam();
        
        // Validasi Bentrok Jadwal (Hanya jika menyewa studio)
        if (currentBooking.studio != null) {
            int jamStartInt = Integer.parseInt(jamMulai.split(":")[0]);
            
            for (int i = 0; i < durasi; i++) {
                String cekJadwal = currentBooking.studio.name + "_" + tanggal + "_" + String.format("%02d:00", (jamStartInt + i));
                if (bookedSchedules.contains(cekJadwal)) {
                    JOptionPane.showMessageDialog(this, 
                        "Mohon Maaf, " + currentBooking.studio.name + " sudah disewa orang lain pada\nTanggal: " + tanggal + " Jam: " + String.format("%02d:00", (jamStartInt + i)), 
                        "Jadwal Bentrok!", JOptionPane.ERROR_MESSAGE);
                    return; // Batalkan checkout
                }
            }
            
            // Jika aman, masukkan ke jadwal terpakai
            for (int i = 0; i < durasi; i++) {
                String lockJadwal = currentBooking.studio.name + "_" + tanggal + "_" + String.format("%02d:00", (jamStartInt + i));
                bookedSchedules.add(lockJadwal);
            }
        }

        // Simpan ke CSV
        simpanKeCSV(tanggal, jamMulai, durasi);

        JOptionPane.showMessageDialog(this, "Pesanan Berhasil Disimpan ke Jadwal & History!\n" + totalLabel.getText());
        playSound("success.wav");
        
        // Reset pesanan
        currentBooking = new Booking(null, 0);
        refreshList();
    }

    void simpanKeCSV(String tanggal, String jamMulai, int durasi) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("history_penyewaan.csv", true))) {
            // Format: Timestamp, Studio, Tanggal Sewa, Jam, Durasi, Total Harga
            String namaStudio = (currentBooking.studio != null) ? currentBooking.studio.name : "Hanya Sewa Alat";
            String timestamp = LocalDate.now().toString();
            pw.println(timestamp + "," + namaStudio + "," + tanggal + "," + jamMulai + "," + durasi + "," + currentBooking.getTotal());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan riwayat: " + e.getMessage());
        }
    }

    void loadBookedSchedulesFromCSV() {
        // Fungsi untuk membaca CSV saat aplikasi dibuka agar jadwal yang sudah disewa kemarin tetap terkunci
        File file = new File("history_penyewaan.csv");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5 && !data[1].equals("Hanya Sewa Alat")) {
                    String namaStudio = data[1];
                    String tglSewa = data[2];
                    int jamMulai = Integer.parseInt(data[3].split(":")[0]);
                    int durasi = Integer.parseInt(data[4]);
                    
                    for (int i = 0; i < durasi; i++) {
                        bookedSchedules.add(namaStudio + "_" + tglSewa + "_" + String.format("%02d:00", (jamMulai + i)));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Gagal memuat history: " + e.getMessage());
        }
    }

    void showHistory() {
        JTextArea txtHistory = new JTextArea(15, 50);
        txtHistory.setEditable(false);
        txtHistory.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        File file = new File("history_penyewaan.csv");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                txtHistory.append(String.format("%-15s %-25s %-15s %-10s %-10s %-15s\n", "TGL PESAN", "STUDIO/ALAT", "TGL SEWA", "JAM", "DURASI", "TOTAL (Rp)"));
                txtHistory.append("----------------------------------------------------------------------------------------------\n");
                String line;
                while ((line = br.readLine()) != null) {
                    String[] d = line.split(",");
                    if (d.length >= 6) {
                        txtHistory.append(String.format("%-15s %-25s %-15s %-10s %-10s %-15s\n", d[0], d[1], d[2], d[3], d[4]+" Jam", d[5]));
                    }
                }
            } catch (IOException e) {
                txtHistory.setText("Error membaca data history.");
            }
        } else {
            txtHistory.setText("Belum ada riwayat transaksi.");
        }

        JOptionPane.showMessageDialog(this, new JScrollPane(txtHistory), "Riwayat Penyewaan", JOptionPane.INFORMATION_MESSAGE);
    }

    // ================= SYARAT POIN 5: AUDIO =================
    void playSound(String soundFileName) {
        try {
            File soundFile = new File("assets/" + soundFileName);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        } catch (Exception ex) {}
    }
}