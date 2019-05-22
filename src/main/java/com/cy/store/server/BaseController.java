package com.cy.store.server;

import org.springframework.beans.factory.annotation.Value;

public class BaseController {

    @Value("${admin.account}")
    protected String adminAccount;
    @Value("${admin.group}")
    protected String adminGroup;
    @Value("${admin.id}")
    protected String adminId;
    @Value("${admin.auth}")
    protected String adminAuth;
    @Value("${admin.session}")
    protected String adminSession;

    @Value("${login.vercode}")
    protected String verCode;

    @Value("${list.pagesize}")
    protected Integer pageSize;

    //title
    @Value("${title.system}")
    protected String systemTitle;

    @Value("${title.module.admin}")
    protected String adminModuleTitle;
    @Value("${title.module.admingroup}")
    protected String admingroupModuleTitle;
    @Value("${title.module.adminlog}")
    protected String adminlogModuleTitle;
    @Value("${title.module.sitepage}")
    protected String sitepageModuleTitle;
    @Value("${title.module.pagetpl}")
    protected String pagetplModuleTitle;
    @Value("${title.module.index}")
    protected String indexModuleTitle;

    @Value("${title.page.list}")
    protected String listPageTitle;
    @Value("${title.page.add}")
    protected String addPageTitle;
    @Value("${title.page.edit}")
    protected String editPageTitle;
    @Value("${title.page.auth}")
    protected String authPageTitle;


    @Value("${file.sitepage-image-path}")
    protected String imageSavePath;

}
