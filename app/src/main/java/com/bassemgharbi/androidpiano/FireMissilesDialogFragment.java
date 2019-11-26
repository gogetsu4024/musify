package com.bassemgharbi.androidpiano;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bassemgharbi.androidpiano.Instruments.Instruments;

import java.util.ArrayList;
import java.util.List;


public class FireMissilesDialogFragment extends DialogFragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View thisView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisView =inflater.inflate(R.layout.fragment_fire_missiles_dialog, container);
        recyclerView = (RecyclerView) thisView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(Instruments.getInstruments(),getContext(),getDialog());
        Log.d("asd",Instruments.getInstruments().toString());
        recyclerView.setAdapter(mAdapter);

        return thisView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
    }

    public static FireMissilesDialogFragment newInstance(String title) {
        FireMissilesDialogFragment frag = new FireMissilesDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }
}