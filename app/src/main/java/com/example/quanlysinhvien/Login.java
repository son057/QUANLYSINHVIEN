package com.example.quanlysinhvien;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Currency;


public class Login extends AppCompatActivity {
    final String DATABASE_NAME = "Login.sqlite";
    String user;
    String password;
    int id;
    SQLiteDatabase database;
    Button btnsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button Login = (Button) findViewById(R.id.btnlogin);
        EditText edtuser = (EditText) findViewById(R.id.username);
        EditText edtpass = (EditText) findViewById(R.id.password);

        btnsignup = findViewById(R.id.btnsignup);
        database = Database.initDatabase(this, DATABASE_NAME);


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Signup.class);
                startActivity(i);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Cursor cursor = database.rawQuery("SELECT * FROM Login", null);
                String user;
                String pass;

                String User = edtuser.getText().toString();
                String Pass = edtpass.getText().toString();
                for(int i = 0; i < cursor.getCount(); i++){
                    cursor.moveToPosition(i);
                    user=cursor.getString(1);
                    pass=cursor.getString(2);

                    if (User.equalsIgnoreCase(user) && Pass.equalsIgnoreCase(pass)) {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    }
                    if(i==cursor.getCount()-1){
                        TextView btnerr = (TextView) findViewById(R.id.err);
                        btnerr.setText("Sai tai khoan hoac mat khau");

                    }
                }
            }
        });
    }

    public void userOnclick(View view) {
        TextView btnerr = (TextView) findViewById(R.id.err);
        btnerr.setText("");
    }

    public void passwordOnclick(View view) {
        TextView btnerr = (TextView) findViewById(R.id.err);
        btnerr.setText("");
    }

}