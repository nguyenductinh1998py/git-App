package com.example.quanlynhanvien;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnThem;
    ListView listView;
    ArrayList<NhanVien> nhanVienArrayList;
    NhanVienAdapter adapter;
    public static ImageView imgView;
    public static Database database;
    int REQUEST_CODE_CAMERA = 111;
    int REQUEST_CODE_FOLDER = 222;
    TextView txtTT;
    int kt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnThem     = (Button) findViewById(R.id.btnAddEmployee);
        listView    = (ListView) findViewById(R.id.ListView);
        txtTT       = (TextView) findViewById(R.id.txtTT);
        nhanVienArrayList = new ArrayList<>();
        adapter     = new NhanVienAdapter(this, R.layout.dong_nhan_vien, nhanVienArrayList);
        listView.setAdapter(adapter);


        database = new Database(this, "QuanLyNhanVien.sqlite", null, 1);
        //database.QueryData("DROP TABLE NhanVien");
        database.QueryData("CREATE TABLE IF NOT EXISTS ChucVu(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenChucVu VARCHAR(50))");
        database.QueryData("CREATE TABLE IF NOT EXISTS NhanVien(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenNhanVien VARCHAR(150), ChucVu VARCHAR(50), HinhAnh BLOB, Phone INTEGER, MoTa VARCHAR(1000))");
        //database.QueryData("INSERT INTO ChucVu VALUES(null, 'Lãnh Đạo')");
        LoadData();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor2 = database.GetData("SELECT COUNT(*) FROM ChucVu");
                cursor2.moveToFirst();
                kt = cursor2.getInt(0);
                if (kt == 0){
                    DialogThemCV();
                }
                else
                    startActivity(new Intent(MainActivity.this, ThemNhanVienActivity.class));
            }
        });

    }
    public void DialogThemCV(){
        final Dialog dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.them_chuc_vu);
        final EditText edtNameCV  = (EditText) dialog2.findViewById(R.id.edtNameCV);
        Button btnEditCV    = (Button) dialog2.findViewById(R.id.btnEditCV);
        Button btnCancelCV  = (Button) dialog2.findViewById(R.id.btnCancelCV);
        Button btnXoaNV     = (Button) dialog2.findViewById(R.id.btnDeleteCV);
        btnCancelCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        btnEditCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNameCV.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Không để trống tên chức vụ.", Toast.LENGTH_SHORT).show();
                }
                else{
                    database.QueryData("INSERT INTO ChucVu VALUES(null, '"+edtNameCV.getText().toString().trim()+"')");
                    Toast.makeText(MainActivity.this, "Đã thêm.", Toast.LENGTH_SHORT).show();
                    dialog2.dismiss();
                }

            }
        });
        btnXoaNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNameCV.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Không để trống tên chức vụ.", Toast.LENGTH_SHORT).show();
                }
                else{
                    database.QueryData("DELETE FROM ChucVu WHERE TenChucVu = '"+edtNameCV.getText().toString().trim()+"'");
                    Toast.makeText(MainActivity.this, "Đã Xóa.", Toast.LENGTH_SHORT).show();
                    dialog2.dismiss();
                }
            }
        });
        dialog2.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_them, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void LoadData(){
        Cursor cursor = database.GetData("SELECT * FROM NhanVien");
        nhanVienArrayList.clear();
        while (cursor.moveToNext()){
            nhanVienArrayList.add(new NhanVien(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3),
                    cursor.getInt(4),
                    cursor.getString(5)));
        }
        adapter.notifyDataSetChanged();
        Cursor cursor1 = database.GetData("SELECT COUNT (*) FROM NhanVien");
        if(cursor1.moveToFirst()){
            txtTT.setText(cursor1.getInt(0)+"");
        }
        else
            txtTT.setText("0");
        if (nhanVienArrayList.size() >= 1){
            btnThem.setVisibility(View.INVISIBLE);
        }
        else
            btnThem.setVisibility(View.VISIBLE);

    }
    private void LoadDataT(String sql){
        Cursor cursor = database.GetData("SELECT * FROM NhanVien WHERE TenNhanVien LIKE '%" +sql+"%'");
        nhanVienArrayList.clear();
        while (cursor.moveToNext()){
            nhanVienArrayList.add(new NhanVien(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3),
                    cursor.getInt(4),
                    cursor.getString(5)));


        }
        adapter.notifyDataSetChanged();
        Cursor cursor1 = database.GetData("SELECT COUNT (*) FROM NhanVien WHERE TenNhanVien LIKE '%" +sql+"%'");
        if (cursor1.moveToFirst()){
            Toast.makeText(this, "Có " + cursor1.getInt(0) + " kết quả đã tìm thấy!!!", Toast.LENGTH_SHORT).show();
            txtTT.setText(cursor1.getInt(0)+"");
        }
        else{
            Toast.makeText(this, "Không tìm thấy!!!", Toast.LENGTH_SHORT).show();
            txtTT.setText("0");
        }


    }
    public void DialogXoaNV(String tenNV, int sdt, final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn muốn xóa nhân viên:" + tenNV + " có Sđt (84)"+ sdt );
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE FROM NhanVien WHERE Id = '" + id +"'");
                Toast.makeText(MainActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                LoadData();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
    public void DialogFind(){
        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_tim);
        Button btnFind      = (Button) dialog1.findViewById(R.id.btnFind);
        Button btnCancelT   = (Button) dialog1.findViewById(R.id.btnCancelT);
        final EditText edtNameT   = (EditText) dialog1.findViewById(R.id.edtNameTim);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNameT.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên nhân viên.", Toast.LENGTH_SHORT).show();
                }
                else{
                    LoadDataT(edtNameT.getText().toString().trim());
                    dialog1.dismiss();
                }

            }
        });
        btnCancelT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.show();

    }
    public void DialoEdit(String ten, int sdt, String chucVu, final int id, byte[] hinh, String moTa){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit);
        Button btnEdit              = (Button) dialog.findViewById(R.id.btnEdit);
        Button btnCancel            = (Button) dialog.findViewById(R.id.btnCancel);
        imgView                     = (ImageView) dialog.findViewById(R.id.imgView);
        ImageButton imgBtnCamera    = (ImageButton) dialog.findViewById(R.id.imgBtnCamera);
        ImageButton imgBtnFolder    = (ImageButton) dialog.findViewById(R.id.imgBtnFolder);
        final EditText edtName      = (EditText) dialog.findViewById(R.id.editName);
        final EditText edtPosition  = (EditText) dialog.findViewById(R.id.edtPosition);
        final EditText edtPhone     = (EditText) dialog.findViewById(R.id.edtPhone);
        final EditText edtMota      = (EditText) dialog.findViewById(R.id.edtMoTa);
        edtName.setText(ten);
        edtPhone.setText(sdt+ "");
        edtPosition.setText(chucVu);
        edtMota.setText(moTa);

        imgBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        imgBtnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] hinhAnh = byteArrayOutputStream.toByteArray();
                Cursor cursor2 = database.GetData("SELECT COUNT(*) FROM ChucVu WHERE TenChucVu = '"+edtPosition.getText().toString().trim() +"'");
                cursor2.moveToFirst();
                kt = cursor2.getInt(0);
                if(edtName.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "Vui lòng không để trống tên", Toast.LENGTH_SHORT).show();
                }
                else if(edtPosition.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Vui lòng không để Chức vụ", Toast.LENGTH_SHORT).show();
                }
                else if(edtPhone.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Vui lòng không để trống Số điện thoại", Toast.LENGTH_SHORT).show();
                }
                else if(kt == 0){
                    Toast.makeText(MainActivity.this, "Chức vụ bạn nhập không hợp lệ", Toast.LENGTH_SHORT).show();
                }
                else{
                    MainActivity.database.UPDATE_NHANVIEN(edtName.getText().toString().trim(), edtPosition.getText().toString().trim(), hinhAnh, Integer.parseInt(edtPhone.getText().toString().trim()), id, edtMota.getText().toString().trim());
                    Toast.makeText(MainActivity.this, "Đã Sửa", Toast.LENGTH_SHORT).show();
                    LoadData();
                }



            }
        });

        Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
        imgView.setImageBitmap(bitmap);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(bitmap);
        }
        if (requestCode ==REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null)
        {
            Uri uri = data.getData();
            try
            {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgView.setImageBitmap(bitmap);
            }
            catch(FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuAdd){
            Cursor cursor2 = database.GetData("SELECT COUNT(*) FROM ChucVu");
            cursor2.moveToFirst();
            kt = cursor2.getInt(0);
            if (kt == 0){
                DialogThemCV();
            }
            else
            startActivity(new Intent(MainActivity.this, ThemNhanVienActivity.class));
        }
        if (item.getItemId() == R.id.menuT){
            DialogFind();
        }
        if (item.getItemId() == R.id.menuTrangChu){
            LoadData();
        }
        return super.onOptionsItemSelected(item);
    }
}
