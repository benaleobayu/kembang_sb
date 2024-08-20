package com.bca.byc.service.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SidebarMenuService {

    public List<MenuGroup> getSidebarMenuList() {
        return Arrays.asList(
                new MenuGroup("User Management", List.of(
                        new MenuParent("User", "user", null, "fas fa-user", Arrays.asList(
                                new MenuList("Registration Inquiry", "/cms/user"),
                                new MenuList("Active User", "/cms/user/active"),
                                new MenuList("Suspended User", "/cms/user/suspended"),
                                new MenuList("Deleted User", "/cms/user/deleted")
                        ))
                )), new MenuGroup("Data Analytic", List.of(
                        new MenuParent("Data Analytic", null, "fas fa-chart-bar", "/cms/data-analytic")
                )), new MenuGroup("Content", Arrays.asList(
                        new MenuParent("Chanel Management", null, "fas fa-book", null),
                        new MenuParent("Content Management", null, "fas fa-book", null),
                        new MenuParent("Blacklist Keyword", null, "fas fa-book", null),
                        new MenuParent("Reported Content", "reported_content", "fas fa-book", null, Arrays.asList(
                                new MenuList("Changel Management", null),
                                new MenuList("Active User", null),
                                new MenuList("Suspended User", null),
                                new MenuList("Deleted User", null)
                        )),
                        new MenuParent("Broadcast Management", null, "fas fa-book", null),
                        new MenuParent("Time Promotion", null, "fas fa-book", null)
                )),
                new MenuGroup("Setting", Arrays.asList(
                        new MenuParent("Master Data", "admin", null, "fas fa-user", Arrays.asList(
                                new MenuList("Location", "/cms/ms/location"),
                                new MenuList("Interest Category", "/cms/ms/interest_category"),
                                new MenuList("Business Category", "/cms/ms/business_category"),
                                new MenuList("Feedback Category", "/cms/ms/feedback_category")
                        )),
                        new MenuParent("Admin", null, "fas fa-book", "/cms/settings/admin"),
                        new MenuParent("Privilege", null, "fas fa-book", "/cms/settings/privilege"),
                        new MenuParent("Others", null, "fas fa-book", "/cms/settings/all"),
                        new MenuParent("Api Docs", null, "fas fa-book", "/swagger-ui/index.html")
                ))

        );
    }

    // for list the group menu
    @Data
    @AllArgsConstructor
    public static class MenuGroup {

        private String name;
        private List<MenuParent> menuParents;
    }

    // for list the parent menu
    @Data
    @AllArgsConstructor
    public static class MenuParent {

        private String name;
        private String parentId;
        private String link;
        private String icon;
        private List<MenuList> menuList = new ArrayList<>();

        // for the menuParent
        public MenuParent(String name, String parentId, String icon, String link) {
            this.name = name;
            this.parentId = parentId;
            this.link = link;
            this.icon = icon;
            this.menuList = new ArrayList<>();
        }
    }

    // for the menuList
    @Data
    @AllArgsConstructor
    public static class MenuList {
        private String name;
        private String link;
    }


}
