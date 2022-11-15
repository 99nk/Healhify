package com.example.apputil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ViewMedicine extends AppCompatActivity {

    String medicine_id;
    TextView name,id,expiry;
    EditText quantity;
    Button updateQuantity,deleteMedicine;
    DatabaseReference medicineRef;
    String medicine_name,medicine_ID,medicine_expiry,medicine_quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicine);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width),(int) (height*.55));

        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.dimAmount = 0.60f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        medicine_id=getIntent().getExtras().get("MedicineKey").toString();
        name=findViewById(R.id.medicine_name);
        id=findViewById(R.id.medicine_id);
        expiry=findViewById(R.id.medicine_expiry_date);
        quantity=findViewById(R.id.medicine_quantity);
        updateQuantity=findViewById(R.id.update_medicine_quantity);
        deleteMedicine=findViewById(R.id.delete_medicine);
        medicineRef= FirebaseDatabase.getInstance().getReference().child("Medicines").child(medicine_id);
        medicineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(ViewMedicine.this, medicine_id, Toast.LENGTH_SHORT).show();
                medicine_name=snapshot.child("name").getValue().toString();
                medicine_ID=snapshot.child("medicineId").getValue().toString();
                medicine_expiry=snapshot.child("expiryDate").getValue().toString();
                medicine_quantity=snapshot.child("quantity").getValue().toString();
                name.setText(medicine_name);
                id.setText(medicine_ID);
                expiry.setText(medicine_expiry);
                quantity.setHint(medicine_quantity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap medicine=new HashMap();
                medicine.put("name",medicine_name);
                medicine.put("medicineId",medicine_ID);
                medicine.put("quantity",quantity.getText().toString());
                medicine.put("expiryDate",medicine_expiry);
                medicineRef.updateChildren(medicine).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Intent intent=new Intent(ViewMedicine.this,ChemistDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        });

        deleteMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medicineRef.removeValue();
                Intent intent=new Intent(ViewMedicine.this,ChemistDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}