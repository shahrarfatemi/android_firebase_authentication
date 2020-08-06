package com.example.firelogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button loginButton,signupButton;
    EditText nameEditText,passwordEditText;

    Intent signupIntent;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signupIntent = new Intent(getApplicationContext(),SignUpActivity.class);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginButton = (Button) findViewById(R.id.loginButtonId1);
        signupButton = (Button) findViewById(R.id.signupButtonId1);
        nameEditText = (EditText) findViewById(R.id.nameText1);
        passwordEditText = (EditText) findViewById(R.id.passwordText1);
        progressBar = (ProgressBar) findViewById(R.id.progressBarId);

        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);

        setTitle("Login Page");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginButtonId1){
            //login
            userLogin();
        }
        else if(v.getId() == R.id.signupButtonId1){

            startActivity(signupIntent);
        }
    }

    public void userLogin(){
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(name.isEmpty()){
            nameEditText.setError("Enter a Name");
            nameEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(name).matches()){
            nameEditText.setError("Enter a valid Email");
            nameEditText.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordEditText.setError("Enter a Password");
            passwordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(name, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            finish();
                            Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            FirebaseAuthException e = (FirebaseAuthException)task.getException();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
