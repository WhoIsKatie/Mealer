package com.uottawa.seg2105.group10;

import static org.junit.Assert.assertEquals;

import com.uottawa.seg2105.group10.backend.Meal;
import com.uottawa.seg2105.group10.backend.Purchase;

import org.junit.Test;

import java.util.ArrayList;

public class Del4DelMainTests {

    public Purchase purchaseForTest = new Purchase("5:00", "cookUID",  "clientUID",  "putine",  "imageID",  "6:00",  "");



    @Test
    public void testgetPickUpTime() {

        assertEquals("Your getPickUpTime method is incorrect", "6:00", purchaseForTest.getPickUpTime());


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
