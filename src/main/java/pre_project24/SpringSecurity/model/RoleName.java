package pre_project24.SpringSecurity.model;

public enum RoleName {
    ROLE_USER,
    ROLE_ADMIN;

    @Override
    public String toString() {
        return name();
    }
}
