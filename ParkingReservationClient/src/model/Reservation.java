package model;

public class Reservation implements Model {
	private String platenumber;
	private String date;
	private String timeslot;
	private String locationID;
	
	public Reservation(String platenumber, String date, String timeslot, String locationID) {
		this.platenumber = platenumber;
		this.date = date;
		this.timeslot = timeslot;
		this.locationID = locationID;
	}

	public String getPlatenumber() {
		return platenumber;
	}

	public void setPlatenumber(String platenumber) {
		this.platenumber = platenumber;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(String timeslot) {
		this.timeslot = timeslot;
	}

	public String getLocationID() {
		return locationID;
	}

	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}
}
