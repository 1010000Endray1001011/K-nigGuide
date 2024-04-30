package com.example.konigguide;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    String emailPattetn = "^[a-zA-Z0-9._-]+@mail\\.ru$";
    String usernamePattetn = "^[a-zA-Z0-9]+$";
    FirebaseAuth mAuth;
    FirebaseDatabase FD = FirebaseDatabase.getInstance("https://konigguide-f057f-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseUser mUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextInputLayout emailInput = findViewById(R.id.email_input_layout_reg);
        TextInputLayout passwordInput = findViewById(R.id.password_input_layout_reg);
        TextInputLayout loginInput = findViewById(R.id.login_input_layout_reg);
        TextInputLayout retryInput = findViewById(R.id.retry_password_input_layout_reg);

        TextInputEditText retry = (TextInputEditText) retryInput.getEditText();
        TextInputEditText emailT = (TextInputEditText) emailInput.getEditText();
        TextInputEditText loginT = (TextInputEditText) loginInput.getEditText();
        TextInputEditText passwordT = (TextInputEditText) passwordInput.getEditText();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

        passwordT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    passwordT.setError("Пароль должен быть не короче 6 символов");
                } else {
                    passwordT.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        retry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = passwordT.getText().toString();
                if (s.length() == 0) {
                    retry.setError("Подтвердите пароль");
                } else if (!s.toString().equals(password)) {
                    retry.setError("Пароли не совпадают");
                } else {
                    retry.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        Button Nextreg = findViewById(R.id.NextRegister);
        Nextreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailT.getText().toString().trim();
                String login = loginT.getText().toString().trim();
                String password = passwordT.getText().toString().trim();
                String retryPassword = retry.getText().toString().trim();

                if (login.isEmpty()) {
                    loginT.setError("Введите логин!");
                    if (email.isEmpty()) {
                        emailT.setError("Введите эл.почту!");
                        if (password.isEmpty()) {
                            passwordT.setError("Введите пароль!");
                            if (retryPassword.isEmpty()) {
                                retry.setError("Подтвердите пароль!");
                            }
                        }
                    }
                } else if (login.length() < 6) {
                    loginT.setError("Пароль должен быть не короче 6 символов");
                } else if (!login.matches(usernamePattetn)) {
                    loginT.setError("Извините, но допускаются только латинские буквы (a-z), цифры (0-9)");
                } else if (!email.matches(emailPattetn)) {
                    emailT.setError("Извините, но допускаются только латинские буквы (a-z), цифры (0-9)");
                } else if (password.isEmpty()) {
                    passwordT.setError("Введите пароль");
                    if (retryPassword.isEmpty()) {
                        retry.setError("Подтвердите пароль");
                    }
                } else if (password.length() < 6) {
                    passwordT.setError("Пароль должен быть не короче 6 символов");
                } else if (retryPassword.isEmpty()) {
                    retry.setError("Подтвердите пароль");
                } else if (!password.equals(retryPassword)) {
                    retry.setError("Пароль не совпадает");
                } else {
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                HashMap<String,String> userInfo = new HashMap<>();
                                userInfo.put("username",login);
                                userInfo.put("email",email);
                                userInfo.put("password",password);
                                FD.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).setValue(userInfo);
                                startActivity(intent);
                                finishAffinity();
                            }else{
                                String error = task.getException().getMessage();
                                if (error != null&&error.contains("The email address is already in use by another account.")) {
                                    Toast.makeText(getApplicationContext(),"Почта уже занята!",Toast.LENGTH_SHORT).show();
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



        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(this, R.color.first_fade_pall),
                        ContextCompat.getColor(this, R.color.second_fade_pall),
                        ContextCompat.getColor(this, R.color.third_fade_pall),
                        ContextCompat.getColor(this, R.color.fourth_fade_pall)});
        findViewById(R.id.backgroundRegister).setBackground(gradientDrawable);

    }
}
