package gmedia.net.id.gmediaticketingapp.Model;

import android.support.annotation.NonNull;

public class TiketModel {
    private String id;
    private String nama;
    private double harga;
    private int stok;

    private int jumlah;

    public TiketModel(String id, String nama, double harga, int stok){
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;

        jumlah = 0;
    }

    public String getNama() {
        return nama;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getJumlah() {
        return jumlah;
    }

    public String getId() {
        return id;
    }

    public double getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }

    @NonNull
    @Override
    public String toString() {
        return nama + " " + jumlah;
    }
}
