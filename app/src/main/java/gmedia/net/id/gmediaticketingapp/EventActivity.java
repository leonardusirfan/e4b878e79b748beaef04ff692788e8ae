package gmedia.net.id.gmediaticketingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gmedia.net.id.gmediaticketingapp.Model.EventModel;
import gmedia.net.id.gmediaticketingapp.Util.ApiVolleyManager;
import gmedia.net.id.gmediaticketingapp.Util.AppLoading;
import gmedia.net.id.gmediaticketingapp.Util.AppRequestCallback;
import gmedia.net.id.gmediaticketingapp.Util.AppSharedPreferences;
import gmedia.net.id.gmediaticketingapp.Util.Converter;
import gmedia.net.id.gmediaticketingapp.Util.JSONBuilder;
import gmedia.net.id.gmediaticketingapp.Util.LoadMoreScrollListener;

public class EventActivity extends AppCompatActivity {

    private String search = "";
    private LoadMoreScrollListener loadManager;
    private EventAdapter adapter;
    private List<EventModel> listEvent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Event");
        }

        RecyclerView rv_event = findViewById(R.id.rv_event);
        rv_event.setItemAnimator(new DefaultItemAnimator());
        rv_event.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter(this, listEvent);
        rv_event.setAdapter(adapter);
        loadManager = new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                loadEvent(false);
            }
        };
        rv_event.addOnScrollListener(loadManager);

        loadEvent(true);
        AppLoading.getInstance().showLoading(this);
    }

    public void loadEvent(final boolean init){
        if(init){
            loadManager.initLoad();
        }

        JSONBuilder body = new JSONBuilder();
        body.add("start", loadManager.getLoaded());
        body.add("limit", 20);
        body.add("search", search);

        ApiVolleyManager.getInstance().addSecureRequest(this, Constant.URL_EVENT_LIST,
                ApiVolleyManager.METHOD_POST, Constant.getTokenHeader(this), body.create(),
                new AppRequestCallback(new AppRequestCallback.RequestListener() {
                    @Override
                    public void onEmpty(String message) {
                        if(init){
                            listEvent.clear();
                            adapter.notifyDataSetChanged();
                        }

                        loadManager.finishLoad(0);
                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onSuccess(String result, String message) {
                        try{
                            if(init){
                                listEvent.clear();
                            }

                            JSONArray response = new JSONArray(result);
                            for(int i = 0; i < response.length(); i++){
                                JSONObject event = response.getJSONObject(i);
                                listEvent.add(new EventModel(event.getString("id"),
                                        event.getString("nama"), event.getString("gambar_banner"),
                                        Converter.stringDToDate(event.getString("tanggal")),
                                        event.getString("lokasi")));
                            }

                            adapter.notifyDataSetChanged();
                            loadManager.finishLoad(response.length());
                        }
                        catch (JSONException e){
                            loadManager.failedLoad();

                            Log.e(Constant.TAG, e.getMessage());
                            Toast.makeText(EventActivity.this,
                                    "Terjadi kesalahan data", Toast.LENGTH_SHORT).show();
                        }

                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onFail(String message) {
                        loadManager.failedLoad();
                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onUnauthorized(String message) {
                        Intent i = new Intent(EventActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        AppSharedPreferences.Logout(EventActivity.this);

                        Toast.makeText(EventActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_event, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search = s;

                AppLoading.getInstance().showLoading(EventActivity.this);
                loadEvent(true);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!searchView.isIconified() && TextUtils.isEmpty(s)) {
                    search = "";
                    loadEvent(true);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setMessage("Apakah anda yakin ingin keluar?");
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent i = new Intent(EventActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    AppSharedPreferences.Logout(EventActivity.this);
                }
            });
            builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create().show();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }
}
