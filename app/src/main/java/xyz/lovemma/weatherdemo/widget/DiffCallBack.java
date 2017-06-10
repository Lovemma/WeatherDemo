package xyz.lovemma.weatherdemo.widget;

import android.support.v7.util.DiffUtil;

import java.util.List;

import xyz.lovemma.weatherdemo.entity.HeWeather5;

/**
 * Created by OO on 2017/6/10.
 */

public class DiffCallBack extends DiffUtil.Callback {
    private List<HeWeather5> oldDataList, newDataList;

    public DiffCallBack(List<HeWeather5> oldDataList, List<HeWeather5> newDataList) {
        this.oldDataList = oldDataList;
        this.newDataList = newDataList;
    }

    @Override
    public int getOldListSize() {
        return oldDataList == null ? 0 : oldDataList.size();
    }

    @Override
    public int getNewListSize() {
        return newDataList == null ? 0 : newDataList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDataList.get(oldItemPosition).getBasic().getCity().equals(newDataList.get(newItemPosition).getBasic().getCity());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        HeWeather5 oldData = oldDataList.get(oldItemPosition);
        HeWeather5 newData = newDataList.get(newItemPosition);
        if (!oldData.getNow().getTmp().equals(newData.getNow().getTmp())) {
            return false;
        }
        if (!oldData.getNow().getCond().getTxt().equals(newData.getNow().getCond().getTxt())) {
            return false;
        }
        return true;
    }
}
