package ws.bilka.abcnews;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import ws.bilka.abcnews.model.NewsItem;
import ws.bilka.abcnews.model.NewsItemThumbnail;

public class RssParser {

    public List<NewsItem> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            inputStream.close();
        }
    }

    protected List<NewsItem> readFeed(XmlPullParser xpp) {

        List<NewsItem> mNewsItems = new LinkedList<>();

        try {
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    NewsItem item = readItemElem(xpp);
                    mNewsItems.add(item);
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return mNewsItems;
    }

    private NewsItem readItemElem(XmlPullParser xpp) throws XmlPullParserException, IOException {

        NewsItem item = new NewsItem();

        while(xpp.next() != XmlPullParser.END_DOCUMENT) {
            if(xpp.getEventType() == XmlPullParser.START_TAG) {
                if (xpp.getName().equalsIgnoreCase("title")) {
                    String title = xpp.nextText().trim();
                    item.setTitle(title.trim());;
                } else if (xpp.getName().equalsIgnoreCase("media:thumbnail")) {
                    int width = Integer.parseInt(xpp.getAttributeValue(null, "width"));
                    int height = Integer.parseInt(xpp.getAttributeValue(null, "height"));
                    String url = xpp.getAttributeValue(null,"url");
                    NewsItemThumbnail thumbnail = new NewsItemThumbnail(url, width, height);
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
                } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                    String date = xpp.nextText();
                    item.setDate(date);
                }
            }
            xpp.next();
            if(xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item"))
                break;
        }
        return item;
    }
}