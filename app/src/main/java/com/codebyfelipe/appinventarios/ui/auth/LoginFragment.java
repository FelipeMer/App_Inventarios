package com.codebyfelipe.appinventarios.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.codebyfelipe.appinventarios.R;
import com.codebyfelipe.appinventarios.databinding.FragmentLoginBinding;
import com.codebyfelipe.appinventarios.util.Resource;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Si ya hay una sesión activa, salta directo al inicio sin pasar por Login
        if (viewModel.isLoggedIn()) {
            navigateToHome();
            return;
        }

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString() : "";
            viewModel.login(email, password);
        });

        viewModel.getLoginResult().observe(getViewLifecycleOwner(), this::handleLoginResult);
    }

    private void handleLoginResult(Resource<com.codebyfelipe.appinventarios.data.remote.dto.LoginResponse> resource) {
        switch (resource.getStatus()) {
            case LOADING:
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnLogin.setEnabled(false);
                binding.tvError.setVisibility(View.GONE);
                break;

            case SUCCESS:
                binding.progressBar.setVisibility(View.GONE);
                binding.btnLogin.setEnabled(true);
                navigateToHome();
                break;

            case ERROR:
                binding.progressBar.setVisibility(View.GONE);
                binding.btnLogin.setEnabled(true);
                binding.tvError.setText(resource.getMessage());
                binding.tvError.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void navigateToHome() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_loginFragment_to_dashboardFragment);
        ;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // evita memory leaks del View Binding en Fragments
    }
}