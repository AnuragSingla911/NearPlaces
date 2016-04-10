package nearplaces.android.com.nearplaces;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by jade on 6/4/16.
 */
public class ChoiceActivity extends AppCompatActivity implements View.OnClickListener, NetworkDispatcher.CallBack {

    private Button mClickbtn;
    private SearchView mEdittext;
    private RadioGroup mTypeViews;
    private AppCompatSpinner mspinner;
    private Button changeType;
    public static boolean[] isChecked = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mClickbtn = (Button) findViewById(R.id.clickBtn);
        changeType = (Button) findViewById(R.id.changeType);
        mClickbtn.setOnClickListener(this);
        changeType.setOnClickListener(this);
        mEdittext = (SearchView) findViewById(R.id.searchView);
        mspinner = (AppCompatSpinner) findViewById(R.id.choiceDropDown);
        mTypeViews = (RadioGroup) findViewById(R.id.radioGroup);

        if (!SupportedTypes.isPrefType) {
            mEdittext.setVisibility(View.VISIBLE);
            mTypeViews.setVisibility(View.GONE);
            changeType.setVisibility(View.GONE);
        } else {
            mEdittext.setVisibility(View.GONE);
            mTypeViews.setVisibility(View.VISIBLE);
            mTypeViews.setVisibility(View.VISIBLE);
        }
        String[] items = new String[]{"Search By Name", "Search By Type"};
        if (isChecked == null) {
            isChecked = new boolean[SupportedTypes.mTypes.length];
            for (int i = 0; i < 5; i++) {
                isChecked[i] = true;
            }
        }

        mEdittext.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                NetworkDispatcher dispatcher = new NetworkDispatcher(newText, NetworkDispatcher.REQUEST_CODE_SUGGESTION);
                dispatcher.setmCallBack(ChoiceActivity.this);
                dispatcher.start();
                return false;
            }
        });

        initViewForTypes();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        mspinner.setAdapter(adapter);
        mspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mEdittext.setVisibility(View.VISIBLE);
                        mTypeViews.setVisibility(View.GONE);
                        changeType.setVisibility(View.GONE);
                        SupportedTypes.isPrefType = false;
                        break;
                    case 1:
                        SupportedTypes.isPrefType = true;
                        mEdittext.setVisibility(View.GONE);
                        changeType.setVisibility(View.VISIBLE);
                        mTypeViews.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initViewForTypes() {
        mTypeViews.removeAllViews();
        for (int i = 0; i < isChecked.length; i++) {
            if (isChecked[i]) {
                RadioButton button = new RadioButton(this);
                button.setText(SupportedTypes.mTypes[i]);
                button.setChecked(false);
                mTypeViews.addView(button);
            }
        }
    }

    private void showDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Select Preferred Types");

        final boolean[] tempCheckedList = ChoiceActivity.isChecked;

        builderSingle.setMultiChoiceItems(SupportedTypes.mTypes, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                ChoiceActivity.isChecked[which] = isChecked;
            }
        });


        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChoiceActivity.isChecked = tempCheckedList;
                        dialog.dismiss();
                    }
                });

        builderSingle.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initViewForTypes();
                dialog.dismiss();

            }
        });
        builderSingle.show();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.clickBtn:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 20);
                    return;
                }
                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location l = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                String text = "";
                boolean isSearchByName;
                if (mEdittext.getVisibility() == View.VISIBLE) {
                    text = mEdittext.getQuery().toString();

                    isSearchByName = true;
                } else {
                    isSearchByName = false;
                    RadioButton btn = ((RadioButton) mTypeViews.findViewById(mTypeViews.getCheckedRadioButtonId()));
                    if (btn != null) {
                        text = btn.getText().toString();
                    }
                }

                if (text == null || "".equalsIgnoreCase(text.trim())) {
                    Toast.makeText(this, "Please select to search", Toast.LENGTH_SHORT).show();
                    return;
                }
                NetworkDispatcher dispatcher = new NetworkDispatcher(l.getLatitude(), l.getLongitude(), 5000,
                        text, isSearchByName, NetworkDispatcher.REQUEST_CODE_NEAR_PLACE);
                dispatcher.setmCallBack(this);
                dispatcher.start();
                break;
            case R.id.changeType:
                showDialog();
                break;

        }
    }

    @Override
    public void onSuccess(int reqCode) {

        if (reqCode == NetworkDispatcher.REQUEST_CODE_NEAR_PLACE) {

            if(StaticContainer.mPlaceList != null && StaticContainer.mPlaceList.size() > 0) {
                Intent intent = new Intent(this, PlaceListActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(ChoiceActivity.this, "no location found for your requested search", Toast.LENGTH_SHORT).show();
            }

        } else if(reqCode == NetworkDispatcher.REQUEST_CODE_SUGGESTION){
            final ListView listview = (ListView) findViewById(R.id.search_layout);
            listview.setAdapter(new SuggestionAdapter(StaticContainer.mSuggestions, this));
            listview.setVisibility(View.VISIBLE);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    listview.setVisibility(View.GONE);

                    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(ChoiceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(ChoiceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ChoiceActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 20);
                        return;
                    }
                    Location l = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    NetworkDispatcher dispatcher = new NetworkDispatcher(StaticContainer.mSuggestions.get(position).getPlaceID());
                    dispatcher.setmCallBack(ChoiceActivity.this);
                    dispatcher.start();
                }
            });

        } else if (reqCode == NetworkDispatcher.REQUEST_CODE_PLACE_DETAILS){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }

    }


}
