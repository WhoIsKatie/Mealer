package com.uottawa.seg2105.group10.temp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;

public class ComplaintModel implements Parcelable {

    String nameOfCook;
    String timeOfComplaint;
    String titleOfComplaint;
    String descriptionOfComplaint;
    String cookUid, clientUid;
    DocumentReference document;


    public ComplaintModel(DocumentReference document, String nameOfCook, String timeOfComplaint, String titleOfComplaint, String descriptionOfComplaint, String cookUid, String clientUid) {
        this.nameOfCook = nameOfCook;
        this.timeOfComplaint = timeOfComplaint;
        this.titleOfComplaint = titleOfComplaint;
        this.descriptionOfComplaint = descriptionOfComplaint;
        this.cookUid = cookUid;
        this.clientUid = clientUid;
        this.document = document;
    }

    protected ComplaintModel(Parcel in) {
        nameOfCook = in.readString();
        timeOfComplaint = in.readString();
        titleOfComplaint = in.readString();
        descriptionOfComplaint = in.readString();
        cookUid = in.readString();
        clientUid = in.readString();
    }

    public static final Creator<ComplaintModel> CREATOR = new Creator<ComplaintModel>() {
        @Override
        public ComplaintModel createFromParcel(Parcel in) {
            return new ComplaintModel(in);
        }

        @Override
        public ComplaintModel[] newArray(int size) {
            return new ComplaintModel[size];
        }
    };

    public DocumentReference getDocument(){
        return document;
    }

    public String getNameOfCook() {
        return nameOfCook;
    }
    public String getTimeOfComplaint() {
        return timeOfComplaint;
    }
    public String getTitleOfComplaint(){return titleOfComplaint;}
    public String getDescriptionOfComplaint(){return descriptionOfComplaint;}
    public String getCookUid(){return cookUid;}
    public String getClientUid(){return clientUid;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(nameOfCook);
        parcel.writeString(timeOfComplaint);
        parcel.writeString(titleOfComplaint);
        parcel.writeString(descriptionOfComplaint);
        parcel.writeString(cookUid);
        parcel.writeString(clientUid);
    }


}


