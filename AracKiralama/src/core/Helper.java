package core;

import javax.swing.*;
import java.awt.*;

public class Helper {

    // setTheme() = UIManager'a Nimbus arayüzünü entegre ediyoruz.
    public static void setTheme(){
        for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if("Nimbus".equals(info.getName())){
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            }
        }
    }

    //showMsg() = Kullanıcılara verilen mesajları daha akıllı bir şekilde kontrol etmek için kullanılan metot.
    public static void showMsg(String str){
        optionPaneTR();
        String msg;
        String title;

        switch (str){
            case "fill":
                msg = "Lütfen Tüm Alanları Doldurunuz";
                title = "Hata!";
                break;
            case "done":
                msg = "İşlem Başarılı";
                title = "Sonuç!";
                break;
            case "notFound":
                msg = "Kayıt Bulunamadı";
                title = "Bulunamadı!";
                break;
            case "error":
                msg = "Hatalı İşlem";
                title = "Hata!";
                break;
            default:
                msg = str;
                title = "Mesaj";
                break;
        }

        JOptionPane.showMessageDialog(null,msg,title,JOptionPane.INFORMATION_MESSAGE);

    }

    //confirm() = Silme işlemlerinde teyit için kullanılan metot..
    public static boolean confirm (String str){
        optionPaneTR();
        String msg;
        if(str.equals("sure")){
            msg = "Bu işlemi yapmak  istediğinize emin misiniz ?";
        } else {
            msg =str;
        }

        return JOptionPane.showConfirmDialog(null,msg,"Emin misin?",JOptionPane.YES_NO_OPTION) == 0;
    }

    //isFieldEmpty() = JTextField'lardan gelen field'ların boş olup olmadığını kontrol eden metot.
    public static boolean isFieldEmpty(JTextField field){
        return field.getText().trim().isEmpty();
    }
    //isFieldListEmpty() = JTextField'lardan gelen field'ların listesinin boş olup olmadığını kontrol eden metot.
    public static boolean isFieldListEmpty(JTextField[] fieldList){
       for(JTextField field : fieldList){
           if(isFieldEmpty(field)) return true;
       }
       return false;
    }
    //getLocationPoint() = gui'de bulunan pencerelerin ölçülerini rahat kontrol etmemizi sağlayan metot
    public static int getLocationPoint (String type, Dimension size){
        return switch (type) {
            case "x" -> (Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2;
            case "y" -> (Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2;
            default -> 0;
        };
    }
    //optionPaneTR() = button isimlerini Türkçe yaptığımız metot.
    public static void optionPaneTR(){
        UIManager.put("OptionPane.okButtonText", "Tamam");
        UIManager.put("OptionPane.yesButtonText", "Evet");
        UIManager.put("OptionPane.noButtonText", "Hayır");
    }


}
