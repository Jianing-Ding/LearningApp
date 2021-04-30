package com.example.mytry.ui.onLineTest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class onlineTestViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public onlineTestViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}