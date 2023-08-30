package com.example.monitorplantarpressure;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Boolean> data = new MutableLiveData<>();

    public void setData(boolean newData) {
        data.setValue(newData);
    }

    public LiveData<Boolean> getData() {
        return data;
    }
}
