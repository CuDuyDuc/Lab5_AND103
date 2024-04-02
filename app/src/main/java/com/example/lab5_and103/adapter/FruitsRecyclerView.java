package com.example.lab5_and103.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    private HttpRequest request;
    private List<Distributor> distributorsList;
    private ChooseImage chooseImage;

    public FruitsRecyclerView(ArrayList<Fruit> listFruits, Context context, HttpRequest request, List<Distributor> distributorsList, ChooseImage chooseImage) {
        this.listFruits = listFruits;
        this.context = context;
        this.request = request;
        this.distributorsList = distributorsList;
        this.chooseImage = chooseImage;
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
//        if(fruit.getImageList().size()>0){
//            File image = new File(fruit.getImageList().get(0));
//            Glide.with(context)
//                    .load(image)
//                    .thumbnail(Glide.with(context).load(R.mipmap.loading))
//                    .into(holder.imageFruit);
//        }

        if(distributorsList.size()>0){
            for (int i = 0; i < distributorsList.size(); i++) {
                if(fruit.getDistributor().equals(distributorsList.get(i).getId())){
                    holder.tvDistributor.setText(distributorsList.get(i).getName());
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return listFruits!=null?listFruits.size():0;
    }

    public class FruitsHolder extends RecyclerView.ViewHolder{

        private TextView tvName,tvDistributor,tvPrice;
        private ImageView imageFruit, btnAddCart;
        public FruitsHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTenSP);
            tvDistributor = itemView.findViewById(R.id.tvDistributor);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAddCart = itemView.findViewById(R.id.btnAddCart);
            imageFruit = itemView.findViewById(R.id.imageSP);
        }
    }
}
