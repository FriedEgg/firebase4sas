import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public abstract class AbstractFirebase4Sas implements Firebase4SasInterface {
    private String firebase;
    private List<String> children = new ArrayList<>();
    private Object obj;

    public void setChildren(ArrayList<String> children) {
        this.children = children;
    }

    public void addChild(String child) {
        this.children.add(child);
    }

    public List<String> getChildren() {
        return children;
    }

    public void setFirebase(String firebase) {
        this.firebase = firebase;
    }

    public String getFirebase() {
        return firebase;
    }

    public void setLoaderObject(Object obj) {
        this.obj = obj;
    }

    public void loadObject() {
        Firebase ref = new Firebase("https://" + firebase + ".firebaseio.com");

        for (String child : children) {
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
