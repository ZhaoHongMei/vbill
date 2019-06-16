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
import com.example.vbill.bean.ChildBill;

import java.util.List;
import java.util.Map;

public class ChildBillRecyclerAdapter extends RecyclerView.Adapter<ChildBillRecyclerAdapter.ViewHolder>{
    private static final String TAG = "ChildBillRecyclerAdapte";
    private List<ChildBill> mChildBillList;
    private Context mContext;

    public ChildBillRecyclerAdapter(Context context,List billList){
        mChildBillList = billList;
        mContext = context;
    }

    public void setMBillList(List<ChildBill> mChildBillList){
        this.mChildBillList = mChildBillList;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        View billView;
        ImageView image;
        TextView amounts;
        TextView type;
        TextView category;

        public ViewHolder( View view) {
            super(view);
            billView = view;
            image = view.findViewById(R.id.image);
            category = view.findViewById(R.id.category);
            amounts = view.findViewById(R.id.amounts);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_bill,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        final ViewGroup innerParent = parent;
        holder.billView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ChildBill childBill =new ChildBill((Map)mChildBillList.get(position));
                Intent intent = new Intent("android.intent.action.Create");
                ChildBill detailChildBill = new ChildBill(childBill.getItemId(),childBill.getCreateDay(),childBill.getCreateTime(),childBill.getImagePath(),
                        childBill.getCategoryCode(),childBill.getCategoryDesc(),childBill.getType(),childBill.getAmount());
                intent.putExtra("bill",detailChildBill);
                mContext.startActivity(intent);

            }
        });
//        holder.image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                ChildBill childBill =new ChildBill((Map)mChildBillList.get(position));
//                Intent intent = new Intent("android.intent.action.Create");
//                intent.putExtra("category",childBill.getCategoryCode());
//                mContext.startActivity(intent);
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ChildBill childBill =new ChildBill((Map)mChildBillList.get(i));
        viewHolder.amounts.setText(childBill.getAmount());
        viewHolder.category.setText(childBill.getCategoryDesc());
        Log.d(TAG, "onBindViewHolder: "+mContext);
        final ViewGroup.LayoutParams layoutParams = viewHolder.image.getLayoutParams();
        layoutParams.width = 80;
        layoutParams.height = 80;
        Glide.with(mContext).load(childBill.getImagePath()).into(viewHolder.image);
        int viewHolderLayoutParams = viewHolder.getLayoutPosition();
    }

    @Override
    public int getItemCount() {
        return mChildBillList.size();
    }


}
