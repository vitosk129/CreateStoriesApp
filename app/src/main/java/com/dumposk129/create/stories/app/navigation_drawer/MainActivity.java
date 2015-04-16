package com.dumposk129.create.stories.app.navigation_drawer;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.dumposk129.create.stories.app.R;
import com.dumposk129.create.stories.app.create.Story;
import com.dumposk129.create.stories.app.create.StoryTable;
import com.dumposk129.create.stories.app.navigation_drawer.NavigationDrawerFragment;


public class MainActivity extends ActionBarActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_below_appbar);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout)findViewById(R.id.drawer_layout), mToolbar);


       StoryTable storyTable = new StoryTable(this);

       // Story story1 = new Story().setName("AAA");
       //storyTable.delete(3);
        Story story = storyTable.getById(2);

       // TextView textView = (TextView) findViewById(R.id.text);
        //textView.setText(story.getId() + ":" + story.getName());

        /*List<Story> stories = null;
        try {
            stories = storyTable.getAll();
            StringBuilder sb = new StringBuilder();
            for (Story s : stories){
                sb.append(s.getId() + "" + s.getName());
                sb.append("\n");
            }
            textView.setText(sb.toString());
        }
        catch (Exception e){
           System.out.print(e.getMessage());
        }*/


    }



/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}