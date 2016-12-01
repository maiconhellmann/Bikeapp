package com.saitama.transport.bikeapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.Log;

import com.saitama.transport.bikeapp.R;

/**
 * Abstracts the most used functions of dialogs
 */
public class AlertUtils {

    /**
     * Displays alert with an informational message.
     */
    public static void showMessage(final Context context, @StringRes int message) {
        AlertUtils.showMessage(context, context.getString(message));
    }

    /**
     *  Displays alert with an informational message.
     */
    public static void showMessage(final Context context, String message) {
        int icDialogInfo =  R.drawable.ic_warning_24dp;
        AlertUtils.showDialog(context, icDialogInfo,context.getString(R.string.label_information), message);
    }

    /**
     *  Displays alert with an advice message.
     */
    public static void showWarning(final Context context, @StringRes int message){
        int icDialog =  R.drawable.ic_warning_24dp;
        AlertUtils.showDialog(context, icDialog, context.getString(R.string.label_warning), context.getString(message));
    }

    /**
     *  Displays alert with an error message.
     */
    public static void showError(final Context context, @StringRes int message){
        int icDialog =  R.drawable.ic_remove_circle_24dp;
        AlertUtils.showDialog(context, icDialog, context.getString(R.string.label_erro), context.getString(message));
    }

    /**
     *  Displays alert with an error message.
     */
    public static void showError(final Context context, Exception e){
        String erro = "";

        if(e != null && e.getClass() != null){
            erro = e.getClass().getSimpleName()+"\n";
        }
        if(e != null && e.getMessage() != null){
            erro += e.getMessage();
        }

        int icDialog =  R.drawable.ic_remove_circle_24dp;
        Log.e(context.getClass().getSimpleName(), erro,e);

        AlertUtils.showDialog(context, icDialog, context.getString(R.string.label_erro), erro);
    }

    /**
     *
     *  Displays alert with an error message.
     */
    public static void showError(final Context context, String error){
        int icDialog =  R.drawable.ic_remove_circle_24dp;
        Log.e(context.getClass().getSimpleName(), error);
        AlertUtils.showDialog(context, icDialog, context.getString(R.string.label_erro), error);
    }

    /**
     * {@link AlertUtils#showDialog(Context, int, String, String, int)}
     */
    public static void showDialog(final Context context, @DrawableRes int icon, String title, String message) {
        AlertUtils.showDialog(context, icon, title, message, 0);
    }
    /**
     *  Displays alert with an message.
     */
    private static void showDialog(final Context context, @DrawableRes int icon, String title, String message, final int idRequest) {
        DialogInterface.OnClickListener action = null;
        boolean cancelable = true;

        action = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        cancelable = false;

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(title);
        alertBuilder.setIcon(icon);
        alertBuilder.setMessage(message);
        alertBuilder.setCancelable(cancelable);
        alertBuilder.setPositiveButton(context.getString(R.string.label_ok), action);
        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }
}
