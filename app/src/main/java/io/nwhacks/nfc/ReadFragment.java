package io.nwhacks.nfc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rice on 11/30/17.
 */

public class ReadFragment extends NFCFragment {
    public static final String ARG_OBJECT = "object";

    private List<String> arguments;
    private Spinner events;
    private TextView recordDisplay;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.read_fragment, container, false);
        events = rootView.findViewById(R.id.event_spinner);
        if (arguments != null) {
            this.setArguments(arguments);
        }
        recordDisplay = rootView.findViewById(R.id.recordDisplay);
        return rootView;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
        if (events != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arguments);
            events.setAdapter(adapter);
        }
    }

    public void loggedIn(FirebaseUser user) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("admin/events");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                arguments = new ArrayList<>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Event e = postSnapshot.getValue(Event.class);
                    arguments.add(e.name);
                }
                setArguments(arguments);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void tagDiscovered(NFCManager mgr, Intent intent) {
        ArrayList<String> records = mgr.readTagFromIntent(intent);
        StringBuilder sb = new StringBuilder();
        sb.append("Tag records:\n");
        for (int i = 0; i<records.size(); i++) {
            sb.append(i);
            sb.append(". ");
            sb.append(records.get(i));
            sb.append("\n");
        }
        String body = sb.toString();
        MainActivity.toast(getContext(), body);
        recordDisplay.setText(body);
    }
}
