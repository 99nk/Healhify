package com.example.apputil;

import static android.os.SystemClock.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginBtn;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    String currUserId;
    TextView sendToRegister;
    DatabaseReference userRef,currUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail=findViewById(R.id.login_email_id);
        mPassword=findViewById(R.id.login_password);
        mLoginBtn=findViewById(R.id.btn_login);
        mAuth= FirebaseAuth.getInstance();
        sendToRegister=findViewById(R.id.loginToReg);
        progressDialog=new ProgressDialog(this);
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        userRef=db.getReference().child("Users");
        sendToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToRegisterActivity();
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
    }

    private void sendToRegisterActivity()
    {
        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void LoginUser()
    {
        String email=mEmail.getText().toString();
        String pass=mPassword.getText().toString();

        if(email.isEmpty()){
            mEmail.setError("Email is required!");
            mEmail.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Enter a valid Email address!");
            mEmail.requestFocus();
            return;
        }
        else if(pass.isEmpty()){
            mPassword.setError("Password is required!");
            mPassword.requestFocus();
            return;
        }
        else if(pass.length()<6 ){
            mPassword.setError("Minimum length of Password is 6 !");
            mPassword.requestFocus();
            return;
        }
        else
        {
            progressDialog.setMessage("Please wait while login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(LoginActivity.this, mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        currUserId=mAuth.getCurrentUser().getUid();
                        currUserRef=userRef.child(currUserId);
                        currUserRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String category=snapshot.child("category").getValue().toString();
                                Toast.makeText(LoginActivity.this, category, Toast.LENGTH_SHORT).show();
                                sendUserToDashboard(category);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+ task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToDashboard(String category)
    {
        switch (category){
            case "Patient":
                Intent patient_intent=new Intent(LoginActivity.this,PatientDashboardActivity.class);
                patient_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(patient_intent);
                break;
            case "Doctor":
                Intent doctor_intent=new Intent(LoginActivity.this,DoctorDashboardActivity.class);
                doctor_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(doctor_intent);
                break;
            case "Chemist":
                Intent chemist_intent=new Intent(LoginActivity.this,ChemistDashboardActivity.class);
                chemist_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chemist_intent);
                break;
            default:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){
            currUserId=mAuth.getCurrentUser().getUid();
            currUserRef=userRef.child(currUserId);
            currUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String category=snapshot.child("category").getValue().toString();
                    Toast.makeText(LoginActivity.this, category, Toast.LENGTH_SHORT).show();
                    sendUserToDashboard(category);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}