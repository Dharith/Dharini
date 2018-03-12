package edu.nus.trailblazelearn.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.nus.trailblazelearn.utility.dbUtil;

public class User {
    private static final String TAG = "User.CLASS";
    private static User user;
    private Map<String, Object> data = new HashMap<>();
    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private dbUtil dbUtil = new dbUtil();
    private User() {
        initialize();
    }

    public static User getInstance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public static void signOut() {
        user = null;
    }

    private void initialize() {
        dbUtil.readWithDocID("users", mAuth.getUid())
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                                setData(document.getData());
                            } else {
                                Log.d(TAG, "No such document, creating new user in DB");
                                Map<String, Object> temp = new HashMap<>();
                                temp.put("name", mAuth.getDisplayName());
                                temp.put("email", mAuth.getEmail());
                                setData(temp);
                                save();
                            }
                        } else {
//                    Add exception handling
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void grantTrainer() {
        data.put("isTrainer", true);
        save();
    }

    public void grantParticipant() {
        data.put("isParticipant", true);
        save();
    }

    public void revokeTrainer() {
        data.remove("isTrainer");
        save();
    }

    public void revokeParticipant() {
        data.remove("isParticipant");
        save();
    }

    //  Saves to Firebase
    public void save() {
        db.collection("users").document(mAuth.getUid()).set(data);
    }

    //  data getter
    public Map<String, Object> getData() {
        return data;
    }

    //  data setter
    private void setData(Map<String, Object> in) {
        data = in;
    }

    //  Enrolls participant to the learning trail
    public void enrollforTrail(String trailID) {
        if (isParticipant()) {
            List<String> list = (List<String>) data.get("enrolledTrails");
            if (list != null) {
                list.add(trailID);
            } else {
                list = new ArrayList<>();
                list.add(trailID);
                data.put("enrolledTrails", list);
            }
            save();
        }
    }

    //  Returns true if user is trainer
    private boolean isTrainer() {
        return data.get("isTrainer") != null;
    }

    //  Returns true if user is participant
    private boolean isParticipant() {
        return data.get("isParticipant") != null;
    }
}