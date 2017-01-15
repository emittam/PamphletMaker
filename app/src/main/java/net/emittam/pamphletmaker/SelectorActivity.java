package net.emittam.pamphletmaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

public class SelectorActivity extends AppCompatActivity {

    protected ListView listView;
    protected CivicPOIListAdapter adapter;
    static HashMap<String, String> temp;

    public static void startActivity(Activity base, boolean view) {
        Intent intent = new Intent(base, SelectorActivity.class);
        intent.putExtra("view", view);
        base.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        setTitle("写真ポイント一覧");

        listView = (ListView) findViewById(R.id.listView);

        this.adapter = new CivicPOIListAdapter(this);
        listView.setAdapter(this.adapter);
        listView.setOnItemClickListener(new CivicPOIListItemClickListener());

        /// 現在地仮置き
        final float lat = 35.7989008f;
        final float lng = 136.2903548f;
        final String filter = createFilter(lat, lng, 5);

        final String query_place =
                "prefix xsd: <http://www.w3.org/2001/XMLSchema#>"
                        + "select ?s ?name ?cat ?lat ?lng ?add ?name ?desc ?img {"
                        + "?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://purl.org/jrrk#CivicPOI>;"
                        + "<http://imi.ipa.go.jp/ns/core/rdf#種別> ?cat;"
                        + "<http://www.w3.org/2003/01/geo/wgs84_pos#long> ?lng;"
                        + "<http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?lat;"
                        + "<http://www.w3.org/2000/01/rdf-schema#label> ?name;"
                        + "<http://imi.ipa.go.jp/ns/core/rdf#説明> ?desc;"
                        + "<http://schema.org/image> ?img;"
                        + "filter(regex(str(?s), 'imadate/ikeda/') && regex(str(?cat), '観光'))"
                        + "}";
        executeSPARQL(query_place);
    }

    /// Nキロメートルフィルター
    public String createFilter(float lat, float lng, float distance) {
        // Nキロメートル範囲内の避難所を取得するためのフィルタを返す
        float lat_degree = 0.0090133729745762f;
        float lng_degree = 0.010966404715491394f;
        float latmin = lat - distance * lat_degree;
        float latmax = lat + distance * lat_degree;
        float lngmin = lng - distance * lng_degree;
        float lngmax = lng + distance * lng_degree;
        return "filter(xsd:float(?lat) < " + String.valueOf(latmax) + " && xsd:float(?lat) > " + String.valueOf(latmin)
                + " && xsd:float(?lng) < " + String.valueOf(lngmax) + " && xsd:float(?lng) > " + String.valueOf(lngmin) + ")";
    }

    public class CivicPOIListAdapter extends ArrayAdapter<CivicPOIModel> {

        LayoutInflater mInflater;
        Context mContext;

        public CivicPOIListAdapter(Context context) {
            super(context, 0);
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.civic_poi_cell, parent, false);
            }

            CivicPOIModel model = getItem(position);

            TextView tv = (TextView) convertView.findViewById(R.id.title);
            tv.setText(model.name);

            tv = (TextView) convertView.findViewById(R.id.sub);
            tv.setText(model.desc);

            ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
            Picasso.with(mContext).load(model.img).into(iv);

            return convertView;
        }
    }

    public void executeSPARQL(final String query) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = null;
                String url = null;

                try {
                    url = "http://sparql.odp.jig.jp/api/v1/sparql";
                    url += "?output=json&query=" + URLEncoder.encode(query, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // リクエストオブジェクトを作って
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                // クライアントオブジェクトを作って
                OkHttpClient client = new OkHttpClient();

                // リクエストして結果を受け取って
                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 返す
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject rootObject = new JSONObject(result);
                    JSONArray dataArray = rootObject.getJSONObject("results").getJSONArray("bindings");

                    for (int i = 0; i < dataArray.length(); i++) {

                        temp = new HashMap<>();
                        temp.put("name", dataArray.getJSONObject(i).getJSONObject("name").getString("value"));
                        temp.put("desc", dataArray.getJSONObject(i).getJSONObject("desc").getString("value"));
                        temp.put("img", dataArray.getJSONObject(i).getJSONObject("img").getString("value"));
                        temp.put("lat", dataArray.getJSONObject(i).getJSONObject("lat").getString("value"));
                        temp.put("lng", dataArray.getJSONObject(i).getJSONObject("lng").getString("value"));

                        if (!dataArray.getJSONObject(i).isNull("rad")) {
                            temp.put("area", dataArray.getJSONObject(i).getJSONObject("rad").getString("value"));
                        }

                        if (!dataArray.getJSONObject(i).isNull("add")) {
                            temp.put("address", dataArray.getJSONObject(i).getJSONObject("add").getString("value"));
                        }

                        adapter.add(new CivicPOIModel(temp));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    class CivicPOIListItemClickListener implements ListView.OnItemClickListener {
        CivicPOIListItemClickListener() {
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = (ListView) parent;
            CivicPOIModel item = (CivicPOIModel) listView.getItemAtPosition(position);
            Boolean type = getIntent().getBooleanExtra("view", false);
            if (type == Boolean.TRUE) {
                SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = defaultPref.edit();
                editor.remove(ImageCollectBroadcastReceiver.SAVE_FILE_PATH_KEY);
                editor.commit();
                PamphletViewerActivity.startActivity(SelectorActivity.this, item.name, item.name + "\n\n" + item.desc);

            } else {
                SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = defaultPref.edit();
                editor.putString(ImageCollectBroadcastReceiver.SAVE_FILE_PATH_KEY, item.name);
                editor.commit();
                Toast.makeText(SelectorActivity.this, "好きなカメラアプリで写真を撮ってください", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
