package nearplaces.android.com.nearplaces;

import java.util.ArrayList;

/**
 * Created by jade on 9/4/16.
 */
public class PlaceDetail {

    String name;
    String address;
    String iconUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public ArrayList<String> getMphotoID() {
        return mphotoID;
    }

    public void setMphotoID(ArrayList<String> mphotoID) {
        this.mphotoID = mphotoID;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    ArrayList<String> mphotoID = new ArrayList<>();
    String longitude;
    String latitude;
    String phone_no;
}
