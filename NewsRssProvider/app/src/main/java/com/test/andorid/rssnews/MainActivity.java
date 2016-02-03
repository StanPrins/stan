package com.test.andorid.rssnews;
        import android.app.Activity;
        import android.content.Intent;
        import android.content.res.Resources;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.util.SparseArray;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.test.andorid.rssparsertest2.lib.RssItem;
        import com.test.andorid.rssparsertest2.lib.RssReader;

        import java.security.acl.Group;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class NewsActivity extends Activity {
    private ListView mList;
    public InteractiveArrayAdapter adapter;
    ArrayList<Map<String, String>> list;
    Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Map map = adapter.getItem(position);
                String desc = "Description : " +map.get("description").toString();
                Log.d("**********", desc);
                Toast.makeText(activity, desc, Toast.LENGTH_LONG).show();
                String guid = map.get("guid").toString();
                Intent intent =  new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(guid));
                startActivity(intent);
            }
        });
        new GetRssFeed().execute("http://feeds.bbci.co.uk/news/rss.xml");
    }
    private class GetRssFeed extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<Map<String, String>>();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                RssReader rssReader = new RssReader(params[0]);
                int max_length =15;
                int location = 0;
                for (RssItem item :rssReader.getItems())
                {
                    if(location <rssReader.getItems().size()) {
                        if (item.getImageUrl() != null) {
                            HashMap<String, String> hash = new HashMap<String, String>();
                            hash.put("title", item.getTitle());
                            hash.put("description", item.getDescription());
                            hash.put("image", item.getImageUrl());
                            hash.put("pubDate", item.getPubDate());
                            hash.put("guid", item.getGuid());
                            list.add(hash);
                            location++;
                        }
                    }
                }


            } catch (Exception e) {
                Log.v("Error Parsing Data", e + "");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new InteractiveArrayAdapter(activity , list);
            adapter.notifyDataSetChanged();
            mList.setAdapter(adapter);
        }
    }
}