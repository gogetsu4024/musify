package com.bassemgharbi.androidpiano.improv.view.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bassemgharbi.androidpiano.R;
import com.bassemgharbi.androidpiano.improv.services.model.Instrument;
import com.bassemgharbi.androidpiano.improv.view.adapters.MyAdapter;
import com.bassemgharbi.androidpiano.improv.viewModel.InstrumentChooserViewModel;

import java.util.List;


public class InstrumentsChooserFragment extends DialogFragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View thisView;
    private InstrumentChooserViewModel instrumentChooserViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisView =inflater.inflate(R.layout.fragment_fire_missiles_dialog, container);
        recyclerView = (RecyclerView) thisView.findViewById(R.id.my_recycler_view);

        instrumentChooserViewModel = ViewModelProviders.of(this)
                .get(InstrumentChooserViewModel.class);
        instrumentChooserViewModel.init();
        instrumentChooserViewModel.getInstruments().observe(this, new Observer<List<Instrument>>() {
            @Override
            public void onChanged(List<Instrument> instruments) {
                mAdapter.notifyDataSetChanged();
            }
        });

        initRecyclerView();

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

    public static InstrumentsChooserFragment newInstance(String title) {
        InstrumentsChooserFragment frag = new InstrumentsChooserFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }
    public void initRecyclerView(){
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(instrumentChooserViewModel.getInstruments().getValue(),getContext(),getDialog());
        recyclerView.setAdapter(mAdapter);
    }
}