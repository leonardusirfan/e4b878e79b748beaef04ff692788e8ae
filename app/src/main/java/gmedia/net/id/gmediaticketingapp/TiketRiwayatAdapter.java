package gmedia.net.id.gmediaticketingapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.leonardus.irfan.bluetoothprinter.BluetoothPrinter;

import java.util.List;

import gmedia.net.id.gmediaticketingapp.Model.RiwayatPenjualanModel;
import gmedia.net.id.gmediaticketingapp.Util.AppSharedPreferences;
import gmedia.net.id.gmediaticketingapp.Util.Converter;
import gmedia.net.id.gmediaticketingapp.Util.FileDownloadManager;
import gmedia.net.id.gmediaticketingapp.Util.MultipleImageLoader;
import gmedia.net.id.gmediaticketingapp.Util.TicketingPrinter;

public class TiketRiwayatAdapter extends RecyclerView.Adapter <TiketRiwayatAdapter.TiketRiwayatViewHolder>{

    private Activity activity;
    private String nama_event;
    private List<RiwayatPenjualanModel> listPenjualan;

    TiketRiwayatAdapter(Activity activity, String nama_event, List<RiwayatPenjualanModel> listPenjualan){
        this.activity = activity;
        this.nama_event = nama_event;
        this.listPenjualan = listPenjualan;
    }

    @NonNull
    @Override
    public TiketRiwayatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TiketRiwayatViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.item_tiket_riwayat, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TiketRiwayatViewHolder tiketRiwayatViewHolder, int i) {
        tiketRiwayatViewHolder.bind(listPenjualan.get(i));
    }

    @Override
    public int getItemCount() {
        return listPenjualan.size();
    }

    class TiketRiwayatViewHolder extends RecyclerView.ViewHolder{

        TextView txt_kode_order, txt_nama, txt_email, txt_nomor, txt_total, txt_tanggal;
        View btn_download, btn_print;

        TiketRiwayatViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_kode_order = itemView.findViewById(R.id.txt_kode_order);
            txt_nama = itemView.findViewById(R.id.txt_nama);
            txt_email = itemView.findViewById(R.id.txt_email);
            txt_nomor = itemView.findViewById(R.id.txt_nomor);
            txt_total = itemView.findViewById(R.id.txt_jumlah);
            btn_download = itemView.findViewById(R.id.btn_download);
            btn_print = itemView.findViewById(R.id.btn_print);
            txt_tanggal = itemView.findViewById(R.id.txt_tanggal);
        }

        void bind(final RiwayatPenjualanModel r){
            txt_kode_order.setText(r.getKode_order());
            String text = " : " + r.getNama();
            txt_nama.setText(text);
            text = " : " + r.getEmail();
            txt_email.setText(text);
            text = " : " + r.getNomor();
            txt_nomor.setText(text);
            txt_total.setText(Converter.doubleToRupiah(r.getTotal_bayar()));

            txt_tanggal.setText(r.getTanggal_transaksi());

            btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(Constant.TAG, "link " + r.getLink());
                    FileDownloadManager manager = new FileDownloadManager(activity, FileDownloadManager.TYPE_PDF);
                    manager.download(r.getLink());
                }
            });

            btn_print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TicketingPrinter bluetoothPrinter = new TicketingPrinter(activity);
                    bluetoothPrinter.startService();
                    bluetoothPrinter.setListener(new BluetoothPrinter.BluetoothListener() {
                        @Override
                        public void onBluetoothConnected() {
                            new MultipleImageLoader(activity, r.getListUrlBarcode(),
                                    new MultipleImageLoader.MultipleImageLoaderListener() {
                                @Override
                                public void onAllLoaded(List<Bitmap> listLoaded) {
                                    bluetoothPrinter.printBarcode(nama_event, listLoaded, r.getNama(), Converter.stringDTToDate(r.getTanggal_transaksi()));
                                }
                            });
                        }

                        @Override
                        public void onBluetoothFailed(String message) {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            if(AppSharedPreferences.getRole(activity).equals("ticketbox")){
                btn_print.setVisibility(View.VISIBLE);
            }
            else{
                btn_print.setVisibility(View.GONE);
            }

            if(r.getLink().isEmpty()){
                btn_download.setVisibility(View.GONE);
            }
            else{
                btn_download.setVisibility(View.VISIBLE);
            }
        }
    }
}
