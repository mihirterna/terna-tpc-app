package org.terna.tpc;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText loginID,loginPassword;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final TextView goToSignUp = (TextView)findViewById(R.id.loginNew);
        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,loginID.getText().toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this,loginPassword.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        loginID = (TextInputEditText)findViewById(R.id.loginId);
        loginPassword = (TextInputEditText)findViewById(R.id.loginPassword);
    }
}
