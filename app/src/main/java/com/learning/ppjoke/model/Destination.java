package com.learning.ppjoke.model;

public class Destination {

    /**
     * asStart : false
     * className : com.learning.myppjoke.ui.dashboard.DashboardFragment
     * id : 849092592
     * isFragment : true
     * needLogin : false
     * pageUrl : /main/tabs/dashboard
     */

    private boolean asStart;
    private String className;
    private int id;
    private boolean isFragment;
    private boolean needLogin;
    private String pageUrl;

    public boolean isAsStart() {
        return asStart;
    }

    public void setAsStart(boolean asStart) {
        this.asStart = asStart;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIsFragment() {
        return isFragment;
    }

    public void setIsFragment(boolean isFragment) {
        this.isFragment = isFragment;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
}
