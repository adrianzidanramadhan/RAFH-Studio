package musicStudio.ui.customer;

import musicStudio.dao.BookingDAO;
import musicStudio.dao.BookingDetailDAO;
import musicStudio.dao.BookingHistoryDAO;
import musicStudio.dao.InstrumentDAO;
import musicStudio.dao.StudioDAO;

import musicStudio.model.Booking;
import musicStudio.model.Instrument;
import musicStudio.model.Studio;

import musicStudio.util.FontUtil;
import musicStudio.util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudioUI extends JFrame {

    //========================
    // DATA
    //========================

    private final ArrayList<Studio> studios = new ArrayList<>();
    private final ArrayList<Instrument> instruments = new ArrayList<>();

    private final Map<String, String> studioFeatures = new HashMap<>();

    private final ArrayList<String> bookedSchedules =
            new ArrayList<>();

    //========================
    // COMPONENT
    //========================

    private JPanel cardContainer;
    private JPanel cartContainer;

    private JLabel titleLabel;
    private JLabel totalLabel;

    private JTextField searchField;

    private JComboBox<String> dateCombo;
    private JComboBox<String> timeCombo;

    private JSpinner hourSpinner;

    //========================
    // BOOKING
    //========================

    private Booking currentBooking;

    private boolean alatOnlyMode = false;

    //========================
    // THEME
    //========================

    private final Color sidebarColor = Theme.SIDEBAR;
    private final Color sidebarHover = Theme.SIDEBAR_HOVER;
    private final Color bgColor = Theme.BACKGROUND;
    private final Color accent = Theme.ACCENT;
    private final Color cardColor = Theme.CARD;

    //========================
    // CONSTRUCTOR
    //========================

    public StudioUI() {

        setTitle("RAFH Studio");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        initComponent();

        loadStudio();

        loadInstrument();

        currentBooking =
                new Booking(
                        null,
                        0
                );

        buildDashboard();

    }

    //========================
    // INIT COMPONENT
    //========================

    private void initComponent() {

        searchField =
                new JTextField(15);

        totalLabel =
                new JLabel("Rp 0");

        cardContainer =
                new JPanel();

        cartContainer =
                new JPanel();

        dateCombo =
                new JComboBox<>();

        timeCombo =
                new JComboBox<>();

        hourSpinner =
                new JSpinner(
                        new SpinnerNumberModel(
                                1,
                                1,
                                24,
                                1
                        )
                );

    }

    //========================
    // LOAD DATA
    //========================

    private void loadStudio() {

        studios.clear();

        studios.addAll(
                StudioDAO.getAllStudio()
        );

    }

    private void loadInstrument() {

        instruments.clear();

        instruments.addAll(
                InstrumentDAO.getAllInstrument()
        );

    }

    //========================
    // BUILD UI
    //========================

    private void buildDashboard() {

        getContentPane().removeAll();

        setLayout(
                new BorderLayout()
        );

        add(
                createSidebar(),
                BorderLayout.WEST
        );

        add(
                createMainContent(),
                BorderLayout.CENTER
        );

        add(
                createOrderPanel(),
                BorderLayout.EAST
        );

        switchMenu(false);

        revalidate();

        repaint();

        setVisible(true);

    }

    //========================
    // SIDEBAR
    //========================

    private JPanel createSidebar() {

        JPanel sidebar =
                new JPanel();

        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );

        sidebar.setPreferredSize(
                new Dimension(
                        220,
                        0
                )
        );

        sidebar.setBackground(
                sidebarColor
        );

        sidebar.setBorder(
                new EmptyBorder(
                        25,
                        15,
                        25,
                        15
                )
        );

        JLabel brand =
                new JLabel("RAFH Studio");

        brand.setFont(
                FontUtil.title()
        );

        brand.setForeground(
                Color.WHITE
        );

        brand.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        brand.setBorder(
                new EmptyBorder(
                        0,
                        5,
                        15,
                        0
                )
        );

        JSeparator separator =
                new JSeparator();

        separator.setMaximumSize(
                new Dimension(
                        190,
                        1
                )
        );

        separator.setForeground(
                new Color(
                        255,
                        255,
                        255,
                        80
                )
        );

        separator.setBackground(
                sidebarColor
        );

        JButton btnStudio =
                createSidebarButton(
                        "🏠 Sewa Studio"
                );

        JButton btnInstrument =
                createSidebarButton(
                        "🎸 Sewa Alat"
                );

        JButton btnHistory =
                createSidebarButton(
                        "📜 Riwayat"
                );

        btnStudio.addActionListener(e -> {

            switchMenu(false);

        });

        btnInstrument.addActionListener(e -> {

            switchMenu(true);

        });

        btnHistory.addActionListener(e -> {

            showHistory();

        });

        sidebar.add(brand);

        sidebar.add(separator);

        sidebar.add(Box.createVerticalStrut(20));

        sidebar.add(btnStudio);

        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(btnInstrument);

        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(btnHistory);

        sidebar.add(Box.createVerticalGlue());

        return sidebar;

    }

    //========================
    // SIDEBAR BUTTON
    //========================

    private JButton createSidebarButton(String text) {

        JButton button =
                new JButton(text);

        button.setMaximumSize(
                new Dimension(
                        190,
                        45
                )
        );

        button.setFont(
                FontUtil.button()
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                sidebarColor
        );

        button.setBorderPainted(false);

        button.setFocusPainted(false);

        button.setOpaque(true);

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                new EmptyBorder(
                        10,
                        15,
                        10,
                        15
                )
        );

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {

                        button.setBackground(
                                sidebarHover
                        );

                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {

                        button.setBackground(
                                sidebarColor
                        );

                    }

                });

        return button;

    }
    //========================
    // MAIN CONTENT
    //========================

    private JPanel createMainContent() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBackground(bgColor);

        //---------------- HEADER ----------------

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(bgColor);

        header.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        titleLabel =
                new JLabel(
                        "Menu Studio"
                );

        titleLabel.setFont(
                FontUtil.heading()
        );

        JLabel dateLabel =
                new JLabel(
                        "📅 " + LocalDate.now()
                );

        dateLabel.setFont(
                FontUtil.small()
        );

        header.add(
                titleLabel,
                BorderLayout.WEST
        );

        header.add(
                dateLabel,
                BorderLayout.EAST
        );

        //---------------- SEARCH ----------------

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setBackground(bgColor);

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        searchPanel.setBackground(bgColor);

        JLabel lblSearch =
                new JLabel("🔍 Cari");

        lblSearch.setFont(
                FontUtil.normal()
        );

        JButton btnSearch =
                new JButton("Search");

        styleButton(
                btnSearch,
                Theme.ACCENT
        );

        btnSearch.addActionListener(e ->
                filterData()
        );

        searchPanel.add(lblSearch);

        searchPanel.add(searchField);

        searchPanel.add(btnSearch);

        top.add(
                searchPanel,
                BorderLayout.WEST
        );

        //---------------- CARD CONTAINER ----------------

        cardContainer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                20,
                                20
                        )
                );

        cardContainer.setBackground(bgColor);

        JScrollPane scroll =
                new JScrollPane(
                        cardContainer
                );

        scroll.setBorder(null);

        scroll.getViewport().setBackground(bgColor);

        scroll.getVerticalScrollBar()
                .setUnitIncrement(16);

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        mainPanel.add(
                top,
                BorderLayout.CENTER
        );

        mainPanel.add(
                scroll,
                BorderLayout.CENTER
        );

        return mainPanel;

    }

    //
    //========================
    // SWITCH MENU
    //========================
    //

    private void switchMenu(boolean alat) {

        alatOnlyMode = alat;

        if(alatOnlyMode){

            titleLabel.setText(
                    "Sewa Alat Musik"
            );

        }else{

            titleLabel.setText(
                    "Sewa Studio"
            );

        }

        searchField.setText("");

        filterData();

    }

    //
    //========================
    // SEARCH
    //========================
    //

    private void filterData() {

        String keyword =
                searchField
                        .getText()
                        .toLowerCase();

        cardContainer.removeAll();

        if(!alatOnlyMode){

            for(Studio studio : studios){

                if(studio.getName()
                        .toLowerCase()
                        .contains(keyword)){

                    String image =
                            "studio_default.png";

                    if(studio.getName().contains("A"))
                        image="studio_a.png";

                    if(studio.getName().contains("B"))
                        image="studio_b.png";

                    if(studio.getName().contains("VIP"))
                        image="studio_vip.png";

                    cardContainer.add(

                            createModernCard(

                                    studio.getName(),

                                    studio.getPricePerHour(),

                                    studioFeatures.getOrDefault(
                                            studio.getName(),
                                            "Studio nyaman dengan fasilitas lengkap."
                                    ),

                                    image,

                                    ()->pilihStudio(studio)

                            )

                    );

                }

            }

        }else{

            for(Instrument instrument : instruments){

                if(instrument.getName()
                        .toLowerCase()
                        .contains(keyword)){

                    String image =
                            "alat_default.png";

                    if(instrument.getName().contains("Elektrik"))
                        image="gitar_elektrik.png";

                    else if(instrument.getName().contains("Akustik"))
                        image="gitar_akustik.png";

                    else if(instrument.getName().contains("Bass"))
                        image="bass.png";

                    else if(instrument.getName().contains("Drum"))
                        image="drum.png";

                    else if(instrument.getName().contains("Mic"))
                        image="mic.png";

                    cardContainer.add(

                            createModernCard(

                                    instrument.getName(),

                                    instrument.getPrice(),

                                    "Sewa per item",

                                    image,

                                    ()->tambahAlat(instrument)

                            )

                    );

                }

            }

        }

        cardContainer.revalidate();

        cardContainer.repaint();

    }

    //
    //========================
    // MODERN CARD
    //========================
    //

    private JPanel createModernCard(

            String name,

            int price,

            String desc,

            String imageName,

            Runnable action

    ){

        JPanel card =
                new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setPreferredSize(
                new Dimension(
                        220,
                        285
                )
        );

        card.setBackground(
                Theme.CARD
        );

        card.setBorder(

                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                Theme.BORDER
                        ),

                        new EmptyBorder(
                                12,
                                12,
                                12,
                                12
                        )

                )

        );

        JLabel image =
                new JLabel();

        image.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        image.setPreferredSize(
                new Dimension(
                        185,
                        110
                )
        );

        try{

            ImageIcon icon =
                    new ImageIcon(

                            getClass().getResource(
                                    "/assets/images/"+imageName
                            )

                    );

            Image img =
                    icon.getImage()
                            .getScaledInstance(
                                    185,
                                    110,
                                    Image.SCALE_SMOOTH
                            );

            image.setIcon(
                    new ImageIcon(img)
            );

        }catch(Exception ex){

            image.setText("No Image");

            image.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

        }

        JLabel lblName =
                new JLabel(
                        "<html><center>"+name+"</center></html>"
                );

        lblName.setFont(
                FontUtil.button()
        );

        lblName.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel lblDesc =
                new JLabel(

                        "<html><center>"+desc+"</center></html>"

                );

        lblDesc.setFont(
                FontUtil.small()
        );

        lblDesc.setForeground(
                Theme.GRAY
        );

        lblDesc.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel lblPrice =
                new JLabel(
                        "Rp "+price
                );

        lblPrice.setFont(
                FontUtil.normal()
        );

        lblPrice.setForeground(
                Theme.ACCENT
        );

        lblPrice.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JButton btn =
                new JButton(
                        "+ Tambah"
                );

        styleButton(
                btn,
                Theme.ACCENT
        );

        btn.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        btn.addActionListener(e->action.run());

        card.add(image);
        card.add(Box.createVerticalStrut(8));
        card.add(lblName);
        card.add(Box.createVerticalStrut(5));
        card.add(lblDesc);
        card.add(Box.createVerticalGlue());
        card.add(lblPrice);
        card.add(Box.createVerticalStrut(12));
        card.add(btn);

        return card;

    }
    //========================
    // ORDER PANEL
    //========================

    private JPanel createOrderPanel() {

        JPanel orderPanel = new JPanel(new BorderLayout());

        orderPanel.setPreferredSize(new Dimension(320, 0));

        orderPanel.setBackground(Theme.CARD);

        orderPanel.setBorder(
                new EmptyBorder(20,15,20,15)
        );

        JLabel title =
                new JLabel("Keranjang");

        title.setFont(FontUtil.subHeading());

        orderPanel.add(title, BorderLayout.NORTH);

        //-------------------------------------------------
        // CART
        //-------------------------------------------------

        cartContainer.setLayout(
                new BoxLayout(
                        cartContainer,
                        BoxLayout.Y_AXIS
                )
        );

        cartContainer.setBackground(
                Theme.CARD
        );

        JScrollPane scroll =
                new JScrollPane(cartContainer);

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        Theme.BORDER
                )
        );

        scroll.getVerticalScrollBar()
                .setUnitIncrement(16);

        orderPanel.add(
                scroll,
                BorderLayout.CENTER
        );

        //-------------------------------------------------
        // BOTTOM
        //-------------------------------------------------

        JPanel bottom =
                new JPanel();

        bottom.setLayout(
                new BoxLayout(
                        bottom,
                        BoxLayout.Y_AXIS
                )
        );

        bottom.setBackground(
                Theme.CARD
        );

        bottom.setBorder(
                new EmptyBorder(15,0,0,0)
        );

        dateCombo.removeAllItems();

        LocalDate today =
                LocalDate.now();

        for(int i=0;i<7;i++){

            dateCombo.addItem(
                    today.plusDays(i).toString()
            );

        }

        dateCombo.addActionListener(e->updateTimeCombo());

        updateTimeCombo();

        JPanel schedule =
                new JPanel(
                        new GridLayout(3,2,5,5)
                );

        schedule.setBackground(
                Theme.CARD
        );

        schedule.add(new JLabel("Tanggal"));

        schedule.add(dateCombo);

        schedule.add(new JLabel("Jam"));

        schedule.add(timeCombo);

        schedule.add(new JLabel("Durasi"));

        hourSpinner.setFont(FontUtil.normal());

        schedule.add(hourSpinner);

        totalLabel.setFont(FontUtil.subHeading());

        totalLabel.setForeground(
                Theme.ACCENT
        );

        JButton checkout =
                new JButton("Checkout");

        styleButton(
                checkout,
                Theme.SUCCESS
        );

        checkout.addActionListener(
                e->prosesCheckout()
        );

        bottom.add(schedule);

        bottom.add(Box.createVerticalStrut(15));

        bottom.add(totalLabel);

        bottom.add(Box.createVerticalStrut(15));

        bottom.add(checkout);

        orderPanel.add(
                bottom,
                BorderLayout.SOUTH
        );

        return orderPanel;

    }
    private JPanel createCartItemPanel(

        String text,

        Runnable deleteAction

    ){

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );

        panel.setBackground(
                Theme.CARD
        );

        panel.setBorder(

                BorderFactory.createCompoundBorder(

                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                Theme.BORDER
                        ),

                        new EmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )

                )

        );

        JLabel label =
                new JLabel(text);

        label.setFont(
                FontUtil.normal()
        );

        JButton delete =
                new JButton("❌");

        delete.setBorderPainted(false);

        delete.setContentAreaFilled(false);

        delete.setFocusPainted(false);

        delete.setForeground(
                Theme.DANGER
        );

        delete.addActionListener(e->deleteAction.run());

        panel.add(label,BorderLayout.CENTER);

        panel.add(delete,BorderLayout.EAST);

        panel.addMouseListener(
                new java.awt.event.MouseAdapter(){

                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e){

                        panel.setBackground(
                                Theme.HOVER
                        );

                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e){

                        panel.setBackground(
                                Theme.CARD
                        );

                    }

                });

        return panel;

    }
    private void styleButton(

        JButton button,

        Color color

    ){

        button.setBackground(color);

        button.setForeground(Color.WHITE);

        button.setFont(
                FontUtil.button()
        );

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setOpaque(true);

        button.setBorder(
                new EmptyBorder(
                        10,
                        20,
                        10,
                        20
                )
        );

        Color hover =
                color.darker();

        button.addMouseListener(
                new java.awt.event.MouseAdapter(){

                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e){

                        button.setBackground(hover);

                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e){

                        button.setBackground(color);

                    }

                });

    }
    private void playClickSound(){

        Toolkit.getDefaultToolkit().beep();

    }

    private void playCustomSound(String path){

        try{

            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(
                            getClass().getResource(path)
                    );

            Clip clip =
                    AudioSystem.getClip();

            clip.open(audio);

            clip.start();

        }catch(Exception ignored){}

    }

    private void playSound(String file){

        playClickSound();

    }
    //========================
    // PILIH STUDIO
    //========================

    private void pilihStudio(Studio studio) {

        int jam = getJam();

        if (currentBooking == null) {

            currentBooking = new Booking(studio, jam);

        } else {

            currentBooking.setStudio(studio);
            currentBooking.setHours(jam);

        }

        playSound("select.wav");

        refreshList();

    }

    //
    //========================
    // TAMBAH ALAT
    //========================
    //

    private void tambahAlat(Instrument instrument) {

        if (currentBooking == null) {

            currentBooking = new Booking(null, getJam());

        }

        int qtySekarang =
                currentBooking.getQty(instrument);

        int dipakaiOrang =
                getExternalUsage(instrument);

        if (qtySekarang + dipakaiOrang >= instrument.getStock()) {

            JOptionPane.showMessageDialog(

                    this,

                    "Stock " + instrument.getName() + " tidak mencukupi.",

                    "Stock Habis",

                    JOptionPane.ERROR_MESSAGE

            );

            return;

        }

        currentBooking.addInstrument(instrument);

        playClickSound();

        refreshList();

    }

    //
    //========================
    // SIMULASI STOCK
    //========================
    //

    private int getExternalUsage(Instrument instrument) {

        return (int) (Math.random() * 3);

    }

    //
    //========================
    // GET JAM
    //========================
    //

    private int getJam() {

        return (Integer) hourSpinner.getValue();

    }

    //
    //========================
    // REFRESH CART
    //========================
    //

    private void refreshList() {

        cartContainer.removeAll();

        if (currentBooking != null) {

            currentBooking.setHours(getJam());

            //-------------------
            // STUDIO
            //-------------------

            if (currentBooking.getStudio() != null) {

                cartContainer.add(

                        createCartItemPanel(

                                "🏢 "
                                        + currentBooking.getStudio().getName()
                                        + " ("
                                        + currentBooking.getHours()
                                        + " Jam)",

                                () -> {

                                    currentBooking.setStudio(null);

                                    refreshList();

                                }

                        )

                );

            }

            //-------------------
            // ALAT
            //-------------------

            for (Instrument instrument :

                    currentBooking.getInstruments().keySet()) {

                int qty =
                        currentBooking.getInstruments().get(instrument);

                cartContainer.add(

                        createCartItemPanel(

                                "🎸 "
                                        + instrument.getName()
                                        + " x"
                                        + qty,

                                () -> {

                                    if (qty > 1) {

                                        currentBooking
                                                .getInstruments()
                                                .put(
                                                        instrument,
                                                        qty - 1
                                                );

                                    } else {

                                        currentBooking
                                                .getInstruments()
                                                .remove(instrument);

                                    }

                                    refreshList();

                                }

                        )

                );

            }

            //-------------------
            // EMPTY
            //-------------------

            if (currentBooking.getStudio() == null
                    && currentBooking.getInstruments().isEmpty()) {

                JPanel empty =
                        new JPanel(
                                new BorderLayout()
                        );

                empty.setBackground(
                        Theme.CARD
                );

                empty.setBorder(
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                );

                JLabel lbl =
                        new JLabel("Keranjang masih kosong");

                lbl.setForeground(
                        Theme.GRAY
                );

                lbl.setFont(
                        FontUtil.normal()
                );

                empty.add(lbl);

                cartContainer.add(empty);

            }

        }

        updateTotal();

        cartContainer.revalidate();

        cartContainer.repaint();

    }

    //
    //========================
    // UPDATE TOTAL
    //========================
    //

    private void updateTotal() {

        if (currentBooking == null) {

            totalLabel.setText("Rp 0");

            return;

        }

        totalLabel.setText(

                "Total : Rp "

                        + currentBooking.getTotal()

        );

    }

    //
    //========================
    // UPDATE TIME
    //========================
    //

    private void updateTimeCombo() {

        timeCombo.removeAllItems();

        int buka = 9;

        int tutup = 22;

        if (dateCombo.getSelectedIndex() == 0) {

            int sekarang =
                    java.time.LocalTime.now().getHour();

            if (sekarang > buka) {

                buka = sekarang;

            }

        }

        if (buka > tutup) {

            timeCombo.addItem("Tutup");

            return;

        }

        for (int i = buka; i <= tutup; i++) {

            timeCombo.addItem(

                    String.format("%02d:00", i)

            );

        }

    }
    //========================
    // CHECKOUT
    //========================

    private void prosesCheckout() {

        if (currentBooking == null ||
                (currentBooking.getStudio() == null
                && currentBooking.getInstruments().isEmpty())) {

            JOptionPane.showMessageDialog(
                    this,
                    "Keranjang masih kosong!"
            );

            return;
        }

        try {

            int customerId = 1;

            LocalDate localDate =
                    LocalDate.parse(
                            (String) dateCombo.getSelectedItem()
                    );

            java.sql.Date tanggal =
                    java.sql.Date.valueOf(localDate);

            java.sql.Time jam =
                    java.sql.Time.valueOf(
                            (String) timeCombo.getSelectedItem() + ":00"
                    );

            int bookingId =
                    BookingDAO.insertBooking(

                            customerId,

                            currentBooking.getStudio().getId(),

                            tanggal,

                            jam,

                            getJam(),

                            currentBooking.getTotal()

                    );

            for (Instrument instrument :

                    currentBooking
                            .getInstruments()
                            .keySet()) {

                BookingDetailDAO.insertDetail(

                        bookingId,

                        instrument.getId(),

                        currentBooking
                                .getInstruments()
                                .get(instrument)

                );

            }

            playCustomSound(
                    "/assets/sound/success.wav"
            );

            JOptionPane.showMessageDialog(

                    this,

                    "Booking berhasil disimpan."

            );

            currentBooking =
                    new Booking(null,0);

            refreshList();

        }

        catch(Exception ex){

            JOptionPane.showMessageDialog(

                    this,

                    ex.getMessage(),

                    "Database Error",

                    JOptionPane.ERROR_MESSAGE

            );

        }

    }
    //========================
    // HISTORY
    //========================

    private void showHistory(){

        JTextArea area =
                new JTextArea();

        area.setEditable(false);

        area.setFont(
                FontUtil.normal()
        );

        for(String row :

                BookingHistoryDAO.getHistory()){

            area.append(row);

            area.append("\n");

        }

        JScrollPane scroll =
                new JScrollPane(area);

        scroll.setPreferredSize(

                new Dimension(

                        850,

                        500

                )

        );

        JOptionPane.showMessageDialog(

                this,

                scroll,

                "Riwayat Booking",

                JOptionPane.INFORMATION_MESSAGE

        );

    }
}