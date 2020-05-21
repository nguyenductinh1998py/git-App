package com.example.quanlynhanvien;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.BitSet;

public class ThemNhanVienActivity extends AppCompatActivity {
    Button btnAdd, btnCancel;
    ImageView imgView;
    ImageButton imgBtnCamera, imgBtnFolder;
    EditText edtName,edtPhone , edtPosition;

    int REQUEST_CODE_CAMERA = 111;
    int REQUEST_CODE_FOLDER = 222;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhan_vien);
        init();
        events();
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
                MainActivity.database.INSERT_NHANVIEN(edtName.getText().toString().trim(), edtPosition.getText().toString().trim(), hinhAnh, Integer.parseInt(edtPhone.getText().toString().trim()));
                Toast.makeText(ThemNhanVienActivity.this, "Đã Thêm", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ThemNhanVienActivity.this, MainActivity.class));
            }
        });
    }

    private void init() {
        btnAdd          = (Button) findViewById(R.id.btnAdd);
        btnCancel       = (Button) findViewById(R.id.btnCancel);
        imgView         = (ImageView) findViewById(R.id.imgView);
        imgBtnCamera    = (ImageButton) findViewById(R.id.imgBtnCamera);
        imgBtnFolder    = (ImageButton) findViewById(R.id.imgBtnFolder);
        edtName         = (EditText) findViewById(R.id.editName);
        edtPosition     = (EditText) findViewById(R.id.edtPosition);
        edtPhone     = (EditText) findViewById(R.id.edtPhone);
    }
}
