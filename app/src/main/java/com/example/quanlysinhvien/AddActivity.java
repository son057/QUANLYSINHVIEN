package com.example.quanlysinhvien;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddActivity extends AppCompatActivity {

    final String DATABASE_NAME = "QLSV.sqlite";

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOSE_PHOTO = 321;

    Button btnChonHinh, btnChupHinh,btnLuu, btnHuy;
    EditText edtTen, edtNS,edtSdt;
    ImageView imgHinhDaiDien;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        addControls();
        addEvents();
    }



    private void addControls() {
        btnChonHinh = (Button) findViewById(R.id.btnChonHinh);
        btnChupHinh = (Button) findViewById(R.id.btnChupHinh);
        btnLuu = (Button) findViewById(R.id.btnLuu);
        btnHuy = (Button) findViewById(R.id.btnHuy);
        edtTen = (EditText) findViewById(R.id.edtTen);
        edtNS = (EditText) findViewById(R.id.edtNS);
        edtSdt = (EditText) findViewById(R.id.edtSdt);
        imgHinhDaiDien = (ImageView) findViewById(R.id.imgHinhDaiDien);
    }



    private void addEvents(){
        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        btnChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }


    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_TAKE_PHOTO);
    }
    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CHOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgHinhDaiDien.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgHinhDaiDien.setImageBitmap(bitmap);
            }
        }
    }
    private void insert() {
        String ten = edtTen.getText().toString();
        String NS  = edtNS.getText().toString();
        String sdt = edtSdt.getText().toString();
        byte[] anh = getByteArrayFromImageView(imgHinhDaiDien);
        ContentValues contentValues = new ContentValues();
        contentValues.put("HoTen", ten);
        contentValues.put("DienThoai", sdt);
        contentValues.put("NamSinh", NS);
        contentValues.put("Anh", anh);
        SQLiteDatabase database = Database.initDatabase(this,
                "QLSV.sqlite");
        database.insert("SinhVien",null, contentValues);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public byte[] getByteArrayFromImageView(ImageView imgv){

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void cancel(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}