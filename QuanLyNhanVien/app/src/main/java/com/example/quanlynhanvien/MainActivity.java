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
        LoadData();
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
    private void LoadData(){
        Cursor cursor = database.GetData("SELECT * FROM NhanVien");
        while (cursor.moveToNext()){
            nhanVienArrayList.add(new NhanVien(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3),
                    cursor.getInt(4)));


        }
        adapter.notifyDataSetChanged();
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
                startActivity(new Intent(MainActivity.this, ThemNhanVienActivity.class));

            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
    public void DialoEdit(String ten, int sdt, String chucVu, final int id, byte[] hinh){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit);
        Button btnEdit              = (Button) dialog.findViewById(R.id.btnEdit);
        Button btnCancel            = (Button) dialog.findViewById(R.id.btnCancel);
        imgView           = (ImageView) dialog.findViewById(R.id.imgView);
        ImageButton imgBtnCamera    = (ImageButton) dialog.findViewById(R.id.imgBtnCamera);
        ImageButton imgBtnFolder    = (ImageButton) dialog.findViewById(R.id.imgBtnFolder);
        final EditText edtName            = (EditText) dialog.findViewById(R.id.editName);
        final EditText edtPosition        = (EditText) dialog.findViewById(R.id.edtPosition);
        final EditText edtPhone           = (EditText) dialog.findViewById(R.id.edtPhone);

        edtName.setText(ten);
        edtPhone.setText(sdt+ "");
        edtPosition.setText(chucVu);
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
                database.QueryData("DELETE FROM NhanVien WHERE Id = '" + id +"'");
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] hinhAnh = byteArrayOutputStream.toByteArray();
                MainActivity.database.INSERT_NHANVIEN(edtName.getText().toString().trim(), edtPosition.getText().toString().trim(), hinhAnh, Integer.parseInt(edtPhone.getText().toString().trim()));
                Toast.makeText(MainActivity.this, "Đã Sửa", Toast.LENGTH_SHORT).show();
                LoadData();
                startActivity(new Intent(MainActivity.this, ThemNhanVienActivity.class));

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
            startActivity(new Intent(MainActivity.this, ThemNhanVienActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
