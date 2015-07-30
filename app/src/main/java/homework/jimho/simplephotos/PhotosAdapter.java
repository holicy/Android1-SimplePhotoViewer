package homework.jimho.simplephotos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotosAdapter extends ArrayAdapter<Photo>
{

    public PhotosAdapter(Context context, List<Photo> photos)
    {
        super(context, android.R.layout.simple_list_item_1, photos);
    }

    @Override
    public View getView (int position, View convert_view, ViewGroup parent)
    {
        if (convert_view == null) {
            convert_view = LayoutInflater.from(getContext()).inflate(R.layout.photo, parent, false);
        }

        Photo photo = getItem(position);

        ImageView image_view = (ImageView) convert_view.findViewById(R.id.ivPhoto);
        image_view.setImageResource(0);
        Picasso.with(getContext()).load(photo.image_url).into(image_view);

        TextView text_view = (TextView) convert_view.findViewById(R.id.tvCaption);
        text_view.setText("@" + photo.user_name + ": " + photo.caption);

        return convert_view;
    }
}
