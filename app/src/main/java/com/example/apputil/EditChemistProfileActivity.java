package com.example.apputil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditChemistProfileActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    DatabaseReference userRef,currUserRef;
    FirebaseAuth mAuth;
    String currentUserId;
    EditText name, email, category, phone;
    Button updateProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chemist_profile);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        currUserRef=userRef.child(currentUserId);
        name=findViewById(R.id.chemist_profile_name);
        email=findViewById(R.id.chemist_profile_email);
        category=findViewById(R.id.chemist_profile_category);
        phone=findViewById(R.id.chemist_profile_phone);
        updateProfile=findViewById(R.id.updateChemistProfile);
        currUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chemist_name=snapshot.child("name").getValue().toString();
                String chemist_email=snapshot.child("email").getValue().toString();
                String chemist_phone=snapshot.child("phoneNumber").getValue().toString();
                name.setHint("Name: "+chemist_name);
                email.setHint("Email Id: "+chemist_email);
                phone.setHint("Phone No.: "+chemist_phone);
                category.setHint("Category: Chemist");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n=name.getText().toString();
                String e=email.getText().toString();
                String p=phone.getText().toString();
                HashMap user=new HashMap();
                user.put("name",n);
                user.put("email",e);
                user.put("phoneNumber",p);
                user.put("category","Chemist");
                currUserRef.updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EditChemistProfileActivity.this, "Details Saved", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(EditChemistProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        sendUserToDashboard();
                    }
                });
            }

            private void sendUserToDashboard() {
                Intent chemist_intent=new Intent(EditChemistProfileActivity.this,ChemistDashboardActivity.class);
                chemist_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chemist_intent);
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
                            Intent profile_intent=new Intent(EditChemistProfileActivity.this,ChemistProfileActivity.class);
                            profile_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(profile_intent);
                            break;
                        case R.id.home:
                            Intent home_intent=new Intent(EditChemistProfileActivity.this,ChemistDashboardActivity.class);
                            home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(home_intent);
                            break;
                    }
                    return true;
                }
            };
}