package com.huynhps09200.mob402_ps09200.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.huynhps09200.mob402_ps09200.DAO.ProductDAO;
import com.huynhps09200.mob402_ps09200.MainActivity;
import com.huynhps09200.mob402_ps09200.Model.SanPham;
import com.huynhps09200.mob402_ps09200.R;
import com.huynhps09200.mob402_ps09200.UpdateProduct;
import com.huynhps09200.mob402_ps09200.noUI;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerView_Sanpham extends RecyclerView.Adapter<RecyclerView_Sanpham.ViewHolder>{
    ArrayList<SanPham> list;
    Context context;
    ProductDAO productDAO;
    noUI noUI;



    public RecyclerView_Sanpham( Context context,ArrayList<SanPham> list) {
        this.list = list;
        this.context = context;
        productDAO=new ProductDAO(context);
        noUI = new noUI(context);
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final SanPham sanPham = list.get(i);
        final String _id= sanPham._id;
        viewHolder.tvName.setText(sanPham.getName());
        viewHolder.tvPrice.setText(sanPham.getPrice()+" VNĐ");
        viewHolder.tvDescription.setText(sanPham.getDescription());
        Picasso.get().load("http://192.168.1.11:4100/"+sanPham.getImage()).into(viewHolder.image);
        viewHolder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateProduct.class);
                Bundle bundle = new Bundle();
// đóng gói kiểu dữ liệu String, Boolean
                bundle.putString("_id",sanPham._id);
                bundle.putString("name",sanPham.getName());
                bundle.putString("price",sanPham.getPrice());
                bundle.putString("des",sanPham.getDescription());
                bundle.putString("image",sanPham.getImage());
// đóng gói bundle vào intent
                intent.putExtras(bundle);
// start SecondActivity
                context.startActivity(intent);

            }
        });
        viewHolder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+_id, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc muốn xóa sản phẩm !!!");
                builder.setCancelable(false);

                builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(context, "Xóa không thành công ", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Xóa thành công ", Toast.LENGTH_LONG).show();
                        productDAO.delete(_id);
                        list.clear();
                        list.addAll(productDAO.getAll());

                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();

            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView tvName,tvDescription,tvPrice;
        Button btnXoa,btnSua;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            tvName=itemView.findViewById(R.id.name);
            tvPrice=itemView.findViewById(R.id.price);
            tvDescription=itemView.findViewById(R.id.mota);
            btnSua=itemView.findViewById(R.id.btnSua);
            btnXoa=itemView.findViewById(R.id.btnXoa);

        }
    }




}

