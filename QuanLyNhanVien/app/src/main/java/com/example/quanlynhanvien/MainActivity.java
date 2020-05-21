package com.example.quanlynhanvien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnThem;
    ListView listView;
    ArrayList<NhanVien> nhanVienArrayList;
    NhanVienAdapter adapter;
    public static Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnThem = (Button) findViewById(R.id.btnAddEmployee);
        listView = (ListView) findViewById(R.id.ListView);
        nhanVienArrayList = new ArrayList<>();
        adapter = new NhanVienAdapter(this, R.layout.dong_nhan_vien, nhanVienArrayList);
        listView.setAdapter(adapter);



        database = new Database(this, "QuanLyNhanVien.sqlite", null, 1);

        database.QueryData("CREATE TABLE IF NOT EXISTS NhanVien(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenNhanVien VARCHAR(150), ChucVu VARCHAR(50), HinhAnh BLOB, Phone INTEGER)");
        Cursor cursor = database.GetData("SELECT * FROM NhanVien");
        while (cursor.moveToNext()){
            nhanVienArrayList.add(new NhanVien(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3),
                    cursor.getInt(4)));


        }
        adapter.notifyDataSetChanged();
        if (nhanVienArrayList.size() >= 1){
            btnThem.setVisibility(View.INVISIBLE);
        }
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ThemNhanVienActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_them, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public  void DialoEdit(){
        Dialog dialog = new Dialog(this);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuAdd){
            startActivity(new Intent(MainActivity.this, ThemNhanVienActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
