CREATE TABLE IF NOT EXISTS endpoints
(
    endpoint_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app_name    VARCHAR(50)         NOT NULL,
    uri         VARCHAR(100) UNIQUE NOT NULL
);

COMMENT ON TABLE endpoints IS 'Эндпоинты';
COMMENT ON COLUMN endpoints.endpoint_id IS 'Идентификатор записи';
COMMENT ON COLUMN endpoints.app_name IS 'Наименование сервиса, которому принадлежит эндпоинт';
COMMENT ON COLUMN endpoints.uri IS 'URI запроса';

CREATE TABLE IF NOT EXISTS hits
(
    hit_id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    endpoint_id BIGINT REFERENCES endpoints (endpoint_id) ON DELETE CASCADE,
    ip_address  VARCHAR(40) NOT NULL,
    sent_dttm   TIMESTAMP   NOT NULL
);

COMMENT ON TABLE hits IS 'Запросы';
COMMENT ON COLUMN hits.hit_id IS 'Идентификатор запроса';
COMMENT ON COLUMN hits.endpoint_id IS 'Идентификатор эндпоинта';
COMMENT ON COLUMN hits.ip_address IS 'IP-адрес, с которого отправлен запрос';
COMMENT ON COLUMN hits.sent_dttm IS 'Дата и время совершения запроса';