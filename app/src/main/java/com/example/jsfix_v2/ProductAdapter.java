package com.example.jsfix_v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<ProductModel> productModelArrayList;

    public ProductAdapter(Context ctx, ArrayList<ProductModel> productModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.productModelArrayList = productModelArrayList;
    }

    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ProductAdapter.MyViewHolder holder, int position) {

        Picasso.get().load(productModelArrayList.get(position).getImgURL()).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView url;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }

}
