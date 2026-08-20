package com.codebyfelipe.appinventarios.data.remote;

import android.content.Context;
import androidx.annotation.NonNull;
import com.codebyfelipe.appinventarios.util.SessionManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final SessionManager sessionManager;

    public AuthInterceptor(Context context) {
        this.sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String token = sessionManager.getToken();

        // Si no hay token (ej. request de login), deja pasar la request tal cual
        if (token == null) {
            return chain.proceed(originalRequest);
        }

        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        return chain.proceed(newRequest);
    }
}