package com.example.forum;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * The UploadHouse class provides methods for simulating upload house operation to satisfy the demand of more than 2500 data in the database
 *
 * @author Xiaochen Lu
 */
public class GenerateData implements Serializable {
    private static final Random random = new Random(System.nanoTime());
    private transient Context mContext;

    private UploadHouse uploadOP;
    public GenerateData(Context context) {
        this.mContext = context;
        uploadOP = new UploadHouse(mContext);
    }
    /**
     * Generates random house-related data.
     *
     * @param userList List of possible usernames to associate with a house.
     * @return A map containing a unique house ID and its associated data.
     */
    public Map<String, Object> generateData(List<String> userList){
        String bedroom = String.valueOf(random.nextInt(6) + 1);
        String unit = String.valueOf(random.nextInt(10)+1)+String.valueOf(random.nextInt(8)+String.valueOf(random.nextInt(8)));
        List<String> states = uploadOP.getProvinces();
        int state_choice = random.nextInt(states.size()-1)+1;
        String state = states.get(state_choice);
        List<String> suburbs = uploadOP.getSuburbs(state);
        int suburb_choice = random.nextInt(suburbs.size()-1)+1;
        String suburb = suburbs.get(suburb_choice);
        List<String> streets = uploadOP.getStreetsForSelectedSuburb(state, suburb);
        int street_choice = random.nextInt(streets.size()-1)+1;
        String street = streets.get(street_choice);
        String building_no = String.valueOf(random.nextInt(100)+1);
        String price_data = String.valueOf(random.nextInt(501) + 400);
        //get userslist and randomly choose a user
        List<String> usernames = userList;
        int user_choice = random.nextInt(usernames.size());
        String user = usernames.get(user_choice);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(new Date());
        String houseId = user+currentTime;
        String data = state+";"+suburb+";"+street+";"+building_no+";"+unit+";"+price_data+";"+bedroom+";"+user+"@Gmail.com"+";"+"0"+";";
        Map<String, Object> updates = new HashMap<>();
        houseId = houseId.replace(":", "");
        updates.put(houseId, data);
        return updates;
    }

    /**
     * Simulates adding generated house data to the Firebase database.
     *
     * @param num      Number of data entries to generate and add.
     * @param userlist List of possible usernames to associate with houses.
     */
    public void simulateData(int num, List<String> userlist){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("House").child("key:HouseId-value:city;suburb;street;building_no;unit;price;bedroom;email;recommend");
        for (int i = 0; i < num ; i++ ){
            mDatabase.updateChildren(generateData(userlist));
        }
    }

}
