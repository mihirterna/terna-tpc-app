package org.terna.tpc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class StudentsList extends AppCompatActivity {
    private ListView listView;
    private Spinner yr,br;
    private List<String> list= new ArrayList<>();
    private String year,branch;
    private Button bt;
    private int flag;
    private  ArrayAdapter<String> arrayAdapter;
    private DatabaseReference databaseReference,mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        listView = (ListView) findViewById(R.id.lStudent);
        yr = (Spinner) findViewById(R.id.comSYr);
        bt=(Button)findViewById(R.id.findStuds);
        br = (Spinner) findViewById(R.id.comSBr);
        databaseReference = FirebaseDatabase.getInstance().getReference("Path");

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=0;
                year=yr.getSelectedItem().toString();
                branch=br.getSelectedItem().toString();
                mPath=databaseReference.child(year).child(branch);
                mPath.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        flag=1;
                        for (DataSnapshot ds1:dataSnapshot.getChildren())
                        {
                            list.add(String.valueOf(ds1.getValue()));
                            arrayAdapter = new ArrayAdapter(StudentsList.this, R.layout.liststudentclass,R.id.tv1,list);
                            listView.setAdapter(arrayAdapter);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }



                });
                if(flag ==0)
                {
                    list.clear();
                    Toast.makeText(StudentsList.this,"No Students in selected field",Toast.LENGTH_LONG).show();
                }

            }

        });





    }
}

