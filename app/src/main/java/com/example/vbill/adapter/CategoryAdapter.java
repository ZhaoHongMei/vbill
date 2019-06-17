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
import com.example.vbill.bean.ChildBill;
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
    private ChildBill selectedchildBill;
    private View defaultSelectedView;

    public CategoryAdapter(Context mContext, List<Category> categoryList, ChildBill childBill) {
        this.mCategoryList = categoryList;
        this.mContext = mContext;
        this.selectedchildBill = childBill;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_summary, viewGroup, false);
        CategoryAdapter.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Category category = mCategoryList.get(i);
        viewHolder.textView.setText(category.getDescription());
        Glide.with(mContext).load(category.getImagePath()).into(viewHolder.imageView);

        if (selectedchildBill != null) {
            if (category.getDescription().equals(selectedchildBill.getCategoryDesc())) {
                Log.d(TAG, "onBindViewHolder selectedchildBill: " + selectedchildBill.getCategoryCode() + "--" + category.getCode());
//                viewHolder.imageView.setBackgroundResource(R.drawable.category_bg_checked);
            }
//            defaultSelectedView = viewHolder.imageView;
//            Log.d(TAG, "onBindViewHolder: defaultSelectedView : " + defaultSelectedView);
        }

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedView != null && lastSelectIndex != -1) {
                    //setSelected 是与 xml中相对应的
                    lastSelectedView.setSelected(false);
                }
//                if (defaultSelectedView != null) {
//                    Log.d(TAG, "onBindViewHolder: defaultSelectedView 1 : " + defaultSelectedView);
//                    defaultSelectedView.setBackgroundResource(R.drawable.category_bg_unchecked);
//                }
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
