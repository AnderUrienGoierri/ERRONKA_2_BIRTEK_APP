package model;

import java.util.HashMap;
import java.util.Map;

public class Hizkuntza {

    // Uneko hizkuntza (Lehenetsia: Euskara)
    public static String hizkuntzaAukeratua = "EU";

    private static final Map<String, String[]> hiztegia = new HashMap<>();

    static {
        // Formatua: { "GAKOA", { "EU", "ES", "EN" } }

        // --- OROKORRAK ---
        gehitu("app_title", "Birtek Kudeaketa", "Gestión Birtek", "Birtek Management");
        gehitu("login_title", "Saioa Hasi", "Iniciar Sesión", "Login");
        gehitu("login_btn", "Sartu", "Entrar", "Login");
        gehitu("email", "Emaila:", "Email:", "Email:");
        gehitu("pass", "Pasahitza:", "Contraseña:", "Password:");
        gehitu("search", "Bilatu:", "Buscar:", "Search:");
        gehitu("logout", "Saioa Itxi", "Cerrar Sesión", "Logout");
        gehitu("logout_confirm", "Ziur zaude saioa itxi nahi duzula?", "¿Seguro que quieres salir?",
                "Are you sure you want to log out?");
        gehitu("refresh", "Eguneratu", "Actualizar", "Refresh");
        gehitu("load_data", "Datuak Kargatu", "Cargar Datos", "Load Data");
        gehitu("check_db", "Egiaztatu DB", "Verificar BD", "Check DB");
        gehitu("exit", "Irten", "Salir", "Exit");

        // --- ADMINISTRAZIOA ---
        gehitu("menu_admin", "ADMINISTRAZIOA", "ADMINISTRACIÓN", "ADMINISTRATION");
        gehitu("tab_employees", "Langileak", "Empleados", "Employees");

        // --- TEKNIKOA ---
        gehitu("menu_tech", "TEKNIKOA (SAT)", "TECNICO (SAT)", "TECHNICAL (SAT)");
        gehitu("tab_repairs", "Konponketak", "Reparaciones", "Repairs");
        gehitu("tab_products", "Produktuak", "Productos", "Products");

        // --- SALMENTAK ---
        gehitu("menu_sales", "SALMENTAK", "VENTAS", "SALES");
        gehitu("tab_clients", "Bezeroak", "Clientes", "Clients");
        gehitu("tab_orders", "Eskaerak", "Pedidos", "Orders");

        // --- LOGISTIKA ---
        gehitu("menu_logistics", "LOGISTIKA", "LOGISTICA", "LOGISTICS");
        gehitu("lbl_logistics", "Hornitzaileen Sarrerak", "Entradas de Proveedores", "Supplier Entries");

        // --- SISTEMAK ---
        gehitu("menu_systems", "SISTEMAK", "SISTEMAS", "SYSTEMS");
        gehitu("sys_title", "Sistemak eta Kudeaketa", "Sistemas y Gestión", "Systems & Management");

        // --- DIALOGOAK ---
        gehitu("dialog_rep_title", "Editatu Konponketa", "Editar Reparación", "Edit Repair");
        gehitu("status", "Egoera:", "Estado:", "Status:");
        gehitu("notes", "Oharrak:", "Notas:", "Notes:");
        gehitu("save", "GORDE", "GUARDAR", "SAVE");
    }

    private static void gehitu(String gakoa, String eu, String es, String en) {
        hiztegia.put(gakoa, new String[] { eu, es, en });
    }

    public static String lortu(String gakoa) {
        String[] balioak = hiztegia.get(gakoa);
        if (balioak == null)
            return "EZAURTU GABE: " + gakoa;

        switch (hizkuntzaAukeratua) {
            case "ES":
                return balioak[1];
            case "EN":
                return balioak[2];
            default:
                return balioak[0]; // EU
        }
    }
}
