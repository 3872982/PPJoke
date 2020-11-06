package com.learning.ppjoke.utils;

import android.content.ComponentName;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.learning.ppjoke.model.Destination;
import com.learning.ppjoke.navigator.FixFragmentNavigator;

import java.util.HashMap;

/**
 * 通过Json配置文件动态生成NavGraph
 */
public class NavGraphBuilder {

    public static void build(FragmentActivity fragmentActivity, NavController controller,int containerId){

        NavigatorProvider navigatorProvider = controller.getNavigatorProvider();


        NavGraph navGraph = new NavGraph(new NavGraphNavigator(navigatorProvider));

        ActivityNavigator activityNavigator = navigatorProvider.getNavigator(ActivityNavigator.class);
        //FragmentNavigator fragmentNavigator = navigatorProvider.getNavigator(FragmentNavigator.class);
        //使用自定义FixFragmentNavigator来作为Fragment节点的导航器
        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(fragmentActivity, fragmentActivity.getSupportFragmentManager(), containerId);
        navigatorProvider.addNavigator(fragmentNavigator);

        HashMap<String, Destination> destinationConfig = AppConfig.getDestinationConfig();

        for (Destination node : destinationConfig.values()) {
            if(node.isIsFragment()){
                FragmentNavigator.Destination destination = fragmentNavigator.createDestination();
                destination.setId(node.getId());
                destination.setClassName(node.getClassName());
                destination.addDeepLink(node.getPageUrl());
                //添加节点到NavGraph
                navGraph.addDestination(destination);
            }else{
                ActivityNavigator.Destination destination = activityNavigator.createDestination();
                destination.setId(node.getId());
                destination.setComponentName(new ComponentName(AppGlobal.getApplication().getPackageName(),node.getClassName()));
                destination.addDeepLink(node.getPageUrl());
                navGraph.addDestination(destination);
            }

            if(node.isAsStart()){
                navGraph.setStartDestination(node.getId());
            }
        }

        controller.setGraph(navGraph);
    }
}
