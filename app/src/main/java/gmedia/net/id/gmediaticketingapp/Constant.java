package gmedia.net.id.gmediaticketingapp;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import gmedia.net.id.gmediaticketingapp.Util.AppSharedPreferences;

public class Constant {

    public final static String TAG = "ticketing_log";

    public final static String EXTRA_ID_EVENT = "id_event";
    public final static String EXTRA_NAMA_EVENT = "nama_event";

    private final static String BASE_URL = "http://mgmt.tukutiket.com/api/";

    public final static String URL_LOGIN = BASE_URL + "auth/login";
    public final static String URL_EVENT_LIST = BASE_URL + "event";
    public final static String URL_EVENT_DETAIL = BASE_URL + "event/view/";
    public final static String URL_EVENT_TIKET = BASE_URL + "event/tiket/";
    public final static String URL_TIKET_JUAL = BASE_URL + "jual/tiket";
    public final static String URL_TIKET_HARGA = BASE_URL + "jual/tiket/hitung-bayar";
    public final static String URL_TIKET_HISTORY = BASE_URL + "jual/history";

    public static Map<String, String> getTokenHeader(Context context){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Auth-Key", "frontend_client");
        headers.put("Client-Service", "Gmedia_EVENT");
        headers.put("User-Id", AppSharedPreferences.getId(context));
        headers.put("Token", AppSharedPreferences.getToken(context));

        return headers;
    }
}