package com.example.barterx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

   private EditText email;
   private EditText email2;
   private EditText password;
   private EditText password2;
   private Button signIn;
   private TextView login;
   private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.txtEmail);
        email2 = findViewById(R.id.txtEmail2);
        password = findViewById(R.id.txtPassword);
        password2 = findViewById(R.id.txtPassword2);
        signIn = findViewById(R.id.btnLogIn);
        login = findViewById(R.id.goBackToLogin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateEmail(email.getText().toString(), email2.getText().toString()) &&
                        validatePassword(password.getText().toString(), password2.getText().toString()))
                {
                   register(email.getText().toString(), password.getText().toString());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });
    }

    private boolean validateEmail(String mail, String mail2)
    {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mail);
        if(matcher.matches())
        {
            if(mail.equals(mail2))
                return true;
            Toast.makeText(getApplicationContext(), "Emails do not match", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
        }
        return false; //Invalid email address, or email is not valid
    }

    private boolean validatePassword(String pass, String pass2){

        if(!pass.equals(pass2))
        {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!(pass.length() >= 8))
        {
            Toast.makeText(getApplicationContext(), "Password must have 8 or more characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        // TODO: Add password validation
        //Do an in depth password analysis using the following rules
            /*
                A password must have:
                    - At least 8 characters
                    - At least 1 special character
                    - At least 1 upper case character
                    - At least 1 lower case character
                    - At least 1 number
                A password must not include the email address of the user
             */
        return true;
    }

    //register new user with email and password if successful redirect to profile activity
    private void register(String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password)//register with email and password
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){// if the registration was successful execute code below
                            FirebaseUser user =mAuth.getCurrentUser();// get an instance of the currently logged user that just registered
                            Toast.makeText(getApplicationContext(),user.getEmail().toString() +" Signup successfully",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignUp.this, UserRegistrationStep2.class));//navigating to profile image activity
                        }else{
                            Toast.makeText(getApplicationContext(),"Registration Failed!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}