package com.nasccped.project2025;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

// class de utilidade para testar conexão
public class NetUtils {

    // testa se está conectado
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
}
