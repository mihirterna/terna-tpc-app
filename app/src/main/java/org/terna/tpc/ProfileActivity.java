package org.terna.tpc;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mStorage = FirebaseStorage.getInstance().getReference("Students");
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Students").child(user.getUid());
        final StorageReference mPath = mStorage.child(user.getUid());
        final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TPC";
        final ImageView profileImageView = (ImageView) findViewById(R.id.user_profile_photo);
        final TextView profileNameView = (TextView) findViewById(R.id.user_profile_name);
        final TextView profileEmailView = (TextView) findViewById(R.id.user_profile_email);
        final TextView profileDobView = (TextView) findViewById(R.id.user_profile_dob);
        final TextView profileStationView = (TextView) findViewById(R.id.user_profile_city);
        final TextView profileMarksView = (TextView) findViewById(R.id.user_profile_marks);
        final TextView profileExtrasView = (TextView) findViewById(R.id.user_profile_extras);
        File dir = new File(filePath);

        ProfileActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            HashMap<String,String> receivedInfo1 = (HashMap<String,String>) dataSnapshot.getValue();
                            HashMap<String, String> receivedAcademics = (HashMap<String, String>) dataSnapshot.child("Academics").getValue();
                            profileNameView.setText("Name: " + receivedInfo1.get("a"));
                            profileEmailView.setText("E-Mail: " + receivedInfo1.get("b"));
                            profileDobView.setText("DateOfBirth: " + receivedInfo1.get("d"));
                            profileStationView.setText("Gender: " + receivedInfo1.get("e"));
                            profileMarksView.setText("FE: " + receivedAcademics.get("FE") + "\nSE: " + receivedAcademics.get("SE") + "\nTE: " + receivedAcademics.get("TE"));
                            profileExtrasView.setText(receivedAcademics.get("EXTRAS"));
                        } catch (Exception r) {
                            r.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("mihir", "loadPost:onCancelled", databaseError.toException());
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        ProfileActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    File rootPath = new File(Environment.getExternalStorageDirectory(), "mjk");
                    final File localFile =  new File(rootPath,"imageName.jpeg");
                    if(localFile.exists())
                    {
                        profileImageView.setImageURI(Uri.fromFile(localFile));

                    }
                    else {
                        mPath.getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("mihir", e.getMessage());
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                });
                    }
                } catch (Exception e) {
                    Log.e("mihir",e.getMessage());
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}