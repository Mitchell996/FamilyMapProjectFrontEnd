package com.example.mitchelljohnson.familymapproject.SearchActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.mitchelljohnson.familymapproject.EventActivity;
import com.example.mitchelljohnson.familymapproject.FilterActivity;
import com.example.mitchelljohnson.familymapproject.PersonActivity.personActivity;
import com.example.mitchelljohnson.familymapproject.R;
import com.example.mitchelljohnson.familymapproject.dataObjects.person;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.event.event;
import com.example.mitchelljohnson.familymapproject.dataObjects.model;

public class SearchActivity extends AppCompatActivity {

    public model m;
    EditText searchBar;
   public  ArrayList<searchObject> foundItems;
    RecyclerView recyclerView;
    private final String PERSON_EXTRA ="SELECTED_PERSON";
    private final String EVENT_SELECTED = "SELECTED_EVENT";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        m = model.getInstance();

        searchBar = findViewById(R.id.search_data);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchBar.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String searchtool = searchBar.getText().toString();
                    foundItems = new ArrayList<>();
                    getAllMatching(searchtool);
                    searchObject[] items = foundItems.toArray(new searchObject[foundItems.size()]);
                    Adapter adapter = new Adapter(SearchActivity.this, items);
                    recyclerView.setAdapter(adapter);
                    return true;
                }
                return false;
            }
        });



    }

    public void getAllMatching(String search){
        //use ".*" + search + ".*"
        person toUse = m.getUser();
        addTheSearches(toUse, search);
    }
    private void addTheSearches(person toUse, String search){
        String reg = ".*" +search.toLowerCase()+ ".*";

            //person spouse = toUse.spouse;
        if(!m.isFatherSide()){
            if(m.onFathersSide(toUse)){
                return;
            }
        }
        if(!m.isMotherSide()){
            if(m.onMothersSide(toUse)){
                return;
            }
        }
        if(!m.isGenderBanned(toUse)) {
            if ((toUse.firstName.matches(reg) || toUse.lastName.matches(reg))) {
                foundItems.add(new searchObject(new String(toUse.firstName + " " + toUse.lastName), toUse));
            }
            for (event even : toUse.getLifeEvents()) {
                if ((even.city.toLowerCase().matches(reg) || even.country.toLowerCase().matches(reg)) && !m.inBannedEventTypes(even)) {
                    foundItems.add(new searchObject(new String(even.city + ", " + even.country),
                            new String(toUse.firstName + " " + toUse.lastName), even));
                }
            }
        }
            if (toUse.father != null) {
                addTheSearches(toUse.father, search);
            }



        if(toUse.spouse != null){

            person spouse = toUse.spouse;
            if(!m.isGenderBanned(spouse)) {
                if (spouse.firstName.toLowerCase().matches(reg) || spouse.lastName.toLowerCase().matches(reg)) {
                    foundItems.add(new searchObject(new String(spouse.firstName + " " + spouse.lastName), spouse));
                }
                for (event even : spouse.getLifeEvents()) {
                    if (even.city.toLowerCase().matches(reg) || even.country.toLowerCase().matches(reg)) {
                        foundItems.add(new searchObject(new String(even.city + ", " + even.country),
                                new String(spouse.firstName + " " + spouse.lastName), even));
                    }
                }
            }
            if(spouse.father != null){
                addTheSearches(spouse.father, search);
            }
        }
    }

    class Adapter extends RecyclerView.Adapter<SearchActivity.Holder> {

        private searchObject[] items;
        private LayoutInflater inflater;

        public Adapter(Context context, searchObject[] items) {
            this.items = items;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public SearchActivity.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.search_recycler_event, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(SearchActivity.Holder holder, int position) {
            searchObject item = items[position];
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mainString;
        private TextView belongsTo;
        private ImageView icon;
        private searchObject item;


        public Holder(View view) {
            super(view);
            mainString = (TextView) view.findViewById(R.id.mainString);
            belongsTo = (TextView) view.findViewById(R.id.belongsTo);
            icon = (ImageView) view.findViewById(R.id.iconView);
            icon.setOnClickListener(this);
        }

        void bind(searchObject item) {
            this.item = item;
            mainString.setText(item.searchedLine);
            belongsTo.setText(item.belongsTo);
            if(item.objectType.equals("event")) {
                icon.setImageResource(R.drawable.ic_search_event);
            }
            else{
                Object o = item.tag;
                person dude = (person) o;
                switch(dude.gender){
                    case "m":
                        icon.setImageResource(R.drawable.ic_temp_man);
                       // genderType.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.holo_blue_bright),
                               // PorterDuff.Mode.MULTIPLY);
                        break;
                    case "f":
                        icon.setImageResource(R.drawable.ic_temp_woman);
                        //genderType.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light),
                               // PorterDuff.Mode.MULTIPLY);
                        break;
                }
            }
        }

        @Override
        public void onClick(View view) {
            if(item.objectType.equals("event")){
                event e = (event)item.tag;
                person related = m.findbyID(e.personID);
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);


                intent.putExtra(EVENT_SELECTED, e);
                intent.putExtra(PERSON_EXTRA, related);
                startActivity(intent);
            }
            else if(item.objectType.equals("person")){
                person input = (person)item.tag;
                Intent intent = new Intent(SearchActivity.this, personActivity.class);
                intent.putExtra(PERSON_EXTRA, input.mother);
                startActivity(intent);
            }
        }

    }
}
