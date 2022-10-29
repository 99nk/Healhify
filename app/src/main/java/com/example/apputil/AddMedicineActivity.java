package com.example.apputil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AddMedicineActivity extends AppCompatActivity {
    EditText mMedicineId,mName,mQuantity,mExpiryDate;
    Button uploadMedicine;
    String MedicineId,Name,Quantity,ExpiryDate;
    DatabaseReference medicinesRef,currUserRef,userRef;
    FirebaseAuth mAuth;
    String currUserId;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        mAuth=FirebaseAuth.getInstance();
        currUserId=mAuth.getCurrentUser().getUid();
        mMedicineId=findViewById(R.id.medicine_id);
        mName=findViewById(R.id.medicine_name);
        mQuantity=findViewById(R.id.medicine_quantity);
        mExpiryDate=findViewById(R.id.medicine_expiry_date);
        uploadMedicine=findViewById(R.id.upload_medicine);

        medicinesRef= FirebaseDatabase.getInstance().getReference().child("Medicines");
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        currUserRef=userRef.child(currUserId);

        currUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                category=snapshot.child("category").getValue().toString();
                Toast.makeText(AddMedicineActivity.this, category, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        uploadMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicineId=mMedicineId.getText().toString();
                Name=mName.getText().toString();
                Quantity=mQuantity.getText().toString();
                ExpiryDate=mExpiryDate.getText().toString();

                HashMap record=new HashMap();
                record.put("name",Name);
                record.put("medicineId",MedicineId);
                record.put("quantity",Quantity);
                record.put("expiryDate",ExpiryDate);

                medicinesRef.child(MedicineId).updateChildren(record).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddMedicineActivity.this, "Details Saved", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AddMedicineActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                sendUserToDashboard();
            }

            private void sendUserToDashboard() {
                switch (category) {
                    case "Patient":
                        Intent patient_intent = new Intent(AddMedicineActivity.this, PatientDashboardActivity.class);
                        patient_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(patient_intent);
                        break;
                    case "Doctor":
                        Intent doctor_intent = new Intent(AddMedicineActivity.this, DoctorDashboardActivity.class);
                        doctor_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(doctor_intent);
                        break;
                    case "Chemist":
                        Intent chemist_intent = new Intent(AddMedicineActivity.this, ChemistDashboardActivity.class);
                        chemist_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(chemist_intent);
                        break;
                }
            }
        });
    }

}