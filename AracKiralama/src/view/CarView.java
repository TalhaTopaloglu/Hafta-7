package view;

import business.CarManager;
import business.ModelManager;
import core.ComboItem;
import core.Helper;
import entity.Car;
import entity.Model;

import javax.swing.*;

public class CarView extends Layout {
    private JPanel container;
    private JComboBox cmb_model;
    private JComboBox cmb_color;
    private JTextField fld_km;
    private JTextField fld_plate;
    private JButton btn_car_save;
    private Car car;
    private CarManager carManager;
    private ModelManager modelManager;

    // Car View'da oluşturduğumuz pencerenin işlemlerini yönetildiği sınıf.
    public CarView(Car car) {
        this.car = car;
        this.carManager = new CarManager();
        this.modelManager = new ModelManager();
        this.add(container);
        this.guiInitialize(300,400);


        this.cmb_color.setModel(new DefaultComboBoxModel<>(Car.Color.values()));
        for( Model model : this.modelManager.findAll()){
            this.cmb_model.addItem(model.getComboItem());
        }

        if(this.car.getId() != 0) {
            ComboItem selectedItem = car.getModel().getComboItem();
            this.cmb_model.getModel().setSelectedItem(selectedItem);
            this.cmb_color.getModel().setSelectedItem(car.getColor());
            this.fld_plate.setText(car.getPlate());
            this.fld_km.setText(Integer.toString(car.getKm()));
        }

        this.btn_car_save.addActionListener(e ->{
            if(Helper.isFieldListEmpty(new JTextField[]{this.fld_km, this.fld_plate})) {
                Helper.showMsg("fill");
            } else {
                boolean result;
                ComboItem selectedModel = (ComboItem) this.cmb_model.getSelectedItem();
                this.car.setModel_id(selectedModel.getKey());
                this.car.setColor((Car.Color) this.cmb_color.getSelectedItem());
                this.car.setPlate(this.fld_plate.getText());
                this.car.setKm(Integer.parseInt(this.fld_km.getText()));
                if(this.car.getId() != 0) {
                    result = this.carManager.update(this.car);
                    Helper.showMsg("done");
                    dispose();
                } else {
                    result = this.carManager.save(this.car);
                }
                if(result) {
                   // Helper.showMsg("done");
                    dispose();
                }else {
                    Helper.showMsg("error");
                }
            }
        });
    }
}



//    public void loadCarComponent() {
//        tableRowSelect(this.tbl_car); // bakılacak
//        this.car_menu = new JPopupMenu();
//        this.car_menu.add("Yeni").addActionListener(e -> {
//            CarView carView = new CarView(new Car());
//            carView.addWindowListener(new WindowAdapter() {
//                @Override
//                public void windowClosed(WindowEvent e) {
//                    loadModelTable(null);
//                    loadCarTable();
//                }
//            });
//        });
//        this.car_menu.add("Güncelle").addActionListener(e -> {
//            int selectedCarId = this.getTableSelectedRow(tbl_car, 0);
//            CarView carView = new CarView(this.carManager.getById(selectedCarId));
//            carView.addWindowListener(new WindowAdapter() {
//                @Override
//                public void windowClosed(WindowEvent e) {
//                    loadCarTable();
//                }
//            });
//        });
//        this.car_menu.add("Sil").addActionListener(e -> {
//            if (Helper.confirm("sure")) {
//                int selectedCarId = this.getTableSelectedRow(tbl_car, 0);
//                if (this.carManager.delete(selectedCarId)) {
//                    Helper.showMsg("done");
//                    loadModelTable(null);
//                    loadCarTable();
//                } else {
//                    Helper.showMsg("error");
//                }
//            }
//        });
//
//        this.tbl_car.setComponentPopupMenu(car_menu);
//
//    }
