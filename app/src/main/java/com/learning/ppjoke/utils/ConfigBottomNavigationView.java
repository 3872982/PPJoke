package com.learning.ppjoke.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.learning.ppjoke.R;
import com.learning.ppjoke.model.Destination;
import com.learning.ppjoke.model.BottomBar;

import java.util.List;

public class ConfigBottomNavigationView extends BottomNavigationView {
    private static int[] mIcons = new int[]{
            R.drawable.icon_tab_home,
            R.drawable.icon_tab_sofa,
            R.drawable.icon_tab_publish,
            R.drawable.icon_tab_find,
            R.drawable.icon_tab_mine
    };

    public ConfigBottomNavigationView(@NonNull Context context) {
        this(context,null);
    }

    public ConfigBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    @SuppressLint("RestrictedApi")
    public ConfigBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取json文件中配置的BottomBar参数
        BottomBar bottomBarConfig = AppConfig.getBottomBarConfig();

        //定义selector 定义二维数组，数组的行就是selector的item数量，也对应colors的数量，列对应的状态
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};

        int[] colors = new int[]{Color.parseColor(bottomBarConfig.getActiveColor()), Color.parseColor(bottomBarConfig.getInActiveColor())};

        ColorStateList colorStateList = new ColorStateList(states,colors);

        //设置item的文本颜色 selector
        setItemTextColor(colorStateList);
        //设置item中icon的颜色 selector
        setItemIconTintList(colorStateList);

        //LABEL_VISIBILITY_LABELED:设置按钮的文本为一直显示模式
        //LABEL_VISIBILITY_AUTO:当按钮个数小于三个时一直显示，或者当按钮个数大于3个且小于5个时，被选中的那个按钮文本才会显示
        //LABEL_VISIBILITY_SELECTED：只有被选中的那个按钮的文本才会显示
        //LABEL_VISIBILITY_UNLABELED:所有的按钮文本都不显示
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        //-----------开始根据配置生成对应的menu
        List<BottomBar.TabBean> tabs = bottomBarConfig.getTabs();

        //为对应的Tab添加Menu
        for (BottomBar.TabBean tab : tabs) {
            if(!tab.isEnable()){
                continue;
            }

            int itemId=getItemId(tab.getPageUrl());

            //Destination中没有对应的节点,不展示
            if( itemId< 0){
                continue;
            }

            MenuItem menuItem = getMenu().add(0, itemId, tab.getIndex(), tab.getTitle());
            menuItem.setIcon(mIcons[tab.getIndex()]);
        }

        //为每个底部icon设置大小
        //分两个循环写 ==>  因为BottomNavigationView 在 添加menu的时候需要对menu进行排序，排序时候回删除所有menu然后重新添加
        int index = 0;
        for (BottomBar.TabBean tab : tabs) {
            if(!tab.isEnable()){
                continue;
            }

            int itemId=getItemId(tab.getPageUrl());

            //Destination中没有对应的节点,不展示
            if( itemId< 0){
                continue;
            }

            int iconSize = dp2Px(tab.getSize());
            //获取menu
            BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) getChildAt(0);
            //获取menu item
            BottomNavigationItemView bottomNavigationItemView = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(index);

            bottomNavigationItemView.setIconSize(iconSize);

            if(TextUtils.isEmpty(tab.getTitle())){
                int tintColor = TextUtils.isEmpty(tab.getTintColor())?Color.parseColor("#ff678f"): Color.parseColor(tab.getTintColor());
                bottomNavigationItemView.setIconTintList(ColorStateList.valueOf(tintColor));
                //禁用点击时，上下浮动缩放的效果
                bottomNavigationItemView.setShifting(false);
            }

            index++;
        }

        //设置底部导航栏默认选中项
        if(bottomBarConfig.getSelectTab() != 0){
            BottomBar.TabBean defautlTab = bottomBarConfig.getTabs().get(bottomBarConfig.getSelectTab());

            if(defautlTab.isEnable()){
                int itemId = getItemId(defautlTab.getPageUrl());
                //这里需要延迟一下 再定位到默认选中的tab
                //因为 咱们需要等待内容区域,也就NavGraphBuilder解析数据并初始化完成，
                //否则会出现 底部按钮切换过去了，但内容区域还没切换过去
                post(()-> setSelectedItemId(itemId));
            }
        }
    }

    private int dp2Px(int dpValue) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return (int)(dpValue * metrics.density +0.5f);

    }

    /**
     * 获取PageURL对应的destination中的ID，Destination与BottomBar中的PageURL是一一对应的
     * @param pageUrl
     * @return
     */
    private int getItemId(String pageUrl) {
        //HashMap中get方法获取不存在的KEY不会报错，但返回值为NULL
        Destination destination = AppConfig.getDestinationConfig().get(pageUrl);

        if(destination == null) return -1;

        return destination.getId();
    }
}
