package com.crispakeinc.ocrdict.domain.entity;

import java.io.Serializable;
import java.util.Date;

public class OcrDictUser implements Serializable {
    public Boolean isMyself = false;
    public Long userIdOnServer;
    public String signinIdTokenExternal;
    public String userDisplayName;
    public String userPhotoUrl;
    public String photoFilePath;
    public String userEmail;
    public Date createdDate;
}
