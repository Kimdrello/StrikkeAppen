package no.hiof.kimandre.strikkeappen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import java.io.File;
import java.io.Serializable;


public class ImageAdapter extends BaseAdapter implements Serializable {
    private Context mContext;
    File[] grids;
    File[] fileDir;
    int dim;


    public ImageAdapter(Context c, int dimensions, String path) {
        mContext = c;
        dim = dimensions;
        File dir = new File(path);
        grids = dir.listFiles();
    }
    public int getCount() {
        return grids.length;
    }
    public String getItem(int position) {
        return grids[position].getAbsolutePath();
    }
    public long getItemId(int position) {
        return position;
    }
    public String getAlbumName(int folderID) {
        return fileDir[folderID].getName();
    }
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Bitmap bm = BitmapFactory.decodeFile(grids[position].getAbsolutePath());
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(dim, dim));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(bm);
        return imageView;
    }
}
