filename fbase "/path/to/firebase-client-jvm-2.3.1.jar"; /*https://cdn.firebase.com/java/firebase-client-android-2.3.1.jar*/
filename fb4sas "/path/to/firebase4sas.jar"; /*https://github.com/FriedEgg/firebase4sas/releases*/

/* create a file reference for the classes we are going to compile below with PROC GROOVY*/
filename cp temp;
proc groovy classpath=cp;

   /* add the two dependent libraries*/
   add classpath=fbase;
   add classpath=fb4sas;

   /* create a Model for the objects we are going to load. */
   submit load;
      public class Person {
         private String sex;
         private double age;
         private double height;
         private double weight;

         public Person(String sex, double age, double height, double weight) {
            this.sex    = sex;
            this.age    = age;
            this.height = height;
            this.weight = weight;
         }

         public String getSex() {
            return sex;
         }

         public double getAge() {
            return age;
         }

         public double getHeight() {
            return height;
         }

         public double getWeight() {
            return weight;
         }

         public String toString() {
            return "Person: Sex=" + sex + ", Age=" + age + ", Height" + height + ", Weight" + weight;
         }
      }
   endsubmit;

   /* create a class from the abstract to perform the load of our model */
   submit load;
      import java.lang.InterruptedException;

      public class PersonLoader extends AbstractFirebase4Sas {
         public PersonLoader(String firebase, String dsn, String name, String sex, double age, double height, double weight) {
            setFirebase(firebase);

            Person p = new Person(sex, age, height, weight);
            setLoaderObject(p);

            addChild(dsn);
            addChild(name);

            try {
               loadObject();
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
      }
   endsubmit;

   /* you can test it in PROC GROOVY */
   /*
   submit;
      lp = new LoadPerson("MYFIREBASE", "class", "Frog", "F", 14.1, 15.1, 16.1);
   endsubmit;
   */

quit;

/* add compiled classes from PROC GROOVY to session CLASSPATH */
options set=classpath "%sysfunc(pathname(cp,f))";

/* use data step JavaObject Component to load our data set to Firebase */
data _null_;
   set sashelp.class indsname=dsn;

   declare javaObj lp;
   lp = _new_ javaObj ("LoadPerson","MYFIREBASE",scan(dsn,2,'.'),name,sex,age,height,weight);

   lp.flushJavaOutput();
   lp.delete();

run;