package com.pbsi2.fakenewsbaby;

import android.app.LoaderManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class NewsActivity extends AppCompatActivity implements
        NewsAdapter.ClickAdapterListener,
        LoaderManager.LoaderCallbacks<ArrayList<BadNews>> {


    private final int LOADER_ID = 14;
    RecyclerView mRecyclerView;
    NewsAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fab;
    GetNews myGetNews;
    private String TAG = NewsActivity.class.getSimpleName();
    private ActionModeCallback actionModeCallback;
    private androidx.appcompat.view.ActionMode actionMode;
    private androidx.appcompat.widget.Toolbar nTopToolbar;
    private LoaderManager.LoaderCallbacks<ArrayList<BadNews>> pCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main);
        nTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(nTopToolbar);
        nTopToolbar.setLogo(R.mipmap.ic_badnews);
        mRecyclerView = findViewById(R.id.newsview);
        mRecyclerView.setHasFixedSize(true);
        // For LINEAR
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        // For a GRID
        //mLayoutManager = new GridLayoutManager(this,5);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        actionModeCallback = new ActionModeCallback();
        ArrayList<BadNews> yourNews = new ArrayList<BadNews>();
        NewsAdapter mAdapter = new NewsAdapter(this, null, this);
        mRecyclerView.setAdapter(mAdapter);
        // Build the Loader

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        Loader<ArrayList<BadNews>> myLoadmanager = getSupportLoaderManager().getLoader(LOADER_ID);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setVisibility(View.GONE);
                //myLoadManager.initLoader(LOADER_ID, yourNews, pCallbacks);

            }
        });
    }

    @Override
    public Loader<ArrayList<BadNews>> onCreateLoader(int id, Bundle args) {
        // Create a new Loader with the following query parameters.
        return new GetNews(this, MainActivity.guardianUrl);

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<BadNews>> loader, ArrayList<BadNews> data) {
        // A switch-case is useful when dealing with multiple Loaders/IDs
        switch (loader.getId()) {
            case LOADER_ID:
                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the SimpleCursorAdapter.
                NewsAdapter mAdapter = new NewsAdapter(this, data, this);
                mRecyclerView.setAdapter(mAdapter);
                break;
        }
        // The listview now displays the queried data.
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<BadNews>> loader) {
        // For whatever reason, the Loader's data is now unavailable.
        // Remove any references to the old data by replacing it with
        // a null Cursor.
        mAdapter.clearSelections();
    }

    @Override
    public void onRowClicked(int position) {
        enableActionMode(position);
    }

    @Override
    public void onRowLongClicked(int position) {
        enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {

        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
            actionMode = null;
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private void selectAll() {
        mAdapter.selectAll();
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }

        actionMode = null;
    }

    private void deleteRows() {
        actionMode = null;
    }

    private void updateColoredRows() {

        actionMode = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Toast.makeText(getApplicationContext(),
                    "REFRESH REQUESTED",
                    Toast.LENGTH_SHORT).show();
            getSupportLoaderManager().restartLoader(LOADER_ID, null, pCallbacks);
            Toast.makeText(getApplicationContext(),
                    "REFRESH DONE",
                    Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }



    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_news, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Log.d("Clicked Action", "here");
            switch (item.getItemId()) {


                case R.id.action_delete:
                    // delete all the selected rows
                    deleteRows();
                    mode.finish();
                    return true;

                case R.id.action_color:
                    updateColoredRows();
                    mode.finish();
                    return true;

                case R.id.action_select_all:
                    selectAll();
                    return true;

                case R.id.action_refresh:

                    onCreateLoader(14, null);
                    getSupportLoaderManager().restartLoader(LOADER_ID, null, pCallbacks);
                    Toast.makeText(getApplicationContext(),
                            "REFRESH DONE",
                            Toast.LENGTH_SHORT).show();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
        }

    }
}