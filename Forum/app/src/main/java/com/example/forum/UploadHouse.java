package com.example.forum;

import android.content.Context;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The UploadHouse class provides methods for reading data from an XML file named "AddressBook.xml".
 * It is particularly focused on fetching geographical data such as provinces, suburbs, and streets.
 *
 * @author Xiaochen Lu
 */
public class UploadHouse implements Serializable {

    // The context is required for reading asset files. Marked as transient to avoid serialization.
    private transient Context mContext;

    /**
     * Constructor initializes the context.
     *
     * @param context Application or Activity context.
     */
    public UploadHouse(Context context) {
        this.mContext = context;
    }

    /**
     * Retrieves a list of provinces from the XML file.
     *
     * @return List of provinces.
     */
    public List<String> getProvinces() {
        List<String> provinces = new ArrayList<>();
        try {
            // Load the XML from assets and initialize the XML parser.
            InputStream is = mContext.getAssets().open("addressBook.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, null);
            int eventType = parser.getEventType();

            // Loop through the XML to find province names.
            while ((eventType != XmlPullParser.END_DOCUMENT)) {
                if (eventType == XmlPullParser.START_TAG && "province".equals(parser.getName())) {
                    provinces.add(parser.getAttributeValue(null, "name"));
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        provinces.add(0, "select your city");
        return provinces;
    }

    /**
     * Retrieves a list of suburbs for a given province from the XML file.
     *
     * @param targetProvince The target province for which suburbs are to be fetched.
     * @return List of suburbs.
     */
    public List<String> getSuburbs(String targetProvince) {
        List<String> suburbs = new ArrayList<>();
        try {
            InputStream is = mContext.getAssets().open("addressBook.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, null);
            int eventType = parser.getEventType();
            boolean isInsideTargetProvince = false;

            // Loop through the XML to find suburb names inside the given province.
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if ("province".equals(parser.getName()) && targetProvince.equals(parser.getAttributeValue(null, "name"))) {
                        isInsideTargetProvince = true;
                    } else if (isInsideTargetProvince && "suburb".equals(parser.getName())) {
                        suburbs.add(parser.getAttributeValue(null, "name"));
                    }
                } else if (eventType == XmlPullParser.END_TAG && "province".equals(parser.getName())) {
                    isInsideTargetProvince = false;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        suburbs.add(0, "select your suburb");
        return suburbs;
    }

    /**
     * Retrieves a list of streets for a given suburb in a given province from the XML file.
     *
     * @param selectedProvince The province in which the suburb is located.
     * @param selectedSuburb   The target suburb for which streets are to be fetched.
     * @return List of streets.
     */
    public List<String> getStreetsForSelectedSuburb(String selectedProvince, String selectedSuburb) {
        List<String> streets = new ArrayList<>();
        try {
            XmlPullParser parser = Xml.newPullParser();
            InputStream is = mContext.getAssets().open("addressBook.xml");
            parser.setInput(is, null);
            int eventType = parser.getEventType();
            boolean insideTargetProvince = false;
            boolean insideTargetSuburb = false;

            // Loop through the XML to find streets inside the given suburb and province.
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = parser.getName();
                    if ("province".equals(tagName) && selectedProvince.equals(parser.getAttributeValue(null, "name"))) {
                        insideTargetProvince = true;
                    } else if (insideTargetProvince && "suburb".equals(tagName) && selectedSuburb.equals(parser.getAttributeValue(null, "name"))) {
                        insideTargetSuburb = true;
                    } else if (insideTargetSuburb && "street".equals(tagName)) {
                        streets.add(parser.nextText());
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("province".equals(parser.getName())) {
                        insideTargetProvince = false;
                    } else if ("suburb".equals(parser.getName())) {
                        insideTargetSuburb = false;
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        streets.add(0, "select your street");
        return streets;
    }
}
