package com.example.quanlynhanvien;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NhanVien nhanVien = nhanVienList.get(position);
        holder.txtName.setText(nhanVien.getTen());
        holder.txtPosition.setText(nhanVien.getChucVu());
        holder.txtPhone.setText("(84)" + nhanVien.getSdt() + "");
        holder.txtMoTa.setText(nhanVien.getMota());
        byte[] hinhAnh = nhanVien.getHinh();
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
        return convertView;
    }
}
