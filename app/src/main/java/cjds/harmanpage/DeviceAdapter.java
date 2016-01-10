package cjds.harmanpage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cjds on 12/19/15.
 */
class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private final List<Util.DeviceData> mValues;
    private final Util util;

    public DeviceAdapter() {
        util = Util.getInstance();
        mValues = util.getDevices();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mItemName.setText(util.getDevices().get(position).deviceObj.deviceName);

        if (util.getDeviceStatus(position)) {
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (util.getDeviceStatus(position)) {

                    util.removeDeviceFromSession(util.getDevices().get(position).deviceObj.deviceId);
                    holder.checkbox.setChecked(false);
                } else {
                    util.addDeviceToSession(util.getDevices().get(position).deviceObj.deviceId);
                    holder.checkbox.setChecked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mItemName;
        public final CheckBox checkbox;
        public Util.DeviceData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            checkbox = (CheckBox) view.findViewById(R.id.select);
            mItemName = (TextView) view.findViewById(R.id.item_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItemName.getText() + "'";
        }
    }

}