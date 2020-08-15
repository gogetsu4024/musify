package com.bassemgharbi.androidpiano.Drums.view.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bassemgharbi.androidpiano.Drums.ViewModel.drumsViewModel;
import com.bassemgharbi.androidpiano.R;

import it.beppi.knoblibrary.Knob;


public class drumsFragment extends Fragment  {

    private View thisView;
    private com.bassemgharbi.androidpiano.Drums.ViewModel.drumsViewModel drumsViewModel;
    private Knob knob;
    private Button snare,crashcymbal1,hihat,hightom,basedrum1,himidtom,lowfloortom,ridecymbal,crashcymbal,lowmidtom,maracas;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView =inflater.inflate(R.layout.fragment_drums, container, false);


        drumsViewModel = ViewModelProviders.of(this).get(drumsViewModel.class);
        drumsViewModel.init();
        snare = thisView.findViewById(R.id.snare);
        crashcymbal1 = thisView.findViewById(R.id.crashcymbal1);
        hihat = thisView.findViewById(R.id.hihat);
        hightom = thisView.findViewById(R.id.hightom);
        basedrum1 = thisView.findViewById(R.id.basedrum1);
        himidtom = thisView.findViewById(R.id.himidtom);
        lowmidtom = thisView.findViewById(R.id.lowmidtom);
        lowfloortom = thisView.findViewById(R.id.lowfloortom);
        ridecymbal = thisView.findViewById(R.id.ridecymbal);
        crashcymbal = thisView.findViewById(R.id.crashcymbal);
        maracas = thisView.findViewById(R.id.maracas);
        snare.setOnTouchListener((e,v)->{
            if (v.getAction() == MotionEvent.ACTION_DOWN) {
                drumsViewModel.getInstrumentChooserRepository().playNote(38);
                return true;
            }
            return false;
        });
        maracas.setOnTouchListener((e,v)->{
            if (v.getAction() == MotionEvent.ACTION_DOWN) {
                drumsViewModel.getInstrumentChooserRepository().playNote(70);
                return true;
            }
            return false;
        });
        crashcymbal1.setOnTouchListener((e,v)->{
            if (v.getAction() == MotionEvent.ACTION_DOWN) {
                drumsViewModel.getInstrumentChooserRepository().playNote(49);
                return true;
            }
            return false;
        });
        hihat.setOnTouchListener((e,v)->{
            if (v.getAction() == MotionEvent.ACTION_DOWN) {
                drumsViewModel.getInstrumentChooserRepository().playNote(46);
                return true;
            }
            return false;
        });
        hightom.setOnTouchListener((e,v)->{
            if (v.getAction() == MotionEvent.ACTION_DOWN) {
                drumsViewModel.getInstrumentChooserRepository().playNote(50);
                return true;
            }
            return false;
        });
        basedrum1.setOnTouchListener((e,v)->{
            if (v.getAction() == MotionEvent.ACTION_DOWN) {
                drumsViewModel.getInstrumentChooserRepository().playNote(36);
                return true;
            }
            return false;
        });
        himidtom.setOnTouchListener((e,v)->{
            if (v.getAction() == MotionEvent.ACTION_DOWN) {
                drumsViewModel.getInstrumentChooserRepository().playNote(48);
                return true;
            }
            return false;
        });
        lowfloortom.setOnTouchListener((e,v)->{
            if (v.getAction() == MotionEvent.ACTION_DOWN) {
                drumsViewModel.getInstrumentChooserRepository().playNote(41);
                return true;
            }
            return false;
        });
        ridecymbal.setOnTouchListener((e,v)->{
            if (v.getAction() == MotionEvent.ACTION_DOWN) {
                drumsViewModel.getInstrumentChooserRepository().playNote(51);
                return true;
            }
            return false;
        });
        crashcymbal.setOnTouchListener((e,v)->{
            if (v.getAction() == MotionEvent.ACTION_DOWN) {

                drumsViewModel.getInstrumentChooserRepository().playNote(49);
                return true;
            }
            return false;
        });
        lowmidtom.setOnTouchListener((e,v)->{
            if (v.getAction() == MotionEvent.ACTION_DOWN) {

                drumsViewModel.getInstrumentChooserRepository().playNote(47);
                return true;
            }
            return false;
        });




        return thisView;
    }



}
