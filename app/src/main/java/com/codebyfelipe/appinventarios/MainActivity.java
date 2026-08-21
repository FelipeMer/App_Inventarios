package com.codebyfelipe.appinventarios;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.codebyfelipe.appinventarios.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // Conecta el BottomNavigationView con el NavController:
            // al tocar un ícono, navega automáticamente al fragment con ese mismo id
            NavigationUI.setupWithNavController(binding.bottomNav, navController);

            // Oculta el bottom nav mientras el usuario está en Login
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.loginFragment) {
                    binding.bottomNav.setVisibility(android.view.View.GONE);
                } else {
                    binding.bottomNav.setVisibility(android.view.View.VISIBLE);
                }
            });
        }
    }
}