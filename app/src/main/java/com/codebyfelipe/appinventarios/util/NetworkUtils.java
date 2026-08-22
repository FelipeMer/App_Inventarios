package com.codebyfelipe.appinventarios.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

public class NetworkUtils { //Función que se encarga de validar si el dispositivpo tiene internet antes de decidir si consultar la API o la memoria cache

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        if (capabilities == null) return false;

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}