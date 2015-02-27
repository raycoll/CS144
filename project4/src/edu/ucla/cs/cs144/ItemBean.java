package edu.ucla.cs.cs144;
import java.util.ArrayList; 

public class ItemBean {
    String id;
    String name;
    ArrayList<String> cats;
    String cb;
    String fb;
    String nb;
    ArrayList<Bid> bids;
    String lat;
    String longitude;
    String loc;
    String country;
    String started;
    String ends;
    String srat;
    String sid;
    String desc;
    String bprice;
    ItemBean() { cats = new ArrayList<String>(); bids = new ArrayList<Bid>();}

    public void setId(String i) {id = i;}
    public void setName(String i) {name = i;}
    public void addCategory(String i) {cats.add(i); }
    public void setCurrentBid(String i) { cb = i;}
    public void setFirstBid(String i) {fb = i;}
    public void setNumBids(String i) {nb = i;}
    public void addBid(Bid i) {bids.add(i); }
    public void setLatitude(String i) {lat = i;}
    public void setLongitude(String i) {longitude = i;}
    public void setLocation(String i) {loc = i;}
    public void setCountry(String i) {country = i;}
    public void setStarted(String i) {started = i;}
    public void setEnds(String i) {ends = i;}
    public void setSellerRating(String i) {srat = i;}
    public void setSellerId(String i) {sid = i;}
    public void setDescription(String i) {desc = i;}
    public void setBuyPrice(String i) {bprice = i;}

    public String getId() {return id; }
    public String getName() {return name; } 
    public String[] getCategories() {return cats.toArray(new String[cats.size()]); } 
    public String getCurrentBid() {return cb; } 
    public String getFirstBid() {return fb; } 
    public String getNumBids() {return nb; } 
    public Bid[] getBids() {return bids.toArray(new Bid[bids.size()]); } 
    public String getLatitude() {return lat; } 
    public String getLongitude() {return longitude; } 
    public String getLocation() {return loc; } 
    public String getCountry() {return country; } 
    public String getStarted() {return started; } 
    public String getEnds() {return ends; } 
    public String getSellerRating() {return srat; } 
    public String getSellerId() {return sid; }
    public String getDescription() {return desc; }  
    public String getBuyPrice() {return bprice;}
}

