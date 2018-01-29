package com.example.mani.notificationapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mName, mEmail, mPassword;
    private String user_id;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        mName = (EditText) findViewById(R.id.et_user_name);
        mEmail = (EditText) findViewById(R.id.et_user_email_regitser);
        mPassword = (EditText) findViewById(R.id.user_password_regitser);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_register);
        Button mRegisterButton = (Button) findViewById(R.id.btn_register);
        Button mAlreadyAccount = (Button) findViewById(R.id.btn_account_already);

        mAlreadyAccount.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_account_already:
                finish();
                break;
            case R.id.btn_register:
                mProgressBar.setVisibility(View.VISIBLE);
                final String name = mName.getText().toString();
                final String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                user_id = mAuth.getCurrentUser().getUid();


                                String token_id = FirebaseInstanceId.getInstance().getToken();

                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("name", name);
                                userMap.put("email", email);
                                userMap.put("token_id", token_id);

                                mStore.collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        sendToMain();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });


                            } else {
                                mProgressBar.setVisibility(View.INVISIBLE);

                                Toast.makeText(RegisterActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
