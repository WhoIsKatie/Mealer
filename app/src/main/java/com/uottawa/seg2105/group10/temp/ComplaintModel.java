package com.uottawa.seg2105.group10.temp;

import com.google.firebase.firestore.DocumentSnapshot;

public class ComplaintModel {

    String nameOfClient;
    String timeOfComplaint;
    String titleOfComplaint;
    String descriptionOfComplaint;
    String cookName;
    String cookUid, clientUid;
    private DocumentSnapshot document;


    public ComplaintModel(String nameOfClient, String timeOfComplaint, String titleOfComplaint, String descriptionOfComplaint, String cookName, String clientUid) {
        this.nameOfClient = nameOfClient;
        this.timeOfComplaint = timeOfComplaint;
        this.titleOfComplaint = titleOfComplaint;
        this.descriptionOfComplaint = descriptionOfComplaint;
        this.cookName = cookName;
        this.clientUid = clientUid;
    }
    public DocumentSnapshot getDocument(){
        return document;
    }

    public String getNameOfClient() {
        return nameOfClient;
    }
    public String getTimeOfComplaint() {
        return timeOfComplaint;
    }
    public String getTitleOfComplaint(){return titleOfComplaint;}
    public String getDescriptionOfComplaint(){return descriptionOfComplaint;}
    public String getCookName(){return cookName;}
    public String getClientUid(){return clientUid;}
}


