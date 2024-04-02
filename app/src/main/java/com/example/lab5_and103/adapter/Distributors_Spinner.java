package com.example.lab5_and103.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lab5_and103.R;
import com.example.lab5_and103.model.Distributor;

import java.util.List;

public class Distributors_Spinner extends BaseAdapter {
    private List<Distributor> list;
    private Context context;

    public Distributors_Spinner(List<Distributor> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_distributor,parent,false);
        Distributor distributor = list.get(position);
        TextView nameDistributor = view.findViewById(R.id.tvNameDistributor);
        nameDistributor.setText(distributor.getName());
        return view;
    }
}
