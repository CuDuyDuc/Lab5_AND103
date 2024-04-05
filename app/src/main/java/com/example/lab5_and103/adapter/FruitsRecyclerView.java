package com.example.lab5_and103.adapter;

import static com.example.lab5_and103.services.ApiServices.IP;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab5_and103.R;
import com.example.lab5_and103.data.ChooseImage;
import com.example.lab5_and103.model.Distributor;
import com.example.lab5_and103.model.Fruit;
import com.example.lab5_and103.services.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class FruitsRecyclerView extends RecyclerView.Adapter<FruitsRecyclerView.FruitsHolder> {
    private ArrayList<Fruit> listFruits;
    private Context context;
    private  FruitClick fruitClick;


    public FruitsRecyclerView(ArrayList<Fruit> listFruits, Context context, FruitClick fruitClick) {
        this.listFruits = listFruits;
        this.context = context;
        this.fruitClick = fruitClick;
    }

    public interface FruitClick {
        void delete(Fruit fruit);
        void edit(Fruit fruit);

    }

    @NonNull
    @Override
    public FruitsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fruits_recyclerview,parent,false);
        return new FruitsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FruitsHolder holder, int position) {
        Fruit fruit = listFruits.get(position);
        if(fruit == null) return;
        Log.d("duc48", "onBindViewHolder: "+fruit.getName());
        holder.tvName.setText(fruit.getName());
        holder.tvPrice.setText(fruit.getPrice());
        Log.d("fafafa", "onBindViewHolder: "+fruit.getImage());
        if (!fruit.getImage().isEmpty()) {
            String url = fruit.getImage().get(0);
            String newUrl = url.replace("localhost", IP);
            Glide.with(context)
                    .load(newUrl)
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .into(holder.imageFruit);
        }

        Distributor distributor = fruit.getDistributor();
        holder.tvDistributor.setText(distributor.getName());
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitClick.delete(fruit);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listFruits!=null?listFruits.size():0;
    }

    public class FruitsHolder extends RecyclerView.ViewHolder{

        private TextView tvName,tvDistributor,tvPrice;
        private ImageView imageFruit, btnEdit, btnDel;
        public FruitsHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTenSP);
            tvDistributor = itemView.findViewById(R.id.tvDistributor);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDel = itemView.findViewById(R.id.btnDel);
            imageFruit = itemView.findViewById(R.id.imageSP);
        }
    }
}
