package com.cy.myappauth.mvp;

import com.cy.myappauth.model.Task;

import java.util.Map;

public interface OverviewPresenter {

    void onAddItemClicked();

    void reloadData();

    void onItemClicked(Task task);

    void onAboutClicked();

    interface View extends AppView {

        void showAddItem();

        void showItem(Task task);

        void showAbout();

        void showLoadedItems(Map<String, Task> tasks);

        void showErrorLoading();

    }

}