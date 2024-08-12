package com.bca.byc.config;

import com.bca.byc.security.AdminPrincipal;
import com.bca.byc.service.component.SidebarMenuService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private SidebarMenuService menuService;

    @Value("${app.base.url}")
    private String baseUrl;

    @ModelAttribute
    public void addUserToModel(Model model, HttpServletRequest request) {
        Authentication authentication = getCurrentAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AdminPrincipal) {
            AdminPrincipal adminPrincipal = (AdminPrincipal) authentication.getPrincipal();
            String routePrefix = "cms";
            model.addAttribute("admin", adminPrincipal.getAdmin());
            model.addAttribute("dashboardUrl", baseUrl + routePrefix + "/dashboard");
            model.addAttribute("auditTrailurl", baseUrl + routePrefix + "/report/audit-trail");
            model.addAttribute("swaggerUrl", baseUrl  + "swagger-ui/index.html");
            model.addAttribute("adminsUrl", baseUrl + routePrefix + "/admins");
            model.addAttribute("rolesUrl", baseUrl + routePrefix + "/roles");
        }

        // content user login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("authorities", authentication.getAuthorities());


        // set sidebar
        List<SidebarMenuService.MenuGroup> menuGroups = menuService.getSidebarMenuList();
        model.addAttribute("menuGroups", menuGroups);

        // set active page
        String currentUrl = request.getRequestURI();
        model.addAttribute("currentUrl", currentUrl);
    }

    private Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
