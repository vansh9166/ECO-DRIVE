package com.example.eco_drive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateNewAcct extends AppCompatActivity {

    private Button CreateAcct;
    private TextInputLayout usernameTF,passwordTF,confirm_passwordTF;
    private ConstraintLayout layout;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";;
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private String TAG = "CreateNewAcct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_acct);

        usernameTF = findViewById(R.id.sign_in_usernameF);
        passwordTF = findViewById(R.id.sign_in_passwordF);
        confirm_passwordTF = findViewById(R.id.sign_in_confirm_passwordF);
        layout = findViewById(R.id.Create_New_Account);
        CreateAcct = findViewById(R.id.signupF);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);

        outside_touch_hide_keyboard();
        signUp();
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent i = new Intent(CreateNewAcct.this, Dashboard.class);
            startActivity(i);
            finish();
        }
    }

    private void SetToast(String mssg){
        Toast.makeText(CreateNewAcct.this, mssg, Toast.LENGTH_SHORT).show();
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

    private Boolean ValidateEmail(){
        if(usernameTF.getEditText().getText().toString().trim().isEmpty()){
            usernameTF.setError("Email Field Can't be empty");
            return false;
        }
        else if(!validateEmailWithMatcher(usernameTF.getEditText().getText().toString())){
            usernameTF.setError("Incorrect Email format");
            return false;
        }
        else{
            usernameTF.setError(null);
            return true;
        }
    }

    private Boolean validateEmailWithMatcher(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private Boolean ValidatePassword(){
        if(passwordTF.getEditText().getText().toString().trim().isEmpty()){
            passwordTF.setError("Password Field Can't be empty");
            return false;
        }
        else if(passwordTF.getEditText().getText().toString().length()<5){
            passwordTF.setError("Password Length is short");
            return false;
        }
        else{
            passwordTF.setError(null);
            return true;
        }
    }

    private Boolean ValidateConfirmPassword() {
        if(confirm_passwordTF.getEditText().getText().toString().trim().isEmpty()){
            confirm_passwordTF.setError("Password Field Can't be empty");
            return false;
        }
        else if(!confirm_passwordTF.getEditText().getText().toString().equals(passwordTF.getEditText().getText().toString())){
            confirm_passwordTF.setError("Password do not match");
            return false;
        }
        else{
            confirm_passwordTF.setError(null);
            return true;
        }
    }

    private void signUp(){
        CreateAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (ValidateEmail() & ValidatePassword() & ValidateConfirmPassword()) {

                        Log.i("Button Clicked", "onClick: ");

                        String email = usernameTF.getEditText().getText().toString();
                        String password = passwordTF.getEditText().getText().toString();

                        progressBar.setVisibility(View.VISIBLE);
                        hideKeyboard();
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(CreateNewAcct.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Log.d(TAG, "createUserWithEmail:success");
                                            SetToast("Account Created");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            SetToast("Authentication failed.");
                                            updateUI(null);
                                        }
                                    }
                                });

                        hideKeyboard();
                    }
                }
                catch (Exception e) {
                    Log.i("error", "onClick: "+e);
                }
            }

        });
    }

}
