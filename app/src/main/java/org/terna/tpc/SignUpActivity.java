package org.terna.tpc;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText name,email,password,conPassword,ID,station;
    private EditText dob;
    private Button submitButton;
    private String n,e,p,cp,d,i,s,key,ch;
    private DatabaseReference mDb;
    private Spinner choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (TextInputEditText)findViewById(R.id.input_name);
        email = (TextInputEditText)findViewById(R.id.input_email);
        password = (TextInputEditText)findViewById(R.id.input_password);
        conPassword = (TextInputEditText)findViewById(R.id.input_conpassword);
        ID = (TextInputEditText)findViewById(R.id.input_id);
        station = (TextInputEditText)findViewById(R.id.input_city);
        dob = (EditText)findViewById(R.id.dobText);
        choice = (Spinner) findViewById(R.id.teamChoice);
        submitButton=(Button) findViewById(R.id.signup_button);
        mDb= FirebaseDatabase.getInstance().getReference("Student");
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
            public void onClick(View view) {

                new DatePickerDialog(SignUpActivity.this,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                n=name.getText().toString();
                e=email.getText().toString();
                p=password.getText().toString();
                cp=conPassword.getText().toString();
                i=ID.getText().toString();
                s=station.getText().toString();
                d=dob.getText().toString();
                ch=choice.getSelectedItem().toString();
                UserInfo ui = new UserInfo(n,e,i,p,d,s,ch);
                key=mDb.push().getKey();
                mDb.child(key).setValue(ui);
                Intent intent = new Intent(SignUpActivity.this, StudentDashboardActivity.class);
                startActivity(intent);

            }
        });



    }
}
