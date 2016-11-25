package ws.bilka.abcnews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    RssAdapter mRssAdapter;
    private List<RssItem> mRssItems;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mListView = (ListView)findViewById(R.id.list);
        mRssItems = new ArrayList<>();

        try {
            URL url = new URL("http://feeds.abcnews.com/abcnews/topstories");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(getInputStream(url), "UTF_8");

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {

                    if (xpp.getName().equalsIgnoreCase("item")) {

                        RssItem item = readItemElem(xpp);
                        mRssItems.add(item);
                    }
                }
                eventType = xpp.next();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mRssAdapter = new RssAdapter(this, mRssItems);
        mListView.setAdapter(mRssAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RssItem item = mRssItems.get(position);
                Uri uri = Uri.parse(item.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private RssItem readItemElem(XmlPullParser xpp) throws XmlPullParserException, IOException {

        Log.d(TAG, "whole item read start");

        RssItem item = new RssItem();
        while(xpp.next() != XmlPullParser.END_DOCUMENT) {

            if(xpp.getEventType() == XmlPullParser.START_TAG) {

                if (xpp.getName().equalsIgnoreCase("title")) {
                    String title = xpp.nextText().trim();
                    item.setTitle(title); // TODO trim spaces
                    Log.d(TAG, "title = " + title);
                } else if (xpp.getName().equalsIgnoreCase("media:thumbnail")) {

                    int width = Integer.parseInt(xpp.getAttributeValue(null, "width"));
                    int height = Integer.parseInt(xpp.getAttributeValue(null, "height"));
                    String url = xpp.getAttributeValue(null,"url");
                    RssItemThumbnail thumbnail = new RssItemThumbnail(url, width, height);

                    if(item.getThumbnail() == null) {
                        item.setThumbnail(thumbnail);
                    } else {
                        if(item.getThumbnail().getWidth() > thumbnail.getWidth()) {
                            item.setThumbnail(thumbnail);
                        }
                    }
                } else if (xpp.getName().equalsIgnoreCase("link")) {
                    String link = xpp.nextText();
                    item.setLink(link);
                }
            }

            xpp.next();

            if(xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item"))
                break;
        }
        Log.d(TAG, "whole item read end");

        return item;
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }
}
