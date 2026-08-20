package com.codebyfelipe.appinventarios.data.repository;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.codebyfelipe.appinventarios.data.remote.ApiClient;
import com.codebyfelipe.appinventarios.data.remote.ApiService;
import com.codebyfelipe.appinventarios.data.remote.dto.LoginRequest;
import com.codebyfelipe.appinventarios.data.remote.dto.LoginResponse;
import com.codebyfelipe.appinventarios.data.remote.dto.RegisterRequest;
import com.codebyfelipe.appinventarios.data.remote.dto.Usuario;
import com.codebyfelipe.appinventarios.util.Resource;
import com.codebyfelipe.appinventarios.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final ApiService apiService;
    private final SessionManager sessionManager;

    public AuthRepository(Context context) {
        this.apiService = ApiClient.getApiService(context);
        this.sessionManager = new SessionManager(context);
    }

    public void login(String email, String password, MutableLiveData<Resource<LoginResponse>> result) {
        result.setValue(Resource.loading());

        apiService.login(new LoginRequest(email, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse body = response.body();

                    // Guarda el token y los datos del usuario para reutilizarlos
                    // en cada request futura (vía AuthInterceptor)
                    sessionManager.saveToken(body.getAccess_token());
                    sessionManager.saveUser(
                            body.getUsuario().getId_usuario(),
                            body.getUsuario().getNombre()
                    );

                    result.setValue(Resource.success(body));
                } else {
                    result.setValue(Resource.error("Credenciales inválidas"));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                result.setValue(Resource.error("Error de conexión: " + t.getMessage()));
            }
        });
    }

    public void register(String nombre, String email, String password, MutableLiveData<Resource<Usuario>> result) {
        result.setValue(Resource.loading());

        RegisterRequest request = new RegisterRequest(nombre, email, password, null);

        apiService.register(request).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("No se pudo registrar el usuario"));
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                result.setValue(Resource.error("Error de conexión: " + t.getMessage()));
            }
        });
    }

    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    public void logout() {
        sessionManager.clearSession();
    }
}