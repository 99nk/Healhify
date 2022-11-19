package com.example.apputil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientProfileActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    DatabaseReference userRef,currUserRef;
    FirebaseAuth mAuth;
    String currentUserId;
    TextView name, email, category, phone;
    Button editProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_activty);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        currUserRef=userRef.child(currentUserId);
        name=findViewById(R.id.patient_profile_name);
        email=findViewById(R.id.patient_profile_email);
        category=findViewById(R.id.patient_profile_category);
        phone=findViewById(R.id.patient_profile_phone);
        editProfile=findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile_intent=new Intent(PatientProfileActivity.this,EditPatientProfileActivity.class);
                profile_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(profile_intent);
            }
        });
        currUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chemist_name=snapshot.child("name").getValue().toString();
                String chemist_email=snapshot.child("email").getValue().toString();
                String chemist_phone=snapshot.child("phoneNumber").getValue().toString();
                name.setText("Name: "+chemist_name);
                email.setText("Email Id: "+chemist_email);
                phone.setText("Phone No.: "+chemist_phone);
                category.setText("Category: Patient");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.profile:
                            Intent profile_intent=new Intent(PatientProfileActivity.this,PatientProfileActivity.class);
                            profile_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(profile_intent);
                            break;
                        case R.id.home:
                            Intent home_intent=new Intent(PatientProfileActivity.this,PatientDashboardActivity.class);
                            home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(home_intent);
                            break;
                    }
                    return true;
                }
            };
}