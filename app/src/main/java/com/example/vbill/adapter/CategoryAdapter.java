package com.example.vbill.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vbill.R;
import com.example.vbill.bean.Category;
import com.example.vbill.create.CreateActivity;
import com.example.vbill.home.HomeActivity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private static final String TAG = "CategoryAdapter";
    private OnItemClickListener mOnItemClickListener;
    private List<Category> mCategoryList;
    private Context mContext;
    private int lastSelectIndex;
    private View lastSelectedView;

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

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Category category = mCategoryList.get(i);
        viewHolder.textView.setText(category.getDescription());
        Glide.with(mContext).load(category.getImagePath()).into(viewHolder.imageView);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedView != null && lastSelectIndex !=-1){
                    //setSelected 是与 xml中相对应的
                    lastSelectedView.setSelected(false);
                }
                lastSelectIndex = i;
                v.setSelected(true);
                lastSelectedView = v;
                //显示layout
                Log.d(TAG, "onClick: getHasCategory" + CreateActivity.getHasCategory());
                CreateActivity.showCreateBodyView();
                CreateActivity.setSelectCategory(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View categoryView;
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryView = itemView;
            textView = itemView.findViewById(R.id.category_txt);
            imageView = itemView.findViewById(R.id.category_image);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
