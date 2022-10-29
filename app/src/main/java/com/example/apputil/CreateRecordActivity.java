package com.example.apputil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateRecordActivity extends AppCompatActivity {

    EditText mName,mDate,mAge,mProblem,mMedicines,mMedicineName;
    Button mPost,checkMedicineQuantity;
    TextView showMedicineQuantity;
    String Name, Date, Age, Problem, Medicines,MedicineName;
    DatabaseReference recordsRef,currUserRef,userRef,medicinesRef;
    FirebaseAuth mAuth;
    String currUserId;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_record);
        mMedicineName=findViewById(R.id.enter_medicine_name);
        checkMedicineQuantity=findViewById(R.id.check_medicines);
        showMedicineQuantity=findViewById(R.id.show_medicine_quantity);
        mName=findViewById(R.id.create_record_name);
        mDate=findViewById(R.id.create_record_date);
        mAge=findViewById(R.id.create_record_age);
        mProblem=findViewById(R.id.create_record_problem);
        mMedicines=findViewById(R.id.create_record_medicines);
        mPost=findViewById(R.id.create_record_post);
        mAuth=FirebaseAuth.getInstance();
        currUserId=mAuth.getCurrentUser().getUid();
        recordsRef= FirebaseDatabase.getInstance().getReference().child("Records");
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        currUserRef=userRef.child(currUserId);
        medicinesRef=FirebaseDatabase.getInstance().getReference().child("Medicines");

        currUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                category=snapshot.child("category").getValue().toString();
                Toast.makeText(CreateRecordActivity.this, category, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        checkMedicineQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicineName=mMedicineName.getText().toString();
                medicinesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            if(snapshot.child("name").getValue().toString().equals(MedicineName)){
                                showMedicineQuantity.setVisibility(View.VISIBLE);
                                showMedicineQuantity.setText(snapshot.child("quantity").getValue().toString());
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name=mName.getText().toString();
                Date=mName.getText().toString();
                Age=mName.getText().toString();
                Problem=mName.getText().toString();
                Medicines=mName.getText().toString();

                HashMap record=new HashMap();
                record.put("name",Name);
                record.put("date",Date);
                record.put("age",Age);
                record.put("problem",Problem);
                record.put("medicines",Medicines);
                record.put("patientId",currUserId);

                recordsRef.child(currUserId).updateChildren(record).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CreateRecordActivity.this, "Details Saved", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CreateRecordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                sendUserToDashboard();
            }

            private void sendUserToDashboard() {
                switch (category) {
                    case "Patient":
                        Intent patient_intent = new Intent(CreateRecordActivity.this, PatientDashboardActivity.class);
                        patient_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(patient_intent);
                        break;
                    case "Doctor":
                        Intent doctor_intent = new Intent(CreateRecordActivity.this, DoctorDashboardActivity.class);
                        doctor_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(doctor_intent);
                        break;
                    case "Chemist":
                        Intent chemist_intent = new Intent(CreateRecordActivity.this, ChemistDashboardActivity.class);
                        chemist_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(chemist_intent);
                        break;
                    default:
                        Intent intent = new Intent(CreateRecordActivity.this, RegisterActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                }
            }
        });

    }
}