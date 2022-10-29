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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChemistDashboardActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    DatabaseReference medicinesRef;
    MedicineAdapter medicineAdapter;
    ArrayList<Medicine> list;
    FloatingActionButton addMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemist_dashboard);
        addMedicine=findViewById(R.id.add_medicine);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        recyclerView=findViewById(R.id.medicines_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        medicinesRef= FirebaseDatabase.getInstance().getReference().child("Medicines");
        ClearAll();
        GetDataFromFirebase();
        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent=new Intent(ChemistDashboardActivity.this,AddMedicineActivity.class);
                home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(home_intent);
            }
        });
    }

    private void GetDataFromFirebase() {

        Query query=medicinesRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for(DataSnapshot snapshot: datasnapshot.getChildren()){
                    Medicine medicine=new Medicine();
                    medicine.setName(snapshot.child("name").getValue().toString());
                    medicine.setMedicineId(snapshot.child("medicineId").getValue().toString());
                    medicine.setQuantity(snapshot.child("quantity").getValue().toString());
                    medicine.setExpiryDate(snapshot.child("expiryDate").getValue().toString());
                    list.add(medicine);
                }
                medicineAdapter=new MedicineAdapter(getApplicationContext(),list);
                recyclerView.setAdapter(medicineAdapter);
                medicineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private  void ClearAll(){
        if(list!=null){
            list.clear();

            if(medicineAdapter!=null){
                medicineAdapter.notifyDataSetChanged();
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
                            Intent profile_intent=new Intent(ChemistDashboardActivity.this,ChemistProfileActivity.class);
                            profile_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(profile_intent);
                            break;
                        case R.id.home:
                            Intent home_intent=new Intent(ChemistDashboardActivity.this,ChemistDashboardActivity.class);
                            home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(home_intent);
                            break;
                    }
                    return true;
                }
            };
}
