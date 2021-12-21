package com.weather.risk.mfi.myfarminfo.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.weather.risk.mfi.myfarminfo.database.DBAdapter;

/**
 * Created by Yogendra Singh on 14-10-2015.
 */
public class Register implements Parcelable {

    public String userName;
    public String visibleName;
    public String mailId;
    public String password;
    public String user_id;
    public String createdDateTime;
    public String mobileNo;

    public Register(){
    }

    protected Register(Parcel in) {
        userName = in.readString();
        visibleName = in.readString();
        mailId = in.readString();
        password = in.readString();
        user_id = in.readString();
        createdDateTime = in.readString();
        mobileNo = in.readString();
    }

    public static final Creator<Register> CREATOR = new Creator<Register>() {
        @Override
        public Register createFromParcel(Parcel in) {
            return new Register(in);
        }

        @Override
        public Register[] newArray(int size) {
            return new Register[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(visibleName);
        dest.writeString(mailId);
        dest.writeString(password);
        dest.writeString(user_id);
        dest.writeString(createdDateTime);
        dest.writeString(mobileNo);
    }

    public String getUserName() {
        if(userName!=null && userName.trim().length()>0){
            return userName;
        }else{
            return "NotSpecified";
        }

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVisibleName() {
        return visibleName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }


    public void setVisibleName(String visibleName) {
        this.visibleName = visibleName;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public boolean save(DBAdapter db,String sendingStatus){
        boolean isSaved = false;
        System.out.println("this.getMailId().trim() : "+this.getMailId().trim());
        Cursor isUserExist = db.isUserExist(this.getMailId().trim());
        if(isUserExist.getCount()>0){
            return isSaved;
        }
        ContentValues values = new ContentValues();
        values.put(DBAdapter.USER_NAME,this.getUserName());
        values.put(DBAdapter.VISIBLE_NAME,this.getVisibleName());
        values.put(DBAdapter.PASSWORD,this.getPassword());
        values.put(DBAdapter.EMAIL_ADDRESS,this.getMailId());
        values.put(DBAdapter.MOBILE_NO,this.getMobileNo());
        values.put(DBAdapter.USER_ID,this.getUser_id());
        values.put(DBAdapter.CREATED_DATE_TIME,this.getCreatedDateTime());
        values.put(DBAdapter.NEED_TO_EDIT,DBAdapter.FALSE);
        values.put(DBAdapter.SENDING_STATUS,sendingStatus);

        long k = db.db.insert(DBAdapter.TABLE_CREDENTIAL,null,values);
        if(k!=-1){
            isSaved = true;
        }

        return isSaved;
    }

}
