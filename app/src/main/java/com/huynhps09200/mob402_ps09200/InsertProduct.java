package com.huynhps09200.mob402_ps09200;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.huynhps09200.mob402_ps09200.DAO.ProductDAO;
import com.huynhps09200.mob402_ps09200.Model.SanPham;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;


public class InsertProduct extends AppCompatActivity {
    TextView btnHuy,btnLuu;
    EditText edtName,edtPrice,edtDes;
    ImageView image;
    int REQUEST_CODE_FOLDER=432;
    Uri mFilePathUri;
    ProductDAO productDAO;
    SanPham sanPham;
    byte[] byteArray;
    ApiService apiService;
    String imagefile;
    String img="";


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

        initRetrofitClient();

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
//                uploadImage();


                sanPham= new SanPham();
                sanPham.Name=edtName.getText().toString();
                sanPham.Price=edtPrice.getText().toString();
                sanPham.Description=edtDes.getText().toString();

                sanPham.Image= imagefile;
                if(!sanPham.Name.isEmpty() && !sanPham.Price.isEmpty() && !sanPham.Description.isEmpty()){
                    //them student
                    productDAO.insert(sanPham);
                    Toast.makeText(InsertProduct.this, "Thêm dữ liệu thành công !!!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(InsertProduct.this,
//                            MainActivity.class);
//                    startActivity(intent);
//                    finish();
                    MainActivity mainActivity= new MainActivity();
                    mainActivity.list.clear();
                    mainActivity.list.addAll(productDAO.getAll());
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
                 byteArray = byteArrayOutputStream.toByteArray();
                img = Base64.encodeToString(byteArray, Base64.DEFAULT);
                multipartImageUpload();

            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void multipartImageUpload() {
        try {
            File filesDir = getApplicationContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");




            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteArray);
            fos.flush();
            fos.close();


            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("uploadd", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "uploadd");

            Call<ResponseBody> req = apiService.postImage(body, name);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 200) {

//                        textView.setText("Uploaded Successfully!");
//                        textView.setTextColor(Color.BLUE);
                    }

                    Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    textView.setText("Uploaded Failed!");
//                    textView.setTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }

            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void initRetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        apiService = new Retrofit.Builder().baseUrl("http://192.168.1.11:4100").client(client).build().create(ApiService.class);
    }


}
