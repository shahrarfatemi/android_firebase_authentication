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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton,signupButton;
    private EditText nameEditText,passwordEditText;
    private Intent loginIntent;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Signup Page");

        loginIntent = new Intent(getApplicationContext(),MainActivity.class);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginButton = (Button) findViewById(R.id.loginButtonId2);
        signupButton = (Button) findViewById(R.id.signupButtonId2);
        nameEditText = (EditText) findViewById(R.id.nameText2);
        passwordEditText = (EditText) findViewById(R.id.passwordText2);
        progressBar = (ProgressBar) findViewById(R.id.progressBarId);

        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signupButtonId2){
            //auth and register
            userRegister();
        }
        else if(v.getId() == R.id.loginButtonId2){

            startActivity(loginIntent);
        }
    }

    public void userRegister() {
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

//        FirebaseUser user = mAuth.getCurrentUser();
//        if(user != null){
//            Toast.makeText(getApplicationContext(), "there is a user "+user.getEmail() , Toast.LENGTH_SHORT).show();
//        }

        if (name.isEmpty()) {
            nameEditText.setError("Enter a Email");
            nameEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(name).matches()){
            nameEditText.setError("Enter a valid Email");
            nameEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Enter a Password");
            passwordEditText.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(name, password)
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
                            Toast.makeText(getApplicationContext(), "registered", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            FirebaseAuthException e = (FirebaseAuthException)task.getException();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            if(e instanceof FirebaseAuthUserCollisionException){
                                nameEditText.requestFocus();
                            }
                            else if(e instanceof FirebaseAuthWeakPasswordException){
                                passwordEditText.requestFocus();
                            }
                        }

                        // ...
                    }
                });

    }
}
