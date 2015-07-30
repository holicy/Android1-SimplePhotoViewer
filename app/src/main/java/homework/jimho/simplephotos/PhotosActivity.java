package homework.jimho.simplephotos;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;


public class PhotosActivity extends ActionBarActivity {

    static String CLIENT_ID = "404983f52d4f4680b740ed6a7c852a5e";

    static String POPULAR_URL = String.format("https://api.instagram.com/v1/media/popular?client_id=%s", CLIENT_ID);

    private List<Photo> photos;
    private ArrayAdapter<Photo> photos_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        photos = new ArrayList<>();

        photos_adapter = new PhotosAdapter(getBaseContext(), photos);

        ListView photos_view = (ListView) findViewById(R.id.lvPhotos);

        photos_view.setAdapter(photos_adapter);

        updatePhotos();
    }

    protected void updatePhotos()
    {
        AsyncHttpClient http_client = new AsyncHttpClient();

        http_client.get(POPULAR_URL, null, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                List<Object> photos_doc;
                try {
                    photos_doc = JsonPath.read(responseString, "$.data");
                } catch (PathNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

                for (Object photo_doc : photos_doc) {
                    ReadContext photo_cx = JsonPath.parse(photo_doc);

                    Photo photo = new Photo();

                    try {
                        photo.user_name = photo_cx.read("$.user.username");
                        photo.image_url = photo_cx.read("$.images.standard_resolution.url");
                        photo.caption = photo_cx.read("$.caption.text");
                        photo.user_image_url = photo_cx.read("$.user.profile_picture");
                        photo.like_count = photo_cx.read("$.likes.count");
                        photo.created_time = photo_cx.read("$.caption.created_time");
                    } catch (PathNotFoundException | NumberFormatException e) {
                        e.printStackTrace();
                        continue;
                    }

                    photos.add(photo);
                }

                photos_adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("API", throwable.getMessage());
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
