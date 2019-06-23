package com.example.vbill.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vbill.R;
import com.example.vbill.bean.ChildBill;
import com.example.vbill.create.CreateActivity;
import com.example.vbill.home.HomeActivity;
import com.example.vbill.util.Constants;
import com.example.vbill.util.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChildBillRecyclerAdapter extends RecyclerView.Adapter<ChildBillRecyclerAdapter.ViewHolder>{
    private static final String TAG = "ChildBillRecyclerAdapte";
    private List<ChildBill> mChildBillList;
    private Context mContext;
    private String customerId;

    public ChildBillRecyclerAdapter(Context context,List billList){
        mChildBillList = billList;
        mContext = context;
    }

    public void setMBillList(List<ChildBill> mChildBillList){
        this.mChildBillList = mChildBillList;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        View billView,moreView;
        ImageView image,moreImage;
        TextView amounts,EditBtn,DeleteBtn,category,type;

        public ViewHolder( View view) {
            super(view);
            billView = view;
            image = view.findViewById(R.id.image);
            category = view.findViewById(R.id.category);
            amounts = view.findViewById(R.id.amounts);
            moreImage = view.findViewById(R.id.moreImage);
            EditBtn= view.findViewById(R.id.home_detail_edit);
            DeleteBtn= view.findViewById(R.id.home_detail_delete);
            moreView = view.findViewById(R.id.home_detail_more);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_bill,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        final ViewGroup innerParent = parent;
        //点击更多显示编辑删除
        holder.moreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏更所按钮
                if(holder.moreView.getVisibility()==View.VISIBLE){
                    holder.moreView.setVisibility(View.GONE);
                }else {
                    holder.moreView.setVisibility(View.VISIBLE);
                }
            }
        });
        //编辑
        holder.EditBtn.setOnClickListener(new View.OnClickListener() {
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
        //删除
        holder.DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ChildBill childBill =new ChildBill((Map)mChildBillList.get(position));
                //将requestjson化。
                Gson gson = new Gson();
                String newChildMapJson = gson.toJson(childBill);
                Log.d(TAG, "onClick: removeBillInfo" + newChildMapJson);
                String address = Constants.SERVER_PREFIX + "v1/esc/accountToDelete/" + childBill.getItemId();
                //http 请求删除
                HttpUtil.sendOkHttpPostRequest(newChildMapJson,address, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "对不起，处理失败，我们会尽快修复。", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d(TAG, "onResponse: removeBill" + responseData);
                        //code 200
                        //关闭这个activity，返回列表刷新列表。
                        Intent homeFragmentIntent = new Intent(mContext, HomeActivity.class);
                        mContext.startActivity(homeFragmentIntent);
                    }
                });
            }
        });
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

    public String getCustomerId(){
        SharedPreferences loginPref;
        loginPref = mContext.getSharedPreferences("login", mContext.MODE_PRIVATE);
        customerId = String.valueOf(loginPref.getInt("userId", -1));
        return customerId;
    }
}
