package com.example.apputil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity{

    EditText mEmailId,mPassword,mConfirmPassword,mName,mPhoneNumber;
    Button mRegister;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    String currUserId;
    TextView sendToLogin;
    String category;
    DatabaseReference userRef,currUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmailId=findViewById(R.id.register_email_id);
        mPassword=findViewById(R.id.register_password);
        mConfirmPassword=findViewById(R.id.register_confirm_password);
        mRegister=findViewById(R.id.register_btn);
        mName=findViewById(R.id.register_name);
        mPhoneNumber=findViewById(R.id.register_phone_number);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        sendToLogin=findViewById(R.id.RegisterToLogin);
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        userRef=db.getReference().child("Users");
        Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categ_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sendToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToLoginActivity();
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();
            }
        });
    }

    private void sendToLoginActivity() {
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void PerformAuth()
     {
         String Email=mEmailId.getText().toString();
         String Password=mPassword.getText().toString();
         String ConfirmPassword=mConfirmPassword.getText().toString();
         String PhoneNumber=mPhoneNumber.getText().toString();
         String Name=mName.getText().toString();

         if(Email.isEmpty()){
             mEmailId.setError("Email is required!");
             mEmailId.requestFocus();
             return;
         }
         else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
             mEmailId.setError("Enter a valid Email address!");
             mEmailId.requestFocus();
             return;
         }
         else if(Password.isEmpty()){
             mPassword.setError("Password is required!");
             mPassword.requestFocus();
             return;
         }
         else if(Password.length()<6 ){
             mPassword.setError("Minimum length of Password is 6 !");
             mPassword.requestFocus();
             return;
         }
         else if(!(ConfirmPassword.equals(Password)))
         {
             mConfirmPassword.setError("Password doesn't match");
             mConfirmPassword.requestFocus();
             return;
         }
         else
         {
             progressDialog.setMessage("Please wait while registering...");
             progressDialog.setTitle("Registration");
             progressDialog.setCanceledOnTouchOutside(false);
             progressDialog.show();

             mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     progressDialog.dismiss();
                     if(task.isSuccessful())
                     {
                         currUserId=mAuth.getCurrentUser().getUid();
                         currUserRef=userRef.child(currUserId);
                         HashMap user=new HashMap();
                         user.put("name",Name);
                         user.put("email",Email);
                         user.put("phoneNumber",PhoneNumber);
                         user.put("category",category);

                         currUserRef.updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                             @Override
                             public void onComplete(@NonNull Task task) {
                                 if(task.isSuccessful()){
                                     Toast.makeText(RegisterActivity.this, "Details Saved", Toast.LENGTH_SHORT).show();
                                 }else{
                                     Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                 }
                             }
                         });

                         sendUserToDashboard();
                         Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                     }
                     else
                     {
                         Toast.makeText(RegisterActivity.this, ""+ task.getException(), Toast.LENGTH_SHORT).show();
                     }
                 }
             });
         }

     }

    private void sendUserToDashboard()
    {
        switch (category){
            case "Patient":
                Intent patient_intent=new Intent(RegisterActivity.this,PatientDashboardActivity.class);
                patient_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(patient_intent);
                break;
            case "Doctor":
                Intent doctor_intent=new Intent(RegisterActivity.this,DoctorDashboardActivity.class);
                doctor_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(doctor_intent);
                break;
            case "Chemist":
                Intent chemist_intent=new Intent(RegisterActivity.this,ChemistDashboardActivity.class);
                chemist_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chemist_intent);
                break;
            default:
                Intent intent=new Intent(RegisterActivity.this,RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
        }
    }
}