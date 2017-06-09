package xyz.lovemma.weatherdemo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import xyz.lovemma.weatherdemo.R;
import xyz.lovemma.weatherdemo.entity.MulitiCity;
import xyz.lovemma.weatherdemo.ui.adapter.viewHolder.baseViewHolder;

/**
 * Created by OO on 2017/6/8.
 */

public class MutiliCityAdapter extends RecyclerView.Adapter<MutiliCityAdapter.MultiCityViewHolder> {
    private Context mContext;
    private List<MulitiCity> mCityList;
    private onMultiCityClickListener mClickListener;

    public MutiliCityAdapter(List<MulitiCity> cityList) {
        mCityList = cityList;
    }

    @Override
    public MultiCityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_city, parent, false);
        return new MultiCityViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MultiCityViewHolder holder, int position) {
        holder.bind(mCityList.get(position));
        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClick(pos);
            }
        });
    }

    public void setOnMultiCityClickListener(onMultiCityClickListener listener) {
        mClickListener = listener;
    }

    public interface onMultiCityClickListener {
        void onClick(int position);
    }

    @Override
    public int getItemCount() {
        return mCityList.size();
    }

    class MultiCityViewHolder extends baseViewHolder<MulitiCity> {
        @BindView(R.id.city)
        TextView city;
        @BindView(R.id.temp)
        TextView temp;
        @BindView(R.id.cond_txt)
        TextView condTxt;
        @BindView(R.id.cond_img)
        ImageView condImg;
        @BindView(R.id.cardView)
        CardView mCardView;

        public MultiCityViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(MulitiCity mulitiCity) {
            city.setText(mulitiCity.getCity());
            temp.setText(mulitiCity.getTemp() + "°");
            condTxt.setText(mulitiCity.getCond());
        }
    }
}
