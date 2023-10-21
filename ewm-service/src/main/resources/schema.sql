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
COMMENT ON COLUMN users.email IS 'Электронная почта';

CREATE TABLE IF NOT EXISTS locations
(
    location_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    latitude    DECIMAL NOT NULL,
    longitude   DECIMAL NOT NULL
);

COMMENT ON TABLE locations IS 'Координаты мест прохождения событий';
COMMENT ON COLUMN locations.location_id IS 'Идентификатор места';
COMMENT ON COLUMN locations.latitude IS 'Широта';
COMMENT ON COLUMN locations.longitude IS 'Долгота';

CREATE TABLE IF NOT EXISTS events
(
    event_id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title             VARCHAR(120)  NOT NULL,
    annotation        VARCHAR(2000) NOT NULL,
    category_id       BIGINT REFERENCES categories (category_id) ON DELETE CASCADE,
    description       VARCHAR(7000) NOT NULL,
    initiator_id      BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    event_dttm        TIMESTAMP     NOT NULL,
    location_id       BIGINT REFERENCES locations (location_id) ON DELETE RESTRICT,
    paid              BOOLEAN       NOT NULL DEFAULT false,
    participant_limit INT           NOT NULL DEFAULT 0,
    moderated         BOOLEAN       NOT NULL DEFAULT false,
    publication_state VARCHAR(10)   NOT NULL,
    publication_dttm  TIMESTAMP,
    create_dttm       TIMESTAMP     NOT NULL DEFAULT now()
);

COMMENT ON TABLE events IS 'События';
COMMENT ON COLUMN events.event_id IS 'Идентификатор события';
COMMENT ON COLUMN events.title IS 'Заголовок';
COMMENT ON COLUMN events.annotation IS 'Краткое описание';
COMMENT ON COLUMN events.category_id IS 'Идентификатор категории';
COMMENT ON COLUMN events.description IS 'Полное описание';
COMMENT ON COLUMN events.initiator_id IS 'Инициатор';
COMMENT ON COLUMN events.event_dttm IS 'Дата и время прохождения';
COMMENT ON COLUMN events.location_id IS 'Идентификатор координат места прохождения';
COMMENT ON COLUMN events.paid IS 'Признак платного события';
COMMENT ON COLUMN events.participant_limit IS 'Максимальное количество участников';
COMMENT ON COLUMN events.moderated IS 'Признак модерации заявок';
COMMENT ON COLUMN events.publication_state IS 'Статус публикации события';
COMMENT ON COLUMN events.publication_dttm IS 'Дата публикации';
COMMENT ON COLUMN events.create_dttm IS 'Дата создания';

CREATE TABLE IF NOT EXISTS requests
(
    request_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    create_dttm  TIMESTAMP   NOT NULL,
    event_id     BIGINT REFERENCES events (event_id) ON DELETE CASCADE,
    requester_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    status       VARCHAR(10) NOT NULL
);

COMMENT ON TABLE requests IS 'Запросы на участие в событиях';
COMMENT ON COLUMN requests.request_id IS 'Идентификатор запроса';
COMMENT ON COLUMN requests.create_dttm IS 'Дата создания';
COMMENT ON COLUMN requests.event_id IS 'Идентификатор события';
COMMENT ON COLUMN requests.requester_id IS 'Идентификатор инициатора';
COMMENT ON COLUMN requests.status IS 'Статус запроса';