package com.example.apputil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PatientDashboardActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    DatabaseReference recordRef;
    RecordAdapter recordAdapter;
    ArrayList<Record> list;
    FirebaseAuth mAuth;
    FloatingActionButton checkDoctorSchedule;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashborad);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        checkDoctorSchedule=findViewById(R.id.check_doctor_schedule);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        recyclerView=findViewById(R.id.records_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        recordRef= FirebaseDatabase.getInstance().getReference().child("Records");
        ClearAll();
        GetDataFromFirebase();
        checkDoctorSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent=new Intent(PatientDashboardActivity.this,CheckDoctorScheduleActivity.class);
                home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(home_intent);
            }
        });
    }

    private void GetDataFromFirebase() {

        Query query=recordRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for(DataSnapshot snapshot: datasnapshot.getChildren()){
                    Record record=new Record();
                    if(snapshot.child("patientId").getValue().toString().equals(currentUserId)){
                        record.setName("Consultant's Name: "+snapshot.child("name").getValue().toString());
                        record.setAge("Age: "+snapshot.child("age").getValue().toString());
                        record.setDate("Date: "+snapshot.child("date").getValue().toString());
                        record.setProblem("Diagnosis: "+snapshot.child("problem").getValue().toString());
                        record.setMedicines("Medicines Prescribed: "+snapshot.child("medicines").getValue().toString());
                        list.add(record);
                    }
                }
                recordAdapter=new RecordAdapter(getApplicationContext(),list);
                recyclerView.setAdapter(recordAdapter);
                recordAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private  void ClearAll(){
        if(list!=null){
            list.clear();

            if(recordAdapter!=null){
                recordAdapter.notifyDataSetChanged();
            }
        }
        list=new ArrayList<>();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.profile:
                            Intent profile_intent=new Intent(PatientDashboardActivity.this,PatientProfileActivity.class);
                            profile_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(profile_intent);
                            break;
                        case R.id.home:
                            Intent home_intent=new Intent(PatientDashboardActivity.this,PatientDashboardActivity.class);
                            home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(home_intent);
                            break;
                    }
                    return true;
                }
            };
}
