package ac.su.inclassspringsecurity.constant;

public enum UserRole {
    SUPER_ADMIN, // (1) Product CRUD, (2) ALL USER CRUD
    ADMIN,       // (1) Product CRUD, (2) ADMIN(self) CRUD & USER CRUD
    USER         // (1) Product Read, (2) USER(self) CRUD
}