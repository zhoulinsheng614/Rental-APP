package com.example.forum;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.forum.databinding.ActivityMainPageBinding;
import com.example.forum.ui.gallery.GalleryFragment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.os.Handler;


public class Main_Page extends AppCompatActivity {
    static String userr;// Username pass from Login Page
    static String district;// Username pass from Login Page
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainPageBinding binding;
    TextView mySignature;// Greeting description in side user profile
    TextView title;// Username shown in side user profile
    ImageView avatar;// Avatar in side user profile
    TextView textView;// Location of simulated device in the world shown in Home Fragment
    LocationManager locationManager;
    LocationListener locationListener;
    TextView tvLocation;
    Geocoder geocoder;
    private Handler handler = new Handler();
    private Runnable runnable;
    int SIMULATION_INTERVAL=30000;//Simulate new house upload every 30s
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        district="Your Location";
        // Retrieve current username from last activity
        Intent intent = getIntent();
        userr = intent.getStringExtra("username");

        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Initialize firebase
        FirebaseApp.initializeApp(this);
        // Initialize text for location shown
        tvLocation = findViewById(R.id.textViewMap);
        //Definition of start GPS detection button
        setSupportActionBar(binding.appBarMainPage.toolbar);
        binding.appBarMainPage.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
                finish();
            }
        });
        binding.appBarMainPage.btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applayUpdateGPS();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_page);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //Load user profile on side user profile, as well as start GPS access
        loadUserProfile(navigationView);
        /**
         * This is listener for GPS location
         * [Data-GPS] is achieved
         * @author Linsheng Zhou
         */
        //Listener for getting GPS info
        tvLocation = findViewById(R.id.textViewMap);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {

                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Reverse Geocoding
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    // Retrieve district info
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses.size() > 0) {
                            // Let district shown on Home Fragment
                            if(!district.equals(addresses.get(0).getLocality())){
                                Toast.makeText(getApplicationContext(), "You are in "+addresses.get(0).getLocality()+".", Toast.LENGTH_SHORT).show();
                            }
                            district=addresses.get(0).getLocality();
                            tvLocation.setText(district);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
                // Ask for permission GPS access
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        // Create a Runnable that will run your function
        runnable = new Runnable() {
            @Override
            public void run() {
                // Call your function here
                simulateDataStream();
                // Schedule the Runnable to run again in 15 seconds
                handler.postDelayed(this, SIMULATION_INTERVAL); // 15,000 milliseconds = 15 seconds
            }
        };

        // Schedule the first run of the Runnable after 15 seconds
        handler.postDelayed(runnable, SIMULATION_INTERVAL); // 15,000 milliseconds = 15 seconds
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main__page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_page);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * This method does something.
     * [Data-Profile] is achieved
     * @param navigationView root view in navigationView which contains user profile components
     * @author Linsheng Zhou
     */
    public void loadUserProfile(NavigationView navigationView) {
        //Generate greetings in user profile
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        String greeting;
        if (hourOfDay >= 4 && hourOfDay < 12) {
            greeting = "Good morning";
        } else if (hourOfDay >= 12 && hourOfDay < 17) {
            greeting = "Good afternoon";
        } else if (hourOfDay >= 17 && hourOfDay < 21) {
            greeting = "Good evening";
        } else {
            greeting = "Good night";
        }

        // Get the 3 elements: avatar, title and signature line
        View headerView = navigationView.getHeaderView(0);
        title = headerView.findViewById(R.id.nametitle);
        mySignature = headerView.findViewById(R.id.mySignature);
        avatar = headerView.findViewById(R.id.imageViewAvatar);
        mySignature.setText(greeting + ", " + userr + "!");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // Get a reference to the users collection in the database and then get the specific user (as specified by the user id in this case).
        DatabaseReference databaseReference = firebaseDatabase.getReference("UsersData").child("1");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    String[] item;
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        item = itemSnapshot.getValue(String.class).split(";");
                        if (!userr.equals(item[0])) {
                            continue;
                        }
                        //Now current user info is loaded
                        title.setText(userr);

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        //Avatars are stored on Firebase Storage
                        StorageReference storageRef = storage.getReference("avatars").child("image" + item[3] + ".jpeg");
                        storageRef.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Handle successful image download here
                                        String imageUrl = uri.toString();
                                        // Load and display the image using Picasso
                                        Picasso.get().load(imageUrl).into(avatar);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors that may occur during the download
                                    }
                                });
                        break;
                    }

                    // You can use the jsonString as needed in your app
                } else {
                    Log.d("FirebaseData", "No data available or data is null");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the read operation
                Log.e("FirebaseError", "Error reading data from Firebase", databaseError.toException());
            }
        });
    }

    public static String getUser() {
        return userr;
    }
    public static String getDistrict() {
        return district;
    }

    /**
     * This method examines device configuration and starts GPS listening
     *
     * @author Linsheng Zhou
     */
    private void logOut() {
        // FirebaseDatabase uses the singleton design pattern (we cannot directly create a new instance of it).
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // Get a reference to the users collection in the database and then get the specific user (as specified by the user id in this case).
        DatabaseReference databaseReference = firebaseDatabase.getReference("UsersData").child("1");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    // Store raw data strings
                    List<String> valuesList = new ArrayList<>();

                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String item = itemSnapshot.getValue(String.class);
                        valuesList.add(item);
                    }
                    // Create an AVL Tree to store accounts
                    AVLTreeFactory factory = AVLTreeFactory.getInstance();
                    AccountTree at = factory.accountTreeCreator(valuesList);
                    // Based on binary search principle, return the account object given the username
                    Account target = at.search(userr);
                    target.state = 0;
                    databaseReference.setValue(at.toList());
                } else {
                    Log.d("FirebaseData", "No data available or data is null");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the read operation
                Log.e("FirebaseError", "Error reading data from Firebase", databaseError.toException());
            }
        });
    }
    private void simulateDataStream(){
        Random random=new Random();
        //Connect the firebase
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("House").child("key:HouseId-value:city;suburb;street;building_no;unit;price;bedroom;email;recommend");
        //generate data
        String data = "ACT"+";"+"Acton"+";"+"AVE "+random.nextInt(100)+";"+random.nextInt(15)+";"+random.nextInt(20)+";"+(300+random.nextInt(600))+";"+(1+random.nextInt(6))+";"+"email"+";"+0+";";
        //update House data in Map form
        ; // Replace with the actual key
        DatabaseReference newChildRef = mDatabase.push();
        newChildRef.setValue(data);
    }
    /**
     * This method checks for version and permissions
     * Starts swift update
     *
     * @author Linsheng Zhou
     */
    public void applayUpdateGPS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET

                }, 0);
                return;
            }
        }
        locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks to prevent memory leaks
        handler.removeCallbacks(runnable);
    }
}
