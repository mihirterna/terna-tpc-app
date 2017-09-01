package org.terna.tpc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class Student_notification extends AppCompatActivity {
    private StorageReference storageReference,storagePath;
    private DatabaseReference databaseReference,dataPath;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String yr,br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notification);
        storageReference= FirebaseStorage.getInstance().getReference("Notifications");
        databaseReference= FirebaseDatabase.getInstance().getReference("Students");
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        dataPath=databaseReference.child(user.getUid());
        dataPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> receivedInfo1 = (HashMap<String,String>) dataSnapshot.getValue();
                yr=receivedInfo1.get("yr");
                br=receivedInfo1.get("br");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        storagePath=storageReference.child(yr).child(br);

    }
}
