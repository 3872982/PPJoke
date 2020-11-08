package com.learning.ppjoke;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.learning.ppjoke.utils.NavGraphBuilder;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //通过代码动态生成NavGraph
        NavGraphBuilder.build(this,navController,R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController);
        //将navGraph与BottomNavigationView进行绑定
        //NavigationUI.setupWithNavController(navView, navController);
        navView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavController controller = Navigation.findNavController(this,R.id.nav_host_fragment);
        controller.navigate(item.getItemId());
        //这里返回true则点击按钮，图标会上下浮动
        return !TextUtils.isEmpty(item.getTitle());
    }
}