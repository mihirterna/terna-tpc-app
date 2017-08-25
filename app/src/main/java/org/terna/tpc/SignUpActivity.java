package org.terna.tpc;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText name,email,password,conPassword,ID,station;
    private EditText dob;
    private Spinner choice;
    private ProgressDialog pd;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        final Button signUp = (Button)findViewById(R.id.signup_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        pd = new ProgressDialog(this);
        choice = (Spinner)findViewById(R.id.teamChoice);
        choice.setPrompt("Choose domain");
        name = (TextInputEditText)findViewById(R.id.input_name);
        email = (TextInputEditText)findViewById(R.id.input_email);
        password = (TextInputEditText)findViewById(R.id.input_password);
        conPassword = (TextInputEditText)findViewById(R.id.input_conpassword);
        ID = (TextInputEditText)findViewById(R.id.input_id);
        station = (TextInputEditText)findViewById(R.id.input_city);
        dob = (EditText)findViewById(R.id.dobText);
        final TextView backToLogin = (TextView)findViewById(R.id.backToLogin);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy",Locale.ENGLISH);
                dob.setText(sdf.format(myCalendar.getTime()));
            }
        };
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUpActivity.this,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
    }

    private void registerUser(){
        String nam = name.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String id = ID.getText().toString().trim();
        String pswd = password.getText().toString().trim();
        String conpswd = conPassword.getText().toString().trim();
        String city = station.getText().toString().trim();
        String dateOfBirth = dob.getText().toString().trim();
        String ch = choice.getSelectedItem().toString();

        if(TextUtils.isEmpty(nam)||TextUtils.isEmpty(mail)||TextUtils.isEmpty(id)||
                TextUtils.isEmpty(pswd)||TextUtils.isEmpty(conpswd)||TextUtils.isEmpty(city)||TextUtils.isEmpty(dateOfBirth))
            Toast.makeText(this,"Please enter proper credentials!",Toast.LENGTH_LONG).show();
        else {
            pd.setMessage("Registering...");
            pd.show();
            UserInfo userInfo = new UserInfo(nam, mail, id, dateOfBirth, city, ch);
            firebaseAuth.createUserWithEmailAndPassword(mail,pswd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                pd.dismiss();
                            }
                            else{
                                Toast.makeText(SignUpActivity.this,"Some error occurred!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
