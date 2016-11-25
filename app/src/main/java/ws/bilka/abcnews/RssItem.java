package ws.bilka.abcnews;


public class RssItem {
    private String title;
    private String link;
    private RssItemThumbnail thumbnail;

    public RssItem() {
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public RssItemThumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(RssItemThumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }
}
