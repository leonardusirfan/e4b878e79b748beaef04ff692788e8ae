package gmedia.net.id.gmediaticketingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import gmedia.net.id.gmediaticketingapp.Util.AppSharedPreferences;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(AppSharedPreferences.isLoggedIn(this)){
            startActivity(new Intent(this, EventActivity.class));
        }
        else{
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish();
    }
}