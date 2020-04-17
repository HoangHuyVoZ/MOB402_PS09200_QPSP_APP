package com.huynhps09200.mob402_ps09200.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.huynhps09200.mob402_ps09200.Model.SanPham;
import com.huynhps09200.mob402_ps09200.noUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    Context context;
    String serverUrl = "http://192.168.1.9:4100";
    List<SanPham> list = new ArrayList<SanPham>();


    //Khai bao socket
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(serverUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    //lang nghe insert
    private Emitter.Listener onInsertProduct = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String data =  args[0].toString();

            noUI noui = new noUI(context);

            if(data == "true"){
                Log.i("insert","Insert thanh cong");

                noui.toast("Insert thanh cong");
                noui.capnhatListView();





            }else{

                Log.i("insert","Insert that bai");

                noui.toast("Insert that bai");

            }

        }
    };
    //lang nghe getAll
    private Emitter.Listener onGetAllProduct = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            SanPham sv = new SanPham();

            JSONObject jsonObject = (JSONObject) args[0];
            //parser JSON
            try {
                sv._id = jsonObject.getString("_id");
                sv.Name = jsonObject.getString("name");
                sv.Price=jsonObject.getString("price");
                sv.Description = jsonObject.getString("description");
                sv.Image = jsonObject.getString("image");

                list.add(sv);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            noUI noui = new noUI(context);
            if(!list.isEmpty()){


                Log.i("GetAll","GetAll thanh cong");

//                noui.toast("GetAll thanh cong");

            }else{

                Log.i("GetAll","GetAll that bai");

//                noui.toast("GetAll that bai");
            }


        }
    };



    public ProductDAO(Context context) {
        this.context = context;
        mSocket.connect();
        mSocket.on("insertProduct", onInsertProduct);
        mSocket.on("getProduct", onGetAllProduct);
    }



    public void insert(final SanPham sanPham) {

        mSocket.emit("insertProduct", sanPham.Name,sanPham.Price, sanPham.Description, sanPham.Image);
        Log.i("gg", "insert: "+sanPham.Image);

    }

    public List<SanPham> getAll(){

        list.clear();

        mSocket.emit("getProduct","Client Android get All Product");



        return list;
    }

    public void update(final SanPham sv) {



    }

    public void delete(final String id) {



    }
}
