//SingUp
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText name,email,password,conPassword,ID;
    private EditText dob;
    private DatabaseReference databaseReference,databasePath;
    private Spinner year,branch;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;
    private RadioGroup gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
        name = (TextInputEditText)findViewById(R.id.input_name);
        email = (TextInputEditText)findViewById(R.id.input_email);
        password = (TextInputEditText)findViewById(R.id.input_password);
        conPassword = (TextInputEditText)findViewById(R.id.input_conpassword);
        ID = (TextInputEditText)findViewById(R.id.input_id);
        dob = (EditText)findViewById(R.id.dobText);
        year=(Spinner)findViewById(R.id.yearChoice);
        branch=(Spinner)findViewById(R.id.branchChoice);
        gender= (RadioGroup) findViewById(R.id.rg);

        final Button submitButton=(Button) findViewById(R.id.signup_button);
        databaseReference= FirebaseDatabase.getInstance().getReference("Students");
        databasePath= FirebaseDatabase.getInstance().getReference("Path");
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
            public void onClick(View view) {  //

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
                final String n = name.getText().toString();
                final String e = email.getText().toString();
                String p = password.getText().toString();
                String cp = conPassword.getText().toString();
                final String i = ID.getText().toString();
                final String d = dob.getText().toString();
                int gen = gender.getCheckedRadioButtonId();
                RadioButton sex = (RadioButton) findViewById(gen);
                final String gender = sex.getText().toString();
                final String yr = year.getSelectedItem().toString();
                final String br = branch.getSelectedItem().toString();

                if(TextUtils.isEmpty(n)||TextUtils.isEmpty(e)||TextUtils.isEmpty(p)||
                        TextUtils.isEmpty(cp)||TextUtils.isEmpty(i)||TextUtils.isEmpty(d))
                    Toast.makeText(SignUpActivity.this,"Enter proper credentials!",Toast.LENGTH_LONG).show();
                else{
                    if(!p.equals(cp))
                        Toast.makeText(SignUpActivity.this,"Mind the password!",Toast.LENGTH_LONG).show();
                    else{
                        pd.setMessage("Signing up...");
                        pd.show();
                        firebaseAuth.createUserWithEmailAndPassword(e,p)
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        pd.dismiss();
                                        if(task.isSuccessful()){
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            Toast.makeText(SignUpActivity.this,"Successful sign up! Now Login.",Toast.LENGTH_LONG).show();
                                            UserInfo ui = new UserInfo(n,e,i,d,gender,yr,br);
                                            databaseReference.child(user.getUid()).setValue(ui);
                                            databasePath.child(yr).child(br).child(user.getUid()).setValue(n);
                                            startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                                            finish();
                                        }
                                    }
                                })
                                .addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
