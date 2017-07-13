package xyz.lovemma.weatherdemo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import xyz.lovemma.weatherdemo.R;
import xyz.lovemma.weatherdemo.db.MutiliCity;
import xyz.lovemma.weatherdemo.entity.HeWeather5;
import xyz.lovemma.weatherdemo.ui.adapter.viewHolder.baseViewHolder;
import xyz.lovemma.weatherdemo.utils.SharedPreferencesUtil;

/**
 * Created by OO on 2017/6/8.
 */

public class MutiliCityAdapter extends RecyclerView.Adapter<MutiliCityAdapter.MultiCityViewHolder> {
    private Context mContext;
    private SharedPreferencesUtil mPreferencesUtil;
    private List<MutiliCity> mCities;
    private onMultiCityClickListener mClickListener;

    public MutiliCityAdapter(List<MutiliCity> weatherList) {
        mCities = weatherList;
    }

    public void setData(List<MutiliCity> cityList) {
        mCities = cityList;
    }

    @Override
    public MultiCityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_city, parent, false);
        return new MultiCityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MultiCityViewHolder holder, int position) {
        holder.bind(mCities.get(position));
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
        return mCities.size();
    }

    class MultiCityViewHolder extends baseViewHolder<MutiliCity> {
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
        protected void bind(MutiliCity mutiliCity) {
            String json = mutiliCity.getJson();
            HeWeather5 weather = new Gson().fromJson(json, HeWeather5.class);
            city.setText(weather.getBasic().getCity());
            temp.setText(weather.getNow().getTmp() + "°");
            condTxt.setText(weather.getNow().getCond().getTxt());
            mPreferencesUtil = new SharedPreferencesUtil(mContext);
            int resId = (int) mPreferencesUtil.get(weather.getNow().getCond().getTxt(), R.drawable.ic_unknow);
            condImg.setImageResource(resId);
        }
    }
}
