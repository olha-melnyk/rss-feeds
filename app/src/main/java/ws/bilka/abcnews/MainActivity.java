package ws.bilka.abcnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import ws.bilka.abcnews.model.NewsItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    RssAdapter mRssAdapter;
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

        Intent intent = new Intent(this, RssService.class);
        intent.putExtra(RssService.RECEIVER, new RssResultReceiver(this));
        startService(intent);
    }

    @SuppressLint("ParcelCreator")
    class RssResultReceiver extends ResultReceiver {

        private Context mContext;

        public RssResultReceiver(Context context) {
            super(new Handler());
            mContext = context;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            final List<NewsItem> items = (List<NewsItem>) resultData.getSerializable(RssService.ITEMS);
            if (items != null) {
                mRssAdapter = new RssAdapter(mContext, items);
                mListView.setAdapter(mRssAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        NewsItem item = items.get(position);
                        Uri uri = Uri.parse(item.getLink());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            }
        };
    };
}
