package com.codebyfelipe.appinventarios.ui.auth;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.codebyfelipe.appinventarios.data.remote.dto.LoginResponse;
import com.codebyfelipe.appinventarios.data.repository.AuthRepository;
import com.codebyfelipe.appinventarios.util.Resource;

public class LoginViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<Resource<LoginResponse>> loginResult = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.authRepository = new AuthRepository(application.getApplicationContext());
    }

    public MutableLiveData<Resource<LoginResponse>> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            loginResult.setValue(Resource.error("Ingresa tu correo y contraseña"));
            return;
        }
        authRepository.login(email.trim(), password, loginResult);
    }

    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }
}