package edu.ucla.cs.cs144;

public class Bid {
    String rating;
    String id;
    String location;
    String country;
    String time;
    String amt;
public void setBidderRating(String r) { rating = r; }
public void setBidderId(String r) { id = r; }
public void setBidderLocation(String r) { location = r; }
public void setBidderCountry(String r) { country = r; }
public void setTime(String r) { time = r; }
public void setAmount(String r) { amt = r; }


public String getBidderRating() { return rating;}
public String getBidderId() { return id;} 
public String getBidderLocation() { return location;} 
public String getBidderCountry() { return country;} 
public String getTime() { return time;} 
public String getAmount() { return amt;} 
}
