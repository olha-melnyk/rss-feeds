package ws.bilka.abcnews;


import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ws.bilka.abcnews.model.NewsItem;

public class RssService extends IntentService {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String RSS_URL = "http://feeds.abcnews.com/abcnews/topstories";

    public static final String ITEMS = "items";
    public static final String RECEIVER = "receiver";

    public RssService() {
        super("RssService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        List<NewsItem> mNewsItems = new ArrayList<>();

        try {
            URL url = new URL(RSS_URL);
            mNewsItems = new RssParser().parse(getInputStream(url));
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(ITEMS, (Serializable) mNewsItems);
        ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
        receiver.send(0, bundle);
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

}
