package com.example.quanlysinhvien;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.nio.file.attribute.AclEntryType;
import java.util.ArrayList;
public class AdapterSinhVien extends BaseAdapter {
    Activity context;
    ArrayList<SinhVien> list;
    public AdapterSinhVien(Activity context, ArrayList<SinhVien> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_row, null);
        ImageView imgHinhDaiDien = (ImageView)
                row.findViewById(R.id.imgHinhDaiDien);
        TextView txtId = (TextView) row.findViewById(R.id.txtId);
        TextView txtTen = (TextView) row.findViewById(R.id.txtTen);
        TextView txtSdt = (TextView) row.findViewById(R.id.txtSdt);
        Button btnXoa = (Button) row.findViewById(R.id.btnXoa);
        Button btnSua = (Button) row.findViewById(R.id.btnSua);
        final SinhVien sinhVien = list.get(position);
        txtId.setText(sinhVien.masv + "");
        txtTen.setText(sinhVien.hoten);
        txtSdt.setText(sinhVien.dienthoai);

        Bitmap bmHinhDaiDien = BitmapFactory.decodeByteArray(sinhVien.anh, 0,sinhVien.anh.length);
        imgHinhDaiDien.setImageBitmap(bmHinhDaiDien);


        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("MaSV", sinhVien.masv);
                context.startActivity(intent);
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa sinh viên này?");
                builder.setPositiveButton("Có", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete(sinhVien.masv);
                            }
                        });
                builder.setNegativeButton("Không", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return row;
    }


    private void delete(int maSV) {
        SQLiteDatabase database =
                Database.initDatabase((Activity) context,"QLSV.sqlite");
        database.delete("SinhVien", "MaSV = ?", new String[]{maSV + ""});
        list.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM SinhVien", null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            int namsinh = cursor.getInt(2);
            String dienthoai = cursor.getString(3);
            byte[] anh = cursor.getBlob(4);
            list.add(new SinhVien(id, ten,namsinh , dienthoai,anh));
        }
        notifyDataSetChanged();
    }
}
