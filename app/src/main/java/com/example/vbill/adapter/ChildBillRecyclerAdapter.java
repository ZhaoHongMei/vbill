package com.example.vbill.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vbill.R;
import com.example.vbill.bean.ChildBill;

import java.util.List;

public class ChildBillRecyclerAdapter extends RecyclerView.Adapter<ChildBillRecyclerAdapter.ViewHolder>{
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
                ChildBill childBill = (ChildBill) mChildBillList.get(position);
                Intent intent = new Intent("android.intent.action.Create");
                ChildBill detailChildBill = new ChildBill(childBill.getAmounts(), childBill.getCategory(), childBill.getType(), childBill.getImage());
                intent.putExtra("position",String.valueOf(position));
                mContext.startActivity(intent);
            }

        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ChildBill childBill =(ChildBill) mChildBillList.get(i);
        viewHolder.amounts.setText(childBill.getAmounts());
        viewHolder.category.setText(childBill.getCategory());
        viewHolder.image.setImageResource(childBill.getImage());
    }

    @Override
    public int getItemCount() {
        return mChildBillList.size();
    }


}
