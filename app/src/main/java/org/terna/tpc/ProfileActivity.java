package org.terna.tpc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Students").child(user.getUid());

        final ImageView profileImageView = (ImageView)findViewById(R.id.user_profile_photo);
        final TextView profileNameView = (TextView)findViewById(R.id.user_profile_name);
        final TextView profileEmailView = (TextView)findViewById(R.id.user_profile_email);
        final TextView profileDobView = (TextView)findViewById(R.id.user_profile_dob);
        final TextView profileStationView = (TextView)findViewById(R.id.user_profile_city);
        final TextView profileMarksView = (TextView)findViewById(R.id.user_profile_marks);
        final TextView profileExtrasView = (TextView)findViewById(R.id.user_profile_extras);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    UserInfo receivedInfo = (UserInfo) dataSnapshot.getValue();
                    HashMap<String,String> receivedAcademics = (HashMap<String,String>)dataSnapshot.child("Academics & Extras").getValue();
                    profileNameView.setText(receivedInfo.getName());
                    profileEmailView.setText(receivedInfo.getEmail());
                    profileDobView.setText(receivedInfo.getDob());
                    profileStationView.setText(receivedInfo.getGender());
                    profileMarksView.setText("FE: "+receivedAcademics.get("FE")+"\nSE: "+receivedAcademics.get("SE")+"\nTE: "+receivedAcademics.get("TE"));
                    profileExtrasView.setText(receivedAcademics.get("EXTRAS"));
                }catch(Exception r){
                    r.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
