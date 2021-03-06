package com.michiganhackers.diabeticons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.michiganhackers.diabeticons.Core.MyApplication;
import com.michiganhackers.diabeticons.Util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jawad on 22/10/15.
 */
public class FavListAdapter extends BaseAdapter {
    private static final String LOGTAG = "MD/HomeListAdapter";

    Context mContext;
    MyApplication mAppReference;
    ArrayList<Integer> mFavoritesList;

    public FavListAdapter(Context context, MyApplication myApplication) {
        this.mContext = context;
        this.mAppReference = myApplication;

        updateData();
    }

    public void updateData() {
        mFavoritesList = new ArrayList<>();

        List<Icon> allIcons = mAppReference.getAllIcons();
        for(int i = 0; i < allIcons.size(); ++i) {
            if(allIcons.get(i).getIsFavorite()) {
                mFavoritesList.add(i);
            }
        }

        notifyDataSetChanged();
    }

    public int getIconIndex(int position) {
        return mFavoritesList.get(position);
    }

    @Override
    public int getCount() {
        return mFavoritesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = null;
        ViewHolder holder;

        if(convertView == null) {
            // Then gotta set up this row for the first time
            LayoutInflater inflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_itemselect, parent, false);

            // Create a ViewHolder to save all the different parts of the row
            holder = new ViewHolder();
            holder.icon = (ImageView) row.findViewById(R.id.icon);
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.favBtn = (ImageButton) row.findViewById(R.id.btn_fav);
            holder.sendBtn = (ImageButton) row.findViewById(R.id.btn_send);

            // Make the row reuse the ViewHolder
            row.setTag(holder);
        }
        else { // Otherwise, use the recycled view
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }

        // Set the title and icon of this item according to the position
        final Icon curIcon = mAppReference.getAllIcons().get(getIconIndex(position));

        holder.title.setText(curIcon.getTitle());
        holder.icon.setImageDrawable(curIcon.getImage());

        // Set the favorite button to toggle correctly
        holder.favBtn.setSelected(curIcon.getIsFavorite());
        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    // Now unselected
                    v.setSelected(false);

                    // Let the entire app know of the change
                    mAppReference.setFavoriteState(getIconIndex(position), false);
                } else {
                    // Now selected
                    v.setSelected(true);

                    // Let the entire app know of the change
                    mAppReference.setFavoriteState(getIconIndex(position), true);
                }
            }
        });

        // Set the send button to send the current image
        holder.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.sendImage(mContext, curIcon.getImage());
            }
        });

        return row;
    }

    class ViewHolder{
        public ImageView icon;
        public TextView title;
        public ImageButton favBtn;
        public ImageButton sendBtn;
    }
}
