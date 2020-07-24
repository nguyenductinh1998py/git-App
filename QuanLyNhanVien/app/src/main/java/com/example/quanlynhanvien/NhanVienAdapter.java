package com.example.quanlynhanvien;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class NhanVienAdapter extends BaseAdapter {
    private MainActivity context;
    private  int layout;
    private List<NhanVien> nhanVienList;

    public NhanVienAdapter(MainActivity context, int layout, List<NhanVien> nhanVienList) {
        this.context = context;
        this.layout = layout;
        this.nhanVienList = nhanVienList;
    }
    public String getSafeSubstring(String s, int maxLength){
        String a = s;
        String [] works = a.split(" ");
        if(!TextUtils.isEmpty(s)){
            if(s.length() >= maxLength){
                if (works.length >= 3)
                    a = works[0]+ " " + works[1] + " "+ works[2] + "...";
                else if (works.length == 2)
                    a = works[0]+ " " + works[1]  + "...";
                else if (works.length <= 1)
                    a = s.substring(0, 9) + "...";
            }
        }
        return a;
    }
    public String getSafeSubstringCV(String s, int maxLength){
        String a = "";
        String [] works = s.split(" ");
        if(!TextUtils.isEmpty(s)){
            if(s.length() >= maxLength){
                for (int i = 0; i < works.length; i++){

                    if (i == works.length - 1){
                        a += works[i];
                    }
                    else
                        a += works[i].charAt(0)+". ";
                }
            }
            else
                return s;
        }
        return a;
    }
    public String getSafeSubstringTen(String s, int maxLength){
        String a = "";
        String [] works = s.split(" ");
        if(!TextUtils.isEmpty(s)){
            if(s.length() >= maxLength){
                for (int i = 0; i < works.length; i++){

                    if (i == works.length - 1){
                        a += works[i];
                    }
                    else
                        a += works[i].charAt(0)+". ";
                }
            }
            else
                return s;
        }
        return a;
    }
    @Override
    public int getCount() {
        return nhanVienList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private  class  ViewHolder{
        TextView txtName, txtPhone, txtPosition, txtMoTa;
        ImageView imgPicture, imgDelete, imgEdit;
        ConstraintLayout dongnhanvien;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null );
            holder.txtName      = (TextView) convertView.findViewById(R.id.txtname);
            holder.txtPosition  = (TextView) convertView.findViewById(R.id.txtPosition);
            holder.txtPhone     = (TextView) convertView.findViewById(R.id.txtPhone);
            holder.imgPicture   = (ImageView) convertView.findViewById(R.id.imgView);
            holder.imgDelete    =(ImageView) convertView.findViewById(R.id.imgDelete);
            holder.imgEdit      = (ImageView) convertView.findViewById(R.id.imgEdit);
            holder.txtMoTa      = (TextView) convertView.findViewById(R.id.txtMoTa);
            holder.dongnhanvien = (ConstraintLayout) convertView.findViewById(R.id.dong_nhan_vien);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NhanVien nhanVien = nhanVienList.get(position);
        holder.txtName.setText(getSafeSubstringTen(nhanVien.getTen(), 15));
        holder.txtPosition.setText(getSafeSubstringCV(nhanVien.getChucVu(), 15));
        holder.txtPhone.setText("(84)" + nhanVien.getSdt() + "");
        holder.txtMoTa.setText(getSafeSubstring(nhanVien.getMota(), 10));
        final byte[] hinhAnh = nhanVien.getHinh();
        final Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
        holder.imgPicture.setImageBitmap(bitmap);
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.DialoEdit(nhanVien.getTen(), nhanVien.getSdt(), nhanVien.getChucVu(), nhanVien.getId(), nhanVien.getHinh(), nhanVien.getMota());
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.DialogXoaNV(nhanVien.getTen(), nhanVien.getSdt(), nhanVien.getId()
                );
            }
        });
        holder.dongnhanvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTiet.class);
                intent.putExtra("text1", nhanVien.getTen());
                intent.putExtra("text2", nhanVien.getChucVu());
                intent.putExtra("text3", "(84)" + nhanVien.getSdt() + "");
                intent.putExtra("nvImage", hinhAnh);
                intent.putExtra("txtMoTa", nhanVien.getMota());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
