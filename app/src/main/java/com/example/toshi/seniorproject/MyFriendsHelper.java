package com.example.toshi.seniorproject;

public class MyFriendsHelper {

    public String imageHelper;
    public String nameHelper;
    public String statusHelper;

    public MyFriendsHelper() {
    }

    public MyFriendsHelper(String imageHelper, String nameHelper, String statusHelper) {
        this.imageHelper = imageHelper;
        this.nameHelper = nameHelper;
        this.statusHelper = statusHelper;
    }

    public String getImage() {
        return imageHelper;
    }

    public void setImage(String imageHelper) {

        this.imageHelper = imageHelper;
    }

    public String getName() {

        return nameHelper;
    }

    public void setName(String nameHelper) {

        this.nameHelper = nameHelper;
    }

    public String getStatus() {

        return statusHelper;
    }

    public void setStatus(String statusHelper) {

        this.statusHelper = statusHelper;
    }

}
