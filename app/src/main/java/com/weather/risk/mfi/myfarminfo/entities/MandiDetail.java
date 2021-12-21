package com.weather.risk.mfi.myfarminfo.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class MandiDetail implements Parcelable {

    String location;
    String latitude;
    String longitude;
    String price;
    String unit;
    String distance;
    String quintal;
    String optimal;


    public MandiDetail(JSONObject jObject)
    {
        try {
            location=jObject.isNull("Location")?"":jObject.getString("Location");
            latitude=jObject.isNull("Latitude")?"":jObject.getString("Latitude");
            longitude=jObject.isNull("Longitude")?"":jObject.getString("Longitude");
            price=jObject.isNull("Price")?"":jObject.getString("Price");
            unit=jObject.isNull("Unit")?"":jObject.getString("Unit");
            quintal=jObject.isNull("Quintal")?"":jObject.getString("Quintal");
            distance=jObject.isNull("Distance")?"":jObject.getString("Distance");
            optimal=jObject.isNull("Optimal")?"":jObject.getString("Optimal");



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    protected MandiDetail(Parcel in) {
        location = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        price = in.readString();
        unit = in.readString();
        distance = in.readString();
        quintal = in.readString();
        optimal = in.readString();
    }

    public static final Creator<MandiDetail> CREATOR = new Creator<MandiDetail>() {
        @Override
        public MandiDetail createFromParcel(Parcel in) {
            return new MandiDetail(in);
        }

        @Override
        public MandiDetail[] newArray(int size) {
            return new MandiDetail[size];
        }
    };

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getQuintal() {
        return quintal;
    }

    public void setQuintal(String quintal) {
        this.quintal = quintal;
    }

    public String getOptimal() {
        return optimal;
    }

    public void setOptimal(String optimal) {
        this.optimal = optimal;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(price);
        dest.writeString(unit);
        dest.writeString(distance);
        dest.writeString(quintal);
        dest.writeString(optimal);
    }
}
