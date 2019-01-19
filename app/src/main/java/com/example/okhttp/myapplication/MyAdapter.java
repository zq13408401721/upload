package com.example.okhttp.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.okhttp.myapplication.bean.DataBean;
import com.example.okhttp.myapplication.bean.JokeBean;
import com.example.okhttp.myapplication.bean.NewBean;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {

    private List<JokeBean.ShowapiResBodyBean.ContentlistBean> list;

    public MyAdapter(List<JokeBean.ShowapiResBodyBean.ContentlistBean> list){
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, null, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JokeBean.ShowapiResBodyBean.ContentlistBean bean = this.list.get(position);
        ((MyHolder)holder).txt.setText(bean.getTitle());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        private TextView txt;

        public MyHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);

        }
    }
}
