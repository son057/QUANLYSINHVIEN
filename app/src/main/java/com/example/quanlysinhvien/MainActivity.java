package com.example.quanlysinhvien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    final String DB_NAME = "QLSV.sqlite";
    SQLiteDatabase database;

    ListView listView;
    ArrayList<SinhVien> list;
    AdapterSinhVien adapter;


    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        readData();
    }


    private void addControls() {

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new AdapterSinhVien(this, list);
        listView.setAdapter(adapter);
    }
    private void readData(){
        database = Database.initDatabase(this, DB_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM SinhVien",null);
        list.clear();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            String hoten = cursor.getString(1);
            int namsinh = cursor.getInt(2);
            String dienthoai = cursor.getString(3);
            byte [] anh = cursor.getBlob(4);
            list.add(new SinhVien(id, hoten, namsinh, dienthoai, anh));
        }
        adapter.notifyDataSetChanged();
    }
}