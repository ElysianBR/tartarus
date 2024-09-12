package br.com.elysian.tartarus.view;


import java.util.Map;

public class RoleViews {

    private static final Map<String, Class<?>> MANAGER = Map.of(
            "ACCOUNT", RoleViews.AccountView.class,
            "BUSINESS_ADMIN", RoleViews.CompanyAdminView.class,
            "ADMIN", RoleViews.AdminView.class

    );

    public static Class<?> get(String k) {
        return MANAGER.get(k);
    }

    public static class AccountView {
    }

    public static class CompanyAdminView extends AccountView {
    }

    public static class AdminView extends AccountView {
    }
}
