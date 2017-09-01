package org.terna.tpc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CommitteeActivity extends AppCompatActivity implements View.OnClickListener {
    Button list, notif, check, cmprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee);
        list = (Button) findViewById(R.id.listStudents);
        check = (Button) findViewById(R.id.checkStudents);
        notif = (Button) findViewById(R.id.notif);
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
            case R.id.checkStudents:

                break;
            case R.id.notif:
                startActivity(new Intent(CommitteeActivity.this, CommitteeSendNotification.class));

                break;
            case R.id.committeeProfile:

                break;

        }
    }
}
