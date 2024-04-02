package com.example.lab5_and103;

import static okhttp3.MediaType.parse;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lab5_and103.adapter.Distributors_Spinner;
import com.example.lab5_and103.adapter.FruitsRecyclerView;
import com.example.lab5_and103.data.ChooseImage;
import com.example.lab5_and103.model.Distributor;
import com.example.lab5_and103.model.Fruit;
import com.example.lab5_and103.model.Response;
import com.example.lab5_and103.services.HttpRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Fruits extends AppCompatActivity {
    private HttpRequest request;
    private RecyclerView recyclerView;
    private FruitsRecyclerView adapter;
    private FloatingActionButton btnAdd;
    private TextInputEditText edtSearch;
    private SharedPreferences sharedPreferences;
    private String token;
    private EditText edtName,edtQuantity,edtPrice,edtStatus,edtDesc;
    private ImageView image_fruit1,image_fruit2,image_fruit3;
    private Spinner spDistributors;
    private List<Distributor> listDistributors;
    private Distributors_Spinner distributorsSpinner;
    private final ArrayList<Uri> selectedImageUris = new ArrayList<>();
    private ChooseImage chooseImage;
    private File file;
    private String id_distributor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruits);
        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.floatingBtn);
        edtSearch = findViewById(R.id.edtSearch);
        request = new HttpRequest();
        chooseImage = new ChooseImage(this);
        sharedPreferences = getSharedPreferences("INFO",MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
        request.callAPI().getListFruit("Bearer "+token).enqueue(responseDataList);
        request.callAPI().getListDistributor().enqueue(responseGetDistributor);
        btnAdd.setOnClickListener(v->{
            showDialog();
        });
    }

    private void getData(ArrayList<Fruit> fruits){
        adapter = new FruitsRecyclerView(fruits,this,request,listDistributors,chooseImage);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_fruits_dialog, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        edtName = view.findViewById(R.id.edtName);
        edtQuantity = view.findViewById(R.id.edtQuantity);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtStatus = view.findViewById(R.id.edtStatus);
        edtDesc = view.findViewById(R.id.edtDescription);
        spDistributors = view.findViewById(R.id.spDistributors);
        image_fruit1 = view.findViewById(R.id.image_fruit1);
        image_fruit2 = view.findViewById(R.id.image_fruit2);
        image_fruit3 = view.findViewById(R.id.image_fruit3);
        spDistributors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Distributor distributor = (Distributor)parent.getAdapter().getItem(position);
                id_distributor = distributor.getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        LinearLayout btnPickImage = view.findViewById(R.id.pickImage);
        btnPickImage.setOnClickListener(v->{
            chooseImage.chooseImages(getImageLauncher);
        });
        distributorsSpinner = new Distributors_Spinner(listDistributors,this);
        spDistributors.setAdapter(distributorsSpinner);
        Button btnAdd = view.findViewById(R.id.btn_Add);
        Button btnBack = view.findViewById(R.id.btn_Back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                alertDialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(v->{
            String name= edtName.getText().toString();
            String quantity=edtQuantity.getText().toString();
            String price =edtPrice.getText().toString();
            String status =edtStatus.getText().toString();
            String description=edtDesc.getText().toString();
            requestBodyMap.put("name",getRequestBody(name));
            requestBodyMap.put("quantity",getRequestBody(quantity));
            requestBodyMap.put("price",getRequestBody(price));
            requestBodyMap.put("status",getRequestBody(status));
            requestBodyMap.put("description",getRequestBody(description));
            requestBodyMap.put("id_distributor",getRequestBody(id_distributor));
            ArrayList<MultipartBody.Part> lsImage = new ArrayList<>();
            RequestBody _image= RequestBody.create(MediaType.parse("image/*"),file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("image",file.getName(),_image);
            lsImage.add(part);
            request.callAPI().addFruitWithImage(requestBodyMap,lsImage).enqueue(new Callback<Response<Fruit>>() {
                @Override
                public void onResponse(Call<Response<Fruit>> call, retrofit2.Response<Response<Fruit>> response) {
                    if(response.isSuccessful()){
                        if(response.body().getStatus()==200){
                            Toast.makeText(Fruits.this, response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            request.callAPI().getListFruit("Bearer "+token).enqueue(responseDataList);

                        }
                    }
                }

                @Override
                public void onFailure(Call<Response<Fruit>> call, Throwable t) {

                }
            });


        });
    }
    private RequestBody getRequestBody(String value){
        return RequestBody.create(parse("multipart/form-data"),value);
    }
    Callback<Response<List<Distributor>>> responseGetDistributor = new Callback<Response<List<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<List<Distributor>>> call, retrofit2.Response<Response<List<Distributor>>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    listDistributors = response.body().getData();

                }
            }
        }

        @Override
        public void onFailure(Call<Response<List<Distributor>>> call, Throwable t) {
            Log.d("duc143", "onFailure: "+t.getMessage());
        }
    };
    Callback<Response<ArrayList<Fruit>>> responseDataList = new Callback<Response<ArrayList<Fruit>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
            if(response.isSuccessful()) {
                if(response.body().getStatus() == 200) {
                    ArrayList<Fruit> data = response.body().getData();
                    getData(data);
                    Log.d("duc209", "onResponse: "+data.size());
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {
            Log.d("GetListFruit", "onFailure: "+t.getMessage());
        }
    };

    ActivityResultLauncher<Intent> getImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            ClipData clipData = data.getClipData();
                            if (clipData != null) {
                                int count = clipData.getItemCount();
                                for (int i = 0; i < count; i++) {
                                    Uri imageUri = clipData.getItemAt(i).getUri();
                                    selectedImageUris.add(imageUri);
                                    file = chooseImage.createFileFromUri(imageUri, "image");
                                    Glide.with(Fruits.this)
                                            .load(imageUri)
                                            .thumbnail(Glide.with(Fruits.this).load(R.mipmap.loading))
                                            .centerCrop()
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .into(getImageViewById(i + 1));
                                }
                            } else {
                                Uri imagePath = data.getData();
                                selectedImageUris.add(imagePath);
                                file = chooseImage.createFileFromUri(imagePath, "image");
                                Glide.with(Fruits.this)
                                        .load(imagePath)
                                        .thumbnail(Glide.with(Fruits.this).load(R.mipmap.loading))
                                        .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(image_fruit1);
                            }
                        }
                    }
                }
            });

    private ImageView getImageViewById(int index) {
        switch (index) {
            case 1:
                return image_fruit1;
            case 2:
                return image_fruit2;
            case 3:
                return image_fruit3;
            default:
                return null;
        }
    }
}