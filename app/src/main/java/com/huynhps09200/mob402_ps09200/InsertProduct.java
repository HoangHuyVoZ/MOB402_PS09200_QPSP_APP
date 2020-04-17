package com.huynhps09200.mob402_ps09200;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huynhps09200.mob402_ps09200.DAO.ProductDAO;
import com.huynhps09200.mob402_ps09200.Model.SanPham;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class InsertProduct extends AppCompatActivity {
    TextView btnHuy,btnLuu;
    EditText edtName,edtPrice,edtDes;
    ImageView image;
    int REQUEST_CODE_FOLDER=432;
    Uri mFilePathUri;
    ProductDAO productDAO;
    SanPham sanPham;
    String img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);
        btnHuy=findViewById(R.id.tvHuy);
        btnLuu=findViewById(R.id.tvLuu);
        edtName=findViewById(R.id.edtName);
        edtPrice=findViewById(R.id.edtPrice);
        edtDes=findViewById(R.id.edtDes);
        productDAO = new ProductDAO(this);
        image=findViewById(R.id.Image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               chooseImage();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(InsertProduct.this,
//                        MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sanPham= new SanPham();

                sanPham.Name=edtName.getText().toString();
                sanPham.Price=edtPrice.getText().toString();
                sanPham.Description=edtDes.getText().toString();
                sanPham.Image= img;
                Toast.makeText(InsertProduct.this, ""+sanPham.Image, Toast.LENGTH_SHORT).show();

                if(!sanPham.Name.isEmpty() && !sanPham.Price.isEmpty() && !sanPham.Description.isEmpty()){
                    //them student
                    productDAO.insert(sanPham);
                    Toast.makeText(InsertProduct.this, "Thêm dữ liệu thành công !!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(InsertProduct.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();


                }else{
                    Toast.makeText(InsertProduct.this, "Vui lòng nhập đầy đủ thông tin!!!", Toast.LENGTH_SHORT).show();
                }

            }


        });
    }
    private void chooseImage() {
        Intent intent= new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select image"),
                REQUEST_CODE_FOLDER);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_FOLDER
                && resultCode== RESULT_OK
                && data != null
                && data.getData()!= null){
            mFilePathUri= data.getData();
            try {
                //getting select media
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),mFilePathUri);

                //getting select image intobitmap
                image.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                img = Base64.encodeToString(byteArray, Base64.DEFAULT);
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
