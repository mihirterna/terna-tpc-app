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

public class CommitteeActivity extends AppCompatActivity implements View.OnClickListener {
    Button list, notif, check, cmprofile;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee);
        list = (Button) findViewById(R.id.listStudents);
        check = (Button) findViewById(R.id.checkStudent);
        notif = (Button) findViewById(R.id.notif);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        cmprofile = (Button) findViewById(R.id.committeeProfile);
        list.setOnClickListener(this);
        check.setOnClickListener(this);
        cmprofile.setOnClickListener(this);
        notif.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listStudents:
                startActivity(new Intent(CommitteeActivity.this, StudentsList.class));

                break;
            case R.id.checkStudent:

                break;
            case R.id.notif:
                startActivity(new Intent(CommitteeActivity.this, CommitteeSendNotices.class));

                break;
            case R.id.committeeProfile:

                break;

        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();
                startActivity(new Intent(CommitteeActivity.this,LoginActivity.class));
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
