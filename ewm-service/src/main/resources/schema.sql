CREATE TABLE IF NOT EXISTS categories
(
    category_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(200) UNIQUE NOT NULL
);

COMMENT ON TABLE categories IS 'Категории событий';
COMMENT ON COLUMN categories.category_id IS 'Идентификатор записи';
COMMENT ON COLUMN categories.name IS 'Название категории';