package com.huynhps09200.mob402_ps09200.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huynhps09200.mob402_ps09200.DAO.ProductDAO;
import com.huynhps09200.mob402_ps09200.Model.SanPham;
import com.huynhps09200.mob402_ps09200.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView_Sanpham extends RecyclerView.Adapter<RecyclerView_Sanpham.ViewHolder>{
    ArrayList<SanPham> list;
    Context context;
    ProductDAO productDAO;


    public RecyclerView_Sanpham( Context context,ArrayList<SanPham> list) {
        this.list = list;
        this.context = context;
        productDAO=new ProductDAO(context);
    }
//    private List<String> data = new ArrayList<>();

//    public RecyclerView_Sanpham(List<String> data) {
//        this.data = data;
//    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View itemView=layoutInflater.inflate(R.layout.sanpham_view,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final SanPham sanPham = list.get(i);
        viewHolder.tvName.setText(sanPham.getName());
        viewHolder.tvPrice.setText(sanPham.getPrice()+" VNĐ");
        viewHolder.tvDescription.setText(sanPham.getDescription());
        Picasso.get().load("http://192.168.1.10:4100/"+sanPham.getImage()).into(viewHolder.image);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView tvName,tvDescription,tvPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            tvName=itemView.findViewById(R.id.name);
            tvPrice=itemView.findViewById(R.id.price);
            tvDescription=itemView.findViewById(R.id.mota);

        }
    }

}

