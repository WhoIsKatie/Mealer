package com.uottawa.seg2105.group10.backend;

import com.google.firebase.auth.FirebaseUser;

public class Client extends User {

    private String ccNumber, ccHolderName, expiryDate, cvc;


    public Client(FirebaseUser user, String firstName, String lastName, String email, String password, String address) {
        super(user, firstName, lastName, email, password, address);
    }

    public boolean setCC(String num, String name, String expiry, String cvc) {
        if (expiry.matches("(?:0[1-9]|1[0-2])/[0-9]{2}") &&
            (ccNumber.length() == 16) && (cvc.length() == 3)) {
            ccNumber = num;
            ccHolderName = name;
            expiryDate = expiry;
            this.cvc = cvc;
            return true;
        }
        return false;
    }
}
