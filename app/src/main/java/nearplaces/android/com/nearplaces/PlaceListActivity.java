package nearplaces.android.com.nearplaces;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jade on 7/4/16.
 */
public class PlaceListActivity extends AppCompatActivity{

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        mRecyclerView = (RecyclerView)findViewById(R.id.placeList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new PlaceListAdapter(this));

    }


    public static class PlaceItemHolder extends RecyclerView.ViewHolder{

        final private ImageView imageView;
        final private TextView name;
        final private TextView address;
        final private View mView;

        public PlaceItemHolder(View itemView) {
            super(itemView);
            mView = itemView;
            imageView = (ImageView)itemView.findViewById(R.id.icon);
            name = (TextView)itemView.findViewById(R.id.name);
            address = (TextView)itemView.findViewById(R.id.address);
        }
    }

    public static class PlaceListAdapter extends RecyclerView.Adapter<PlaceItemHolder> implements NetworkDispatcher.CallBack{

        private Context mContext;
        private ArrayList<Places> mPlacesList;

        PlaceListAdapter(Context context){
            mContext = context;
            mPlacesList = StaticContainer.mPlaceList;
        }


        @Override
        public PlaceItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new PlaceItemHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false));
        }

        @Override
        public void onBindViewHolder(PlaceItemHolder holder, final int position) {

            holder.address.setText(mPlacesList.get(position).getAddress());
            holder.name.setText(mPlacesList.get(position).getName());

            Picasso.with(mContext).load(mPlacesList.get(position).getPhotoID()).into(holder.imageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        NetworkDispatcher dispatcher = new NetworkDispatcher(mPlacesList.get(position).getPlaceID());
                        dispatcher.setmCallBack(PlaceListAdapter.this);
                        dispatcher.start();

                }
            });


        }

        @Override
        public int getItemCount() {
            return mPlacesList != null ? mPlacesList.size() : 0;
        }

        @Override
        public void onSuccess(int requestCode) {
            Intent intent = new Intent(mContext, MapsActivity.class);
            mContext.startActivity(intent);
        }
    }
}
