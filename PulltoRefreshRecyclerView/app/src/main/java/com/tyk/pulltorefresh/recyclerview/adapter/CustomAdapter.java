package com.tyk.pulltorefresh.recyclerview.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulltorefresh.tyk.library.adapter.MultipleAdapter;
import com.pulltorefresh.tyk.library.adapter.viewholder.BaseViewHolder;
import com.tyk.pulltorefresh.recyclerview.R;


/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class CustomAdapter extends MultipleAdapter<String,CustomAdapter.ViewHolder > {


    Activity activity;

    public CustomAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(activity).inflate(R.layout.item_card,parent,false);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(ViewHolder holder, String s, int position) {

        holder.tv_card_title.setText(s);

    }

    @Override
    public int customItemViewType(int position) {
        return position;
    }


    class ViewHolder extends BaseViewHolder {

        CardView cv_item_content;
        TextView tv_card_title;

        public ViewHolder(View itemView) {
            super(itemView);

            cv_item_content= (CardView) itemView.findViewById(R.id.cv_item_content);
            tv_card_title= (TextView) itemView.findViewById(R.id.tv_card_title);

        }

    }

}
