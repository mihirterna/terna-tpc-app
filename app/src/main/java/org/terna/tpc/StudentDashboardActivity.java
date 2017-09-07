package org.terna.tpc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
public class StudentDashboardActivity extends AppCompatActivity {
    private  FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(StudentDashboardActivity.this,LoginActivity.class));
            finish();
        }

        final Button addDetails = (Button)findViewById(R.id.add_Details);
        addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentDashboardActivity.this,AddStudentDetails.class));
            }
        });
        final Button profilePage = (Button)findViewById(R.id.profile);
        profilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentDashboardActivity.this,ProfileActivity.class));
            }
        });
        final Button notifications = (Button)findViewById(R.id.notification_button);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentDashboardActivity.this,Student_notices.class));
            }
        });
        final Button checkStatus = (Button)findViewById(R.id.status_check);
        checkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: implement activity
            }
        });
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
