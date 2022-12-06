package com.uottawa.seg2105.group10.recyclers;

public class ComplaintModel{

    String cookName;
    String clientName;
    String timeOfComplaint;
    String titleOfComplaint;
    String descriptionOfComplaint;
    String cookUid, clientUid;
    String docID;
    boolean status;



    public ComplaintModel(String nameOfClient, String nameOfCook,String timeOfComplaint, String titleOfComplaint, String descriptionOfComplaint, String cookUid, String clientUid){
        this.clientName = nameOfClient;
        this.cookName = nameOfCook;
        this.timeOfComplaint = timeOfComplaint;
        this.titleOfComplaint = titleOfComplaint;
        this.descriptionOfComplaint = descriptionOfComplaint;
        this.cookUid = cookUid;
        this.clientUid = clientUid;
        this.status = true;
    }

    public String getDocID(){
        return docID;
    }

    public String getNameOfCook() {
        return cookName;
    }
    public String getClientName() {return clientName;}
    public String getTimeOfComplaint() {
        return timeOfComplaint;
    }
    public String getTitleOfComplaint(){return titleOfComplaint;}
    public String getDescriptionOfComplaint(){return descriptionOfComplaint;}
    public String getCookUid(){return cookUid;}
    public String getClientUid(){return clientUid;}

}


