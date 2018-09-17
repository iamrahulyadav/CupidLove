package com.example.cupidlove.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankita on 5/2/2018.
 */

public class Configuration {
    @SerializedName("GOOGLE_PLACE_API_KEY")
    @Expose
    public String gOOGLEPLACEAPIKEY;
    @SerializedName("FACEBOOK_KEY")
    @Expose
    public String fACEBOOKKEY;
    @SerializedName("XMPP_ENABLE")
    @Expose
    public String xMPPENABLE;
    @SerializedName("XMPP_HOST")
    @Expose
    public String xMPPHOST;
    @SerializedName("APP_XMPP_HOST")
    @Expose
    public String aPPXMPPHOST;
    @SerializedName("APP_XMPP_SERVER")
    @Expose
    public String aPPXMPPSERVER;
    @SerializedName("XMPP_DEFAULT_PASSWORD")
    @Expose
    public String xMPPDEFAULTPASSWORD;
    @SerializedName("XMPP_SERVER")
    @Expose
    public String xMPPSERVER;
    @SerializedName("PUSH_ENABLE_SANDBOX")
    @Expose
    public String pUSHENABLESANDBOX;
    @SerializedName("PUSH_SANDBOX_GATEWAY_URL")
    @Expose
    public String pUSHSANDBOXGATEWAYURL;
    @SerializedName("PUSH_GATEWAY_URL")
    @Expose
    public String pUSHGATEWAYURL;
    @SerializedName("ANDROID_FCM_KEY")
    @Expose
    public String aNDROIDFCMKEY;
    @SerializedName("INSTAGRAM_CALLBACK_BASE")
    @Expose
    public String iNSTAGRAMCALLBACKBASE;
    @SerializedName("INSTAGRAM_CLIENT_SECRET")
    @Expose
    public String iNSTAGRAMCLIENTSECRET;
    @SerializedName("INSTAGRAM_CLIENT_ID")
    @Expose
    public String iNSTAGRAMCLIENTID;
    @SerializedName("adMobKey")
    @Expose
    public String adMobKey;
    @SerializedName("adMobVideoKey")
    @Expose
    public String adMobVideoKey;
    @SerializedName("RemoveAddInAppPurchase")
    @Expose
    public String removeAddInAppPurchase;
    @SerializedName("TermsAndConditionsUrl")
    @Expose
    public String termsAndConditionsUrl;
    @SerializedName("RemoveAddInAppBilling")
    @Expose
    public String removeAddInAppBilling;

    public Configuration withGOOGLEPLACEAPIKEY(String gOOGLEPLACEAPIKEY) {
        this.gOOGLEPLACEAPIKEY = gOOGLEPLACEAPIKEY;
        return this;
    }

    public Configuration withFACEBOOKKEY(String fACEBOOKKEY) {
        this.fACEBOOKKEY = fACEBOOKKEY;
        return this;
    }

    public Configuration withXMPPENABLE(String xMPPENABLE) {
        this.xMPPENABLE = xMPPENABLE;
        return this;
    }

    public Configuration withXMPPHOST(String xMPPHOST) {
        this.xMPPHOST = xMPPHOST;
        return this;
    }

    public Configuration withAPPXMPPHOST(String aPPXMPPHOST) {
        this.aPPXMPPHOST = aPPXMPPHOST;
        return this;
    }

    public Configuration withAPPXMPPSERVER(String aPPXMPPSERVER) {
        this.aPPXMPPSERVER = aPPXMPPSERVER;
        return this;
    }

    public Configuration withXMPPDEFAULTPASSWORD(String xMPPDEFAULTPASSWORD) {
        this.xMPPDEFAULTPASSWORD = xMPPDEFAULTPASSWORD;
        return this;
    }

    public Configuration withXMPPSERVER(String xMPPSERVER) {
        this.xMPPSERVER = xMPPSERVER;
        return this;
    }

    public Configuration withPUSHENABLESANDBOX(String pUSHENABLESANDBOX) {
        this.pUSHENABLESANDBOX = pUSHENABLESANDBOX;
        return this;
    }

    public Configuration withPUSHSANDBOXGATEWAYURL(String pUSHSANDBOXGATEWAYURL) {
        this.pUSHSANDBOXGATEWAYURL = pUSHSANDBOXGATEWAYURL;
        return this;
    }

    public Configuration withPUSHGATEWAYURL(String pUSHGATEWAYURL) {
        this.pUSHGATEWAYURL = pUSHGATEWAYURL;
        return this;
    }

    public Configuration withANDROIDFCMKEY(String aNDROIDFCMKEY) {
        this.aNDROIDFCMKEY = aNDROIDFCMKEY;
        return this;
    }

    public Configuration withINSTAGRAMCALLBACKBASE(String iNSTAGRAMCALLBACKBASE) {
        this.iNSTAGRAMCALLBACKBASE = iNSTAGRAMCALLBACKBASE;
        return this;
    }

    public Configuration withINSTAGRAMCLIENTSECRET(String iNSTAGRAMCLIENTSECRET) {
        this.iNSTAGRAMCLIENTSECRET = iNSTAGRAMCLIENTSECRET;
        return this;
    }

    public Configuration withINSTAGRAMCLIENTID(String iNSTAGRAMCLIENTID) {
        this.iNSTAGRAMCLIENTID = iNSTAGRAMCLIENTID;
        return this;
    }

    public Configuration withAdMobKey(String adMobKey) {
        this.adMobKey = adMobKey;
        return this;
    }

    public Configuration withAdMobVideoKey(String adMobVideoKey) {
        this.adMobVideoKey = adMobVideoKey;
        return this;
    }

    public Configuration withRemoveAddInAppPurchase(String removeAddInAppPurchase) {
        this.removeAddInAppPurchase = removeAddInAppPurchase;
        return this;
    }

    public Configuration withRemoveAddInAppBilling(String removeAddInAppBilling) {
        this.removeAddInAppBilling = removeAddInAppBilling;
        return this;
    }

    public Configuration withTermsAndConditionsUrl(String termsAndConditionsUrl) {
        this.termsAndConditionsUrl = termsAndConditionsUrl;
        return this;
    }
}
