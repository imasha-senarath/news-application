package com.example.newsapplication;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class FakeNewsFragment extends Fragment
{
    private View fakeNewsView;

    private RecyclerViewAdapater recyclerViewAdapater;
    private ArrayList<News> newsArrayList;
    private RecyclerView fakeNewsList;
    private RequestQueue requestQueue;


    public FakeNewsFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fakeNewsView =  inflater.inflate(R.layout.fragment_fake_news, container, false);

        fakeNewsList = fakeNewsView.findViewById(R.id.fake_news_list);
        fakeNewsList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        fakeNewsList.setLayoutManager(linearLayoutManager);

        newsArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());

        URLChecking();

        return fakeNewsView;
    }


    private void URLChecking()
    {
        /* IP Address = Wireless LAN adapter Wi-Fi: IPv4 Address */
        String URL = "http://192.168.8.101:5500/json/news.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        /* Main API */
                        parseJson("http://192.168.8.101:5500/json/news.json");
                        //Toast.makeText(getContext(), "The main API was connected", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        /* Backup API */
                        parseJson("http://192.168.8.101:5500/json/news.json");
                        //Toast.makeText(getContext(), "The backup API was connected", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }


    private void parseJson(String URL)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try
                        {
                            for(int i=0; i < response.length(); i++)
                            {
                                JSONObject jsonobject = response.getJSONObject(i);
                                String headline    = jsonobject.getString("title");
                                String description  = jsonobject.getString("description");
                                String date = jsonobject.getString("date");
                                String newsType = jsonobject.getString("newstype");

                                newsArrayList.add(new News(date,description,headline, newsType, "fakeNews"));
                            }

                            recyclerViewAdapater = new RecyclerViewAdapater(getContext(), newsArrayList);
                            fakeNewsList.setAdapter(recyclerViewAdapater);

                        }
                        catch (JSONException e)
                        {
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }


}
