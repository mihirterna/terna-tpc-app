package org.terna.tpc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText loginID,loginPassword;
    private ProgressDialog pd;
    private FirebaseAuth firebaseAuth;
    final String abc="mihir@gmail.com";
    final String xyz = "123456";
    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pd = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
       if(firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, StudentDashboardActivity.class));
            finish();
        }
        final TextView goToSignUp = (TextView)findViewById(R.id.loginNew);
        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        final Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaidateUser();
                loginUser();
            }
        });
        loginID = (TextInputEditText)findViewById(R.id.loginId);
        loginPassword = (TextInputEditText)findViewById(R.id.loginPassword);
    }

    private void vaidateUser() {
        email = loginID.getText().toString().trim();
        password = loginPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this,"Enter proper credentials!",Toast.LENGTH_LONG).show();
        }
    }

    private void loginUser(){

        if (email.equalsIgnoreCase(abc)&&password.equalsIgnoreCase(xyz))
        {
            firebaseAuth.signInWithEmailAndPassword(email,password);
            startActivity(new Intent(LoginActivity.this,CommitteeActivity.class));
        }
        else{
            pd.setMessage("Logging in...");
            pd.show();

            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if(task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, StudentDashboardActivity.class));
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}
