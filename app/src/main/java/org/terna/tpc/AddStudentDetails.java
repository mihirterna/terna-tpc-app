package org.terna.tpc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class AddStudentDetails extends AppCompatActivity {

    private ImageButton profilePicture;
    private EditText fe,se,te,extra;
    private ProgressDialog pd;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Spinner ch;
    private Uri uri;
    private static final int PROFILE_IMAGE_REQUEST = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student_details);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Students");
        databaseReference = FirebaseDatabase.getInstance().getReference("Students");

        pd = new ProgressDialog(this);
        profilePicture = (ImageButton)findViewById(R.id.profileImage);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });
        fe = (EditText)findViewById(R.id.feMarks);
        se = (EditText)findViewById(R.id.seMarks);
        te = (EditText)findViewById(R.id.teMarks);
        extra = (EditText)findViewById(R.id.extras);
        ch=(Spinner) findViewById(R.id.teamChoice);

        final Button addToFirebase = (Button)findViewById(R.id.add_Details);
        addToFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }

    @SuppressWarnings("VisibleForTests")
    private void uploadData() {
        if (uri == null)
            Toast.makeText(getApplicationContext(), "Select Profile Image", Toast.LENGTH_LONG).show();
        else{
            final String FEM = fe.getText().toString().trim();
            final String SEM = se.getText().toString().trim();
            final String TEM = te.getText().toString().trim();
            final String choice = ch.getSelectedItem().toString();
            final String EXT = extra.getText().toString().trim();
            if (TextUtils.isEmpty(FEM) || TextUtils.isEmpty(SEM) || TextUtils.isEmpty(TEM))
                Toast.makeText(getApplicationContext(), "Enter proper credentials", Toast.LENGTH_LONG).show();
            else {
                pd.setTitle("Doing work...");
                pd.show();
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                AddStudentDetails.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        storageReference.child(user.getUid()).putFile(uri)
                                .addOnSuccessListener(AddStudentDetails.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(AddStudentDetails.this, StudentDashboardActivity.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(AddStudentDetails.this, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnProgressListener(AddStudentDetails.this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot tt) {
                                        double progress = (100.0 * tt.getBytesTransferred() / tt.getTotalByteCount());
                                        pd.setMessage((int) progress + "% uploaded.");
                                        pd.show();
                                    }
                                });
                    }
                });
                AddStudentDetails.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, String> marks = new HashMap<>();
                        marks.put("FE", FEM);
                        marks.put("SE", SEM);
                        marks.put("TE", TEM);
                        marks.put("Interest in",choice);
                        if (TextUtils.isEmpty(EXT)) marks.put("EXTRAS", "nothing");
                        else marks.put("EXTRAS", EXT);
                        databaseReference.child(user.getUid()).child("Academics").setValue(marks)
                                .addOnCompleteListener(AddStudentDetails.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(AddStudentDetails.this, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });
            }
        }
    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),PROFILE_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PROFILE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            try {
                Bitmap imageProfile = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                profilePicture.setImageBitmap(imageProfile);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
