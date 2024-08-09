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
                        new MenuParent("User", "user", "", "fas fa-user", Arrays.asList(
                                new MenuList("Registration Inquiry", "/cms/users"),
                                new MenuList("Active User", "/cms/users/active"),
                                new MenuList("Suspended User", "/cms/users/suspended"),
                                new MenuList("Deleted User", "/cms/users/deleted")
                        ))
                )), new MenuGroup("Data Analytic", List.of(
                        new MenuParent("Data Analytic", "", "fas fa-chart-bar", "/cms/data-analytic")
                )), new MenuGroup("Content", Arrays.asList(
                        new MenuParent("Chanel Management", "", "fas fa-book", ""),
                        new MenuParent("Content Management", "", "fas fa-book", ""),
                        new MenuParent("Blacklist Keyword", "", "fas fa-book", ""),
                        new MenuParent("Reported Content", "user", "fas fa-book", "", Arrays.asList(
                                new MenuList("Changel Management", ""),
                                new MenuList("Active User", ""),
                                new MenuList("Suspended User", ""),
                                new MenuList("Deleted User", "")
                        )),
                        new MenuParent("Broadcast Management", "", "fas fa-book", ""),
                        new MenuParent("Time Promotion", "", "fas fa-book", "")
                )),
                new MenuGroup("Setting", Arrays.asList(
                        new MenuParent("Master Data", "admin", "", "fas fa-user", Arrays.asList(
                                new MenuList("Location", "/cms/ms/location"),
                                new MenuList("Business Category", "/cms/ms/business-category")
                        )),
                        new MenuParent("Admin", "", "fas fa-book", "/cms/ms/admin"),
                        new MenuParent("Privilege", "", "fas fa-book", "/cms/ms/privilege")
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
