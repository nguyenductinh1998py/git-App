package com.example.quanlynhanvien;

public class NhanVien {
    private int Id;
    private String Ten;
    private String ChucVu;
    private int Sdt;

    public int getSdt() {
        return Sdt;
    }

    public void setSdt(int sdt) {
        Sdt = sdt;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getChucVu() {
        return ChucVu;
    }

    public void setChucVu(String chucVu) {
        ChucVu = chucVu;
    }

    public byte[] getHinh() {
        return Hinh;
    }

    public void setHinh(byte[] hinh) {
        Hinh = hinh;
    }

    private byte[] Hinh;

    public NhanVien(int id, String ten, String chucVu, byte[] hinh, int sdt) {
        Id = id;
        Ten = ten;
        ChucVu = chucVu;
        Sdt = sdt;
        Hinh = hinh;
    }
}
