package com.uottawa.seg2105.group10;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.backend.Admin;
import com.uottawa.seg2105.group10.backend.Cook;
import com.uottawa.seg2105.group10.backend.UserManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Main local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@SuppressWarnings("ConstantConditions")
@RunWith(MockitoJUnitRunner.class)
public class MainUnitTest {

    @Mock
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore dBase = FirebaseFirestore.getInstance();
    private DocumentReference[] complaints, cooks;

    @Before
    public void runBefore() {
        // Sally Silly: complaint dismissed
        // Michelle Phan: suspended temporarily, complaint dismissed, temporarily
        // Roberto Toe: suspended indefinitely, temporarily

        complaints = new DocumentReference[]{
                dBase.collection("complaints").document("tyhoJpuavvbAbcSfhpb7"),
                dBase.collection("complaints").document("W5fPnDccYVkNQEOdG2gr"),
                dBase.collection("complaints").document("4YreFWehP3i23w990jIj"),
                dBase.collection("complaints").document("3VMxGQUIpFKudKhuUUKF"),
                dBase.collection("complaints").document("ZgdBaK76HiWQHJ4WiK8M"),
                dBase.collection("complaints").document("pWoo0y8RpKLQaJJl0xrJ")};

        cooks = new DocumentReference[]{
                dBase.collection("users").document("BcoNqvaX8XcKAsm9ikPSDuBRJbH2"),
                dBase.collection("users").document("UiLo7nmfCubuDsk4k7WbDTVHNOG3"),
                dBase.collection("users").document("ui4w9EJS40RK4HLuJ3hymCSCMRk1")};
    }

    @Test
    public void testDismissComplaint() {
        Admin.dismissComplaint(complaints[1]);
        // Verifies if complaint document in Firebase Firestore was correctly modified
        assertTrue("cannot access complaint collection :(", complaints[1].get().isSuccessful());
        assertTrue("Complaint was deleted", complaints[1].get().getResult().exists());
        assertFalse("Complaint status remains active in database", (boolean) complaints[1].get().getResult().get("status"));

        // Verifies if user information document in Firebase Firestore was correctly modified
        assertTrue("cannot access user collection :(", cooks[1].get().isSuccessful());
        assertTrue("user doc was deleted", cooks[1].get().getResult().exists());
        assertFalse("cook was wrongly suspended in database", (boolean) cooks[1].get().getResult().get("status"));

        // Checks status of the local cook user account from UserManager
        Cook cook = UserManager.getCooks().get(complaints[1].get().getResult().get("cookUid"));
        assertNotNull("Mealer cook was deleted", cook);
        assertFalse("Mealer cook was wrongly suspended!", cook.isSuspended());
    }

    @Test
    public void testSuspendTemporarily() {
        LocalDateTime originalT = (LocalDateTime) cooks[2].get().getResult().get("suspensionEnd");
        Duration month = Duration.ofDays(30);
        Admin.suspendCook(complaints[2], month);
        Admin.suspendCook(complaints[2], month); // Should be redundant call as complaint[2] was already dismissed

        // Verifies if complaint document in Firebase Firestore was correctly modified
        assertTrue("cannot access complaint collection :(", complaints[2].get().isSuccessful());
        assertTrue("Complaint was deleted", complaints[2].get().getResult().exists());
        assertFalse("Complaint status remains active in database", (boolean) complaints[2].get().getResult().get("status"));

        // Verifies if user information document in Firebase Firestore was correctly modified
        assertTrue("cannot access user collection :(", cooks[2].get().isSuccessful());
        assertTrue("user doc was deleted", cooks[2].get().getResult().exists());
        assertTrue("cook suspension was not updated in database", (boolean) cooks[2].get().getResult().get("status"));
        assertEquals(LocalDateTime.now().plus(month), cooks[2].get().getResult().get("suspensionEnd"));

        // Checks status of the local cook user account from UserManager
        Cook cook = UserManager.getCooks().get(complaints[2].get().getResult().get("cookUid"));
        assertNotNull("Mealer cook was deleted", cook);
        assertTrue("Mealer cook was not suspended!", cook.isSuspended());
        assertEquals(LocalDateTime.now().plus(month), cook.getSuspensionEnd());

        assertEquals(cooks[2].get().getResult().get("suspensionEnd"), cook.getSuspensionEnd());
        assertNotEquals(originalT, cook.getSuspensionEnd());
    }

    @Test
    public void testSuspendIndefinitely() {
        Admin.suspendCook(complaints[3]);

        // Verifies if complaint document in Firebase Firestore was correctly modified
        assertTrue("cannot access complaint collection :(", complaints[3].get().isSuccessful());
        assertTrue("Complaint was deleted", complaints[3].get().getResult().exists());
        assertFalse("Complaint status remains active in database", (boolean) complaints[3].get().getResult().get("status"));

        // Verifies if user information document in Firebase Firestore was correctly modified
        assertTrue("cannot access user collection :(", cooks[3].get().isSuccessful());
        assertTrue("user doc was deleted", cooks[3].get().getResult().exists());
        assertTrue("cook suspension was not updated in database", (boolean) cooks[3].get().getResult().get("status"));
        assertNull("Suspension length was incorrectly established in database", cooks[3].get().getResult().get("suspensionEnd"));

        // Checks status of the local cook user account from UserManager
        Cook cook = UserManager.getCooks().get(complaints[3].get().getResult().get("cookUid"));
        assertNotNull("Mealer cook was deleted", cook);
        assertTrue("Mealer cook was not suspended!", cook.isSuspended());
        assertNull("Suspension length was incorrectly established", cook.getSuspensionEnd());

        assertEquals(cooks[3].get().getResult().get("suspensionEnd"), cook.getSuspensionEnd());
    }

    @Test
    public void testDismissComplaintForSuspendedCook() {
        LocalDateTime originalT = (LocalDateTime) cooks[2].get().getResult().get("suspensionEnd");
        Admin.dismissComplaint(complaints[5]);
        // Verifies if complaint document in Firebase Firestore was correctly modified
        assertTrue("cannot access complaint collection :(", complaints[5].get().isSuccessful());
        assertTrue("Complaint was deleted", complaints[5].get().getResult().exists());
        assertFalse("Complaint status remains active in database", (boolean) complaints[5].get().getResult().get("status"));

        // Verifies if user information document in Firebase Firestore was correctly modified
        assertTrue("cannot access user collection :(", cooks[2].get().isSuccessful());
        assertTrue("user doc was deleted", cooks[2].get().getResult().exists());
        assertTrue("original cook suspension was removed in database", (boolean) cooks[2].get().getResult().get("status"));
        assertEquals(originalT, cooks[2].get().getResult().get("suspensionEnd"));

        // Checks status of the local cook user account from UserManager
        Cook cook = UserManager.getCooks().get(complaints[5].get().getResult().get("cookUid"));
        assertNotNull("Mealer cook was deleted", cook);
        assertTrue("Mealer cook suspension was removed!", cook.isSuspended());
        assertEquals(originalT, cook.getSuspensionEnd());
    }

    @Test
    public void testIncreaseSuspension() {
        LocalDateTime originalT = (LocalDateTime) cooks[2].get().getResult().get("suspensionEnd");
        Duration threeMonths = Duration.ofDays(90);
        Admin.suspendCook(complaints[6], threeMonths);

        // Verifies if complaint document in Firebase Firestore was correctly modified
        assertTrue("cannot access complaint collection :(", complaints[2].get().isSuccessful());
        assertTrue("Complaint was deleted", complaints[2].get().getResult().exists());
        assertFalse("Complaint status remains active in database", (boolean) complaints[2].get().getResult().get("status"));

        // Verifies if user information document in Firebase Firestore was correctly modified
        assertTrue("cannot access user collection :(", cooks[2].get().isSuccessful());
        assertTrue("user doc was deleted", cooks[2].get().getResult().exists());
        assertTrue("cook suspension was set to false in database", (boolean) cooks[2].get().getResult().get("status"));
        assertEquals(originalT.plus(threeMonths), cooks[2].get().getResult().get("suspensionEnd"));

        // Checks status of the local cook user account from UserManager
        Cook cook = UserManager.getCooks().get(complaints[2].get().getResult().get("cookUid"));
        assertNotNull("Mealer cook was deleted", cook);
        assertTrue("Mealer cook suspension was removed!", cook.isSuspended());
        assertEquals(originalT.plus(threeMonths), cook.getSuspensionEnd());

        assertEquals(cooks[2].get().getResult().get("suspensionEnd"), cook.getSuspensionEnd());

    }

    @Test
    public void testSuspendTempOnIndefinitelySuspended() {
        Duration month = Duration.ofDays(30);
        Admin.suspendCook(complaints[4], month);

        // Verifies if complaint document in Firebase Firestore was correctly modified
        assertTrue("cannot access complaint collection :(", complaints[4].get().isSuccessful());
        assertTrue("Complaint was deleted", complaints[4].get().getResult().exists());
        assertFalse("Complaint status remains active in database", (boolean) complaints[4].get().getResult().get("status"));

        // Verifies if user information document in Firebase Firestore was correctly modified
        assertTrue("cannot access user collection :(", cooks[3].get().isSuccessful());
        assertTrue("user doc was deleted", cooks[3].get().getResult().exists());
        assertTrue("cook suspension was not updated in database", (boolean) cooks[3].get().getResult().get("status"));
        assertNull("Suspension length was incorrectly established in database", cooks[3].get().getResult().get("suspensionEnd"));

        // Checks status of the local cook user account from UserManager
        Cook cook = UserManager.getCooks().get(complaints[4].get().getResult().get("cookUid"));
        assertNotNull("Mealer cook was deleted", cook);
        assertTrue("Mealer cook was not suspended!", cook.isSuspended());
        assertNull("Suspension length was incorrectly established - overrode original indefinite suspension", cook.getSuspensionEnd());
    }
}