package com.example.forum;

import androidx.appcompat.app.AppCompatActivity;

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
 * This activity enables users to delete their accounts from our system if they no loner use it.
 * [Data-Deletion] is achieved
 * @author Linsheng Zhou
 */
public class AccountDelete extends AppCompatActivity {
    EditText etAccount; // The input box for username
    EditText etPassword; // The input box for password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_delete);

        // Find string in edit text
        etAccount = findViewById(R.id.delete_account);
        etPassword = findViewById(R.id.delete_password);
    }

    public void applyDelete(View v) {
        // Retrieve username and password
        String inputAccount = etAccount.getText().toString();
        String inputPassword = etPassword.getText().toString();
        //Clear input box
        etAccount.setText("");
        etPassword.setText("");
        //Connect database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("UsersData").child("1");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    // List of strings which are raw info stored in database
                    List<String> valuesList = new ArrayList<>();
                    // Retrieve info from database to memory list
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String item = itemSnapshot.getValue(String.class);
                        valuesList.add(item);
                    }
                    //Singleton for AVLTreeFactory, get the unique object
                    AVLTreeFactory factory = AVLTreeFactory.getInstance();
                    //Factory transforms raw data strings to account AVL tree
                    AccountTree at = factory.accountTreeCreator(valuesList);
                    // Apply binary search to return the Account object where the info of this user is stored
                    Account target = at.search(inputAccount);

                    if (target == null) {
                        // It fails to find required username because it doesn't exist in AVL tree, also not in database
                        Toast.makeText(getApplicationContext(), "Username doesn't exist!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (inputPassword.equals(target.password)) {
                            //Required username is found in AVL tree, thus delete this account
                            at.delete(inputAccount);
                        } else {
                            // Wrong password is entered
                            Toast.makeText(getApplicationContext(), "Wrong password! Try again!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    // Return a list of raw data info
                    valuesList = at.toList();
//                    Iterator<Account> iterator = at.iterator();
//                    while (iterator.hasNext()) {
//                        Account account = iterator.next();
//                        valuesList.add(account.account+";"+account.password);
//                    }
                    //Reset UsersData sub-database
                    databaseReference.setValue(valuesList);
                    Toast.makeText(getApplicationContext(), "Successful Delete", Toast.LENGTH_SHORT).show();
                    // Go back to login page
                    finish();
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
}