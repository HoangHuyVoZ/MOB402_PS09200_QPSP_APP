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

public class RegisterActivity extends AppCompatActivity {
    Button btnCreate;
    EditText edtEmail,edtPass,edtConfirm;
    TextView back;
    Animation translatex,translatey,fadeIn;
    ImageView logo,nen;
    private ProgressDialog pDialog;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.11:4100");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Emitter.Listener onRegister = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String data =  args[0].toString();

            noUI noui = new noUI(RegisterActivity.this);

            if(data == "true"){

                noui.toast("Đăng kí thành công");

                // Launch login activity
                Intent intent = new Intent(
                        RegisterActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }else{
                noui.toast("Đăng kí thất bại");

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mSocket.connect();
        mSocket.on("register", onRegister);
        btnCreate = findViewById(R.id.btnRes);
        nen =findViewById(R.id.nen);
        logo=findViewById(R.id.logo);
        edtEmail= findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        back =findViewById(R.id.back);
        edtConfirm = findViewById(R.id.edtConfirm);
        translatex = AnimationUtils.loadAnimation(this,R.anim.translatex);
        translatey = AnimationUtils.loadAnimation(this,R.anim.translatey);
        fadeIn = AnimationUtils.loadAnimation(this,R.anim.fadein);
        edtConfirm.startAnimation(translatex);
        edtEmail.startAnimation(translatex);
        edtPass.startAnimation(translatex);
        back.startAnimation(translatey);
        btnCreate.startAnimation(translatey);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
//        nen.startAnimation(fadeIn);
//        logo.startAnimation(fadeIn);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPass.getText().toString().trim();
                String confirm = edtConfirm.getText().toString().trim();
                if (  !email.isEmpty() && !password.isEmpty() && !confirm.isEmpty()) {
                    if(password.equals(confirm)){
                        registerUser(email, password);
                    }else {
                        Toast.makeText(getApplicationContext(),
                                "Mật khẩu không trùng nhau !!!", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Vui lòng nhập đầy đủ thông tin !!!", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();

            }
        });
    }
    private void registerUser( final String email,
                              final String password) {


        mSocket.emit("register",email,password );

        pDialog.setMessage("Đang xử lí ...");
        showDialog();
    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()){
            pDialog.dismiss();
            pDialog.cancel();
        }
    }
}
