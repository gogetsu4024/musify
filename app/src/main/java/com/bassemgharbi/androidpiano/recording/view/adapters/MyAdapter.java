package com.bassemgharbi.androidpiano.recording.view.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bassemgharbi.androidpiano.R;
import com.bassemgharbi.androidpiano.recording.services.model.InstrumentRecord;
import com.bassemgharbi.androidpiano.recording.view.ui.MyCustomView;
import com.leff.midi.util.MidiUtil;

import java.io.IOException;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<InstrumentRecord> mDataset;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public ImageView image;
        private MyCustomView customView;
        public MyViewHolder(View v) {
            super(v);
            //text = v.findViewById(R.id.title);
            image = v.findViewById(R.id.imageView2);
            customView = v.findViewById(R.id.customView);

        }
        public MyCustomView getCustomView() {
            return customView;
        }
    }


    public MyAdapter(List<InstrumentRecord> myDataset) {
        mDataset = myDataset;
    }


    public void setTasks(List<InstrumentRecord> dataset) {
        mDataset = dataset;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Log.d(mDataset.get(position).getMidiTrack().getLengthInTicks()+"","length in ticks");
        try {
            holder.customView.setVariables(MidiUtil.ticksToMs(mDataset.get(position).getMidiTrack().getLengthInTicks(),mDataset.get(position).getTempo().getMpqn(),mDataset.get(position).getResolution()),mDataset.get(position).getMidiTrack(),mDataset.get(position).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.customView.invalidate();
        holder.image.setImageURI(Uri.parse("android.resource://com.bassemgharbi.androidpiano/drawable/"+mDataset.get(position).getInstrument().getImagePath()));

    }
    private View.OnCreateContextMenuListener vC = new View.OnCreateContextMenuListener() {


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    };

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
