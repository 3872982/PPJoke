package com.learning.ppjoke.model;

import java.util.List;

public class BottomBar {

    /**
     * activeColor : #333333
     * inActiveColor : #666666
     * selectTab : 0
     * tabs : [{"enable":true,"index":0,"pageUrl":"main/tabs/home","size":24,"title":"首页"},{"enable":true,"index":1,"pageUrl":"main/tabs/sofa","size":24,"title":"沙发"},{"enable":true,"index":2,"pageUrl":"main/tabs/publish","size":40,"tintColor":"#ff678f","title":""},{"enable":true,"index":3,"pageUrl":"main/tabs/find","size":24,"title":"发现"},{"enable":true,"index":4,"pageUrl":"main/tabs/my","size":24,"title":"我的"}]
     */

    private String activeColor;
    private String inActiveColor;
    private int selectTab;
    private List<TabBean> tabs;

    public String getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(String activeColor) {
        this.activeColor = activeColor;
    }

    public String getInActiveColor() {
        return inActiveColor;
    }

    public void setInActiveColor(String inActiveColor) {
        this.inActiveColor = inActiveColor;
    }

    public int getSelectTab() {
        return selectTab;
    }

    public void setSelectTab(int selectTab) {
        this.selectTab = selectTab;
    }

    public List<TabBean> getTabs() {
        return tabs;
    }

    public void setTabs(List<TabBean> tabs) {
        this.tabs = tabs;
    }

    public static class TabBean {

        /**
         * enable : true
         * index : 0
         * pageUrl : main/tabs/home
         * size : 24
         * title : 首页
         * tintColor : #ff678f
         */

        private boolean enable;
        private int index;
        private String pageUrl;
        private int size;
        private String title;
        private String tintColor;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTintColor() {
            return tintColor;
        }

        public void setTintColor(String tintColor) {
            this.tintColor = tintColor;
        }
    }
}
