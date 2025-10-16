package com.empresa.crudmixto.entity;

/**
 * Enum que define los roles disponibles en el sistema
 */
public enum Rol {
    ADMIN("ROLE_ADMIN", "Administrador"),
    SUPERVISOR("ROLE_SUPERVISOR", "Supervisor"),
    EMPLEADO("ROLE_EMPLEADO", "Empleado");
    
    private final String authority;
    private final String displayName;
    
    Rol(String authority, String displayName) {
        this.authority = authority;
        this.displayName = displayName;
    }
    
    public String getAuthority() {
        return authority;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
