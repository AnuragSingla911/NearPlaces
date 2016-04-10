package nearplaces.android.com.nearplaces;

import java.util.ArrayList;

/**
 * Created by jade on 9/4/16.
 */
public class Suggestion {

    private String suggestion;

    private String placeID;

    private ArrayList<MatchSubString> matchSubStrings = new ArrayList<>();

    public ArrayList<MatchSubString> getMatchSubStrings() {
        return matchSubStrings;
    }

    public void setMatchSubStrings(ArrayList<MatchSubString> matchSubStrings) {
        this.matchSubStrings = matchSubStrings;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public class MatchSubString{
        public int getStartIndex() {
            return startIndex;
        }

        public void setStartIndex(int startIndex) {
            this.startIndex = startIndex;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        int startIndex;
        int offset;
    }


    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
