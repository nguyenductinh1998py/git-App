package com.example.quanlynhanvien;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ThemNhanVienActivity extends AppCompatActivity {
    Button btnAdd, btnCancel;
    ImageView imgView;
    ImageButton imgBtnCamera, imgBtnFolder, imgList;
    EditText edtName,edtPhone , edtPosition, edtMoTa;
    Database database;
    int kt= 0;

    int REQUEST_CODE_CAMERA = 111;
    int REQUEST_CODE_FOLDER = 222;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhan_vien);
        database = new Database(this, "QuanLyNhanVien.sqlite", null, 1);
        init();
        events();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_them_chucvu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuthemchucvu){
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
                        Toast.makeText(ThemNhanVienActivity.this, "Không để trống tên chức vụ.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        database.QueryData("INSERT INTO ChucVu VALUES(null, '"+edtNameCV.getText().toString().trim()+"')");
                        Toast.makeText(ThemNhanVienActivity.this, "Đã thêm.", Toast.LENGTH_SHORT).show();
                        dialog2.dismiss();
                    }

                }
            });
            btnXoaNV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edtNameCV.getText().toString().trim().equals("")){
                        Toast.makeText(ThemNhanVienActivity.this, "Không để trống tên chức vụ.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        database.QueryData("DELETE FROM ChucVu WHERE TenChucVu = '"+edtNameCV.getText().toString().trim()+"'");
                        Toast.makeText(ThemNhanVienActivity.this, "Đã Xóa.", Toast.LENGTH_SHORT).show();
                        dialog2.dismiss();
                    }
                }
            });
            dialog2.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialodSpiner() {
        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.chon_chucvu);
        final Spinner spinner = (Spinner) dialog1.findViewById(R.id.spinner3);
        final ArrayList<String> arrayChucVu = new ArrayList<String>();
        Cursor cursor = database.GetData("SELECT TenChucVu FROM ChucVu");
        arrayChucVu.clear();
        while (cursor.moveToNext()){
            arrayChucVu.add(cursor.getString(0));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arrayChucVu);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtPosition.setText(arrayChucVu.get(position));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        dialog1.show();
    }

    @Override
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

    private void events() {

        imgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialodSpiner();
            }
        });
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
        btnAdd.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(ThemNhanVienActivity.this, "Vui lòng không để trống tên", Toast.LENGTH_SHORT).show();

                }
                else if(edtPosition.getText().toString().trim().equals("")){
                    Toast.makeText(ThemNhanVienActivity.this, "Vui lòng không để Chức vụ", Toast.LENGTH_SHORT).show();
                }
                else if(edtPhone.getText().toString().trim().equals("")){
                    Toast.makeText(ThemNhanVienActivity.this, "Vui lòng không để trống Số điện thoại", Toast.LENGTH_SHORT).show();
                }
                else if(kt == 0){
                    Toast.makeText(ThemNhanVienActivity.this, "Chức vụ bạn nhập không hợp lệ", Toast.LENGTH_SHORT).show();
                }
                else {
                    MainActivity.database.INSERT_NHANVIEN(edtName.getText().toString().trim(), edtPosition.getText().toString().trim(), hinhAnh, Integer.parseInt(edtPhone.getText().toString().trim()), edtMoTa.getText().toString().trim());
                    Toast.makeText(ThemNhanVienActivity.this, "Đã Thêm", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ThemNhanVienActivity.this, MainActivity.class));
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThemNhanVienActivity.this, MainActivity.class));
            }
        });
    }

    private void init() {
        btnAdd          = (Button) findViewById(R.id.btnEdit);
        btnCancel       = (Button) findViewById(R.id.btnCancel);
        imgView         = (ImageView) findViewById(R.id.imgView);
        imgBtnCamera    = (ImageButton) findViewById(R.id.imgBtnCamera);
        imgBtnFolder    = (ImageButton) findViewById(R.id.imgBtnFolder);
        imgList         = (ImageButton) findViewById(R.id.imageButton);
        edtName         = (EditText) findViewById(R.id.editName);
        edtPosition     = (EditText) findViewById(R.id.edtPosition);
        edtPhone        = (EditText) findViewById(R.id.edtPhone);
        edtMoTa         = (EditText) findViewById(R.id.edtMoTa);

    }
}
