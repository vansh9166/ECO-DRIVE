package com.example.eco_drive;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private TextView forget_password,create_acct;
    private Button login;
    private TextInputLayout usernameTF,passwordTF;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forget_password = findViewById(R.id.forget_password);
        create_acct = findViewById(R.id.create_acct);
        login = findViewById(R.id.login);
        usernameTF = findViewById(R.id.usernameF);
        passwordTF = findViewById(R.id.passwordF);
        progressBar = findViewById(R.id.progressBar);
        layout = findViewById(R.id.Constraint);
        mAuth = FirebaseAuth.getInstance();

        log_in();
        forgetPassword();
        createNewAccount();
        outside_touch_hide_keyboard();
        clear_error_message();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            FirebaseAnalytics mFirebase;
            mFirebase = FirebaseAnalytics.getInstance(MainActivity.this);
            mFirebase.setUserId("7727830042");
            Log.i(TAG, "updateUI: " + currentUser.getUid() + ": " + currentUser.getEmail());
            Intent i = new Intent(MainActivity.this, Dashboard.class);
            startActivity(i);
            finish();
        }
    }

    private void SetToast(String mssg){
        Toast.makeText(MainActivity.this, mssg, Toast.LENGTH_SHORT).show();
    }

    private void SetLog(String mssg){
        Log.i(TAG, "toast: "+mssg);
    }

    private  void clear_error_message(){
        passwordTF.setError(null);
        usernameTF.setError(null);
    }

    private void outside_touch_hide_keyboard(){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void forgetPassword(){
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Forgot password", "onClick: label 1");

                Intent i = new Intent(MainActivity.this, ForgetPassword.class);
                startActivity(i);
            }
        });
    }

    private void createNewAccount(){
        create_acct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Create new account", "onClick: label 2");

                Intent i = new Intent(MainActivity.this, CreateNewAcct.class);
                startActivity(i);
            }
        });
    }

    private Boolean ValidateEmail(){
        if(usernameTF.getEditText().getText().toString().trim().isEmpty()){
            usernameTF.setError("Email Field Can't be empty");
            return false;
        }
        else{
            usernameTF.setError(null);
            return true;
        }
    }

    private Boolean ValidatePassword(){
        if(passwordTF.getEditText().getText().toString().trim().isEmpty()){
            passwordTF.setError("Password Field Can't be empty");
            return false;
        }
        else{
            passwordTF.setError(null);
            return true;
        }
    }

    private void log_in(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(ValidateEmail() & ValidatePassword()) {
                        String email = usernameTF.getEditText().getText().toString().trim();
                        String password = passwordTF.getEditText().getText().toString().trim();

                        progressBar.setVisibility(View.VISIBLE);
                        hideKeyboard();

                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "signInWithEmail:success");
                                            SetToast("Successful Login");
                                            Intent i = new Intent(MainActivity.this, Dashboard.class);
                                            startActivity(i);
                                            finish();

                                        } else {
                                            hideKeyboard();
                                            progressBar.setVisibility(View.INVISIBLE);
                                            SetToast("Authentication failed");
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            updateUI(null);
                                        }
                                    }
                                });
                    }
                }
                catch (Exception e){
                    Log.i("Exception handler", "Exception: " + e);
                }
            }
        });
    }
}
