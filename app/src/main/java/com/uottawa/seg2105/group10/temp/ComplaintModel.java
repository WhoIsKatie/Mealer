package com.uottawa.seg2105.group10.temp;

public class ComplaintModel{

    String nameOfCook;
    String timeOfComplaint;
    String titleOfComplaint;
    String descriptionOfComplaint;
    String cookUid, clientUid;
    String docID;


    public ComplaintModel(String docID, String nameOfCook, String timeOfComplaint, String titleOfComplaint, String descriptionOfComplaint, String cookUid, String clientUid) {
        this.nameOfCook = nameOfCook;
        this.timeOfComplaint = timeOfComplaint;
        this.titleOfComplaint = titleOfComplaint;
        this.descriptionOfComplaint = descriptionOfComplaint;
        this.cookUid = cookUid;
        this.clientUid = clientUid;
        this.docID = docID;
    }

    public String getDocID(){
        return docID;
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


