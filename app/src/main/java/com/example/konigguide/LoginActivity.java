package com.example.konigguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.konigguide.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailEditText, passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputLayout emailInput = findViewById(R.id.email_input_layout);
        TextInputLayout passwordInput = findViewById(R.id.password_input_layout);
        emailEditText = (TextInputEditText) emailInput.getEditText();
        passwordEditText = (TextInputEditText) passwordInput.getEditText();

        TextView NextReg = findViewById(R.id.RegistrationLink);
        Button NextLogin = findViewById(R.id.NextLogin);

        NextLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = emailEditText.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    emailEditText.setError("Введите эл.почту!");
                    passwordEditText.setError("Введите пароль!");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    emailEditText.setError(null);
                    passwordEditText.setError("Введите пароль!");
                    return;
                }else {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Успешный успех",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            } else {
                                String error = task.getException().getMessage();
                                if (error != null&&error.contains("A network error")) {
                                    Toast.makeText(getApplicationContext(),"Подключитесь к интернету",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (error != null&&error.contains("The email address is badly formatted")){
                                    Toast.makeText(getApplicationContext(),"Некорректный адрес эл.почты",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (error != null&&error.contains("The supplied auth credential is incorrect, malformed or has expired.")) {
                                    Toast.makeText(getApplicationContext(),"Неверный адрес эл.почты или пароль",Toast.LENGTH_SHORT).show();
                                    return;
                                }else {
                                    Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });

        NextReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });


        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(this, R.color.first_fade_pall),
                        ContextCompat.getColor(this, R.color.second_fade_pall),
                        ContextCompat.getColor(this, R.color.third_fade_pall),
                        ContextCompat.getColor(this, R.color.fourth_fade_pall)});
        findViewById(R.id.backgroundLogin).setBackground(gradientDrawable);

    }

}