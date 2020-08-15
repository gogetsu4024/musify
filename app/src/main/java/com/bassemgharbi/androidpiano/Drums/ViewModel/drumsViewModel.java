package com.bassemgharbi.androidpiano.Drums.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bassemgharbi.androidpiano.Drums.Services.repository.drumsRepository;
import com.bassemgharbi.androidpiano.improv.services.repository.InstrumentChooserRepository;

import java.util.List;

public class drumsViewModel extends ViewModel {
    private drumsRepository instrumentChooserRepository;

    public void init(){

        instrumentChooserRepository = drumsRepository.getInstance();

    }

    public drumsRepository getInstrumentChooserRepository() {
        return instrumentChooserRepository;
    }
}
