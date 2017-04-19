package info.zadnjipozdrav.zadnjipozdrav;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Date;


public class Obituary {

    private long mId;
    private String mName;
    private String mFamilyName;
    private String mFathersName;
    private String mMaidenName;
    private Date mBirthDate;
    private Date mDeathDate;
    private String mText;
    private Date mFuneralDate;
    private Date mFuneralTime;
    private Bitmap mPicture;
    private int mReligion;
    private Cemetery mCemetery;
    private Borough mBorough;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getFamilyName() {
        return mFamilyName;
    }

    public void setFamilyName(String familyName) {
        mFamilyName = familyName;
    }

    public String getFathersName() {
        return mFathersName;
    }

    public void setFathersName(String fathersName) {
        mFathersName = fathersName;
    }

    public String getMaidenName() {
        return mMaidenName;
    }

    public void setMaidenName(String maidenName) {
        mMaidenName = maidenName;
    }

    public Date getBirthDate() {
        return mBirthDate;
    }

    public void setBirthDate(Date birthDate) {
        mBirthDate = birthDate;
    }

    public void setBirthDate(long birthDate) {
        mBirthDate = new Date(birthDate * 1000);
    }

    public Date getDeathDate() {
        return mDeathDate;
    }

    public void setDeathDate(Date deathDate) {
        mDeathDate = deathDate;
    }

    public void setDeathDate(long deathDate) {
        mDeathDate = new Date(deathDate * 1000);
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Date getFuneralDate() {
        return mFuneralDate;
    }

    public void setFuneralDate(Date funeralDateAndTime) {
        mFuneralDate = funeralDateAndTime;
    }

    public void setFuneralDate(long funeralDateAndTime) {
        mFuneralDate = new Date(funeralDateAndTime * 1000);
    }

    public Date getFuneralTime() {
        return mFuneralTime;
    }

    public void setFuneralTime(Date funeralTime) {
        mFuneralTime = funeralTime;
    }

    public void setFuneralTime(long funeralTime) {
        mFuneralTime = new Date(funeralTime * 1000);
    }

    public Bitmap getPicture() {
        return mPicture;
    }

    public void setPicture(Bitmap picture) {
        mPicture = picture;
    }

    public void setPicture(byte[] picture) {
        if (picture != null) {
            mPicture = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        }
        mPicture = null;
    }

    public int getReligion() {
        return mReligion;
    }

    public void setReligion(int religion) {
        mReligion = religion;
    }

    public Cemetery getCemetery() {
        return mCemetery;
    }

    public void setCemetery(Cemetery cemetery) {
        mCemetery = cemetery;
    }

    public Borough getBorough() {
        return mBorough;
    }

    public void setBorough(Borough borough) {
        mBorough = borough;
    }
}
