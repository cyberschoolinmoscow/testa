package com.cy.myappauth.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.cy.myappauth.R;
import com.cy.myappauth.mvp.AppActivity;
import com.cy.myappauth.mvp.CrudPresenter;
import com.cy.myappauth.mvp.CrudPresenterImplementation;
import com.cy.myappauth.model.Task;


public class CrudActivity  extends AppActivity<CrudPresenterImplementation> implements CrudPresenterImplementation.View {

    private static final String KEY_TITLE = "key:title";
    private static final String KEY_DESCRIPTION = "key:description";
    private static final String KEY_MODE = "key:mode";
    EditText title;
    EditText description;

    private CrudPresenterImplementation presenter;

    public static Intent getIntentFor(
            final Activity activity,
            final Task task) {
        final Intent intent = new Intent(activity, CrudActivity.class);
        final Bundle bundle = new Bundle();
        if (task != null) {
            bundle.putString(KEY_TITLE, task.getTitle());
            bundle.putString(KEY_DESCRIPTION, task.getDescription());
        }
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title=findViewById(R.id.edit_title);
        description=findViewById(R.id.edit_description);
        // recover from state
        if (savedInstanceState != null) {
            title.setText(savedInstanceState.getString(KEY_TITLE));
            description.setText(savedInstanceState.getString(KEY_DESCRIPTION));
            getPresenter().setMode(savedInstanceState.getInt(KEY_MODE));
        } else if (getIntent().getExtras() != null) {
            final Bundle bundle = getIntent().getExtras();
            title.setText(bundle.getString(KEY_TITLE));
            description.setText(bundle.getString(KEY_DESCRIPTION));
            getPresenter().setMode(CrudPresenter.MODE_EDIT);
            title.setEnabled(getPresenter().enableTitleEdit()); // don't allow changing key (title) when editing
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_crud;
    }

    @Override
    protected CrudPresenterImplementation getPresenter() {
        if (presenter == null) {
            presenter = new CrudPresenterImplementation();
        }
        return presenter;
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITLE, title.getText().toString());
        outState.putString(KEY_DESCRIPTION, description.getText().toString());
        outState.putInt(KEY_MODE, getPresenter().getMode());
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_crud, menu);
        menu.findItem(R.id.delete).setVisible(getPresenter().showDeleteMenu()); // only show delete when in edit mode
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (R.id.done == item.getItemId()) {
            getPresenter().onDoneClicked(
                    title.getText().toString(),
                    description.getText().toString()
            );
            return true;
        } else if (R.id.delete == item.getItemId()) {
            getPresenter().deleteItem(title.getText().toString());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showInputInvalid() {
        showMessage(R.string.input_invalid, null);
    }

    @NonNull
    @Override
    protected View getFallbackView() {
        return title;
    }

    @Override
    public void showUpdateDone() {
        showMessage(R.string.update_done, new MessageCallback() {
            @Override
            public void done() {
                finish();
            }
        });
    }

    @Override
    public void showUpdateFailed() {
        showMessage(R.string.update_failed, null);
    }

    @Override
    public void showDeleteConfirmation(final String id) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(R.string.confirm_delete_item)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {
                        getPresenter().deleteItemConfirmed(id);
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    public void showItemDeleted() {
        showMessage(R.string.item_deleted, new MessageCallback() {
            @Override
            public void done() {
                finish();
            }
        });
    }

    @Override
    public void showItemDeleteFailed() {
        showMessage(R.string.update_failed, null);
    }
}