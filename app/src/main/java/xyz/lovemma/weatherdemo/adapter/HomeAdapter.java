package xyz.lovemma.weatherdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import xyz.lovemma.weatherdemo.R;
import xyz.lovemma.weatherdemo.entity.HeWeather5;
import xyz.lovemma.weatherdemo.utils.DateUtil;

/**
 * Created by OO on 2017/5/19.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_NOW = 0;
    private static final int ITEM_TYPE_AQI = 1;
    private static final int ITEM_TYPE_HOURLY = 2;
    private static final int ITEM_TYPE_DAILY = 3;
    private static final int ITEM_TYPE_SUGGESTION = 4;

    private HeWeather5 mWeather;
    private Context mContext;


    public HomeAdapter(HeWeather5 weather) {
        mWeather = weather;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        switch (viewType) {
            case ITEM_TYPE_NOW:
                return new NowViewHolder(LayoutInflater.from(mContext).inflate(R.layout.now, parent, false));
            case ITEM_TYPE_AQI:
                return new AqiViewHolder(LayoutInflater.from(mContext).inflate(R.layout.aqi, parent, false));
            case ITEM_TYPE_HOURLY:
                return new HourlyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.hourly_forecast, parent, false));
            case ITEM_TYPE_DAILY:
                return new DailyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.daily_forecast, parent, false));
            case ITEM_TYPE_SUGGESTION:
                return new SuggertionViewHolder(LayoutInflater.from(mContext).inflate(R.layout.suggestion, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ITEM_TYPE_NOW;
            case 1:
                return ITEM_TYPE_AQI;
            case 2:
                return ITEM_TYPE_HOURLY;
            case 3:
                return ITEM_TYPE_DAILY;
            case 4:
                return ITEM_TYPE_SUGGESTION;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                ((NowViewHolder) holder).bind(mWeather);
                break;
            case 1:
                ((AqiViewHolder) holder).bind(mWeather);
                break;
            case 2:
                ((HourlyViewHolder) holder).bind(mWeather);
                break;
            case 3:
                ((DailyViewHolder) holder).bind(mWeather);
                break;
            case 4:
                ((SuggertionViewHolder) holder).bind(mWeather);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (mWeather.getStatus() != null) ? 5 : 0;
    }

    class NowViewHolder extends baseViewHolder<HeWeather5> {
        @BindView(R.id.weather_icon)
        ImageView icon;
        @BindView(R.id.weather_tmp)
        TextView tmp;
        @BindView(R.id.city)
        TextView city;
        @BindView(R.id.cond)
        TextView cond;

        public NowViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(HeWeather5 heWeather5) {
            tmp.setText(heWeather5.getNow().getTmp() + "℃");
            city.setText(heWeather5.getBasic().getCity());
            cond.setText(heWeather5.getNow().getCond().getTxt());
        }
    }

    class AqiViewHolder extends baseViewHolder<HeWeather5> {
        @BindView(R.id.wind_spd)
        TextView windSpd;
        @BindView(R.id.pm25)
        TextView pm25;
        @BindView(R.id.hum)
        TextView hum;

        public AqiViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(HeWeather5 heWeather5) {
            windSpd.setText(heWeather5.getNow().getWind().getSpd());
            pm25.setText(heWeather5.getAqi().getCity().getPm25());
            hum.setText(heWeather5.getNow().getHum());
        }
    }

    class HourlyViewHolder extends baseViewHolder<HeWeather5> {
        @BindView(R.id.hourly_forecast)
        LinearLayout hourlyForecast;
        int size = mWeather.getHourly_forecast().size();
        private TextView[] cond = new TextView[size];
        private ImageView[] condIcon = new ImageView[size];
        private TextView[] time = new TextView[size];

        public HourlyViewHolder(View itemView) {
            super(itemView);

            for (int i = 0; i < size; i++) {
                View view = View.inflate(mContext, R.layout.item_hourly_forecast, null);
                cond[i] = (TextView) view.findViewById(R.id.cond);
                condIcon[i] = (ImageView) view.findViewById(R.id.cond_icon);
                time[i] = (TextView) view.findViewById(R.id.time);
                hourlyForecast.addView(view);
            }
        }

        @Override
        protected void bind(HeWeather5 heWeather5) {
            for (int i = 0; i < size; i++) {
                cond[i].setText(heWeather5.getHourly_forecast().get(i).getCond().getTxt());

                String mDate = heWeather5.getHourly_forecast().get(i).getDate();
                time[i].setText(
                        mDate.substring(mDate.length() - 5, mDate.length()));
            }
        }
    }

    class DailyViewHolder extends baseViewHolder<HeWeather5> {
        @BindView(R.id.daily_forecast)
        LinearLayout dailyForecast;
        int size = mWeather.getDaily_forecast().size();
        private ImageView[] icon = new ImageView[size];
        private TextView[] date = new TextView[size];
        private TextView[] temp = new TextView[size];
        private TextView[] txt = new TextView[size];

        public DailyViewHolder(View itemView) {
            super(itemView);
            for (int i = 0; i < size; i++) {
                View view = View.inflate(mContext, R.layout.item_daily_forecast, null);
                icon[i] = (ImageView) view.findViewById(R.id.forecast_icon);
                date[i] = (TextView) view.findViewById(R.id.forecast_date);
                temp[i] = (TextView) view.findViewById(R.id.forecast_temp);
                txt[i] = (TextView) view.findViewById(R.id.forecast_txt);
                dailyForecast.addView(view);
            }
        }

        @Override
        protected void bind(HeWeather5 heWeather5) {
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    date[i].setText("今天");
                } else {
                    date[i].setText(DateUtil.dayForWeek(heWeather5.getDaily_forecast().get(i).getDate()));
                }
                temp[i].setText(
                        String.format("%s℃ - %s℃",
                                heWeather5.getDaily_forecast().get(i).getTmp().getMin(),
                                heWeather5.getDaily_forecast().get(i).getTmp().getMax()));
                txt[i].setText(
                        String.format("%s。 %s %s %s km/h。 降水几率 %s%%。",
                                heWeather5.getDaily_forecast().get(i).getCond().getTxt_d(),
                                heWeather5.getDaily_forecast().get(i).getWind().getSc(),
                                heWeather5.getDaily_forecast().get(i).getWind().getDir(),
                                heWeather5.getDaily_forecast().get(i).getWind().getSpd(),
                                heWeather5.getDaily_forecast().get(i).getPop()));
            }
        }
    }

    class SuggertionViewHolder extends baseViewHolder<HeWeather5> {
        @BindView(R.id.cloth_brief)
        TextView clothBrief;
        @BindView(R.id.cloth_txt)
        TextView clothTxt;
        @BindView(R.id.sport_brief)
        TextView sportBrief;
        @BindView(R.id.sport_txt)
        TextView sportTxt;
        @BindView(R.id.uv_brief)
        TextView uvBrief;
        @BindView(R.id.uv_txt)
        TextView uvTxt;
        @BindView(R.id.flu_brief)
        TextView fluBrief;
        @BindView(R.id.flu_txt)
        TextView fluTxt;

        public SuggertionViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(HeWeather5 heWeather5) {
            clothBrief.setText(String.format("穿衣指数---%s", heWeather5.getSuggestion().getDrsg().getBrf()));
            clothTxt.setText(heWeather5.getSuggestion().getDrsg().getTxt());

            sportBrief.setText(String.format("运动指数---%s", heWeather5.getSuggestion().getSport().getBrf()));
            sportTxt.setText(heWeather5.getSuggestion().getSport().getTxt());

            uvBrief.setText(String.format("紫外线指数---%s", heWeather5.getSuggestion().getUv().getBrf()));
            uvTxt.setText(heWeather5.getSuggestion().getUv().getTxt());

            fluBrief.setText(String.format("感冒指数---%s", heWeather5.getSuggestion().getFlu().getBrf()));
            fluTxt.setText(heWeather5.getSuggestion().getFlu().getTxt());
        }
    }

}
