package com.example.lab5_and103;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lab5_and103.model.Response;
import com.example.lab5_and103.model.User;
import com.example.lab5_and103.services.HttpRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Register extends AppCompatActivity {
    private EditText edtUserName, edtPassword, edtEmail, edtName;
    private Button btn_Register;
    private ImageView imgAvatar;
    private HttpRequest httpRequest;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Mapping();
        httpRequest = new HttpRequest();
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Register.this, "click", Toast.LENGTH_SHORT).show();
                // sử dụng RequestBody
                RequestBody _username = RequestBody.create(MediaType.parse("multipart/form-data"), edtUserName.getText().toString().trim());
                RequestBody _password = RequestBody.create(MediaType.parse("multipart/form-data"), edtPassword.getText().toString().trim());
                RequestBody _email = RequestBody.create(MediaType.parse("multipart/form-data"), edtEmail.getText().toString().trim());
                RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"), edtName.getText().toString().trim());
                MultipartBody.Part multipartBody;
                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                    // avatar là cùng tên với key trong multipart
                } else {
                    multipartBody = null;
                }
                httpRequest.callAPI().register(_username, _password, _email, _name, multipartBody).enqueue(responseUser);
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    private void chooseImage() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getImage.launch(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // there are no request codes
                Intent data = result.getData();
                Uri imagePath = data.getData();
                // file avatar bien global
                file = createFileFromUri(imagePath, "avarta");
                // glide de load hinh
                Glide.with(Register.this)
                        .load(file) // load file hinh
                        .thumbnail(Glide.with(Register.this).load(R.drawable.loading))
                        .centerCrop() // center cắt ảnh
                        .circleCrop() // bo tròn ảnh
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // clear cache
                        .skipMemoryCache(true)
                        .into(imgAvatar);
            }
        }
    });

    // hàm tạo file hình từ Uri
    private File createFileFromUri(Uri path, String name) {
        File _file = new File(Register.this.getCacheDir(), name + ".png");
        try {
            InputStream in = Register.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    Callback<Response<User>> responseUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response.isSuccessful()){
                // check status code
                if (response.body().getStatus() == 200){
                    Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this, Login.class));
                }
            }
        }
        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            Log.d(">>> GetListDistributor", "onFailure" + t.getMessage());
        }
    };

    public void Mapping() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        btn_Register = findViewById(R.id.btn_Register);
        imgAvatar = findViewById(R.id.imgAvatar);
    }
}