package com.example.magazininstrumente.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magazininstrumente.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnLogin;
    private TextView tvRegister;
    private TextView tvRegister2;
    private EditText etEmail;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }catch (Exception e){}
        setContentView(R.layout.activity_main);

        tvRegister = findViewById(R.id.tvRegister);
        tvRegister2 = findViewById(R.id.tvRegister2);
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etParolaLogin);
        btnLogin = findViewById(R.id.btnAutentificare);

        tvRegister.setOnClickListener(clickRegister());
        tvRegister2.setOnClickListener(clickRegister());

        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if(email.isEmpty()){
            etEmail.setError("Email can not be empty!");
        }
        if(password.isEmpty()){
            etPassword.setError("Password can not be empty");
        }else {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();

                        //deschid pagina principala
                        Intent intent  = new Intent(MainActivity.this, HomeActivity.class);
                        MainActivity.this.startActivity(intent);

//                        Intent intent  = new Intent(MainActivity.this, ProductActivity.class);
//                        MainActivity.this.startActivity(intent);

                    }else{
                        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                        etPassword.setText(null);
                    }
                }
            });
        }
    }

    private View.OnClickListener clickRegister(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(intent);

            }
        };
    }
}