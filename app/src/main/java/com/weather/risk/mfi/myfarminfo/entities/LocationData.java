package com.weather.risk.mfi.myfarminfo.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationData implements Parcelable  {
	
	private String locationName;
	private String latitude;
	private String longitude;
	private String modifiedDate;
	
	public LocationData() {
		super();
	}
	
	public LocationData(Parcel in) {
		
		String[] data = new String[4];

		in.readStringArray(data);
		
		this.locationName = data[0];
		this.latitude = data[1];
		this.longitude = data[2];
		this.modifiedDate = data[3];

	}
	
	
	public static final Creator<LocationData> CREATOR = new Creator<LocationData>() {

		@Override
		public LocationData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new LocationData(source); // using parcelable constructor
		}

		@Override
		public LocationData[] newArray(int size) {
			// TODO Auto-generated method stub
			return new LocationData[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {this.locationName,this.latitude,this.longitude,this.modifiedDate });
		
	}
	
	

	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLatitude() {
		if(latitude == null){
			return "0.0";
		}
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		if(longitude == null){
			return "0.0";
		}
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	

}
