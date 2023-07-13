DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS CATEGORIES CASCADE;
DROP TABLE IF EXISTS EVENTS CASCADE;
DROP TABLE IF EXISTS REQUESTS CASCADE;
DROP TABLE IF EXISTS COMPILATIONS CASCADE;

CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID INT                 NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME    VARCHAR(320)        NOT NULL,
    EMAIL   VARCHAR(320) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS CATEGORIES
(
    CATEGORY_ID INT                 NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME        VARCHAR(320) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS EVENTS
(
    EVENT_ID           INT                         NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    ANNOTATION         VARCHAR(2000)               NOT NULL,
    CATEGORY_ID        INT                         NOT NULL REFERENCES CATEGORIES (CATEGORY_ID),
    DESCRIPTION        VARCHAR(7000),
    EVENT_DATE         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    LON                FLOAT                       NOT NULL,
    LAT                FLOAT                       NOT NULL,
    PAID               BOOLEAN                     NOT NULL,
    PARTICIPANT_LIMIT  INT                         NOT NULL,
    REQUEST_MODERATION BOOLEAN                     NOT NULL,
    TITLE              VARCHAR(120)                NOT NULL,
    STATUS             VARCHAR(9)                  NOT NULL,
    CREATED_ON         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PUBLISHED_ON       TIMESTAMP WITHOUT TIME ZONE,
    VIEWS              INT,
    CONFIRMED_REQUESTS INT,
    USER_ID            INT                         NOT NULL REFERENCES USERS (USER_ID)
);

CREATE TABLE IF NOT EXISTS REQUESTS
(
    REQUEST_ID   INT                         NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    REQUEST_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    EVENT_ID     INT                         NOT NULL REFERENCES EVENTS (EVENT_ID),
    USER_ID      INT                         NOT NULL REFERENCES USERS (USER_ID),
    STATUS       VARCHAR(9)                  NOT NULL
);

CREATE TABLE IF NOT EXISTS COMPILATIONS
(
    COMPILATION_ID INT         NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    PINNED         BOOLEAN,
    TITLE          VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS COMPILATIONS_EVENTS
(
    COMPILATION_ID INT NOT NULL REFERENCES COMPILATIONS (COMPILATION_ID),
    EVENT_ID       INT NOT NULL REFERENCES EVENTS (EVENT_ID)
);