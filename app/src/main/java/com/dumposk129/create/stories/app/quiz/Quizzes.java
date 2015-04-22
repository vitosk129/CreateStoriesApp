package com.dumposk129.create.stories.app.quiz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dumposk129.create.stories.app.R;
import com.dumposk129.create.stories.app.api.ApiConfig;
import com.dumposk129.create.stories.app.api.Quiz;
import com.dumposk129.create.stories.app.navigation_drawer.NavigationDrawerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Quizzes extends ActionBarActivity {

    private Toolbar mToolbar;
    private ListView listView;
    private TextView tvQuizID, tvQuizName;

    JSONArray quizzes = null;

    ArrayList<HashMap<String, String>> quizList;

    List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_list_all_stories_name);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);

        quizList = new ArrayList<>();
        data = new ArrayList<>();


        // Assign data
       // final String[] data = new String[]{"Test1", "Test2", "Test3", "T4", "T5"};

        // Create ArrayAdapter
        //ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, data);

        // Send listItem to ListView
        listView = (ListView) findViewById(R.id.listViewStoriesName);
        tvQuizID = (TextView) findViewById(R.id.quizzId);
        tvQuizName = (TextView) findViewById(R.id.quizzesName);

        //listView.setAdapter(adapter);

       new LoadAllQuiz().execute();
        // Set Item Click Listener
/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(QuizFragment.this);
                builder.setTitle(R.string.choose_item).setItems(R.array.create_answer, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent;
                                switch (which){
                                    case 0:
                                        intent = new Intent(QuizFragment.this, Question.class);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        intent = new Intent(QuizFragment.this, Answer.class);
                                        startActivity(intent);
                                        break;
                                    case 2: dialog.dismiss();
                                }
                            }
                        });
                builder.show();
            }
        });
*/
    }


    class LoadAllQuiz extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // Get Data from api
            JSONObject json = Quiz.getAllQuiz();

            Log.d("All Quiz", json.toString());
            try {
                int success = json.getInt(ApiConfig.TAG_SUCCESS);
                if (success == 1){
                    quizzes = json.getJSONArray(ApiConfig.TAG_QUIZ);

                    for (int i = 0; i < quizzes.length(); i++){
                        JSONObject c = quizzes.getJSONObject(i);

                        String id = c.getString(ApiConfig.TAG_QUIZID);
                        String title_name = c.getString(ApiConfig.TAG_STORYTITLE);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("qid", id);
                        map.put("name", title_name);

                        data.add(title_name);
                        quizList.add(map);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            Quizzes.this, quizList, R.layout.quizzes_id_name,
                            new String[]{ApiConfig.TAG_QUIZID, ApiConfig.TAG_STORYTITLE},
                            new int[]{R.id.quizzId, R.id.quizzesName});
                    //{R.id.qid, R.id.name});
                   // ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, quizList);
                    listView.setAdapter(adapter);
                    }
                });
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_fragment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "select search", Toast.LENGTH_LONG).show();
        }
        return true;
    }*/
}