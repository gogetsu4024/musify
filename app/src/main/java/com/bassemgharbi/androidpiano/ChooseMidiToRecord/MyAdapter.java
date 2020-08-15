package com.bassemgharbi.androidpiano.ChooseMidiToRecord;

import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bassemgharbi.androidpiano.R;
import com.bassemgharbi.androidpiano.recording.view.ui.RecordActivity;

import java.io.File;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<FileInSystem> mDataset;
    private String selectedPath;
    private FileInSystem currentFile;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public ImageView image;
        public MyViewHolder(View v) {
            super(v);
            //text = v.findViewById(R.id.title);
            image = v.findViewById(R.id.person_photo);
            text = v.findViewById(R.id.person_name);
        }
    }


    public MyAdapter(List<FileInSystem> myDataset) {
        mDataset = myDataset;
    }


    public void setTasks(List<FileInSystem> dataset) {
        mDataset = dataset;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_midi_file, parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.text.setText(mDataset.get(position).getName());
        holder.itemView.setOnCreateContextMenuListener(vC);
        selectedPath =mDataset.get(position).getPath();
        currentFile = mDataset.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.itemView.showContextMenu();
                Log.d("yo","teeeeeeeeeest");
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), mDataset.get(position).getPath(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(holder.itemView.getContext(), RecordActivity.class);
                intent.putExtra("FilePath", mDataset.get(position).getPath());
                holder.itemView.getContext().startActivity(intent);
            }
        });
        //zid .getName() itha 3adit model fi blasit String
        //holder.text.setText(mDataset.get(position).getTitle());
        //tinsech ism il package
        //tsawir zidhom bil resource manager


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private MenuItem.OnMenuItemClickListener mMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case 0:{
                    Log.d("path",selectedPath);
                    File fdelete = new File(selectedPath);
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            System.out.println("file Deleted :" + selectedPath);
                            mDataset.remove(currentFile);
                            notifyDataSetChanged();
                        } else {
                            System.out.println("file not Deleted :" + selectedPath);
                        }
                    }
                    return true;}

                case 1:
                    // do "Map"
                    return true;
            }

            return false;
        }
    };
    private View.OnCreateContextMenuListener vC = new View.OnCreateContextMenuListener() {

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Choose what To do !");

            menu.add(0, 0, 0, "Delete")
                    .setOnMenuItemClickListener(mMenuItemClickListener);
            menu.add(0, 1, 0, "Info")
                    .setOnMenuItemClickListener(mMenuItemClickListener);


        }
    };


}
