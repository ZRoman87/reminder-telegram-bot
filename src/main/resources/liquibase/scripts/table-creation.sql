-- liquibase formatted sql

-- changeset rzrazhevskiy:22

CREATE TABLE notification_task (
                       id SERIAL,
                       chat_id BIGINT,
                       text TEXT,
                       scheduled_dispatch_date_time TIMESTAMP
)