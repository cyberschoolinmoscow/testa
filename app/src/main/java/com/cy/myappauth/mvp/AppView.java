package com.cy.myappauth.mvp;

import android.support.annotation.StringRes;

public interface AppView {
    /**
 * A generic approach on showing messages to the user
 *
 * @param messageResourceId
 * @param callback
 */
void showMessage(
        @StringRes int messageResourceId,
        MessageCallback callback);

    /**
     * Callback for handling message show is complete
     */
    interface MessageCallback {

        void done();

    }

}