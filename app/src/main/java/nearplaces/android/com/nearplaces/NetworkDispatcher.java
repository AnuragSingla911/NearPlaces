package nearplaces.android.com.nearplaces;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jade on 6/4/16.
 */
public class NetworkDispatcher extends Thread {

    int requestCode;
    public static int REQUEST_CODE_SUGGESTION = 100;
    public static int REQUEST_CODE_NEAR_PLACE = 101;
    public static int REQUEST_CODE_PLACE_DETAILS = 102;

    public interface CallBack {
        void onSuccess(int requestCode);
    }

    Handler uiHandler;
    CallBack mCallBack;

    public void setmCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    private static final String API_KEY = "AIzaSyCcNHSYigZLihuhtzWjgpczn0q3msZzXwc";
    StringBuilder googlePlacesUrl = null;

    public NetworkDispatcher(String placeID){

        this.requestCode = REQUEST_CODE_PLACE_DETAILS;
        uiHandler = new Handler();
        googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlacesUrl.append("placeid=" + placeID + "&key=" + API_KEY);
    }

    public NetworkDispatcher(String query,int requestCode) {
        this.requestCode = requestCode;
        uiHandler = new Handler();
        googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
        googlePlacesUrl.append("input=" + query + "&types=geocode&key=" + API_KEY);
    }

    public NetworkDispatcher(double latitude, double longitude, double radiusInMeters, String type, boolean isSearchByName,int requestCode) {

        this.requestCode = requestCode;
        uiHandler = new Handler();
        googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + radiusInMeters);
        if (isSearchByName)
            googlePlacesUrl.append("&name=" + type);
        else
            googlePlacesUrl.append("&type=" + type);

        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + API_KEY);
    }

    @Override
    public void run() {
        super.run();
        InputStream inputStream = null;
        String httpData = "";
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(googlePlacesUrl.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            httpData = stringBuffer.toString();

            if (requestCode == REQUEST_CODE_SUGGESTION) {
                StaticContainer.mSuggestions = new SuggestionParser().parseSuggestions(httpData);
            } else if(requestCode == REQUEST_CODE_NEAR_PLACE){
                StaticContainer.mPlaceList = new PlaceParser().parsePlaces(httpData);
            } else if(requestCode == REQUEST_CODE_PLACE_DETAILS){
                StaticContainer.mDetail = new PlaceDetailParser().parsePlaceDetail(httpData);
            }


            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(inputStream != null)
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            urlConnection.disconnect();
        }

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCallBack != null) {
                    mCallBack.onSuccess(requestCode);
                }
            }
        });

    }


}
