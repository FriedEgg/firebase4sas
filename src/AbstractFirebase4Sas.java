import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.lang.InterruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

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

    public void loadObject() throws InterruptedException {
        Firebase ref = new Firebase("https://" + firebase + ".firebaseio.com");

        for (String child : children) {
            ref = ref.child(child);
        }

        final Semaphore semaphore = new Semaphore(1, true);
        semaphore.acquire();
        ref.setValue(obj, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Data could not be saved. " + firebaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                }
                semaphore.release();
            }
        });
        Thread.sleep(new Random().nextInt(4000));
        semaphore.acquire();
    }
}
