package com.example.mytry.ui.shop;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ShopViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

//    private MutableLiveData<List<>>

    public ShopViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is shop fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
