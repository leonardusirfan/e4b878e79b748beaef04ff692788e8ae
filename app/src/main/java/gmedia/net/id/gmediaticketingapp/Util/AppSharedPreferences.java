package gmedia.net.id.gmediaticketingapp.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSharedPreferences {
    private static final String LOGIN_PREF = "login_status";
    private static final String ID_PREF = "user_id";
    private static final String TOKEN_PREF = "token";
    private static final String ROLE_PREF = "account_role";

    private static SharedPreferences getPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isLoggedIn(Context context){
        return getPreferences(context).getBoolean(LOGIN_PREF, false);
    }

    public static String getToken(Context context){
        return getPreferences(context).getString(TOKEN_PREF, "");
    }

    public static String getId(Context context){
        return getPreferences(context).getString(ID_PREF, "");
    }

    public static String getRole(Context context){
        return getPreferences(context).getString(ROLE_PREF, "");
    }

    public static void Login(Context context, String id, String token, String role){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGIN_PREF, true);
        editor.putString(ID_PREF, id);
        editor.putString(TOKEN_PREF, token);
        editor.putString(ROLE_PREF, role);
        editor.apply();
    }

    public static void Logout(Context context){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGIN_PREF, false);
        editor.putString(ID_PREF, "");
        editor.putString(TOKEN_PREF, "");
        editor.putString(ROLE_PREF, "");
        editor.apply();
    }
}