/*
package com.uottawa.seg2105.group10;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.uottawa.seg2105.group10.backend.Admin;
import com.uottawa.seg2105.group10.backend.Client;
import com.uottawa.seg2105.group10.backend.Cook;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

*/
/**
 * Main local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 *//*

public class MainUnitTest {

    Client testClient;
    Cook testCook;

    @Before
    public void runBefore() {
        testClient = new Client();
        testCook = new Cook();
    }

    */
/**Verifies if all Admin user instances refer to the same singular instance.*//*

    @Test
    public void testAdminInstance() {
        Admin testAdmin = Admin.getInstance();
        Admin testAdmin2 = Admin.getInstance();
        assertEquals("Admin does not implement singleton pattern!", testAdmin, testAdmin2);
    }

    */
/**Verifies if Client class' setCC() method validates credit card information.*//*

    @Test
    public void testClientCCVerifEmptyString() {
        assertFalse("TestEmptyString1 Failed!", testClient.setCC("", "", "", ""));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());

        testClient = new Client();
        assertFalse("TestEmptyString2 Failed!", testClient.setCC("", "First Last", "0101", "123"));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());

        testClient = new Client();
        assertFalse("TestEmptyString3 Failed!", testClient.setCC("1234567890123456", "", "0101", "123"));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());

        testClient = new Client();
        assertFalse("TestEmptyString4 Failed!", testClient.setCC("1234567890123456", "First Last", "", "123"));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());

        testClient = new Client();
        assertFalse("TestEmptyString5 Failed!", testClient.setCC("1234567890123456", "First Last", "0101", ""));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());
    }

    @Test
    public void testClientCCVerifNum() {
        assertFalse("TestInvalidCardNum1 Failed!", testClient.setCC("123456789012345", "First Last", "0101", "123"));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());

        testClient = new Client();
        assertFalse("TestInvalidCardNum2 Failed!", testClient.setCC("123456789012345!", "First Last", "0101", "123"));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());

        testClient = new Client();
        assertFalse("TestInvalidCardNum3 Failed!", testClient.setCC("12345678901234567", "First Last", "0101", "123"));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());
    }

    @Test
    public void testClientCCVerifExp() {
        assertFalse("TestInvalidExpiry1 Failed!", testClient.setCC("1234567890123456", "First Last", "2325", "123"));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());

        testClient = new Client();
        assertFalse("TestInvalidExpiry2 Failed!", testClient.setCC("1234567890123456", "First Last", "0000", "123"));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());

        testClient = new Client();
        assertFalse("TestInvalidExpiry3 Failed!", testClient.setCC("1234567890123456", "First Last", "DATE", "123"));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());

        testClient = new Client();
        assertFalse("TestInvalidExpiry4 Failed!", testClient.setCC("1234567890123456", "First Last", "@1#2", "123"));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());
    }

    @Test
    public void testClientCCVerifCVC() {
        assertFalse("TestInvalidCVC1 Failed!", testClient.setCC("1234567890123456", "First Last", "0101", "cvc"));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());

        testClient = new Client();
        assertFalse("TestInvalidCVC2 Failed!", testClient.setCC("1234567890123456", "First Last", "0101", "!23"));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());

        testClient = new Client();
        assertFalse("TestInvalidCVC3 Failed!", testClient.setCC("1234567890123456", "First Last", "0101", "12"));
        assertNull(testClient.getCcNumber());
        assertNull(testClient.getCcHolderName());
        assertNull(testClient.getExpiryDate());
        assertNull(testClient.getCvc());
    }

    @Test
    public void testClientCCVerifSuccess() {
        assertTrue("TestSetCC Failed!", testClient.setCC("1234567890123456", "First Last", "0101", "123"));
        assertEquals("1234567890123456", testClient.getCcNumber());
        assertEquals("First Last", testClient.getCcHolderName());
        assertEquals("0101", testClient.getExpiryDate());
        assertEquals("123", testClient.getCvc());
    }

    */
/**Tests Cook class' setSuspension() method.*//*

    @Test
    public void testSuspendCook0Duration() {
        assertFalse("Cook was instantiated with active suspension!", testCook.isSuspended());
        assertTrue("addSuspension failed to verify duration length!", (testCook.addSuspension(Duration.ofDays(0))));
        assertTrue("addSuspension failed to verify duration length!", testCook.isSuspended());
        assertEquals(null, testCook.getSuspensionEnd());
    }

    @Test
    public void testSuspendCookIndefinitely() {
        assertFalse("Cook was instantiated with active suspension!", testCook.isSuspended());
        assertTrue("addSuspension failed to suspend cook!", (testCook.addSuspension(null)));
        assertTrue("addSuspension failed to suspend cook!", testCook.isSuspended());
        assertEquals(null, testCook.getSuspensionEnd());
    }

    @Test
    public void testSuspendCookTemporarily() {
        assertFalse("Cook was instantiated with active suspension!", testCook.isSuspended());
        assertTrue("addSuspension failed to suspend cook!", (testCook.addSuspension(Duration.ofDays(10))));
        assertTrue("addSuspension failed to suspend cook!", testCook.isSuspended());
        assertEquals(LocalDateTime.now().plus(Duration.ofDays(10)).truncatedTo(ChronoUnit.SECONDS), testCook.getSuspensionEnd().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    public void testIncreaseSuspension() {
        assertFalse("Cook was instantiated with active suspension!", testCook.isSuspended());
        assertTrue("addSuspension failed to suspend cook!", (testCook.addSuspension(Duration.ofDays(10))));
        assertTrue("addSuspension failed to suspend cook!", testCook.isSuspended());
        assertEquals(LocalDateTime.now().plus(Duration.ofDays(10)).truncatedTo(ChronoUnit.SECONDS), testCook.getSuspensionEnd().truncatedTo(ChronoUnit.SECONDS));

        LocalDateTime originalT = testCook.getSuspensionEnd();
        assertTrue((testCook.addSuspension(Duration.ofDays(10))));
        assertTrue(testCook.isSuspended());
        assertEquals("addSuspension() failed to increase suspension duration!",
                originalT.plus(Duration.ofDays(10)).truncatedTo(ChronoUnit.SECONDS), testCook.getSuspensionEnd().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    public void testSuspendTemporarilyOnIndefinitelySuspended() {
        assertFalse("Cook was instantiated with active suspension!", testCook.isSuspended());
        assertTrue("addSuspension failed to suspend cook!", (testCook.addSuspension(null)));
        assertTrue("addSuspension failed to suspend cook!", testCook.isSuspended());
        assertEquals(null, testCook.getSuspensionEnd());

        assertTrue((testCook.addSuspension(Duration.ofDays(10))));
        assertTrue(testCook.isSuspended());
        assertEquals("addSuspension() overrode original suspension with temporary!", null, testCook.getSuspensionEnd());
    }
}*/
