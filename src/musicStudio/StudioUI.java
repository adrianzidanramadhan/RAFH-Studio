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

//    DefaultListModel<String> orderModel = new DefaultListModel<>();
//    JList<String> orderList = new JList<>(orderModel);
    
    JPanel cartContainer = new JPanel();

    JLabel totalLabel = new JLabel("Rp 0");
    JTextField searchField = new JTextField(15);
    
    // Input Waktu & Penjadwalan
    JComboBox<String> dateCombo = new JComboBox<>();
    JComboBox<String> timeCombo = new JComboBox<>();
    // Spinner dengan format: nilai awal 1, minimal 1, maksimal 24, langkah 1
    JSpinner hourSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 24, 1));

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

        instruments.add(new Instrument("Gitar Elektrik Fender", 40000, 5));
        instruments.add(new Instrument("Gitar Akustik Yamaha", 20000, 8));
        instruments.add(new Instrument("Drum Set Pearl", 80000, 3));
        instruments.add(new Instrument("Mic Shure SM58", 15000, 5));
        instruments.add(new Instrument("Bass Ibanez", 35000, 5));
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
        
        // Memberikan padding/jarak aman dari tepi ujung layar
        sidebar.setBorder(new EmptyBorder(25, 15, 25, 15));

        // Judul Aplikasi
        JLabel brand = new JLabel("RAFH Studio");
        brand.setFont(new Font("SansSerif", Font.BOLD, 22));
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT); // Rata kiri
        brand.setBorder(new EmptyBorder(0, 5, 10, 0)); // Spasi bawah sebelum garis

        // Garis Pemisah (Separator)
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(190, 1));
        separator.setForeground(new Color(255, 255, 255, 80)); // Putih transparan
        separator.setBackground(sidebarColor); // Menghilangkan bayangan default
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Tombol Menu
        JButton btnStudio = createSidebarButton("🏠 Sewa Studio");
        JButton btnAlat = createSidebarButton("🎸 Sewa Alat");
        JButton btnHistory = createSidebarButton("📜 Riwayat (CSV)");

        btnStudio.addActionListener(e -> { playSound("click.wav"); switchMenu(false); });
        btnAlat.addActionListener(e -> { playSound("click.wav"); switchMenu(true); });
        btnHistory.addActionListener(e -> { playSound("click.wav"); showHistory(); });

        // Memasukkan komponen ke dalam sidebar
        sidebar.add(brand);
        sidebar.add(separator);
        sidebar.add(Box.createVerticalStrut(20)); // Spasi kosong setelah garis
        sidebar.add(btnStudio);
        sidebar.add(Box.createVerticalStrut(5));  // Spasi antar tombol
        sidebar.add(btnAlat);
        sidebar.add(Box.createVerticalStrut(5));  // Spasi antar tombol
        sidebar.add(btnHistory);
        sidebar.add(Box.createVerticalGlue()); // Mendorong sisa ruang ke bawah

        return sidebar;
    }

    JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(190, 45)); // Ukuran tombol seragam
        btn.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(sidebarColor);
        
        // Menghilangkan border default bawaan Java Swing
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        
        // Rata Kiri dengan margin teks di dalamnya
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT); 
        btn.setBorder(new EmptyBorder(10, 15, 10, 15)); 
        
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Menambahkan Animasi Hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(sidebarHover); // Berubah terang saat disorot
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(sidebarColor); // Kembali normal saat ditinggalkan
            }
        });

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
        orderTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        orderPanel.add(orderTitle, BorderLayout.NORTH);

        // GANTI JLIST DENGAN CART CONTAINER
        cartContainer.setLayout(new BoxLayout(cartContainer, BoxLayout.Y_AXIS));
        cartContainer.setBackground(Color.WHITE);
        JScrollPane orderScroll = new JScrollPane(cartContainer);
        orderScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        orderScroll.getVerticalScrollBar().setUnitIncrement(16); // Agar scroll lancar
        orderPanel.add(orderScroll, BorderLayout.CENTER);

        // -- Area Penjadwalan & Checkout --
        JPanel bottomOrder = new JPanel();
        bottomOrder.setLayout(new BoxLayout(bottomOrder, BoxLayout.Y_AXIS));
        bottomOrder.setBackground(Color.WHITE);
        bottomOrder.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Setup ComboBox Tanggal
        dateCombo.removeAllItems();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            dateCombo.addItem(today.plusDays(i).format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        }
        
        dateCombo.addActionListener(e -> updateTimeCombo());
        updateTimeCombo();

        JPanel schedulePanel = new JPanel(new GridLayout(3, 2, 5, 5));
        schedulePanel.setBackground(Color.WHITE);
        schedulePanel.add(new JLabel("Tanggal:"));
        schedulePanel.add(dateCombo);
        schedulePanel.add(new JLabel("Jam Mulai:"));
        schedulePanel.add(timeCombo);
        schedulePanel.add(new JLabel("Durasi (Jam):"));
        hourSpinner.setFont(new Font("SansSerif", Font.BOLD, 14));
        schedulePanel.add(hourSpinner);

        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        totalLabel.setForeground(primaryAccent);
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCheckout = new JButton("✅ Checkout");
        styleButton(btnCheckout, new Color(40, 167, 69));
        btnCheckout.addActionListener(e -> prosesCheckout());

        bottomOrder.add(schedulePanel);
        bottomOrder.add(Box.createVerticalStrut(15));
        bottomOrder.add(totalLabel);
        bottomOrder.add(Box.createVerticalStrut(10));
        // btnKurangi dihapus dari sini karena pindah ke setiap baris item
        bottomOrder.add(btnCheckout);

        orderPanel.add(bottomOrder, BorderLayout.SOUTH);

        return orderPanel;
    }
    
    JPanel createCartItemPanel(String text, Runnable onDelete) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        // Garis bawah tipis
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            new EmptyBorder(10, 10, 10, 10)
        ));

        // Teks Item
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(label, BorderLayout.CENTER);

        // Tombol Hapus Inline (❌)
        JButton btnDelete = new JButton("❌");
        btnDelete.setFocusPainted(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setContentAreaFilled(false);
        btnDelete.setForeground(new Color(220, 53, 69)); // Warna Merah
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.addActionListener(e -> onDelete.run());
        panel.add(btnDelete, BorderLayout.EAST);

        // Animasi Hover
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(240, 245, 250)); // Biru sangat muda
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.WHITE);
            }
        });

        return panel;
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
                    
                    // Menentukan nama gambar berdasarkan nama studio
                    String imgFile = "studio_default.png";
                    if(s.name.contains("Studio A")) imgFile = "studio_a.png";
                    else if(s.name.contains("Studio B")) imgFile = "studio_b.png";
                    else if(s.name.contains("VIP")) imgFile = "studio_vip.png";

                    cardContainer.add(createModernCard(s.name, s.pricePerHour, desc, imgFile, () -> pilihStudio(s)));
                }
            }
        } else {
            for (Instrument i : instruments) {
                if (i.name.toLowerCase().contains(keyword)) {
                    
                    // Menentukan nama gambar berdasarkan nama alat
                    String imgFile = "alat_default.png";
                    if(i.name.contains("Elektrik")) imgFile = "gitar_elektrik.png";
                    else if(i.name.contains("Akustik")) imgFile = "gitar_akustik.png";
                    else if(i.name.contains("Drum")) imgFile = "drum.png";
                    else if(i.name.contains("Mic")) imgFile = "mic.png";
                    else if(i.name.contains("Bass")) imgFile = "bass.png";

                    cardContainer.add(createModernCard(i.name, i.price, "Satuan", imgFile, () -> tambahAlat(i)));
                }
            }
        }

        cardContainer.revalidate();
        cardContainer.repaint();
    }

    // ================= CARD DESIGN (Diperbarui dengan Deskripsi) =================
    JPanel createModernCard(String name, int price, String description, String imageFileName, Runnable action) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(220, 280));

        JLabel imgLabel = new JLabel();
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imgLabel.setPreferredSize(new Dimension(190, 110));
        imgLabel.setMaximumSize(new Dimension(190, 110));

        try {
            // Mencari gambar di dalam package "assets/images/"
            java.net.URL imgURL = getClass().getResource("/assets/images/" + imageFileName);
            
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image scaledImg = icon.getImage().getScaledInstance(190, 110, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(scaledImg));
            } else {
                // Fallback jika nama file tidak cocok
                imgLabel.setText("Gambar Tdk Ditemukan");
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imgLabel.setOpaque(true);
                imgLabel.setBackground(new Color(230, 230, 230));
            }
        } catch (Exception e) {
            imgLabel.setText("Error Load Image");
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imgLabel.setOpaque(true);
            imgLabel.setBackground(new Color(230, 230, 230));
        }

        JLabel nameLabel = new JLabel("<html><div style='text-align: center; width: 170px;'>" + name + "</div></html>");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(10, 0, 5, 0));
        
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
        card.add(descLabel);
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(btnAdd);

        return card;
    }
    
    JPanel createCartItemPanel(String text) {
        // Buat panel utama untuk satu baris item
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Tinggi baris
        
        // Buat border: Garis abu-abu tipis di bawah, dan padding di dalam
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            new EmptyBorder(10, 15, 10, 15)
        ));

        // Teks item
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(label, BorderLayout.CENTER);

        // Tambahkan Animasi Hover untuk baris ini
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Berubah jadi abu-abu kebiruan sangat muda saat disorot
                panel.setBackground(new Color(240, 245, 250)); 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Kembali putih saat mouse keluar
                panel.setBackground(Color.WHITE);
            }
        });

        return panel;
    }

    void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        
        // Memastikan tombol mulus dan warnanya solid
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

        // Menambahkan Animasi Hover Secara Otomatis
        Color hoverColor = bg.darker(); // Java otomatis membuat versi gelap dari warnanya
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverColor); // Berubah warna saat mouse masuk
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg); // Kembali ke warna asal saat mouse keluar
            }
        });
    }
    
    void playClickSound() {
        Toolkit.getDefaultToolkit().beep();
    }
    
    void playCustomSound(String path) {
        try {
            javax.sound.sampled.AudioInputStream audio =
                    javax.sound.sampled.AudioSystem.getAudioInputStream(
                            getClass().getResource(path));

            javax.sound.sampled.Clip clip =
                    javax.sound.sampled.AudioSystem.getClip();

            clip.open(audio);
            clip.start();

        } catch (Exception e) {
            System.out.println("Sound error: " + e.getMessage());
        }
    }
    
    // Fungsi penengah untuk memutar suara
    void playSound(String fileName) {
        playClickSound(); 
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
        
        int currentQty = currentBooking.getQty(i);
        int usedByOthers = getExternalUsage(i);

        if (currentQty + usedByOthers >= i.stock) {
            JOptionPane.showMessageDialog(this,
                    "Stok " + i.name + " tidak cukup!",
                    "Gagal",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        currentBooking.addInstrument(i);
        playClickSound();
        refreshList();
    }
    
    int getExternalUsage(Instrument i) {
        // simulasi random orang lain pakai
        return (int)(Math.random() * 3); // 0 - 2
    }

    int getJam() {
        return (int) hourSpinner.getValue();
    }

    void refreshList() {
        cartContainer.removeAll(); // Bersihkan panel keranjang
        
        if (currentBooking != null) {
            currentBooking.hours = getJam(); 
            
            // Tampilkan Studio (Jika ada)
            if (currentBooking.studio != null) {
                cartContainer.add(createCartItemPanel(
                    "🏢 " + currentBooking.studio.name + " (" + currentBooking.hours + " Jam)", 
                    () -> {
                        // Logika saat tombol ❌ ditekan
                        currentBooking.studio = null; 
                        playSound("click.wav");
                        refreshList(); 
                    }
                ));
            }
            
            // Tampilkan Alat Musik (Jika ada)
            if (currentBooking.instruments != null) {
                for (Instrument i : currentBooking.instruments.keySet()) {
                    int qty = currentBooking.instruments.get(i);
                    cartContainer.add(createCartItemPanel(
                        "🎸 " + i.name + " x" + qty, 
                        () -> {
                            // Logika saat tombol ❌ ditekan
                            if (qty > 1) {
                                currentBooking.instruments.put(i, qty - 1); // Kurangi 1
                            } else {
                                currentBooking.instruments.remove(i); // Hapus jika sisa 1
                            }
                            playSound("click.wav");
                            refreshList(); 
                        }
                    ));
                }
            }
            
            // Jika Keranjang Kosong
            if (currentBooking.studio == null && currentBooking.instruments.isEmpty()) {
                JPanel emptyPanel = new JPanel(new BorderLayout());
                emptyPanel.setBackground(Color.WHITE);
                emptyPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
                JLabel emptyLabel = new JLabel("Keranjang masih kosong...");
                emptyLabel.setForeground(Color.GRAY);
                emptyPanel.add(emptyLabel, BorderLayout.CENTER);
                cartContainer.add(emptyPanel);
            }
        }
        
        updateTotal();
        
        // Perintah wajib untuk me-render ulang UI
        cartContainer.revalidate();
        cartContainer.repaint();
    }

    void updateTotal() {
        if (currentBooking != null && (currentBooking.studio != null || !currentBooking.instruments.isEmpty())) {
            totalLabel.setText("Total: Rp " + currentBooking.getTotal());
        } else {
            totalLabel.setText("Total: Rp 0");
        }
    }
    void updateTimeCombo() {
        timeCombo.removeAllItems();
        
        int startHour = 9; // Jam buka standar: 09:00
        int endHour = 22;  // Jam tutup: 22:00

        // Cek apakah yang dipilih di dropdown tanggal adalah "Hari Ini" (Index ke-0)
        if (dateCombo.getSelectedIndex() == 0) {
            int currentHour = java.time.LocalTime.now().getHour();
            
            // Jika jam sekarang sudah lewat dari jam buka, mulai dari jam sekarang
            if (currentHour > startHour) {
                startHour = currentHour;
            }
        }

        // Jika studio sudah tutup (misal pesan jam 11 malam untuk hari ini)
        if (startHour > endHour) {
            timeCombo.addItem("Tutup");
            return;
        }

        // Masukkan jam ke dalam ComboBox
        for (int i = startHour; i <= endHour; i++) {
            timeCombo.addItem(String.format("%02d:00", i));
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
        // PLAY SOUND KHUSUS
        playCustomSound("/assets/sound/success.wav");
        JOptionPane.showMessageDialog(this, "Pesanan Berhasil Disimpan ke Jadwal & History!\n" + totalLabel.getText());
        
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
        JTextArea txtHistory = new JTextArea(15, 100);
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


}