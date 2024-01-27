package view;

import business.BookManager;
import business.BrandManager;
import business.CarManager;
import business.ModelManager;
import core.ComboItem;
import core.Helper;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.ArrayList;

public class AdminView extends Layout {
    private JPanel container;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_brand;
    private JScrollPane scl_brand;
    private JTable tbl_brand;
    private JTable tbl_car;
    private JPanel pnl_model;
    private JScrollPane scrl_model;
    private JTable tbl_model;
    private JComboBox cmb_s_model_brand;
    private JComboBox cmb_s_model_type;
    private JComboBox cmb_s_model_fuel;
    private JComboBox cmb_s_model_gear;
    private JButton btn_search_model;
    private JButton btn_cancel_model;
    private JPanel pnl_car;
    private JScrollPane scrl_car;
    private JPanel pnl_booking_search;
    private JFormattedTextField fld_finish_date;
    private JFormattedTextField fld_start_date;
    private JComboBox cmb_booking_gear;
    private JComboBox cmb_booking_fuel;
    private JComboBox cmb_booking_type;
    private JButton btn_search_book;
    private JTable tbl_book;
    private JButton btn_cancel_booking;
    private JScrollPane scrl_book;
    private JTable tbl_reserved_car;
    private JComboBox cmb_reserved_car;
    private JButton btn_cncl_reserved;
    private JButton btn_reserved_serach;
    private User user;
    private DefaultTableModel tmdl_brand = new DefaultTableModel();
    private DefaultTableModel tmdl_model = new DefaultTableModel();
    private DefaultTableModel tmdl_car = new DefaultTableModel();
    private DefaultTableModel tmdl_booking = new DefaultTableModel();

    private DefaultTableModel tmdl_reserved = new DefaultTableModel();
    private ModelManager modelManager;
    private BrandManager brandManager;
    private CarManager carManager;
    private BookManager bookManager;
    private JPopupMenu brand_menu;
    private JPopupMenu model_menu;
    private JPopupMenu car_menu;
    private JPopupMenu booking_menu;
    private JPopupMenu reserved_menu;
    private Object[] col_model;
    private Object[] col_car;
    private Object[] col_reserved;
    Object[] col_booking_list;

    // Constructor
    public AdminView(User user) {
        this.brandManager = new BrandManager();
        this.modelManager = new ModelManager();
        this.carManager = new CarManager();
        this.bookManager = new BookManager();
        this.add(container);
        this.guiInitialize(1000, 500);
        this.user = user;
        if (this.user == null) {
            dispose();
        }

        this.lbl_welcome.setText("Hoşgeldiniz : " + this.user.getUsername());

        //General Cod
        loadComponent();

        //Brand Tab Menu
        loadBrandTable();
        loadBrandComponent();

        //Model Tab Menu
        loadModelTable(null);
        loadModelComponent();
        loadModelFilter();

        //Car Tab Menu
        loadCarTable();
        loadCarComponent();

        // Booking Car Tab Menu
        loadBookingTable(null);
        loadBookingComponent();
        loadBookingFilter();

        //Reserved Tab Menu
        loadReservedTable(null);
        loadReservedComponent();
        loadReservedFilter();

    }

    //loadComponent() = Çıkış Yap button'una işlev tanımlıyoruz
    private void loadComponent(){
        btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginView loginView = new LoginView();
            }
        });
    }
    //LoadReservedFilter() = Kiralanmış araçların plakaya göre filtrelenmesi
    public void loadReservedFilter(){
        this.cmb_reserved_car.removeAllItems();
        for(Car obj : this.carManager.findAll()){
            this.cmb_reserved_car.addItem(new ComboItem(obj.getId(),obj.getPlate()));
        }
        this.cmb_reserved_car.setSelectedItem(null);
    }
    //loadReservedTable() = Kiralamalar panelinin tablolaştığı metot.
    public void loadReservedTable(ArrayList<Object[]> reserved_list){
        this.col_reserved = new Object[]{"ID", "Plaka", "Araç Marka", "Araç Modeli", "Müşteri Adı", "Telefon", "Mail","Kimlik Numarası", "Başlangıç Tarihi", "Bitiş Tarihi", "Fiyat"};
        if(reserved_list == null){
            reserved_list = this.bookManager.getForTable(col_reserved.length, this.bookManager.findAll());
        }
        createTable(this.tmdl_reserved, this.tbl_reserved_car, col_reserved, reserved_list);
    }
    //loadReservedComponent() = yeni PopupMenu oluşturtuğumuz ve iptal etme işlemini yönettiğiz metot.
    public void loadReservedComponent(){
        tableRowSelect(tbl_reserved_car);
        this.reserved_menu = new JPopupMenu();
        this.reserved_menu.add("İptal Et").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectedReservedId = this.getTableSelectedRow(tbl_reserved_car, 0);
                if (this.bookManager.delete(selectedReservedId)) {
                    Helper.showMsg("done");
                    loadReservedTable(null);
                } else {
                    Helper.showMsg("error");
                }
            }
        });

        this.tbl_reserved_car.setComponentPopupMenu(reserved_menu);

        btn_reserved_serach.addActionListener(e -> {
            ComboItem selectedCar = (ComboItem)this.cmb_reserved_car.getSelectedItem();
            int carId = 0;
            if(selectedCar != null){
                carId = selectedCar.getKey();
            }

            ArrayList<Book> reservedListBySearch = this.bookManager.searchForTable(carId);
            ArrayList<Object[]> reservedRowListBySearch = this.bookManager.getForTable(this.col_reserved.length, reservedListBySearch);
            loadReservedTable(reservedRowListBySearch);

        });

        btn_cncl_reserved.addActionListener(e -> {
            loadReservedFilter();
            loadReservedTable(null);
        });


    }
    //loadBrandComponent() = yeni PopupMenu oluşturtuğumuz ve Brand tablosunda  ekleme silme ve güncelleme işlemlerini yönettiğimiz metot.
    public void loadBrandComponent() {
        tableRowSelect(this.tbl_brand);
        this.brand_menu = new JPopupMenu();
        this.brand_menu.add("Yeni").addActionListener(e -> {
            BrandView brandView = new BrandView(null);
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();

                }
            });
        });
        this.brand_menu.add("Güncelle").addActionListener(e -> {
            int selectBrandId = this.getTableSelectedRow(tbl_brand, 0);
            BrandView brandView = new BrandView(this.brandManager.getById(selectBrandId));
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                    loadReservedTable(null);
                    loadCarTable();
                }
            });

        });
        this.brand_menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectBrandId = this.getTableSelectedRow(tbl_brand, 0);
                if (this.brandManager.delete(selectBrandId)) {
                    Helper.showMsg("done");
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                    loadCarTable();
                } else {
                    Helper.showMsg("error");
                }
            }
        });
        this.tbl_brand.setComponentPopupMenu(brand_menu);
    }
    //loadBrandTable() = Markalar panelinin tablolaştığı metot.
    public void loadBrandTable() {
        Object[] col_brand = {"Marka ID", "Marka Adı"};
        ArrayList<Object[]> brandList = this.brandManager.getForTable(col_brand.length);
        this.createTable(this.tmdl_brand, this.tbl_brand, col_brand, brandList);
    }
    //LoadModelComponent() = yeni PopupMenu oluşturtuğumuz ve Model tablosunda  ekleme, silme ve güncelleme işlemlerini yönettiğimiz metot.
    public void loadModelComponent() {
        tableRowSelect(this.tbl_model);
        this.model_menu = new JPopupMenu();
        this.model_menu.add("Yeni").addActionListener(e -> {
            ModelView modelView = new ModelView(new Model());
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable(null);
                }
            });
        });
        this.model_menu.add("Güncelle").addActionListener(e -> {
            int selectedModelId = this.getTableSelectedRow(tbl_model, 0);
            ModelView modelView = new ModelView(this.modelManager.getById(selectedModelId));
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable(null);
                    loadCarTable();
                    loadBookingTable(null);
                    loadReservedTable(null);
                }
            });
        });
        this.model_menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectedModelId = this.getTableSelectedRow(tbl_model, 0);
                if (this.modelManager.delete(selectedModelId)) {
                    Helper.showMsg("done");
                    loadModelTable(null);
                } else {
                    Helper.showMsg("error");
                }
            }
        });

        this.tbl_model.setComponentPopupMenu(model_menu);

        this.btn_search_model.addActionListener(e -> {
            ComboItem selectedBrand = (ComboItem) this.cmb_s_model_brand.getSelectedItem();
            int brandId = 0;
            if(selectedBrand != null){
                brandId = selectedBrand.getKey();
            }
            ArrayList<Model> modelListBySearch = this.modelManager.searchForTable(
                    brandId,
                    (Model.Fuel) cmb_s_model_fuel.getSelectedItem(),
                    (Model.Gear) cmb_s_model_gear.getSelectedItem(),
                    (Model.Type) cmb_s_model_type.getSelectedItem()
            );
            //System.out.println(modelListBySearch);

            ArrayList<Object[]> modelRowListBySearch = this.modelManager.getForTable(this.col_model.length, modelListBySearch);
            loadModelTable(modelRowListBySearch);
        });

        this.btn_cancel_model.addActionListener(e ->{
            this.cmb_s_model_type.setSelectedItem(null);
            this.cmb_s_model_gear.setSelectedItem(null);
            this.cmb_s_model_fuel.setSelectedItem(null);
            this.cmb_s_model_brand.setSelectedItem(null);
            loadModelTable(null);
        });
    }
    // loadModelTable() = Model panelinin tablolaştığı metot.
    public void loadModelTable(ArrayList<Object[]> modelList) {
        this.col_model = new Object[]{"Model ID", "Marka", "Model Adı", "Tip", "Yıl", "Yakıt Türü", "Vites"};
        if(modelList == null){
            modelList = this.modelManager.getForTable(col_model.length, this.modelManager.findAll());
        }
        createTable(this.tmdl_model, this.tbl_model, col_model, modelList);
    }
    //loadModelFilter() = Modellerin Type , Gear ve Fuel değişkenlerine göre filtrelendirildiği metot.
    public void loadModelFilter() {
        this.cmb_s_model_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cmb_s_model_type.setSelectedItem(null);
        this.cmb_s_model_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.cmb_s_model_gear.setSelectedItem(null);
        this.cmb_s_model_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_s_model_fuel.setSelectedItem(null);
        loadModelFilterBrand();
    }
    //loadModelFilterBrand() = Modellerin markalarına göre filtrelendiği metot
    public void loadModelFilterBrand() {
        this.cmb_s_model_brand.removeAllItems();
        for (Brand obj : brandManager.findAll()) {
            this.cmb_s_model_brand.addItem(new ComboItem(obj.getId(), obj.getName()));
        }
    }
    //loadCarComponent() =  yeni PopupMenu oluşturtuğumuz ve Araç tablosunda  ekleme, silme ve güncelleme işlemlerini yönettiğimiz metot.
    public void loadCarComponent() {
        tableRowSelect(this.tbl_car);
        this.car_menu = new JPopupMenu();
        this.car_menu.add("Yeni").addActionListener(e -> {
            CarView carView = new CarView(new Car());
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable();
                    loadBookingTable(null);
                    loadReservedTable(null);
                }
            });
        });
        this.car_menu.add("Güncelle").addActionListener(e -> {
            int selectedCarId = this.getTableSelectedRow(tbl_car, 0);
            CarView carView = new CarView(this.carManager.getById(selectedCarId));
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable();
                    loadBookingTable(null);
                    loadReservedTable(null);
                }
            });
        });
        this.car_menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectedCarId = this.getTableSelectedRow(tbl_car, 0);
                if (this.carManager.delete(selectedCarId)) {
                    Helper.showMsg("done");
                    loadCarTable();
                } else {
                    Helper.showMsg("error");
                }
            }
        });

        this.tbl_car.setComponentPopupMenu(car_menu);

    }
    //loadCarTable() = Araçlar panelinin tablolaştığı metot.
    public void loadCarTable() {
        col_car = new Object[]{"ID", "Marka", "Model", "Plaka", "Renk", "KM", "Yıl", "Tip", "Yakıt Türü", "Vites"};
        ArrayList<Object[]> carList = this.carManager.getForTable(col_car.length, this.carManager.findAll());
        this.createTable(this.tmdl_car, this.tbl_car,col_car,carList);
    }
    //loadBookingComponent() = yeni PopupMenu oluşturtuğumuz ve Kiralanabilir Araçlar tablosunda  rezervasyom yap işlemini yönettiğimiz metot.
    private void loadBookingComponent(){
        tableRowSelect(this.tbl_book);
        this.booking_menu = new JPopupMenu();
        this.booking_menu.add("Rezervasyon Yap").addActionListener(e -> {
            int selectCarId = this.getTableSelectedRow(this.tbl_book, 0);
            BookingView bookingView = new BookingView(
                    this.carManager.getById(selectCarId),
                    this.fld_start_date.getText(),
                    this.fld_finish_date.getText()
            );
            bookingView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBookingTable(null);
                    loadBookingFilter();
                    loadReservedTable(null);
                }
            });

        });
        this.tbl_book.setComponentPopupMenu(booking_menu);

        btn_search_book.addActionListener(e -> {
            ArrayList<Car> carList = this.carManager.searchForBooking(
                    fld_start_date.getText(),
                    fld_finish_date.getText(),
                    (Model.Type) cmb_booking_type.getSelectedItem(),
                    (Model.Gear) cmb_booking_gear.getSelectedItem(),
                    (Model.Fuel) cmb_booking_fuel.getSelectedItem()
            );

            ArrayList<Object[]> carBookingRow = this.carManager.getForTable(this.col_car.length,carList);
            loadBookingTable(carBookingRow);
        });
        btn_cancel_booking.addActionListener(e -> {
            loadBookingFilter();
        });
    }
    //loadBookingTable() = Kiralanabilir Araçlar panelinin tablolaştığı metot.
    private void loadBookingTable (ArrayList<Object[]> bookList){
        col_booking_list =new Object[]{"ID","Marka","Model","Plaka","Renk","KM","Yıl","Tip","Yakıt Türü","Vites"};
        createTable(this.tmdl_booking,this.tbl_book,col_booking_list,bookList);

    }
    //loadBookingFilter() = Kiralanabilir Araçların Type , Gear ve Fuel değişkenlerine göre filtrelendirildiği metot.
    public void loadBookingFilter(){
        this.cmb_booking_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cmb_booking_type.setSelectedItem(null);
        this.cmb_booking_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.cmb_booking_gear.setSelectedItem(null);
        this.cmb_booking_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_booking_fuel.setSelectedItem(null);
    }
    //createUIComponents() = JFormattedTextField olan fieldların MAskFormatter ile tarihlere format verdiğimiz metot.
    private void createUIComponents() throws ParseException {

        this.fld_start_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fld_start_date.setText("10/10/2023");

        this.fld_finish_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fld_finish_date.setText("20/10/2023");

    }
}
