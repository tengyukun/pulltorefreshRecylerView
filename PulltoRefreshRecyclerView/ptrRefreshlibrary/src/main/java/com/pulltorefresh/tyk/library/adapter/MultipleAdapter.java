package com.pulltorefresh.tyk.library.adapter;


import com.pulltorefresh.tyk.library.adapter.viewholder.BaseViewHolder;

/**
 * 多条目adapter
 * Created by tyk on 2016/11/31 0031.
 */

public abstract class MultipleAdapter<T,VH extends BaseViewHolder> extends CommonAdapter<T,VH> {

    @Override
    public int getItemViewType(int position) {
        if(getFooter() != null && getItemCount() - 1 == position){
            return TYPE_FOOTER;
        }

        if(customItemViewType(position) == TYPE_FOOTER){
            throw new RuntimeException("customItemViewType(int position) 返回值不能为"+TYPE_FOOTER);
        }
        return customItemViewType(position);
    }

    public abstract int customItemViewType(int position);
}
