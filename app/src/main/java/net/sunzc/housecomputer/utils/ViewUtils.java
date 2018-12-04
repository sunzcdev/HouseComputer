package net.sunzc.housecomputer.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import net.sunzc.housecomputer.R;

public class ViewUtils {
    private static Toast mToast;

    public static void toast(Context context, CharSequence msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void addAnimation(Dialog mcDialog) {
        Window window = mcDialog.getWindow();
        window.setWindowAnimations(R.style.animation);
    }

    public static void showWarningDialog(Context context, CharSequence msg, DialogInterface.OnClickListener listener) {
        showWarningDialog(context, context.getString(R.string.app_name), msg, listener);
    }

    public static void showWarningDialog(Context context, CharSequence title, CharSequence msg, DialogInterface.OnClickListener clickListener) {
        showWarningDialog(context, title, msg, clickListener, null);
    }

    public static void showWarningDialog(Context context, CharSequence title, CharSequence msg, DialogInterface.OnClickListener clickListener, DialogInterface.OnClickListener dismissListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        if (!TextUtils.isEmpty(msg))
            builder.setMessage(msg);
        if (clickListener != null) {
            builder.setPositiveButton(android.R.string.ok, clickListener);
        }
        if (dismissListener == null) {
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setNegativeButton(android.R.string.cancel, dismissListener);
        }
        addAnimation(builder.show());
    }

    public static void dialog(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        addAnimation(builder.show());
    }

    public static void showInputDialog(Context context, CharSequence hint, InputListener listener) {
        showInputDialog(context, hint, null, listener);
    }

    public static void showInputDialog(Context context, CharSequence title, CharSequence defaultText, final InputListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        final EditText editText = new EditText(context);
        if (!TextUtils.isEmpty(defaultText)) {
            editText.setText(defaultText);
        }
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        builder.setView(editText)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onInput(editText.getText().toString());
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        addAnimation(builder.show());
    }

    public interface InputListener {
        void onInput(String text);
    }
}