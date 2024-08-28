package com.example.forum;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
/**
 * This activity provides UI for users to log in
 *
 * @author Linsheng Zhou
 */
public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void applyLogin(View view) {
        // Find edit text for username and password
        EditText usernameEditText = findViewById(R.id.input_account);
        EditText passwordEditText = findViewById(R.id.input_password);
        // Read username and password
        String enteredUsername = usernameEditText.getText().toString();
        String enteredPassword = passwordEditText.getText().toString();

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
                    AVLTreeFactory factory=AVLTreeFactory.getInstance();
                    AccountTree at=factory.accountTreeCreator(valuesList);
                    // Based on binary search principle, return the account object given the username
                    Account target= at.search(enteredUsername);

                    if (target==null) {
                        //Such username is not found in AVL tree
                        Toast.makeText(getApplicationContext(), "Username doesn't exist!", Toast.LENGTH_SHORT).show();
                    } else {
                        //This username exists in AVL tree
                        if (enteredPassword.equals(target.password)) {
                            //Correct password
                            if(target.state==0){
                                // This account is offline
                                Intent intent = new Intent(getApplicationContext(), Main_Page.class);
                                intent.putExtra("username",enteredUsername);
                                target.state=1;
                                databaseReference.setValue(at.toList());
                                startActivity(intent);
                            }else {
                                // This account is online on another device and refuse login
                                Toast.makeText(getApplicationContext(), "This account is online in another device!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //Wrong password
                            Toast.makeText(getApplicationContext(), "Wrong password! Try again!", Toast.LENGTH_SHORT).show();

                        }
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
    // Jump to account deletion activity
    public void gotoDelete(View v){
        Intent intent=new Intent(this, AccountDelete.class);
        startActivity(intent);
    }
}