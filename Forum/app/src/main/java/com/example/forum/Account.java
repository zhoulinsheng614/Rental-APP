package com.example.forum;

/**
 * This class stores user info for each user, including username, password, status and avatar id
 *
 * @author Linsheng Zhou
 */
public class Account {
    String account;// user name
    String password;// user password
    int state;// Status: 0 for offline and 1 for online
    int imageId;// No. of image id in avatar repo on Firebase
    Account left;// Left child
    Account right;// Right child
    int height;// height of this node in avl tree

    public Account(String account, String password, int state, int imageId) {
        this.account = account;
        this.password = password;
        this.state = state;
        this.imageId = imageId;
        this.height = 1;
        this.left = null;
        this.right = null;
    }

}

