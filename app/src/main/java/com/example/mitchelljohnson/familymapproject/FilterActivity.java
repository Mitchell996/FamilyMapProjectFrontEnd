

package com.example.mitchelljohnson.familymapproject;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.mitchelljohnson.familymapproject.dataObjects.model;
import java.util.ArrayList;

import java.util.Set;
import java.util.TreeSet;

public class FilterActivity extends AppCompatActivity {

    // model related items (should be in Model class)

    model m = model.getInstance();
    ArrayList<String> eventToggles = m.getAllEventTypes();
    //String[] eventToggles =
    Set<String> selected = new TreeSet<>(m.getBannedEventTypes());
    // end of model related items

    private TextView textView;
    private RecyclerView recyclerView;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(!eventToggles.contains("Father's side")) {
            eventToggles.add("Father's side");
            eventToggles.add("Mother's side");
            eventToggles.add("Male Events");
            eventToggles.add("Female Events");
        }
        m = model.getInstance();
        selected = new TreeSet<>(m.getBannedEventTypes());
        updateUI(eventToggles.toArray(new String[eventToggles.size()]));
    }

    void updateUI(String[] items) {

        selected.clear();
        adapter = new Adapter(this, items);
        recyclerView.setAdapter(adapter);
    }

    void updateSelected() {

        if(m.getBannedEventTypes().contains("Mother's side")){
            m.setMotherSide(false);
        }
        else{
            m.setMotherSide(true);
        }
        if(m.getBannedEventTypes().contains("Father's side")){
            m.setFatherSide(false);
        }
        else{
            m.setFatherSide(true);
        }
    }

    class Adapter extends RecyclerView.Adapter<Holder> {

        private String[] items;
        private LayoutInflater inflater;

        public Adapter(Context context, String[] items) {
            this.items = items;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.filter_recycler, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            String item = items[position];
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView;
        private Switch eventSwitch;
        private String item;

        public Holder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.textview);
            eventSwitch = (Switch) view.findViewById(R.id.eventSwitch);
            eventSwitch.setOnClickListener(this);
        }

        void bind(String item) {
            this.item = item;
            textView.setText(item);
            if(!(m.inBannedEventTypes(item))) {
                eventSwitch.setChecked(true);
            }
            else{
                eventSwitch.setChecked(false);
            }
        }

        @Override
        public void onClick(View view) {
            if (eventSwitch.isChecked())
                m.removeBannedEventType(item);
            else
                m.addBannedEvent(item);
            if(item.equals("Male Events")){
                m.toggleMale();
            }
            if(item.equals("Female Events")){
                m.toggleFemale();
            }
           updateSelected();
        }

    }

}
