INSERT INTO roles (role_type, created_at, updated_at)
SELECT 'ROLE_USER', NOW(), NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE role_type = 'ROLE_USER'
);

INSERT INTO roles (role_type, created_at, updated_at)
SELECT 'ROLE_ADMIN', NOW(), NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE role_type = 'ROLE_ADMIN'
);