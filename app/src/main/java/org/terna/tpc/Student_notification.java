package org.terna.tpc;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
    private List<String> header = new ArrayList<>();
    private List<String> exten = new ArrayList<>();
    private DownloadManager downloadManager;
    private ArrayAdapter<String> arrayAdapter;
    private ListView notifList;
    private FirebaseUser user;
    private String yr,br,extension,name,a1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notification);
        final Button refreshButton = (Button) findViewById(R.id.refreshNotifButton);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String epx="jpg",pdf="pdf";

        user = firebaseAuth.getCurrentUser();
        data1=FirebaseDatabase.getInstance().getReference("Notifications");
        storageReference= FirebaseStorage.getInstance().getReference("Notifications");
        databaseReference= FirebaseDatabase.getInstance().getReference("Students");
        notifList= (ListView) findViewById(R.id.student_notif_list);
        dataPath=databaseReference.child(user.getUid());
        dataPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, String> receivedInfo1 = (HashMap<String, String>) dataSnapshot.getValue();
                    yr = receivedInfo1.get("f");
                    br = receivedInfo1.get("g");
                    Toast.makeText(Student_notification.this, yr + br, Toast.LENGTH_LONG).show();
                    data2=data1.child(yr).child(br);
                    storagePath=storageReference.child(yr).child(br);
                } catch (Exception r) {
                    r.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        snList.clear();
                        header.clear();
                        exten.clear();
                        for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                            extension = String.valueOf(ds1.getValue());
                            name = ds1.getKey();
                            sp1 = storagePath.child(name);
                            snList.add(name + "." + extension);
                            a1=sp1.getName();
                            header.add(sp1.getName());
                            exten.add(extension);
                            arrayAdapter = new ArrayAdapter<>(Student_notification.this, R.layout.liststudentclass, R.id.tv1, header);
                            notifList.setAdapter(arrayAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
            }
            });
        notifList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    File fk1 = new File(Environment.getExternalStorageDirectory(), "mjk");
                    if(!fk1.exists()) {
                        fk1.mkdirs();
                    }
                    downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    if(extension.equalsIgnoreCase(epx))
                    {
                        Toast.makeText(Student_notification.this,storageReference.child(header.get(position)+".jpeg").getDownloadUrl().toString(),Toast.LENGTH_LONG).show();

                                /*Uri uri = Uri.fromFile(file);
                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                downloadManager.enqueue(request); */

                    }
                    else if (extension.equalsIgnoreCase(pdf))
                    {
                        File file = File.createTempFile("application","pdf");
                        storagePath.child(header.get(position)).getFile(file);
                        Uri uri = Uri.fromFile(file);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        downloadManager.enqueue(request);
                    }

                    //Uri uri = Uri.parse(String.valueOf(storagePath.child(header.get(position)).getDownloadUrl()));


                }
                catch (Exception e) {
                    Log.e("mihir",e.getMessage());
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}
