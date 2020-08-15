package com.bassemgharbi.androidpiano.recording.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bassemgharbi.androidpiano.recording.services.model.InstrumentRecord;
import com.bassemgharbi.androidpiano.recording.services.repository.InstrumentPlayerRepository;
import com.bassemgharbi.androidpiano.recording.services.repository.InstrumentRecordRepository;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.meta.Tempo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstrumentRecordViewModel extends ViewModel {

    private MutableLiveData<List<InstrumentRecord>> instrumentMutableLiveData;
    private InstrumentRecordRepository instrumentRecordRepository;
    private InstrumentPlayerRepository instrumentPlayerRepository;
    private MutableLiveData<Boolean> fileMutableLiveData = new MutableLiveData<>();

    public void init(String path) throws IOException {
        if (instrumentMutableLiveData!= null){
            return;
        }
        instrumentRecordRepository = InstrumentRecordRepository.getInstance(path);
        instrumentPlayerRepository = InstrumentPlayerRepository.getInstance(path);
        instrumentMutableLiveData = instrumentRecordRepository.getTracks(path);
        fileMutableLiveData = instrumentRecordRepository.getInputChanged();

    }

    public LiveData<List<InstrumentRecord>> getTracks() {
        return instrumentMutableLiveData;
    }
    public void clear(){
        InstrumentRecordRepository.clear();
        InstrumentPlayerRepository.clear();
        instrumentRecordRepository =null;
        instrumentPlayerRepository = null;
        instrumentMutableLiveData = null;
    }
    public LiveData<Boolean> getFile(){
        return fileMutableLiveData;
    }
    public void notifyChange(){
        fileMutableLiveData.setValue(true);
    }

    public InstrumentPlayerRepository getInstrumentPlayerRepository() {
        return instrumentPlayerRepository;
    }
}
