package com.nasccped.project2025;

import android.content.Context;
import android.widget.Toast;

// class util para alertas de Toast
public class ToastUtil {

    // contexto
    private final Context context;

    public ToastUtil(Context context) {
        this.context = context;
    }

    // para alertas pequenos
    public void shortAlert(String message) {
        int shortValue = Toast.LENGTH_SHORT;
        Toast.makeText(context, message, shortValue).show();
    }

    // para alertas longos
    public void longAlert(String message) {
        int longValue = Toast.LENGTH_LONG;
        Toast.makeText(context, message, longValue).show();
    }
}
