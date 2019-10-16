package gmedia.net.id.gmediaticketingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import gmedia.net.id.gmediaticketingapp.Util.ApiVolleyManager;
import gmedia.net.id.gmediaticketingapp.Util.AppLoading;
import gmedia.net.id.gmediaticketingapp.Util.AppRequestCallback;
import gmedia.net.id.gmediaticketingapp.Util.AppSharedPreferences;
import gmedia.net.id.gmediaticketingapp.Util.JSONBuilder;

public class LoginActivity extends AppCompatActivity {

    private EditText txt_username, txt_password;
    private ImageView img_visible;

    private boolean password_visible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);
        img_visible = findViewById(R.id.img_visible);

        img_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_visible = !password_visible;
                if(password_visible){
                    txt_password.setTransformationMethod(null);
                    txt_password.setSelection(txt_password.getText().length());
                    img_visible.setImageResource(R.drawable.visible);
                }
                else{
                    txt_password.setTransformationMethod(new PasswordTransformationMethod());
                    txt_password.setSelection(txt_password.getText().length());
                    img_visible.setImageResource(R.drawable.invisible);
                }
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_username.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,
                            "Kolom username belum terisi", Toast.LENGTH_SHORT).show();
                }
                else if(txt_password.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,
                            "Kolom password belum terisi", Toast.LENGTH_SHORT).show();
                }
                else{
                    login(txt_username.getText().toString(),
                            txt_password.getText().toString());
                }
            }
        });
    }

    private void login(String username, String password){
        AppLoading.getInstance().showLoading(this);
        JSONBuilder body = new JSONBuilder();
        body.add("username", username);
        body.add("password", password);

        ApiVolleyManager.getInstance().addRequest(this, Constant.URL_LOGIN,
                ApiVolleyManager.METHOD_POST, Constant.getTokenHeader(this),
                body.create(), new AppRequestCallback(new AppRequestCallback.RequestListener() {
                    @Override
                    public void onEmpty(String message) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onSuccess(String result, String message) {
                        try{
                            JSONObject obj = new JSONObject(result);
                            String id = obj.getString("users_id");
                            String token = obj.getString("token");
                            String role = obj.getString("role");

                            AppSharedPreferences.Login(LoginActivity.this, id, token, role);
                            startActivity(new Intent(LoginActivity.this, EventActivity.class));
                            finish();
                        }
                        catch (JSONException e){
                            Log.e(Constant.TAG, e.getMessage());
                        }

                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        AppLoading.getInstance().stopLoading();
                    }

                    @Override
                    public void onUnauthorized(String message) {
                        Intent i = new Intent(LoginActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        AppSharedPreferences.Logout(LoginActivity.this);

                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
