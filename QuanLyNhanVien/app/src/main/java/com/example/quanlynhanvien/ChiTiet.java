package com.example.quanlynhanvien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChiTiet extends AppCompatActivity {
    ImageView nvImageView;
    TextView textName, textChucVu, textSDT, textMoTa;
    String txt1, txt2, txt3, txt4;
    byte[] bytes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        nvImageView = (ImageView) findViewById(R.id.NhanVienImageView);
        textName = (TextView) findViewById(R.id.textName);
        textChucVu = (TextView) findViewById(R.id.textChucVu);
        textSDT = (TextView) findViewById(R.id.textSDT);
        textMoTa = (TextView) findViewById(R.id.textMoTa);
        setData();
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_ac2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuTrangChu2){
            startActivity(new Intent(ChiTiet.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        if (getIntent().hasExtra("text1") && getIntent().hasExtra("text2") && getIntent().hasExtra("text3") && getIntent().hasExtra("nvImage") && getIntent().hasExtra("txtMoTa")){
            txt1 = getIntent().getStringExtra("text1");
            txt2 = getIntent().getStringExtra("text2");
            txt3 = getIntent().getStringExtra("text3");
            bytes = getIntent().getByteArrayExtra("nvImage");
            txt4 = getIntent().getStringExtra("txtMoTa");
        }
        else {
            Toast.makeText(this, "Không có dữ liệu.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData() {
        textName.setText(txt1);
        textChucVu.setText(txt2);
        textSDT.setText(txt3);
        textMoTa.setText(txt4);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        nvImageView.setImageBitmap(bitmap);
    }

}
