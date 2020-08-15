package com.bassemgharbi.androidpiano.improv.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bassemgharbi.androidpiano.improv.services.model.Instrument;
import com.bassemgharbi.androidpiano.improv.services.repository.InstrumentChooserRepository;

import java.util.List;

public class InstrumentChooserViewModel extends ViewModel {
    private MutableLiveData<List<Instrument>> instrumentMutableLiveData;
    private InstrumentChooserRepository instrumentChooserRepository;

    public void init(){
        if (instrumentMutableLiveData!= null){
            return;
        }
        instrumentChooserRepository = InstrumentChooserRepository.getInstance();
        instrumentMutableLiveData = instrumentChooserRepository.getInstruments();

    }

    public LiveData<List<Instrument>> getInstruments() {
        return instrumentMutableLiveData;
    }

}
