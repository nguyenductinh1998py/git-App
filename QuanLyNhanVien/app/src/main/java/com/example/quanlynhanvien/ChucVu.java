package com.example.quanlynhanvien;

public class ChucVu {
    private int Id;

    public ChucVu(int id, String chucVuname) {
        Id = id;
        ChucVuname = chucVuname;
    }

    private String ChucVuname;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getChucVuname() {
        return ChucVuname;
    }

    public void setChucVuname(String chucVuname) {
        ChucVuname = chucVuname;
    }
}
