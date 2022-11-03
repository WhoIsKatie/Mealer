package com.uottawa.seg2105.group10.temp;

import com.google.firebase.firestore.DocumentSnapshot;

public class ComplaintModel {

    String nameOfCook;
    String timeOfComplaint;
    String titleOfComplaint;
    String descriptionOfComplaint;
    String cookUid, clientUid;
    private DocumentSnapshot document;


    public ComplaintModel(String nameOfCook, String timeOfComplaint, String titleOfComplaint, String descriptionOfComplaint, String cookUid, String clientUid) {
        this.nameOfCook = nameOfCook;
        this.timeOfComplaint = timeOfComplaint;
        this.titleOfComplaint = titleOfComplaint;
        this.descriptionOfComplaint = descriptionOfComplaint;
        this.cookUid = cookUid;
        this.clientUid = clientUid;
    }
    public DocumentSnapshot getDocument(){
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
}


