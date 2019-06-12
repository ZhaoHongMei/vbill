package com.example.vbill.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vbill.R;
import com.example.vbill.bean.Category;
import com.example.vbill.create.CreateActivity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private static final String TAG = "CategoryAdapter";
    private List<Category> mCategoryList;
    private Context mContext;

    public CategoryAdapter(Context mContext, List<Category> categoryList) {
        this.mCategoryList = categoryList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_summary,viewGroup,false);
        CategoryAdapter.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Category category = mCategoryList.get(i);
        viewHolder.textView.setText(category.getDescription());
        Glide.with(mContext).load(category.getImagePath()).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.category_txt);
            imageView = itemView.findViewById(R.id.category_image);
        }
    }
}
