package org.terna.tpc;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Student_notification extends AppCompatActivity {
    private StorageReference storageReference,storagePath,sp1,sp2;
    private DatabaseReference databaseReference,dataPath,data1,data2;
    private List<String> snList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private ListView notifList;
    private String yr,br,extension,name,a1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notification);
        final Button refreshButton = (Button) findViewById(R.id.refreshNotifButton);
        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference= FirebaseStorage.getInstance().getReference("Notifications");
        databaseReference= FirebaseDatabase.getInstance().getReference("Students");
        notifList= (ListView) findViewById(R.id.student_notif_list);
        dataPath=databaseReference.child(user);
        dataPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, String> receivedInfo1 = (HashMap<String, String>) dataSnapshot.getValue();
                    yr = receivedInfo1.get("f");
                    br = receivedInfo1.get("g");
                    Toast.makeText(Student_notification.this, yr + br, Toast.LENGTH_LONG).show();
                } catch (Exception r) {
                    r.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        data1=FirebaseDatabase.getInstance().getReference("Notifications").child(yr).child(br);
        storagePath=storageReference.child(yr).child(br);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds1 : dataSnapshot.getChildren())
                        {
                            extension= String.valueOf(ds1.getValue());
                            name=ds1.getKey();
                            a1=storagePath.child(name).getName();
                            snList.add(a1);
                            arrayAdapter = new ArrayAdapter(Student_notification.this, R.layout.liststudentclass,R.id.tv1,snList);
                            notifList.setAdapter(arrayAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }
}
