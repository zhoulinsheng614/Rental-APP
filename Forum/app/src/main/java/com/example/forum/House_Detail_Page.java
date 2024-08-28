package com.example.forum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/** This class shows house detail information when users click from main page
 * @author Wangtao Jia*/

public class House_Detail_Page extends AppCompatActivity {
    String likes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail_page);

        Intent intent = getIntent();
        House house = (House) intent.getExtras().getSerializable("houseData");
        int imageNumber = intent.getIntExtra("imageid",0);

        if (house == null){
            Toast.makeText(this,"House gone",Toast.LENGTH_LONG).show();
        }

        assert house != null;

        // get id
        String id = house.getId();

        // initialize house information
        String price = String.valueOf(house.getPrice());
        String location = house.getUnit()+ "" + house.getStreetNumber()+" "+house.getStreet() + " " + house.getSuburb()+ " " + house.getCity();
        String title = String.valueOf(house.getXbxb());
        likes = String.valueOf(house.getLikes());
        String email = house.getEmail();

        // set relative text to house information

        // price
        TextView textview4 = findViewById(R.id.textView4);
        textview4.setText("$"+price);
        // house street and location
        TextView textview1 = findViewById(R.id.textView1);
        textview1.setText(location);
        // xbxb
        TextView textview3 = findViewById(R.id.textView3);
        if (Integer.parseInt(title) > 1){
            textview3.setText(title + " " + "Bedrooms");
        }else {
            textview3.setText(title + " " + "Bedroom");
        }
        // email
        TextView emailText = findViewById(R.id.textView5);
        emailText.setText("Email:" + " " + email);
        // likes
        TextView textView6 = findViewById(R.id.textView6);
        textView6.setText(likes);

        // likes button
        Button buttonLikes = findViewById(R.id.buttonLikes);
        buttonLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(House_Detail_Page.this,"Like + 1",Toast.LENGTH_LONG).show();
                String newLikes = String.valueOf(Integer.parseInt(likes)+1);
                likes = newLikes;
                textView6.setText(newLikes);
                house.setLikes(Integer.parseInt(newLikes));
                saveLikestoFirebase(newLikes,id);
            }
        });

        // load inside image relative to house appearance
        String imageName = "houseinside" + imageNumber;
        int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        ImageView imageView = findViewById(R.id.imageView2);
        imageView.setImageResource(imageResId);
    }

    /** Upload updated likes to firebase */
    public void saveLikestoFirebase(String likes, String id){

        // FirebaseDatabase uses the singleton design pattern (we cannot directly create a new instance of it).
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // Get a reference to the users collection in the database and then get the specific user (as specified by the user id in this case).
        DatabaseReference databaseReference = firebaseDatabase.getReference("House").child("key:HouseId-value:city;suburb;street;building_no;unit;price;bedroom;email;recommend");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        String item = itemSnapshot.getKey();


                        if (item != null && item.equals(id)) {


                            String value = itemSnapshot.getValue(String.class);

                            // Check to ensure the value is not null and has enough characters to safely perform substring
                            if (value != null) {
                                String[] valueList = value.split(";");
                                String newValue = "";

                                for (int i =0;i< valueList.length;i++){
                                    if (i != valueList.length-1){
                                        newValue = newValue + valueList[i] + ";";
                                    }else {
                                        newValue = newValue + likes + ";";
                                    }
                                }
                                //Here we should update the value in the database, using DatabaseReference
                                itemSnapshot.getRef().setValue(newValue);

                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to retrieve the data
                System.err.println("Failed to retrieve data, error: " + error.toException());
            }
        });
    }

}