package com.example.lab5_and103;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab5_and103.adapter.DistributorsRecyclerView;
import com.example.lab5_and103.model.Distributor;
import com.example.lab5_and103.model.Response;
import com.example.lab5_and103.services.HttpRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private HttpRequest request;
    private RecyclerView recyclerView;
    private DistributorsRecyclerView adapter;
    private FloatingActionButton btnAdd;
    private TextInputEditText edtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.floatingBtn);
        edtSearch = findViewById(R.id.edtSearch);
        request = new HttpRequest();
        request.callAPI()
                .getListDistributor()
                .enqueue(getDistributorAPI);
        btnAdd.setOnClickListener(v->{
            showDialog();
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    String key = edtSearch.getText().toString().trim();
                    request.callAPI()
                            .getSearchDistributors(key)
                            .enqueue(getDistributorAPI);
                    return true;
                }
                return false;
            }
        });
    }
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_dialog, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();

        EditText edtNameDistributor = view.findViewById(R.id.edtName);
        Button btnAdd = view.findViewById(R.id.dialogAdd);
        Button btnBack = view.findViewById(R.id.dialogBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                alertDialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(v->{
            if(!edtNameDistributor.getText().toString().isEmpty()){
                Distributor distributor = new Distributor();
                distributor.setName(edtNameDistributor.getText().toString().trim());
                request.callAPI()
                        .addDistributor(distributor)
                        .enqueue(responseCallback);
                alertDialog.dismiss();
            }else {
                Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getData(List<Distributor> distributors){
        adapter = new DistributorsRecyclerView(distributors,this,request,responseCallback);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    Callback<Response<List<Distributor>>> getDistributorAPI= new Callback<Response<List<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<List<Distributor>>> call, retrofit2.Response<Response<List<Distributor>>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    List<Distributor> list = response.body().getData();
                    getData(list);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<List<Distributor>>> call, Throwable t) {
            Log.d("phuocdz",""+t.getMessage());
        }
    };
    Callback<Response<Distributor>> responseCallback = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    request.callAPI()
                            .getListDistributor()
                            .enqueue(getDistributorAPI);
                    Toast.makeText(MainActivity.this, ""+response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable t) {
            Log.d("phuocdz", "onFailure: "+t.getMessage());
        }
    };
}