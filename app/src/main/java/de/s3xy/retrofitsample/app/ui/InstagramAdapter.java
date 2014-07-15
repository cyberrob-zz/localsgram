package de.s3xy.retrofitsample.app.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.s3xy.retrofitsample.app.R;
import de.s3xy.retrofitsample.app.pojo.PopularPhotos;

/**
 * Created by robertwang on 14/6/12.
 */
public class InstagramAdapter extends BaseAdapter {

    private Context mContext;
    private PopularPhotos photos;

    public PopularPhotos getPhotos() {
        return photos;
    }

    public void setPhotos(PopularPhotos photos) {
        this.photos = photos;
    }

    public InstagramAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return (this.photos == null) ? 0 :
                this.photos.getData().size();
    }

    @Override
    public String getItem(int i) {
        return (this.photos == null) ? "" :
                this.photos.getData().get(i).getImages().getStandard_resolution().getUrl();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        SquaredImageView squaredImageView = (SquaredImageView) view;

        if (view == null) {
            squaredImageView = new SquaredImageView(mContext);
            squaredImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        String url = getItem(i);

        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .fit()
                .into(squaredImageView);

        return squaredImageView;
    }
}
