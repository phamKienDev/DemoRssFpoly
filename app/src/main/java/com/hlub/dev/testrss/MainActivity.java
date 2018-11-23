package com.hlub.dev.testrss;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvRss;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> list;
    private ArrayList<String> listLink;
    private EditText edtTextRss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvRss = findViewById(R.id.lvRss);
        edtTextRss = (EditText) findViewById(R.id.edtTextRss);

        list = new ArrayList<>();
        listLink = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);
        lvRss.setAdapter(adapter);

        lvRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //chuyen sang man hinh chi tiet
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                intent.putExtra("linkTinTuc", listLink.get(i));
                startActivity(intent);
            }
        });
    }

    public void readRss(View view) {
        if(edtTextRss.getText().toString().length()>0)
        new ReadRss().execute(edtTextRss.getText().toString());
    }

    private class ReadRss extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);

                //Doc du lieu
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);//xuong dong
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);

            //list chua danh sach item
            NodeList nodeList = document.getElementsByTagName("item");

            String tieuDe = "";
            String link = "";


            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                tieuDe = parser.getValue(element, "title");
                link=parser.getValue(element, "link");
                list.add(tieuDe);
                listLink.add(link);

                //cap nhat thay doi
                list.set(i,tieuDe);
                listLink.set(i,link);
            }

            adapter.notifyDataSetChanged();


        }
    }


}
