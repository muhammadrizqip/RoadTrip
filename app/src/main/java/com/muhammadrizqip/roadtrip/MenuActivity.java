package com.muhammadrizqip.roadtrip;

import android.Manifest;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogout;
    private TextView weluser;
    private FirebaseAuth firebaseAuth;
    private CardView menu1, menu3,menu5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Membutuhkan Izin Lokasi", Toast.LENGTH_SHORT).show();
            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        } else {
            // Permission has already been granted
            Toast.makeText(this, "Izin Lokasi diberikan", Toast.LENGTH_SHORT).show();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()== null){
            startActivity(new Intent(this,LoginActivity.class));
        }
        FirebaseUser user= firebaseAuth.getCurrentUser();

        weluser = (TextView) findViewById(R.id.weluser);
        weluser.setText("welcome "+user.getEmail());
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        menu1 = (CardView) findViewById(R.id.menu1);
        menu3 = (CardView) findViewById(R.id.menu3);
        menu5 = (CardView) findViewById(R.id.menu5);

        buttonLogout.setOnClickListener(this);
        menu1.setOnClickListener(this);
        menu3.setOnClickListener(this);
        menu5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v== buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (v== menu1){
            finish();
            startActivity(new Intent(this, WisataActivity.class));
        }
        if (v== menu3){
            finish();
            startActivity(new Intent(this, listWisata.class));
        }
        if (v== menu5){
            finish();
            startActivity(new Intent(this, MapsActivity2.class));
        }
    }
}
