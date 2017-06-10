package xyz.lovemma.weatherdemo.ui.adapter;

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
import xyz.lovemma.weatherdemo.entity.HeWeather5;
import xyz.lovemma.weatherdemo.ui.adapter.viewHolder.baseViewHolder;

/**
 * Created by OO on 2017/6/8.
 */

public class MutiliCityAdapter extends RecyclerView.Adapter<MutiliCityAdapter.MultiCityViewHolder> {
    private List<HeWeather5> mWeatherList;
    private onMultiCityClickListener mClickListener;

    public MutiliCityAdapter(List<HeWeather5> weatherList) {
        mWeatherList = weatherList;
    }

    public void setData(List<HeWeather5> weatherList) {
        mWeatherList = weatherList;
    }

    @Override
    public MultiCityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_city, parent, false);
        return new MultiCityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MultiCityViewHolder holder, int position) {
        holder.bind(mWeatherList.get(position));
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
        return mWeatherList.size();
    }

    class MultiCityViewHolder extends baseViewHolder<HeWeather5> {
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
        protected void bind(HeWeather5 heWeather5) {
            city.setText(heWeather5.getBasic().getCity());
            temp.setText(heWeather5.getNow().getTmp() + "°");
            condTxt.setText(heWeather5.getNow().getCond().getTxt());
        }
    }
}
