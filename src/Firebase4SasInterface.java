import java.util.ArrayList;
import java.util.List;

public interface Firebase4SasInterface {
   String firebase = "";
   List<String> children = new ArrayList<>();

   void setChildren(ArrayList<String> children);

   void addChild(String child);

   List<String> getChildren();

   void setFirebase(String firebase);

   void loadObject();
}