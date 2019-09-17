package gmedia.net.id.gmediaticketingapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gmedia.net.id.gmediaticketingapp.Model.TiketModel;
import gmedia.net.id.gmediaticketingapp.Util.ApiVolleyManager;
import gmedia.net.id.gmediaticketingapp.Util.AppLoading;
import gmedia.net.id.gmediaticketingapp.Util.AppRequestCallback;
import gmedia.net.id.gmediaticketingapp.Util.Converter;
import gmedia.net.id.gmediaticketingapp.Util.ImageLoader;
import gmedia.net.id.gmediaticketingapp.Util.JSONBuilder;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class TiketBeliFragment extends Fragment {

    private TiketActivity activity;
    private List<TiketModel> listJenisTiket = new ArrayList<>();
    private TiketBeliAdapter adapter;

    private SwipeRefreshLayout layout_refresh;
    private ImageView img_konser;
    private EditText txt_nik, txt_nama, txt_email, txt_nomor, txt_promo;

    public TiketBeliFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (TiketActivity) getActivity();
        View v = inflater.inflate(R.layout.fragment_beli_tiket, container, false);

        img_konser = v.findViewById(R.id.img_konser);
        txt_nik = v.findViewById(R.id.txt_nik);
        txt_nama = v.findViewById(R.id.txt_nama);
        txt_email = v.findViewById(R.id.txt_email);
        txt_nomor = v.findViewById(R.id.txt_nomor);
        txt_promo = v.findViewById(R.id.txt_promo);

        RecyclerView rv_tiket = v.findViewById(R.id.rv_tiket);
        rv_tiket.setItemAnimator(new DefaultItemAnimator());
        rv_tiket.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new TiketBeliAdapter(activity, listJenisTiket);
        rv_tiket.setAdapter(adapter);

        layout_refresh = v.findViewById(R.id.layout_refresh);

        v.findViewById(R.id.btn_beli).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cekIsValidJumlahTiket()){
                    Toast.makeText(activity, "Belum ada tiket yang dipilih", Toast.LENGTH_SHORT).show();
                }
                else{
                    prepayment();
                }
            }
        });

        layout_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDetailEvent();
                layout_refresh.setRefreshing(false);
            }
        });

        loadDetailEvent();

        return v;
    }

    private boolean cekIsValidJumlahTiket(){
        for(TiketModel t : listJenisTiket){
            if(t.getJumlah() > 0){
                return true;
            }
        }
        return false;
    }

    private void loadDetailEvent(){
        AppLoading.getInstance().showLoading(activity);
        ApiVolleyManager.getInstance().addRequest(activity, Constant.URL_EVENT_DETAIL + activity.id_event,
                ApiVolleyManager.METHOD_GET, Constant.getTokenHeader(activity),
                new AppRequestCallback(new AppRequestCallback.RequestListener() {
                    @Override
                    public void onEmpty(String message) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onSuccess(String result, String message) {
                        try{
                            JSONObject event = new JSONObject(result);

                            ImageLoader.load(activity, event.getString("gambar_banner"), img_konser);
                            loadJenisTiket();
                        }
                        catch (JSONException e){
                            Log.e(Constant.TAG, e.getMessage());
                            Toast.makeText(activity, R.string.error_json, Toast.LENGTH_SHORT).show();

                            AppLoading.getInstance().stopLoading();
                        }
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        AppLoading.getInstance().stopLoading();
                    }
                }));
    }

    private void loadJenisTiket(){
        ApiVolleyManager.getInstance().addRequest(activity, Constant.URL_EVENT_TIKET + activity.id_event,
                ApiVolleyManager.METHOD_GET, Constant.getTokenHeader(activity),
                new AppRequestCallback(new AppRequestCallback.RequestListener() {
                    @Override
                    public void onEmpty(String message) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onSuccess(String result, String message) {
                        try {
                            listJenisTiket.clear();
                            JSONArray response = new JSONArray(result);
                            for(int i = 0; i < response.length(); i++){
                                JSONObject tiket = response.getJSONObject(i);
                                listJenisTiket.add(new TiketModel(tiket.getString("id"),
                                        tiket.getString("nama"), tiket.getDouble("harga"),
                                        tiket.getInt("sisa_stok")));
                            }

                            adapter.notifyDataSetChanged();
                        }
                        catch (JSONException e){
                            Log.e(Constant.TAG, e.getMessage());
                            Toast.makeText(activity, R.string.error_json, Toast.LENGTH_SHORT).show();
                        }

                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        AppLoading.getInstance().stopLoading();
                    }
                }));
    }

    private void showKonfirmasiDialog(double total, double diskon, double pajak, double bayar){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

        int device_TotalWidth = size.x;

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_konfirmasi_pembelian);
        if(dialog.getWindow() != null){
            dialog.getWindow().setLayout(device_TotalWidth * 90 / 100, WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView txt_total, txt_diskon, txt_bayar, btn_ya, btn_batal;
        txt_total = dialog.findViewById(R.id.txt_total);
        txt_diskon = dialog.findViewById(R.id.txt_diskon);
        txt_bayar = dialog.findViewById(R.id.txt_bayar);

        btn_ya = dialog.findViewById(R.id.btn_ya);
        btn_batal = dialog.findViewById(R.id.btn_batal);

        String text;
        text = " : " + Converter.doubleToRupiah(total);
        txt_total.setText(text);
        text = " : " + Converter.doubleToRupiah(diskon);
        txt_diskon.setText(text);
        text = " : " + Converter.doubleToRupiah(bayar);
        txt_bayar.setText(text);

        btn_ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pembelian();
                dialog.dismiss();
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void prepayment(){
        AppLoading.getInstance().showLoading(activity);

        JSONBuilder body = new JSONBuilder();
        body.add("id_event", activity.id_event);
        List<JSONObject> listTiketBeli = new ArrayList<>();
        for(TiketModel t : listJenisTiket){
            if(t.getJumlah() > 0){
                JSONBuilder obj = new JSONBuilder();
                obj.add("id", t.getId());
                obj.add("jumlah", t.getJumlah());
                listTiketBeli.add(obj.create());
            }
        }
        body.add("tickets", new JSONArray(listTiketBeli));
        body.add("kode_voucher", txt_promo.getText().toString());

        ApiVolleyManager.getInstance().addSecureRequest(activity, Constant.URL_TIKET_HARGA,
                ApiVolleyManager.METHOD_POST, Constant.getTokenHeader(activity), body.create(),
                new AppRequestCallback(new AppRequestCallback.RequestListener() {
                    @Override
                    public void onEmpty(String message) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onSuccess(String result, String message) {
                        try{
                            JSONObject response = new JSONObject(result);
                            showKonfirmasiDialog(response.getDouble("total_harga"),
                                    response.getDouble("nominal_diskon"),
                                    response.getDouble("nominal_pajak"),
                                    response.getDouble("total_bayar"));
                        }
                        catch (JSONException e){
                            Log.e(Constant.TAG, e.getMessage());
                            Toast.makeText(activity, R.string.error_json, Toast.LENGTH_SHORT).show();
                        }

                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        AppLoading.getInstance().stopLoading();
                    }
                }));
    }

    private void pembelian(){
        AppLoading.getInstance().showLoading(activity);

        JSONBuilder body = new JSONBuilder();
        body.add("id_event", activity.id_event);
        List<JSONObject> listTiketBeli = new ArrayList<>();
        for(TiketModel t : listJenisTiket){
            if(t.getJumlah() > 0){
                JSONBuilder obj = new JSONBuilder();
                obj.add("id", t.getId());
                obj.add("jumlah", t.getJumlah());
                listTiketBeli.add(obj.create());
            }
        }
        body.add("tickets", new JSONArray(listTiketBeli));
        body.add("kode_voucher", txt_promo.getText().toString());
        body.add("nama_pembeli", txt_nama.getText().toString());
        body.add("nik_pembeli", txt_nik.getText().toString());
        body.add("email_pembeli", txt_email.getText().toString());
        body.add("no_telp_pembeli", txt_nomor.getText().toString());

        ApiVolleyManager.getInstance().addSecureRequest(activity, Constant.URL_TIKET_JUAL,
                ApiVolleyManager.METHOD_POST, Constant.getTokenHeader(activity), body.create(),
                new AppRequestCallback(new AppRequestCallback.RequestListener() {
                    @Override
                    public void onEmpty(String message) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onSuccess(String result, String message) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        AppLoading.getInstance().stopLoading();

                        activity.tab_kuis.getTabAt(1).select();
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        AppLoading.getInstance().stopLoading();
                    }
                }));
    }
}
