package entity;

import java.time.LocalDate;

public class Book {
    private int id;
    private int car_id;
    private Car car;
    private String name;
    private String idno;
    private String mpno;
    private String mail;
    private LocalDate start_date;
    private LocalDate finish_date;
    private String bCase;
    private String note;
    private int prc;

    public Book(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getMpno() {
        return mpno;
    }

    public void setMpno(String mpno) {
        this.mpno = mpno;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(LocalDate finish_date) {
        this.finish_date = finish_date;
    }

    public String getbCase() {
        return bCase;
    }

    public void setbCase(String bCase) {
        this.bCase = bCase;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPrc() {
        return prc;
    }

    public void setPrc(int prc) {
        this.prc = prc;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", car_id=" + car_id +
                ", car=" + car +
                ", name='" + name + '\'' +
                ", idno='" + idno + '\'' +
                ", mpno='" + mpno + '\'' +
                ", mail='" + mail + '\'' +
                ", start_date=" + start_date +
                ", finish_date=" + finish_date +
                ", bCase='" + bCase + '\'' +
                ", note='" + note + '\'' +
                ", prc=" + prc +
                '}';
    }
}
