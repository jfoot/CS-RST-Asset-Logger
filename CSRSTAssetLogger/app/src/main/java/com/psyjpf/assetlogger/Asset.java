package com.psyjpf.assetlogger;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Entity;

@Entity
public class Asset implements Parcelable  {
    public MutableLiveData<String> ComputerName = new MutableLiveData<>();
    public MutableLiveData<String> IpAddr = new MutableLiveData<>();
    public MutableLiveData<String> MacAddr = new MutableLiveData<>();
    public MutableLiveData<String> SerialNumber = new MutableLiveData<>();
    public MutableLiveData<String> CompManufacturer = new MutableLiveData<>();
    public MutableLiveData<String> CompModel = new MutableLiveData<>();
    public MutableLiveData<String> CompModelAlt = new MutableLiveData<>();
    public MutableLiveData<String> Os = new MutableLiveData<>();
    public MutableLiveData<String> AssetTag = new MutableLiveData<>("");


    public void setAssetTag(String tag){
        if(AssetTag.getValue().trim().equals("") && !tag.trim().equals(""))
            AssetTag.setValue(tag);
    }



    public void merge(Asset other){
        this.ComputerName.postValue(other.ComputerName.getValue());
        this.IpAddr.postValue(other.IpAddr.getValue());
        this.MacAddr.postValue(other.MacAddr.getValue());
        this.SerialNumber.postValue(other.SerialNumber.getValue());
        this.CompManufacturer.postValue(other.CompManufacturer.getValue());
        this.CompModel.postValue(other.CompModel.getValue());
        this.CompModelAlt.postValue(other.CompModelAlt.getValue());
        this.Os.postValue(other.Os.getValue());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ComputerName.getValue());
        dest.writeString(IpAddr.getValue());
        dest.writeString(MacAddr.getValue());
        dest.writeString(SerialNumber.getValue());
        dest.writeString(CompManufacturer.getValue());
        dest.writeString(CompModel.getValue());
        dest.writeString(CompModelAlt.getValue());
        dest.writeString(Os.getValue());
        dest.writeString(AssetTag.getValue());
    }

    public Asset(){

    }

    private Asset(Parcel in) {
        ComputerName.setValue(in.readString());
        IpAddr.setValue(in.readString());
        MacAddr.setValue(in.readString());
        SerialNumber.setValue(in.readString());
        CompManufacturer.setValue(in.readString());
        CompModel.setValue(in.readString());
        CompModelAlt.setValue(in.readString());
        Os.setValue(in.readString());
        AssetTag.setValue(in.readString());
    }

    public static final Parcelable.Creator<Asset> CREATOR = new Parcelable.Creator<Asset>() {
        public Asset createFromParcel(Parcel in) {
            return new Asset(in);
        }

        public Asset[] newArray(int size) {
            return new Asset[size];
        }
    };
}
