package com.codebyfelipe.appinventarios.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.codebyfelipe.appinventarios.R;
import com.codebyfelipe.appinventarios.databinding.FragmentProfileBinding;
import com.codebyfelipe.appinventarios.util.SessionManager;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext().getApplicationContext());
        binding.tvUserName.setText(sessionManager.getUserName());

        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.clearSession();

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_global_to_loginFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}