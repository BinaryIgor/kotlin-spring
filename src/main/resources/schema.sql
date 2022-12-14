CREATE TABLE inventory (
    id UUID PRIMARY KEY,
    online BOOLEAN NOT NULL,
    filter_type TEXT NOT NULL,
    version INTEGER NOT NULL
);

CREATE TABLE inventory_filter_list (
    inventory_id UUID NOT NULL REFERENCES inventory(id) ON DELETE CASCADE,
    value TEXT NOT NULL,

    PRIMARY KEY(inventory_id, value)
)