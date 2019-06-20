package com.muhammadrizqip.roadtrip;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
