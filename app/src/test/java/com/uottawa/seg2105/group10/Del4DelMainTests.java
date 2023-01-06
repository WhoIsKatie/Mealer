package com.uottawa.seg2105.group10;

import static org.junit.Assert.assertEquals;

import com.uottawa.seg2105.group10.repositories.Purchase;

import org.junit.Test;

public class Del4DelMainTests {

    public Purchase purchaseForTest = new Purchase("5:00", "cookUID",  "clientUID",  "poutine",  "imageID",  "6:00",  "", "", "APPROVED", false, false);



    @Test
    public void testgetPickUpTime() {

        assertEquals("Your getPickUpTime method is incorrect", "6:00", purchaseForTest.getPickupTime());


    }
    @Test
    public void testgetRequestTime() {

        assertEquals("Your getPickUpTime method is incorrect", "5:00", purchaseForTest.getRequestTime());


    }    @Test
    public void testgetCookUID() {

        assertEquals("Your getPickUpTime method is incorrect", "cookUID", purchaseForTest.getCookUID());


    }    @Test
    public void testgetClientName() {

        assertEquals("Your getPickUpTime method is incorrect", "clientUID", purchaseForTest.getClientUID());


    }






}
