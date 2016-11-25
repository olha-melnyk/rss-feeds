package ws.bilka.abcnews;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;
import ws.bilka.abcnews.model.NewsItem;

public class RssAdapter extends BaseAdapter {

    private final List<NewsItem> items;
    private final Context context;

    public RssAdapter(Context context, List<NewsItem> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.rss_item, null);
            holder = new ViewHolder();

            holder.itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
            convertView.setTag(holder);

            holder.itemDate = (TextView) convertView.findViewById(R.id.itemDate);
            convertView.setTag(holder);

            holder.itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemTitle.setText(items.get(position).getTitle());
        holder.itemDate.setText(items.get(position).getDate());

        String imageUrl = items.get(position).getThumbnail().getUrl();

        Picasso.with(convertView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.itemImage);

        return convertView;
    }

    static class ViewHolder {
        TextView itemTitle;
        TextView itemDate;
        ImageView itemImage;
    }
}
