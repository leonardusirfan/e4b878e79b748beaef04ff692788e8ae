package gmedia.net.id.gmediaticketingapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gmedia.net.id.gmediaticketingapp.Model.RiwayatPenjualanModel;
import gmedia.net.id.gmediaticketingapp.Util.ApiVolleyManager;
import gmedia.net.id.gmediaticketingapp.Util.AppLoading;
import gmedia.net.id.gmediaticketingapp.Util.AppRequestCallback;
import gmedia.net.id.gmediaticketingapp.Util.Converter;
import gmedia.net.id.gmediaticketingapp.Util.JSONBuilder;
import gmedia.net.id.gmediaticketingapp.Util.LoadMoreScrollListener;

public class TiketRiwayatFragment extends Fragment {

    private Activity activity;
    private SwipeRefreshLayout layout_refresh;
    private LoadMoreScrollListener loadManager;
    private TiketRiwayatAdapter adapter;
    private List<RiwayatPenjualanModel> listPenjualan = new ArrayList<>();

    public TiketRiwayatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        View v = inflater.inflate(R.layout.fragment_tiket_riwayat, container, false);

        layout_refresh = v.findViewById(R.id.layout_refresh);
        RecyclerView rv_tiket = v.findViewById(R.id.rv_tiket);
        rv_tiket.setItemAnimator(new DefaultItemAnimator());
        rv_tiket.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new TiketRiwayatAdapter(activity, listPenjualan);
        rv_tiket.setAdapter(adapter);
        loadManager = new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                loadRiwayat(false);
            }
        };
        rv_tiket.addOnScrollListener(loadManager);

        layout_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRiwayat(true);
                layout_refresh.setRefreshing(false);
            }
        });

        loadRiwayat(true);

        return v;
    }

    private void loadRiwayat(final boolean init){
        if(init){
            AppLoading.getInstance().showLoading(activity);
            loadManager.initLoad();
        }

        JSONBuilder body = new JSONBuilder();
        body.add("start", loadManager.getLoaded());
        body.add("limit", 20);
        body.add("search", "");
        Log.d(Constant.TAG, body.create().toString());

        ApiVolleyManager.getInstance().addSecureRequest(activity, Constant.URL_TIKET_HISTORY,
                ApiVolleyManager.METHOD_POST, Constant.getTokenHeader(activity), body.create(),
                new AppRequestCallback(new AppRequestCallback.RequestListener() {
                    @Override
                    public void onEmpty(String message) {
                        if(init){
                            listPenjualan.clear();
                            adapter.notifyDataSetChanged();
                        }

                        loadManager.failedLoad();
                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onSuccess(String result, String message) {
                        try{
                            if(init){
                                listPenjualan.clear();
                            }

                            JSONArray response = new JSONArray(result);
                            for(int i = 0; i < response.length(); i++){
                                JSONObject riwayat = response.getJSONObject(i);
                                listPenjualan.add(new RiwayatPenjualanModel(riwayat.getString("id"),
                                        riwayat.getString("order_id"), riwayat.getString("nama_pembeli"),
                                        riwayat.getString("email_pembeli"), riwayat.getString("no_telp_pembeli"),
                                        Converter.stringDTSToDate(riwayat.getString("tanggal_transaksi")),
                                        riwayat.getDouble("total_bayar"), riwayat.getString("link_download")));
                            }

                            loadManager.finishLoad(response.length());
                            adapter.notifyDataSetChanged();
                        }
                        catch (JSONException e){
                            loadManager.failedLoad();

                            Toast.makeText(activity, R.string.error_json, Toast.LENGTH_SHORT).show();
                            Log.e(Constant.TAG, e.getMessage());
                        }

                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                        loadManager.failedLoad();
                        AppLoading.getInstance().stopLoading();
                    }
                }));
    }
}
