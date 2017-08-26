package org.terna.tpc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StudentDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private Button profile,add,check,notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(StudentDashboardActivity.this,LoginActivity.class));
            finish();
        }
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        profile=(Button) findViewById(R.id.profile);
        add=(Button) findViewById(R.id.add_Details);
        check=(Button) findViewById(R.id.status_check);
        notif=(Button) findViewById(R.id.notification_button);
        profile.setOnClickListener(this);
        add.setOnClickListener(this);
        check.setOnClickListener(this);
        notif.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.profile:
                break;
            case R.id.add_Details:
                break;
            case R.id.status_check:
                break;
            case R.id.notification_button:
                break;


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();
                startActivity(new Intent(StudentDashboardActivity.this,LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_dashboard_menu, menu);
        return true;
    }
}
