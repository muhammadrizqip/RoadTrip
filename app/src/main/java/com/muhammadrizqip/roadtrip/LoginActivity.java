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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {

    private Button buttonLogin;
    private EditText editTextEmail;
    private  EditText editTextPassw;
    private  TextView textViewSignUp;

    private  FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth= FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            // profil activity here
            finish();
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        }


        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassw = (EditText) findViewById(R.id.editTextPassw);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

        progressDialog = new ProgressDialog(this);
        buttonLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
        }


        private void userLogin(){
            String em = editTextEmail.getText().toString().trim();
            String ps = editTextPassw.getText().toString().trim();

            if (em.isEmpty()){
                //email is empty
                //Toast.makeText(this, "please enter email", Toast.LENGTH_LONG).show();
                editTextEmail.setError("Email is Required");
               // editTextEmail.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()){
                editTextEmail.setError("Please Enter a Valid Email");
               // editTextEmail.requestFocus();
                return;
            }

            if (ps.isEmpty()){
                editTextPassw.setError("Password is Required");
                //editTextPassw.requestFocus();
                return;
            }
            if (ps.length()<6){
                editTextPassw.setError("Minimum Length of Password Should be 6");
               // editTextPassw.requestFocus();
                return;
            }

            progressDialog.setMessage("PLEASE WAIT...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(em,ps)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this, MenuActivity.class );
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    @Override
    public void onClick(View view) {
        if (view == buttonLogin){
            userLogin();
        }
        if (view == textViewSignUp){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }


    }
}