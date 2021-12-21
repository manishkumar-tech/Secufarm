package com.weather.risk.mfi.myfarminfo.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.weather.risk.mfi.myfarminfo.database.DBAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by WRMS on 17-12-2015.
 */
public class CropQueryData implements Parcelable {

    private String dbId;
    private String besalDoseApply;
    private String farmName;
    private String crop;
    private String cropID;
    private String variety;
    private String sowPeriodForm;
    private String sowPeriodTo;
    private String basalDoseN;
    private String otherNutrition;
    private String farmId;
    private String basalDoseP;
    private String basalDoseK;
    private String yourCencern;
    private String nutrient;
    private String content;
    private String soilApplication;

    public CropQueryData() {

    }

    /*	private static final String DATABASE_CREAT_QUERY_CROP =
            "create table "+TABLE_QUERY_CROP+" ("+ID+" integer primary key autoincrement,"
					+FARM_ID+" text not null,"
					+FARM_NAME+" text not null,"
					+CROP_ID+" text not null,"
					+CROPS_VARIETY+" text not null,"
					+CROP_FROM+" text not null,"
					+CROP_TO+" text not null,"
					+BASAL_DOSE_APPLY+" text not null,"
					+VALUE_N+" text not null,"
					+VALUE_P+" text not null,"
					+VALUE_K+" text not null,"
					+OTHER_NUTRIENT+" text not null,"
					+CONCERN+" text not null);";
*/
    public CropQueryData(Cursor cursor) {

        this.dbId = cursor.getString(cursor.getColumnIndex(DBAdapter.ID));
        this.besalDoseApply = cursor.getString(cursor.getColumnIndex(DBAdapter.BASAL_DOSE_APPLY));
        this.farmName = cursor.getString(cursor.getColumnIndex(DBAdapter.FARM_NAME));
//        this.crop =  cursor.getString(cursor.getColumnIndex(DBAdapter.CROP));
        this.cropID = cursor.getString(cursor.getColumnIndex(DBAdapter.CROP_ID));
        this.variety = cursor.getString(cursor.getColumnIndex(DBAdapter.CROPS_VARIETY));
        String dateFrom = cursor.getString(cursor.getColumnIndex(DBAdapter.CROP_FROM));

        if (dateFrom != null && dateFrom.length() > 5) {
            try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
            Date date = dateFormatter.parse(dateFrom);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
            dateFrom = sdf.format(date);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
        this.sowPeriodForm = dateFrom;

        String dateTo = cursor.getString(cursor.getColumnIndex(DBAdapter.CROP_TO));

        if (dateTo!=null && dateTo.length()>5) {
            try {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
                Date date = dateFormatter.parse(dateTo);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                dateTo = sdf.format(date);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.sowPeriodTo = dateTo;

//        this.sowPeriodTo = cursor.getString(cursor.getColumnIndex(DBAdapter.CROP_TO));
        this.basalDoseN = cursor.getString(cursor.getColumnIndex(DBAdapter.VALUE_N));
        this.otherNutrition = cursor.getString(cursor.getColumnIndex(DBAdapter.OTHER_NUTRIENT));
        this.farmId = cursor.getString(cursor.getColumnIndex(DBAdapter.FARM_ID));
        this.basalDoseP = cursor.getString(cursor.getColumnIndex(DBAdapter.VALUE_P));
        this.basalDoseK = cursor.getString(cursor.getColumnIndex(DBAdapter.VALUE_K));
        this.yourCencern = cursor.getString(cursor.getColumnIndex(DBAdapter.CONCERN));
        this.nutrient = "";
        this.content = "";
        this.soilApplication = "";
    }

    public boolean updateStatusToSent(DBAdapter db, String id) {
        boolean isUpdated = false;

        ContentValues values = new ContentValues();
        values.put(DBAdapter.ID, id);
        values.put(DBAdapter.FARM_ID, this.getFarmId());
        values.put(DBAdapter.FARM_NAME, this.getFarmName());
        values.put(DBAdapter.CROP_ID, this.getCropID());
        values.put(DBAdapter.CROPS_VARIETY, this.getVariety());
        values.put(DBAdapter.CROP_FROM, this.getSowPeriodForm());
        values.put(DBAdapter.CROP_TO, this.getSowPeriodTo());
        values.put(DBAdapter.BASAL_DOSE_APPLY, this.getBesalDoseApply());
        values.put(DBAdapter.VALUE_N, this.getBasalDoseN());
        values.put(DBAdapter.VALUE_P, this.getBasalDoseP());
        values.put(DBAdapter.VALUE_K, this.getBasalDoseK());
        values.put(DBAdapter.OTHER_NUTRIENT, this.getOtherNutrition());
        values.put(DBAdapter.CONCERN, this.getYourCencern());
        values.put(DBAdapter.SENDING_STATUS, DBAdapter.SENT);

        long k = db.db.update(DBAdapter.TABLE_QUERY_CROP, values, DBAdapter.ID + " ='" + id + "'", null);

        if (k != -1) {
            isUpdated = true;
        }

        return isUpdated;
    }


    protected CropQueryData(Parcel in) {
        String[] data = new String[17];
        in.readStringArray(data);

        this.besalDoseApply = data[0];
        this.farmName = data[1];
        this.crop = data[2];
        this.cropID = data[3];
        this.variety = data[4];
        this.sowPeriodForm = data[5];
        this.sowPeriodTo = data[6];
        this.basalDoseN = data[7];
        this.otherNutrition = data[8];
        this.farmId = data[9];
        this.basalDoseP = data[10];
        this.basalDoseK = data[11];
        this.yourCencern = data[12];
        this.nutrient = data[13];
        this.content = data[14];
        this.soilApplication = data[15];
        this.dbId = data[16];

    }

    public String getDbId() {
        if (dbId != null && dbId.trim().length() > 0) {
            return dbId;
        } else {
            return "-1";
        }
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public long insert(DBAdapter db, String sendingStatus) {
        long inserted = -1;

        Cursor getCropQuery = db.getCropQueryById(this.getDbId());

        ContentValues values = new ContentValues();
        System.out.println("Saving Farm id : " + this.getFarmId());
        values.put(DBAdapter.FARM_ID, this.getFarmId());
        values.put(DBAdapter.FARM_NAME, this.getFarmName());
        values.put(DBAdapter.CROP_ID, this.getCropID());
        values.put(DBAdapter.CROPS_VARIETY, this.getVariety());
        values.put(DBAdapter.CROP_FROM, this.getSowPeriodForm());
        values.put(DBAdapter.CROP_TO, this.getSowPeriodTo());
        values.put(DBAdapter.BASAL_DOSE_APPLY, this.getBesalDoseApply());
        values.put(DBAdapter.VALUE_N, this.getBasalDoseN());
        values.put(DBAdapter.VALUE_P, this.getBasalDoseP());
        values.put(DBAdapter.VALUE_K, this.getBasalDoseK());
        values.put(DBAdapter.OTHER_NUTRIENT, this.getOtherNutrition());
        values.put(DBAdapter.CONCERN, this.getYourCencern());
        values.put(DBAdapter.SENDING_STATUS, sendingStatus);

        if (getCropQuery.getCount() > 0) {
            inserted = db.db.update(DBAdapter.TABLE_QUERY_CROP, values, DBAdapter.ID+" ='"+this.getDbId()+"'",null);
        } else {
            inserted = db.db.insert(DBAdapter.TABLE_QUERY_CROP, null, values);
        }

        return inserted;
    }

    public static final Creator<CropQueryData> CREATOR = new Creator<CropQueryData>() {
        @Override
        public CropQueryData createFromParcel(Parcel in) {
            return new CropQueryData(in);
        }

        @Override
        public CropQueryData[] newArray(int size) {
            return new CropQueryData[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringArray(new String[]{
                this.besalDoseApply,
                this.farmName,
                this.crop,
                this.cropID,
                this.variety,
                this.sowPeriodForm,
                this.sowPeriodTo,
                this.basalDoseN,
                this.otherNutrition,
                this.farmId,
                this.basalDoseP,
                this.basalDoseK,
                this.yourCencern,
                this.nutrient,
                this.content,
                this.soilApplication,
                this.dbId});
    }


    public String getBesalDoseApply() {
        if (besalDoseApply == null) {
            besalDoseApply = "0";
        }
        return besalDoseApply;
    }

    public void setBesalDoseApply(String besalDoseApply) {
        this.besalDoseApply = besalDoseApply;
    }

    public String getFarmName() {
        return farmName == null ? "" : farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getCrop() {
        return crop == null ? "" : crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getCropID() {
        return (cropID != null && cropID.trim().length() > 0) ? cropID : "-1";
    }

    public void setCropID(String cropID) {
        this.cropID = cropID;
    }

    public String getVariety() {
        return variety == null ? "" : variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getSowPeriodForm() {
        return sowPeriodForm == null ? "" : sowPeriodForm;
    }

    public void setSowPeriodForm(String sowPeriodForm) {
        this.sowPeriodForm = sowPeriodForm;
    }

    public String getSowPeriodTo() {
        return sowPeriodTo == null ? "" : sowPeriodTo;
    }

    public void setSowPeriodTo(String sowPeriodTo) {
        this.sowPeriodTo = sowPeriodTo;
    }

    public String getBasalDoseN() {
        return basalDoseN == null ? "0" : basalDoseN;
    }

    public void setBasalDoseN(String basalDoseN) {
        this.basalDoseN = basalDoseN;
    }

    public String getOtherNutrition() {
        return otherNutrition == null ? "" : otherNutrition;
    }

    public void setOtherNutrition(String otherNutrition) {
        this.otherNutrition = otherNutrition;
    }

    public String getFarmId() {
        return farmId == null ? "" : farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getBasalDoseP() {
        return basalDoseP == null ? "0" : basalDoseP;
    }

    public void setBasalDoseP(String basalDoseP) {
        this.basalDoseP = basalDoseP;
    }

    public String getBasalDoseK() {
        return basalDoseK == null ? "0" : basalDoseK;
    }

    public void setBasalDoseK(String basalDoseK) {
        this.basalDoseK = basalDoseK;
    }

    public String getYourCencern() {
        return yourCencern == null ? "" : yourCencern;
    }

    public void setYourCencern(String yourCencern) {
        this.yourCencern = yourCencern;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSoilApplication() {
        return soilApplication == null ? "" : soilApplication;
    }

    public void setSoilApplication(String soilApplication) {
        this.soilApplication = soilApplication;
    }

    public String getNutrient() {

        return nutrient == null ? "" : nutrient;
    }

    public void setNutrient(String nutrient) {
        this.nutrient = nutrient;
    }
}
