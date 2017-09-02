package org.terna.tpc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommitteeSendNotification extends AppCompatActivity {
    private String year,branch;

    private ProgressDialog pd;
    private StorageReference storagePath;
    private DatabaseReference data1,data2,dataPath;
    private Uri uri;
    private List<String> list= new ArrayList<>();
    private String name,extension,path,mKey;
    private ListView pdfList;
    private ArrayAdapter<String> arrayAdapter;
    private static final int File_Request_code = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee_send_notification);
        final Spinner yr = (Spinner) findViewById(R.id.yearChoice);
        pd = new ProgressDialog(this);
        final Spinner br = (Spinner) findViewById(R.id.branchChoice);
        final Button submit = (Button) findViewById(R.id.pdfAddButton);
        pdfList=(ListView)findViewById(R.id.pdfList);
        final StorageReference mStorage = FirebaseStorage.getInstance().getReference("Notifications");
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Notifications");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year=yr.getSelectedItem().toString();
                branch=br.getSelectedItem().toString();
                storagePath=mStorage.child(year).child(branch);
                dataPath=mDatabase.child(year).child(branch);
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
            File file= new File(uri.getPath());
            name = file.getName();
            path=String.valueOf(uri.getPath());
            extension=path.substring(path.lastIndexOf(".") + 1);
            mKey=dataPath.push().getKey();
            dataPath.child(mKey).setValue(extension);
            uploadData();
        }
    }

    @SuppressWarnings("VisibleForTests")
    private void uploadData() {
        CommitteeSendNotification.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                pd.show();
                storagePath.child(mKey).child(name).putFile(uri)
                        .addOnSuccessListener(CommitteeSendNotification.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                pd.dismiss();
                                list.add(year+" "+branch+" "+name);
                                arrayAdapter = new ArrayAdapter(CommitteeSendNotification.this, R.layout.liststudentclass,R.id.tv1,list);
                                pdfList.setAdapter(arrayAdapter);
                                Toast.makeText(CommitteeSendNotification.this,name+" is sent",Toast.LENGTH_LONG).show();
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

                } catch (Exception e) {
                    Log.e("mihir",e.getMessage());
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
            }


}
