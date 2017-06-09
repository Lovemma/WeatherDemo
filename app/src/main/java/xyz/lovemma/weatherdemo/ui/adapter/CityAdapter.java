package xyz.lovemma.weatherdemo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import xyz.lovemma.weatherdemo.R;
import xyz.lovemma.weatherdemo.ui.adapter.viewHolder.baseViewHolder;
import xyz.lovemma.weatherdemo.db.City;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private Context mContext;
    private List<City> mDataList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public CityAdapter(List<City> dataList) {
        this.mDataList = dataList;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false));
    }

    @Override
    public void onBindViewHolder(final CityViewHolder holder, final int position) {
        String s = mDataList.get(position).getCityZh() + " " + mDataList.get(position).getProvinceZh() + " " + mDataList.get(position).getCountryZh();
        holder.bind(s);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void clearData() {
        mDataList.clear();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int pos);
    }

    class CityViewHolder extends baseViewHolder<String> {

        @BindView(R.id.item_city)
        TextView mItemCity;
        @BindView(R.id.cardView)
        CardView mCardView;

        public CityViewHolder(View itemView) {
            super(itemView);
        }

        protected void bind(String s) {
            mItemCity.setText(s);
        }
    }
}
