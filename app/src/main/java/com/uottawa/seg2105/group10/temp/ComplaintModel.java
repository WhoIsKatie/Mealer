package com.uottawa.seg2105.group10.temp;

public class ComplaintModel {

    String nameOfUser;
    String timeOfComplaint;
    String titleOfComplaint;
    String descriptionOfComplaint;
    String cookComplaint;


    public ComplaintModel(String nameOfUser, String timeOfComplaint, String titleOfComplaint, String descriptionOfComplaint, String cookComplaint) {
        this.nameOfUser = nameOfUser;
        this.timeOfComplaint = timeOfComplaint;
        this.titleOfComplaint = titleOfComplaint;
        this.descriptionOfComplaint = descriptionOfComplaint;
        this.cookComplaint = cookComplaint;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }
    public String getTimeOfComplaint() {
        return timeOfComplaint;
    }
    public String getTitleOfComplaint(){return titleOfComplaint;}
    public String getDescriptionOfComplaint(){return descriptionOfComplaint;}
    public String getCookComplaint(){return cookComplaint;}
}


