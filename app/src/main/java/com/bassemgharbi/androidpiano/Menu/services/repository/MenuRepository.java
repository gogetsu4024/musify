package com.bassemgharbi.androidpiano.Menu.services.repository;

import androidx.lifecycle.MutableLiveData;

import com.bassemgharbi.androidpiano.Menu.services.model.Model;
import com.bassemgharbi.androidpiano.R;

import java.util.ArrayList;
import java.util.List;

public class MenuRepository {
    static final ArrayList<Model> menuItems = new ArrayList<>();
    private static MenuRepository single_instance = null;

    public static MenuRepository getInstance(){
        if (single_instance ==null){
            single_instance = new MenuRepository();
        }
        return single_instance;
    }
    public MutableLiveData<List<Model>> getData() {
        setMenu();
        final MutableLiveData<List<Model>> data =
                new MutableLiveData<>();
        data.setValue(menuItems);
        return data;
    }

    private void setMenu(){
        menuItems.add(new Model(R.drawable.the_bridge, "studio mode", "This is the place where all your creativity comes to Play. Make your own Song..."));
        menuItems.add(new Model(R.drawable.improvmode, "Improve Mode", "Put a Backing Track and improvise along to it."));
        menuItems.add(new Model(R.drawable.drums2, "drums mode", "this is your own personal drums in the touch of your hands"));

    }
}
