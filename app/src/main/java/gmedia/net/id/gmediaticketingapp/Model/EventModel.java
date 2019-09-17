package gmedia.net.id.gmediaticketingapp.Model;

import java.util.Date;

public class EventModel {
    private String id;
    private String nama;
    private String gambar;
    private Date tanggal;
    private String lokasi;

    public EventModel(String id, String nama, String gambar, Date tanggal, String lokasi){
        this.id = id;
        this.nama = nama;
        this.gambar = gambar;
        this.tanggal = tanggal;
        this.lokasi = lokasi;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public String getGambar() {
        return gambar;
    }

    public String getLokasi() {
        return lokasi;
    }
}
