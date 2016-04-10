package nearplaces.android.com.nearplaces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jade on 9/4/16.
 */
public class SuggestionParser {

    public ArrayList<Suggestion> parseSuggestions(String object1){

        JSONObject object = null;
        try {
            object = new JSONObject(object1);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<Suggestion>();
        }

        ArrayList<Suggestion> suggestions = new ArrayList<>();
        JSONArray predictions = object.optJSONArray("predictions");
        if(predictions != null && predictions.length() > 0){
            for(int i=0; i< predictions.length(); i++){
                JSONObject prefiction = predictions.optJSONObject(i);
                if(prefiction != null){
                    Suggestion suggestion = new Suggestion();
                    suggestion.setSuggestion(prefiction.optString("description"));
                    suggestion.setPlaceID(prefiction.optString("place_id"));

                    JSONArray matchArray = prefiction.optJSONArray("matched_substrings");
                    if(matchArray != null && matchArray.length() > 0){
                        ArrayList<Suggestion.MatchSubString> list = new ArrayList<>();
                        for(int j=0; j< matchArray.length() ; j++){
                            JSONObject match = matchArray.optJSONObject(j);
                            if(match != null){
                                Suggestion.MatchSubString matchSubString = suggestion.new MatchSubString();
                                matchSubString.setStartIndex(match.optInt("offset"));
                                matchSubString.setOffset(match.optInt("length"));
                                list.add(matchSubString);
                            }
                        }
                        suggestion.setMatchSubStrings(list);
                    }
                    suggestions.add(suggestion);
                }

            }

        }

        return suggestions;
    }
}
