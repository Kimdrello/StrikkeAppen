package no.hiof.kimandre.strikkeappen;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.io.Serializable;
import java.util.List;

public class SharedImagesAdapter extends BaseAdapter implements Serializable {
    private Context mContext;
    private List<Uploads> grids;
    int dim;

    public SharedImagesAdapter(Context c, int dimensions, List<Uploads> uris) {
        mContext = c;
        dim = dimensions;
        grids = uris;
    }

    public int getCount() {
        return grids.size();
    }

    public Uploads getItem(int position) {
        return grids.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(dim, dim));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }
        Uploads upl = grids.get(position);
        Glide.with(mContext).load(upl.getUrl()).into(imageView);
        return imageView;
    }
    //Kilder:
    //https://www.simplifiedcoding.net/firebase-storage-example/
    //https://stackoverflow.com/questions/37335102/how-to-get-an-array-with-all-pictures
    //https://stackoverflow.com/questions/37335102/how-to-get-an-array-with-all-pictures/37337436#37337436
}
