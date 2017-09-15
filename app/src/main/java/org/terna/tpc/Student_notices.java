package org.terna.tpc;

import android.app.DownloadManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Student_notices extends AppCompatActivity {
    private StorageReference storageReference,storagePath,sp1;
    private DatabaseReference databaseReference,dataPath,data1,data2;
    private List<String> snList = new ArrayList<>();
    private List<String> header = new ArrayList<>();
    private List<String> exten = new ArrayList<>();
    private DownloadManager downloadManager;
    private ArrayAdapter<String> arrayAdapter;
    private ListView notifList;
    private FirebaseUser user;
    private String yr,br,extension,name,a1,ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notification);
        final Button refreshButton = (Button) findViewById(R.id.refreshNotifButton);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();
        data1=FirebaseDatabase.getInstance().getReference("Notices");
        storageReference= FirebaseStorage.getInstance().getReference("Notices");
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
                    Toast.makeText(Student_notices.this, yr + br, Toast.LENGTH_LONG).show();
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
                            ref=sp1.getName();
                            a1=ref.replace("+"," ");
                            snList.add(a1 + "." + extension);
                            header.add(a1);
                            exten.add(extension);
                            arrayAdapter = new ArrayAdapter<>(Student_notices.this, R.layout.liststudentclass, R.id.tv1, snList);
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
                    downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    storagePath.child(header.get(position)).getDownloadUrl()
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Student_notices.this,"Fail"+e.toString(),Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(Student_notices.this,"File is downloading...",Toast.LENGTH_LONG).show();
                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                downloadManager.enqueue(request);
                            }
                        });
                }
                catch (Exception e) {
                    Log.e("mihir",e.getMessage());
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}
