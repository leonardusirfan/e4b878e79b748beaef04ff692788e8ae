package gmedia.net.id.gmediaticketingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import gmedia.net.id.gmediaticketingapp.Model.TiketModel;
import gmedia.net.id.gmediaticketingapp.Util.Converter;

public class TiketBeliAdapter extends RecyclerView.Adapter<TiketBeliAdapter.TiketBeliViewHolder> {

    private Context context;
    private List<TiketModel> listTiket;

    TiketBeliAdapter(Context context, List<TiketModel> listTiket){
        this.context = context;
        this.listTiket = listTiket;
    }

    @NonNull
    @Override
    public TiketBeliViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TiketBeliViewHolder(LayoutInflater.from(context).
                inflate(R.layout.item_tiket_beli, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TiketBeliViewHolder holder, int i) {
        final TiketModel t = listTiket.get(i);

        holder.txt_jenis.setText(t.getNama());
        String harga = "Harga : " + Converter.doubleToRupiah(t.getHarga());
        holder.txt_harga.setText(harga);
        String stok = "Stok  : " + t.getStok();
        holder.txt_stok.setText(stok);
        holder.spn_jumlah.setSelection(t.getJumlah());
        holder.spn_jumlah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                t.setJumlah(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listTiket.size();
    }

    class TiketBeliViewHolder extends RecyclerView.ViewHolder{

        TextView txt_jenis, txt_harga, txt_stok;
        Spinner spn_jumlah;

        TiketBeliViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_jenis = itemView.findViewById(R.id.txt_jenis);
            txt_harga = itemView.findViewById(R.id.txt_harga);
            spn_jumlah = itemView.findViewById(R.id.spn_jumlah);
            txt_stok = itemView.findViewById(R.id.txt_stok);
        }
    }
}
