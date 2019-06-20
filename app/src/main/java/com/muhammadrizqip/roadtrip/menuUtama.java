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

public class menuUtama extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogout;
    private TextView weluser;
    private FirebaseAuth firebaseAuth;
    private CardView menu2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__utama);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()== null){
            startActivity(new Intent(this,LoginActivity.class));
        }
        FirebaseUser user= firebaseAuth.getCurrentUser();

        weluser = (TextView) findViewById(R.id.weluser);
        weluser.setText("welcome "+user.getEmail());
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        menu2= (CardView) findViewById(R.id.menu2);
        buttonLogout.setOnClickListener(this);
        menu2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v== buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (v== menu2){
            finish();
            startActivity(new Intent(this, WisataActivity.class));
        }
    }
}
