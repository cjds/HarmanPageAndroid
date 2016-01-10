package cjds.harmanpage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    private final List<Util.RoomData> mValues;
    private final Util util;

    public RoomAdapter() {
        util=Util.getInstance();
        mValues = util.getRooms();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        holder.mItem = mValues.get(position);
        holder.mItemName.setText(mValues.get(position).groupObj.groupName);

        if (util.getRoomStatus(position)) {
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (util.getRoomStatus(position)) {

                    util.removeRoomFromSession(util.getRooms().get(position).groupObj.groupId);
                    holder.checkbox.setChecked(false);
            } else {
                util.addRoomToSession(util.getRooms().get(position).groupObj.groupId);
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
        public Util.RoomData mItem;

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
