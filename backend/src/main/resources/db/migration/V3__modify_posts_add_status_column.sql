ALTER TABLE posts
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'DRAFT';

ALTER TABLE posts
    ADD CONSTRAINT chk_posts_status
    CHECK (
        status IN (
                   'DRAFT',
                   'PUBLISHED'
        ));