package org.terna.tpc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CommitteeActivity extends AppCompatActivity {
    Button list,check,view,cmprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee);
        list=(Button)findViewById(R.id.listStudents);
        check=(Button)findViewById(R.id.checkStudents);
        view=(Button)findViewById(R.id.viewStudents);
        cmprofile=(Button)findViewById(R.id.committeeProfile);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.listStudents:
                        startActivity(new Intent(CommitteeActivity.this,StudentsList.class));

                        break;
                    case R.id.checkStudents:

                        break;
                    case R.id.viewStudents:

                        break;
                    case R.id.committeeProfile:

                        break;



                }
            }
        });


    }
}
