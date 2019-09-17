package gmedia.net.id.gmediaticketingapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gmedia.net.id.gmediaticketingapp.Model.EventModel;
import gmedia.net.id.gmediaticketingapp.Util.Converter;
import gmedia.net.id.gmediaticketingapp.Util.ImageLoader;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Activity activity;
    private List<EventModel> listEvent;

    EventAdapter(Activity activity, List<EventModel> listEvent){
        this.activity = activity;
        this.listEvent = listEvent;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new EventViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.item_event, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {
        eventViewHolder.bind(listEvent.get(i));
    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder{

        View layout_root;
        ImageView img_event;
        TextView txt_nama, txt_tanggal, txt_lokasi;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_root = itemView.findViewById(R.id.layout_root);
            img_event = itemView.findViewById(R.id.img_event);
            txt_nama = itemView.findViewById(R.id.txt_nama);
            txt_tanggal = itemView.findViewById(R.id.txt_tanggal);
            txt_lokasi = itemView.findViewById(R.id.txt_lokasi);
        }

        void bind(final EventModel e){
            txt_nama.setText(e.getNama());
            String tanggal = "Tanggal : " + Converter.DToStringInverse(e.getTanggal());
            txt_tanggal.setText(tanggal);
            txt_lokasi.setText(e.getLokasi());

            ImageLoader.load(activity, e.getGambar(), img_event);

            layout_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, TiketActivity.class);
                    i.putExtra(Constant.EXTRA_ID_EVENT, e.getId());
                    activity.startActivity(i);
                }
            });
        }
    }
}
