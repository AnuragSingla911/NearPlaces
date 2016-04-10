package nearplaces.android.com.nearplaces;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import java.util.ArrayList;

/**
 * Created by jade on 9/4/16.
 */
public class SuggestionAdapter extends BaseAdapter {

    ArrayList<Suggestion> mSuggestionList = new ArrayList<>();
    Context mContext;

    public SuggestionAdapter(ArrayList<Suggestion> mSuggestionList, Context context){
        this.mSuggestionList = mSuggestionList;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return mSuggestionList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.spinner_item
                    , parent, false);
        }

        SpannableString string = new SpannableString(mSuggestionList.get(position).getSuggestion());

        ArrayList<Suggestion.MatchSubString> list = mSuggestionList.get(position).getMatchSubStrings();

        if(list != null && list.size() > 0){
            for(Suggestion.MatchSubString matchSubString: list){
                string.setSpan(new ForegroundColorSpan(Color.RED), matchSubString.getStartIndex(), matchSubString.getOffset()
                , Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }

        ((CheckedTextView)convertView).setText(string);


        return convertView;
    }
}
