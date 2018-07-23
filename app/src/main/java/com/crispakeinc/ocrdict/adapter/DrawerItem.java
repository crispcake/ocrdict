package com.crispakeinc.ocrdict.adapter;

public class DrawerItem {
    Boolean isHeader;
	Class activityClass;
    String ItemName;
	Integer imgResID;

    public DrawerItem(Boolean isHeader, Class activityClass, String itemName, Integer imgResID) {
        this.isHeader = isHeader;
        this.activityClass = activityClass;
        ItemName = itemName;
        this.imgResID = imgResID;
    }

    public Boolean getIsHeader() {
        return isHeader;
    }

    public void setIsHeader(Boolean isHeader) {
        this.isHeader = isHeader;
    }

    public Class getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class activityClass) {
        this.activityClass = activityClass;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public Integer getImgResID() {
        return imgResID;
    }
    public void setImgResID(Integer imgResID) {
        this.imgResID = imgResID;
    }
}
