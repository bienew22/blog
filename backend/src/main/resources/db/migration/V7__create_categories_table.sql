CREATE TABLE categories (
    id BIGINT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    category_desc VARCHAR(100),
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE posts
    ADD COLUMN category_id BIGINT NOT NULL;

ALTER TABLE posts
    ADD CONSTRAINT fk_post_category
        FOREIGN KEY (category_id) REFERENCES categories (id);

CREATE INDEX idx_post_category_id ON posts (category_id);
