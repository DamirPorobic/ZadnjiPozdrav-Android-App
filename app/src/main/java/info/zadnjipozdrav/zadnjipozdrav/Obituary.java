package info.zadnjipozdrav.zadnjipozdrav;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Date;


public class Obituary {

    private long id;
    private String name;
    private String familyName;
    private String fathersName;
    private String maidenName;
    private Date birthDate;
    private Date deathDate;
    private String text;
    private Date funeralDate;
    private Date funeralTime;
    private Bitmap picture;
    private int religion;
    private Cemetery cemetery;
    private Borough borough;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getMaidenName() {
        return maidenName;
    }

    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = new Date(birthDate * 1000);
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public void setDeathDate(long deathDate) {
        this.deathDate = new Date(deathDate * 1000);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getFuneralDate() {
        return funeralDate;
    }

    public void setFuneralDate(Date funeralDateAndTime) {
        funeralDate = funeralDateAndTime;
    }

    public void setFuneralDate(long funeralDateAndTime) {
        funeralDate = new Date(funeralDateAndTime * 1000);
    }

    public Date getFuneralTime() {
        return funeralTime;
    }

    public void setFuneralTime(Date funeralTime) {
        this.funeralTime = funeralTime;
    }

    public void setFuneralTime(long funeralTime) {
        this.funeralTime = new Date(funeralTime * 1000);
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public void setPicture(byte[] picture) {
        if (picture != null) {
            this.picture = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        } else {
            this.picture = null;
        }
    }

    public int getReligion() {
        return religion;
    }

    public void setReligion(int religion) {
        this.religion = religion;
    }

    public Cemetery getCemetery() {
        return cemetery;
    }

    public void setCemetery(Cemetery cemetery) {
        this.cemetery = cemetery;
    }

    public Borough getBorough() {
        return borough;
    }

    public void setBorough(Borough borough) {
        this.borough = borough;
    }
}
