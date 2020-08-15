package com.bassemgharbi.androidpiano.Menu.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bassemgharbi.androidpiano.Menu.services.model.Model;
import com.bassemgharbi.androidpiano.Menu.services.repository.MenuRepository;

import java.util.List;

public class MenuViewModel extends ViewModel {
    private MutableLiveData<List<Model>> listMutableLiveData;
    private MenuRepository menuRepository;

    public void init(){
        if (listMutableLiveData!= null){
            return;
        }
        menuRepository = MenuRepository.getInstance();
        listMutableLiveData = menuRepository.getData();

    }

    public LiveData<List<Model>> getData() {
        return listMutableLiveData;
    }
}
