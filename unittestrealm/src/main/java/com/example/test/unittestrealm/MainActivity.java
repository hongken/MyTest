package com.example.test.unittestrealm;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test.unittestrealm.model.Person;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @BindView(R.id.clean_up)    Button cleanUp;
    @BindView(R.id.container)   LinearLayout container;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(getApplicationContext());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        container.removeAllViews();

        // Open the default Realm for the UI thread.
        realm = Realm.getDefaultInstance();

        // Clean up from previous run
        cleanUp();

        basicCRUD(realm);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String info = "";
                info += complexQuery();
                return null;
            }
        }.execute();

    }

    private void cleanUp() {
        // Delete all persons
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Person.class);
            }
        });
    }

    private void showStatus(String txt) {
        TextView textView = new TextView(this);
        textView.setText(txt);
        container.addView(textView);

    }

    private void basicCRUD(Realm realm) {
        showStatus("Perform basic Create/Read/Update/Delete (CRUD) operations...");

        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                // Add a person
                Person person = realm.createObject(Person.class);
                person.setId(1);
                person.setName("John Young");
                person.setAge(14);
            }
        });

        // Find the first person (no query conditions) and read a field
        final Person person = realm.where(Person.class).findFirst();
        showStatus(person.getName() + ":" + person.getAge());

        // Update person in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                person.setName("John Senior");
                person.setAge(89);
            }
        });

        showStatus(String.format("%1%s got older: %2%s", person.getName(), person.getAge()));

        // Add two more people
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Person person = realm.createObject(Person.class);
                person.setName("Jane");
                person.setAge(27);

                Person person2 = realm.createObject(Person.class);
                person2.setName("Robert");
                person2.setAge(42);
            }
        });

        RealmResults<Person> people = realm.where(Person.class).findAll();
        showStatus(String.format("Found %s people", people.size()));
        for (Person p : people) {
            showStatus(String.format("Found %s", p.getName()));
        }

    }

    private String complexQuery() {
        String status = "\n\nPerforming complex Query operation...";

        Realm realm = Realm.getDefaultInstance();
        status += "\nNumber of people in the DB: " + realm.where(Person.class).count();

        // Find all persons where age between 1 and 99 and name begins with "J".
        RealmResults<Person> result = realm.where(Person.class)
                .between("age", 1, 99)
                .beginsWith("name", "J").findAll();
        status += "\nNumber of people aged between 1 and 99 who's name start with 'J': " + result.size();

        realm.close();
        return status;
    }

    @OnClick(R.id.clean_up)
    public void onClick(View v) {
        v.setEnabled(false);
        cleanUp();
        v.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();// Remember to close Realm when done.
    }
}
