package org.terna.tpc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class CommitteeSendNotification extends AppCompatActivity {
    private String year,branch;

    private ProgressDialog pd;
    private StorageReference storagePath;
    private Uri uri;
    private static final int File_Request_code = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee_send_notification);
        final Spinner yr = (Spinner) findViewById(R.id.yearChoice);
        final Spinner br = (Spinner) findViewById(R.id.branchChoice);
        final Button submit = (Button) findViewById(R.id.pdfAddButton);
        final TextView pdfName = (TextView) findViewById(R.id.pdfFileName);
        final StorageReference mStorage = FirebaseStorage.getInstance().getReference("Notifications");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year=yr.getSelectedItem().toString();
                branch=br.getSelectedItem().toString();
                storagePath=mStorage.child(year).child(branch);
                showImageChooser();
            }
        });


    }
    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType(".pdf/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select File"),File_Request_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == File_Request_code && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();

                TextView pdfFile = (TextView) findViewById(R.id.pdfFileName);

                 uploadData();



        }
    }

    @SuppressWarnings("VisibleForTests")
    private void uploadData() {

        CommitteeSendNotification.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pd.setTitle("Doing work...");
                pd.show();
                storagePath.putFile(uri)
                        .addOnSuccessListener(CommitteeSendNotification.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(CommitteeSendNotification.this, StudentDashboardActivity.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(CommitteeSendNotification.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(CommitteeSendNotification.this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot tt) {
                                double progress = (100.0 * tt.getBytesTransferred() / tt.getTotalByteCount());
                                pd.setMessage((int) progress + "% uploaded.");
                                pd.show();
                            }
                        });
            }
        });
    }
}
