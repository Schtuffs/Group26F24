package com.driveapp.driverater.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
    }

    public void SetText(String text) {
        System.out.println(mText.getValue());
        mText.setValue(text);
    }

    public LiveData<String> getText() {
        return mText;
    }
}