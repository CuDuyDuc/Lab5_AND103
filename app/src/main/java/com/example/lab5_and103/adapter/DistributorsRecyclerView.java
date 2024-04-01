package com.example.lab5_and103.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_and103.R;
import com.example.lab5_and103.model.Distributor;
import com.example.lab5_and103.services.HttpRequest;

import java.util.List;

import retrofit2.Callback;

public class DistributorsRecyclerView extends RecyclerView.Adapter<DistributorsRecyclerView.DistributorHolder> {
    private List<Distributor> distributors;
    private Context context;
    private HttpRequest request;
    private Callback responseCallBack;

    public DistributorsRecyclerView(List<Distributor> distributors, Context context, HttpRequest request, Callback responseCallBack) {
        this.distributors = distributors;
        this.context = context;
        this.request = request;
        this.responseCallBack = responseCallBack;
    }

    @NonNull
    @Override
    public DistributorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.distributor_recycler_view,parent,false);
        return new DistributorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistributorHolder holder, int position) {
        Distributor distributor = distributors.get(position);
        if(distributor==null) return;
        holder.name.setText(distributor.getName());
        holder.btnDelete.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn có muốn xóa ?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    request.callAPI()
                            .getDeleteDistributor(distributor.getId())
                            .enqueue(responseCallBack);


                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        holder.btnUpdate.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater =((Activity)context).getLayoutInflater();
            View view = inflater.inflate(R.layout.update_dialog, null);
            builder.setView(view);

            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.setCancelable(false);
            alertDialog.show();

            EditText edtNameDistributor = view.findViewById(R.id.edtName);
            Button btnUpdate = view.findViewById(R.id.dialogUpdate);
            Button btnBack = view.findViewById(R.id.dialogBack);
            edtNameDistributor.setText(distributor.getName());
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    alertDialog.dismiss();
                }
            });
            btnUpdate.setOnClickListener(v1->{
                if(!edtNameDistributor.getText().toString().isEmpty()){
                    Distributor dataDistributor = new Distributor();
                    dataDistributor.setName(edtNameDistributor.getText().toString().trim());
                    request.callAPI()
                            .getUpdateDistributor(distributor.getId(),dataDistributor)
                            .enqueue(responseCallBack);
                    alertDialog.dismiss();
                }else {
                    Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return distributors!=null?distributors.size():0;
    }

    public class DistributorHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private Button btnDelete, btnUpdate;
        public DistributorHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvTenSP);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }
}
