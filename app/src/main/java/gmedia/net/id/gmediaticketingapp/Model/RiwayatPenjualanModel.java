package gmedia.net.id.gmediaticketingapp.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gmedia.net.id.gmediaticketingapp.Util.Converter;

public class RiwayatPenjualanModel {
    private String id;
    private String kode_order;
    private Date tanggal_transaksi;
    private double total_bayar;

    //Informasi pembeli
    private String nama, email, nomor;
    private String link;

    private List<String> listUrlBarcode = new ArrayList<>();

    public RiwayatPenjualanModel(String id, String kode_order, String nama, String email, String nomor,
                                 Date tanggal_transaksi, double total_bayar, String link, List<String> listUrlBarcode){
        this.id = id;
        this.kode_order = kode_order;
        this.nama = nama;
        this.email = email;
        this.nomor = nomor;
        this.tanggal_transaksi = tanggal_transaksi;
        this.total_bayar = total_bayar;
        this.link = link;
        this.listUrlBarcode = listUrlBarcode;
    }

    public String getId() {
        return id;
    }

    public String getTanggal_transaksi() {
        return Converter.DTToString(tanggal_transaksi);
    }

    public double getTotal_bayar() {
        return total_bayar;
    }

    public String getKode_order() {
        return kode_order;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getNomor() {
        return nomor;
    }

    public String getLink() {
        return link;
    }

    public List<String> getListUrlBarcode() {
        return listUrlBarcode;
    }
}
