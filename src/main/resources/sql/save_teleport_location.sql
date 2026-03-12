CREATE TABLE IF NOT EXISTS players (
    uuid VARCHAR(36) PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS location_categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT DEFAULT '',
    material_name TEXT NOT NULL DEFAULT 'STONE',
    priority INTEGER NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_location_categories_priority
ON location_categories(priority);

CREATE TABLE IF NOT EXISTS save_teleport_locations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT DEFAULT '',
    group_key VARCHAR(36) NOT NULL,
    material_name TEXT NOT NULL DEFAULT 'STONE',
    world VARCHAR NOT NULL DEFAULT 'world',
    x REAL NOT NULL DEFAULT 0,
    y REAL NOT NULL DEFAULT 0,
    z REAL NOT NULL DEFAULT 0,
    yaw REAL NOT NULL DEFAULT 0,
    pitch REAL NOT NULL DEFAULT 0,
    priority INTEGER NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_save_teleport_locations_group_key_priority
ON save_teleport_locations(group_key, priority);