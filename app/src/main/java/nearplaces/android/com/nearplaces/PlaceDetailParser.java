package nearplaces.android.com.nearplaces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jade on 9/4/16.
 */
public class PlaceDetailParser {

    public PlaceDetail parsePlaceDetail(String json){
        JSONObject object = null;
        try {
            object = new JSONObject(json);
            object = object.optJSONObject("result");
            if(object == null){
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        PlaceDetail detail = new PlaceDetail();
        detail.setName(object.optString("name"));
        detail.setAddress(object.optString("formatted_address"));
        detail.setIconUrl(object.optString("icon"));
        detail.setPhone_no(object.optString("formatted_phone_number"));
        JSONArray photos = object.optJSONArray("photos");
        if(photos != null && photos.length() > 0){
            ArrayList<String> mphotoIds = new ArrayList<>();
            for(int  i=0; i< photos.length();i++){
                JSONObject photo = photos.optJSONObject(i);
                if(photo != null){
                    mphotoIds.add(photo.optString("photo_reference"));
                }
            }
            detail.setMphotoID(mphotoIds);
        }

        JSONObject geometry = object.optJSONObject("geometry");
        if(geometry != null){
            JSONObject location = geometry.optJSONObject("location");
            if(location != null ){
                detail.setLongitude(location.optString("lng"));
                detail.setLatitude(location.optString("lat"));
            }
        }
        return detail;
    }
}
