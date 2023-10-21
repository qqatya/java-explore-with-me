CREATE TABLE IF NOT EXISTS categories
(
    category_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(200) UNIQUE NOT NULL
);

COMMENT ON TABLE categories IS 'Категории событий';
COMMENT ON COLUMN categories.category_id IS 'Идентификатор категории';
COMMENT ON COLUMN categories.name IS 'Название';

CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    VARCHAR(250)        NOT NULL,
    email   VARCHAR(320) UNIQUE NOT NULL
);

COMMENT ON TABLE users IS 'Пользователи';
COMMENT ON COLUMN users.user_id IS 'Идентификатор пользователя';
COMMENT ON COLUMN users.name IS 'Имя';
COMMENT ON COLUMN users.email IS 'Электронная почта'