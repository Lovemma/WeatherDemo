package xyz.lovemma.weatherdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.lovemma.weatherdemo.R;
import xyz.lovemma.weatherdemo.entity.HourlyForecast;
import xyz.lovemma.weatherdemo.utils.SharedPreferencesUtil;

/**
 * Created by OO on 2017/5/22.
 */

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {
    private List<HourlyForecast> mData;
    private SharedPreferencesUtil mSharedPreferencesUtil;

    public HourlyForecastAdapter(List<HourlyForecast> data) {
        mData = data;
    }

    @Override
    public HourlyForecastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mSharedPreferencesUtil = new SharedPreferencesUtil(parent.getContext().getApplicationContext());
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly_forecast, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HourlyForecast hourlyForecast = mData.get(position);
        holder.cond.setText(hourlyForecast.getCond().getTxt());
        holder.condIcon.setImageResource((int) mSharedPreferencesUtil.get(hourlyForecast.getCond().getTxt(), R.drawable.ic_unknow));
        String date = hourlyForecast.getDate();
        holder.time.setText(date.substring(date.length() - 5, date.length()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cond)
        TextView cond;
        @BindView(R.id.cond_icon)
        ImageView condIcon;
        @BindView(R.id.time)
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
