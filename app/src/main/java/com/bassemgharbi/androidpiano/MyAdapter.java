package com.bassemgharbi.androidpiano;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bassemgharbi.androidpiano.Instruments.Instrument;
import com.bassemgharbi.androidpiano.Instruments.Instruments;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Instrument> mDataset;
    private Context context;
    private Dialog dialog;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView instrumentGroup;
        public TextView instrumentName;
        public ImageView instrumentImage;
        public MyViewHolder(View v) {
            super(v);
            instrumentName = v.findViewById(R.id.instrumentName);
            instrumentGroup = v.findViewById(R.id.instrumentGroup);
            instrumentImage = v.findViewById(R.id.instrumentImage);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Instrument> myDataset, Context context, Dialog dialog) {
        this.context = context;
        mDataset = myDataset;
        this.dialog = dialog;
    }


    public void setTasks(List<Instrument> dataset) {
        mDataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_instruments, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.instrumentName.setText(mDataset.get(position).getName());
        holder.instrumentGroup.setText(mDataset.get(position).getGroup());
        holder.instrumentImage.setImageURI(Uri.parse("android.resource://com.bassemgharbi.androidpiano/drawable/"+mDataset.get(position).getImagePath()));
        //holder.iv.setImageIcon();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (((TextView)v.findViewById(R.id.instrumentName)).getText()!=null)
                //Toast.makeText(holder.itemView.getContext(), ((TextView)v.findViewById(R.id.instrumentName)).getText(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent("Instrument change");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                intent.putExtra("midiHex",mDataset.get(position).getMidiHexCode());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                dialog.dismiss();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
