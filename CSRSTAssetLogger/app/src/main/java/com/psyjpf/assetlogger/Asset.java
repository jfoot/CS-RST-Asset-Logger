package com.psyjpf.assetlogger;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Asset extends ViewModel {
    public MutableLiveData<String> ComputerName;
    public MutableLiveData<String> IpAddr;
    public MutableLiveData<String> MacAddr;
    public MutableLiveData<String> SerialNumber;
    public MutableLiveData<String> CompManufacturer;
    public MutableLiveData<String> CompModel;
    public MutableLiveData<String> CompModelAlt;
    public MutableLiveData<String> Os;
    public MutableLiveData<String> AssetTag;


    public void merge(Asset other){
        this.ComputerName.setValue(other.ComputerName.getValue());
        this.IpAddr.setValue(other.IpAddr.getValue());
        this.MacAddr.setValue(other.MacAddr.getValue());
        this.SerialNumber.setValue(other.SerialNumber.getValue());
        this.CompManufacturer.setValue(other.CompManufacturer.getValue());
        this.CompModel.setValue(other.CompModel.getValue());
        this.CompModelAlt.setValue(other.CompModelAlt.getValue());
        this.Os.setValue(other.Os.getValue());
    }
}
