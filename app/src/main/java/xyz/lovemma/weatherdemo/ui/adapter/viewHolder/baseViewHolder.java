package xyz.lovemma.weatherdemo.ui.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by OO on 2017/5/21.
 */

public abstract class baseViewHolder<T> extends RecyclerView.ViewHolder {

    public baseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    protected abstract void bind(T t);
}
