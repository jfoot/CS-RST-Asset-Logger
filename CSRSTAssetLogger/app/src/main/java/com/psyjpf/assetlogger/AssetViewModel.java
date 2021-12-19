package com.psyjpf.assetlogger;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;




public class AssetViewModel extends ViewModel   {

    public MutableLiveData<Asset> Asset = new MutableLiveData<>(new Asset());

    public Boolean isSetup = false;

    public AssetViewModel(){

    }




}
