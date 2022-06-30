package com.cy.myappauth.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.myappauth.R;
import com.cy.myappauth.adapter.TaskAdapter;
import com.cy.myappauth.model.Task;
import com.cy.myappauth.mvp.AppActivity;
import com.cy.myappauth.mvp.OverviewPresenterImplementation;
import com.cy.myappauth.remote.RemoteServiceImplementation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class ShopActivity extends AppActivity<OverviewPresenterImplementation> implements OverviewPresenterImplementation.View {

        Toolbar toolbar;
        FloatingActionButton fab;
        RecyclerView recyclerView;
        private OverviewPresenterImplementation presenter;
        private TaskAdapter taskAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            toolbar=findViewById(R.id.toolbar);
            recyclerView=findViewById(R.id.recycler_tasks);
fab=findViewById(R.id.fab);


         //  setSupportActionBar(toolbar);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPresenter().onAddItemClicked();
                }
            });
            initRecyclerView();
        }

        @Override
        protected int getLayoutResourceId() {
            return R.layout.activity_shop;
        }

        @Override
        protected OverviewPresenterImplementation getPresenter() {
            if (presenter == null) {
                presenter = new OverviewPresenterImplementation(RemoteServiceImplementation.getInstance());
            }
            return presenter;
        }

        @NonNull
        @Override
        protected View getFallbackView() {
            return fab;
        }

        @Override
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
            if (id == R.id.action_about) {
                getPresenter().onAboutClicked();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        protected void onResume() {
            super.onResume();
            getPresenter().reloadData();
        }

        private void initRecyclerView() {
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(llm);
            taskAdapter = new TaskAdapter(getApplicationContext(), new TaskAdapter.OnItemSelectedCallback() {
                @Override
                public void onClick(final Task task) {
                    getPresenter().onItemClicked(task);
                }
            });
            recyclerView.setAdapter(taskAdapter);
        }

        @Override
        public void showAddItem() {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("message");

            myRef.setValue("Hello, World!");
            startActivity(new Intent(getApplicationContext(), CrudActivity.class));
        }

        @Override
        public void showItem(final Task task) {
            startActivity(CrudActivity.getIntentFor(ShopActivity.this, task));
        }

        @Override
        public void showAbout() {
            showMessage(R.string.info_about, null);
        }

        @Override
        public void showLoadedItems(final Map<String, Task> tasks) {
            taskAdapter.setItems(new ArrayList<>(tasks.values()));
        }

        @Override
        public void showErrorLoading() {
            showMessage(R.string.error_loading_data, null);
        }
    }