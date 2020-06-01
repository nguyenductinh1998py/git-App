package com.example.quanlynhanvien;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public void  QueryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    public void INSERT_NHANVIEN(String ten, String chucVu, byte[] hinh, int sdt, String moTa){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO NhanVien VALUES(null, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, ten);
        statement.bindString(2, chucVu);
        statement.bindBlob(3, hinh);
        statement.bindLong(4, sdt);
        statement.bindString(5, moTa);
        statement.executeInsert();
    }
    public void UPDATE_NHANVIEN(String ten, String chucVu, byte[] hinh, int sdt, int id, String moTa){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE NhanVien SET TenNhanVien = ?, ChucVu = ?, HinhAnh = ?, Phone = ?, MoTa = ? WHERE Id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, ten);
        statement.bindString(2, chucVu);
        statement.bindBlob(3, hinh);
        statement.bindLong(4, sdt);
        statement.bindString(5, moTa);
        statement.bindLong(6, id);
        statement.executeInsert();
    }

    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
