package io.github.ngspace.topdownshooter.utils;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.function.Consumer;

public class UserData implements Serializable {
    public String firebaseUid;
    public int run = 0;
    public int health = 100;
    public int ammo = 100;

    public static void init(FirebaseUser user, FirebaseDatabase database, Consumer<UserData> finishedreading) {
        var ref = database.getReference().child(user.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Logcat.log("Loaded user data: " + dataSnapshot.getValue());
                UserData usr = dataSnapshot.getValue(UserData.class);
                if(usr==null) {
                    Logcat.log("User does not exist, creating new user data.");
                    usr = new UserData();
                }

                // Ensure uID is valid and is set
                usr.firebaseUid = user.getUid();

                FirebaseDatabase.getInstance().getReference(usr.firebaseUid).setValue(usr);
                finishedreading.accept(usr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Logcat.log("Can't read", error.getMessage());
                throw error.toException();
            }
        });
    }
}
