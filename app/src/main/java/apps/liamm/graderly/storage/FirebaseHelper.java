package apps.liamm.graderly.storage;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {

    private final static String TAG = FirebaseHelper.class.getSimpleName();
    private final FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

    public void addUserInfoToDatabase(String forename, String surname) {
        Log.d(TAG, "Attempting to add a user with a forename and surname.");

        Map<String, Object> user = new HashMap<>();
        user.put("forename", forename);
        user.put("surname", surname);

        final boolean[] result = new boolean[1];

        mDatabase.collection("users")
                .add(user)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Successfully added user information to database.");
                            result[0] = true;
                        } else {
                            Log.d(TAG, "Failed to add user information to database.");
                            result[0] = false;
                        }
                    }
                });

    }

    public void addUserInfoToDatabase(String forename, String surname, String mobile) {
        Log.d(TAG, "Attempting to add a user with a forename, surname and mobile.");

        Map<String, Object> user = new HashMap<>();
        user.put("forename", forename);
        user.put("surname", surname);
        user.put("mobile", mobile);

        final boolean[] result = new boolean[1];

        mDatabase.collection("users")
                .add(user)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Successfully added user information to database.");
                            result[0] = true;
                        } else {
                            Log.d(TAG, "Failed to add user information to database.");
                            result[0] = false;
                        }
                    }
                });

    }
}
