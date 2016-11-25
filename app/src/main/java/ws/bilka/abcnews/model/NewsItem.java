package ws.bilka.abcnews.model;


public class NewsItem {
    private String title;
    private String link;
    private String date;
    private NewsItemThumbnail thumbnail;

    public NewsItem() {
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

    public NewsItemThumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(NewsItemThumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
