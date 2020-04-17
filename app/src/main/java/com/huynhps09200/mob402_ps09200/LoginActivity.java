package com.huynhps09200.mob402_ps09200;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;



public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText edtEmail,edtPass;
    TextView res;
    Animation translatex,translatey,fadeIn;
    ImageView logo,nen;
    public ProgressDialog pDialog;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.10:4100");
        } catch (URISyntaxException e) {}
    }

    // lang nghe su kien login va xu ly
    public Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String data =  args[0].toString();
            noUI noui = new noUI(LoginActivity.this);
            if(data == "true"){
                noui.toast("Đăng nhập thành công");
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                noui.toast("Đăng nhập thất bại");
            }
            hideDialog();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocket.connect();
        mSocket.on("login", onLogin);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        nen =findViewById(R.id.nen);
        logo=findViewById(R.id.logo);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail= findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        res =findViewById(R.id.TvRes);
        translatex = AnimationUtils.loadAnimation(this,R.anim.translatex);
        translatey = AnimationUtils.loadAnimation(this,R.anim.translatey);
        fadeIn = AnimationUtils.loadAnimation(this,R.anim.fadein);

        edtEmail.startAnimation(translatex);
        edtPass.startAnimation(translatex);
        res.startAnimation(translatey);
        btnLogin.startAnimation(translatey);
//        nen.startAnimation(fadeIn);
//        logo.startAnimation(fadeIn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPass.getText().toString().trim();
                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Vui lòng nhập đủ email và mật khẩu", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
       res.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
               startActivity(intent);
           }
       });
    }


    public void checkLogin(final String email, final String password) {
        // Tag used to cancel the request

        pDialog.setMessage("Đang xử lí ...");
        showDialog();

        mSocket.emit("login", email, password);

    }

    public void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
