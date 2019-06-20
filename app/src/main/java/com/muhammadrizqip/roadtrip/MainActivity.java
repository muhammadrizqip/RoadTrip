package com.muhammadrizqip.roadtrip;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignup;
    private EditText editTextEmail;
    private EditText editTextPassw;
    private TextView textViewLogin;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassw = (EditText) findViewById(R.id.editTextPassw);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);

        buttonSignup.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);

    }

    private void registerUser(){
        String emaill = editTextEmail.getText().toString().trim();
        String password = editTextPassw.getText().toString().trim();

        if (emaill.isEmpty()){
            //email is empty
            //Toast.makeText(this, "please enter email", Toast.LENGTH_LONG).show();
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emaill).matches()){
            editTextEmail.setError("Please Enter a Valid Email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editTextPassw.setError("Password is Required");
            editTextPassw.requestFocus();
            return;
        }
        if (password.length()<6){
            editTextPassw.setError("Minimum Length of Password Should be 6");
            editTextPassw.requestFocus();
            return;
        }


        progressDialog.setMessage("REGISTERING PLEASE WAIT...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(emaill,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "User Registered Succesfull",Toast.LENGTH_SHORT).show();
                }else{
                   if (task.getException() instanceof FirebaseAuthUserCollisionException){
                       Toast.makeText(getApplicationContext(), "You Are Already Registered", Toast.LENGTH_SHORT).show();
                   }else {
                       Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                   }

                }
            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignup){
            registerUser();
        }
        if  (view == textViewLogin){
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}
