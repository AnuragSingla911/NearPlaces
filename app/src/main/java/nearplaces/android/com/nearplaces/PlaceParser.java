package nearplaces.android.com.nearplaces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jade on 7/4/16.
 */
public class PlaceParser {

    public ArrayList<Places> parsePlaces(String s) {
        ArrayList<Places> mPlaces = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(s);
            if (object != null) {
                JSONArray array = object.optJSONArray("results");
                if (array != null && array.length() > 0) {
                    for(int i =0; i< array.length() ; i++) {
                        JSONObject object1 = array.optJSONObject(i);
                        Places place = new Places();
                        place.setAddress(object1.optString("vicinity"));
                        place.setPlaceID(object1.optString("place_id"));
                        place.setPhotoID(object1.optString("icon"));
                        place.setName(object1.optString("name"));
                        JSONObject geometry = object1.optJSONObject("geometry");
                        if(geometry != null){
                            JSONObject location = geometry.optJSONObject("location");
                            if(location != null){
                                place.setLatitude(location.optString("lat"));
                                place.setLongitude(location.optString("lng"));
                            }
                        }
                        mPlaces.add(place);
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mPlaces;
    }
}
