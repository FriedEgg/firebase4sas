import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Firebase4SAS extends AbstractFirebase4Sas{
    private String firebase;
    private List<String> children = new ArrayList<>();

    public void setChildren(ArrayList<String> children) {
        this.children = children;
    }

    public void addChild(String child) {
        this.children.add(child);
    }

    public List<String> getChildren() {
        return this.children;
    }

    public void setFirebase(String firebase) {
        this.firebase = firebase;
    }

    public String getFirebase() {
        return this.firebase;
    }

    public void loadObject(Object obj) {
        Firebase ref = new Firebase("https://" + this.firebase + ".firebaseio.com");

        for (String child : this.children) {
            ref = ref.child(child);
        }

        final CountDownLatch done = new CountDownLatch(1);
        ref.setValue(obj, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Data could not be saved. " + firebaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                }
                done.countDown();
            }
        });
        try {
            done.await();
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }
    }
}
