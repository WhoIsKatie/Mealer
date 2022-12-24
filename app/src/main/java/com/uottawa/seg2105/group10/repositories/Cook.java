package com.uottawa.seg2105.group10.repositories;

import com.google.firebase.firestore.DocumentReference;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class Cook extends User{
	private String description, address, cheque;
	private boolean suspended = false;
	private static final String TAG = "Cook.java";
	private String suspensionEnd = null;
	private DocumentReference userDoc = null;

	private double ratingSum = 0.0;
	private int completedOrders, numReviews;

	/** Constructor for Firebase access.
	 *  Do not use locally unless you want an empty cook user.
	 */
	public Cook() {
		super("Cook Class", "Tess", "Harper", "tessharp@outlook.com", "pass123!", "Cook", "");
		ratingSum = 0.0;
		completedOrders = 0;
		numReviews = 0;
	}

	public Cook(Map<String, String> data) {
		super("Cook Class",
				data.get("firstName"),
				data.get("lastName"),
				data.get("email"),
				data.get("password"),
				"Cook",
				data.get("uid"));
		description = data.get("description");
		address = data.get("address");
		cheque = data.get("cheque");
		ratingSum = 0.0;
		completedOrders = 0;
		numReviews = 0;
	}

	/*public Cook(String firstName, String lastName, String email, String password, String address,
				String description, String cheque, int orders, int reviews, double ratingSum, String uid) {
		super("Cook Class",
				firstName,
				lastName,
				email,
				password,
				"Cook",
				uid);
		this.description = description;
		this.address = address;
		this.cheque = cheque;
		this.ratingSum = ratingSum;
		completedOrders = orders;
		numReviews = reviews;
	}*/

	//Getter methods
	public double getRating() {return ratingSum/numReviews;}
	public double getRatingSum() {return ratingSum;}
	public int getNumReviews() {return numReviews;}
	public int getCompletedOrders() {return completedOrders;}
	public String getSuspensionEnd() {return suspensionEnd;}
	public String getDescription() {return description;}
	public String getAddress() {return address;}
	public String getCheque() {return cheque;}

	public void setSuspended(){suspended = true;}
	public boolean isSuspended() {return suspended;}

	public void incrementCompletedOrder() {completedOrders++;}

	public boolean addRating(double x) {
		if (x > 5 || x < 0) {
			return false;
		}
		ratingSum += x;
		numReviews++;
		return true;
		//return this.updateFireStore();
	}

	public boolean addSuspension(Duration length) {
		if (length != null) {
			if (length.isZero() || length.isNegative()) length = null;
			if (suspensionEnd != null)
				suspensionEnd = LocalDateTime.parse(suspensionEnd).plus(length).toString();
			else {
				if (length != null && suspended == false)
					suspensionEnd = LocalDateTime.now().plus(length).toString();
				else suspensionEnd = null;
			}

		} else suspensionEnd = null;
		suspended = true;
		return true;
		//return this.updateFireStore();
	}

}