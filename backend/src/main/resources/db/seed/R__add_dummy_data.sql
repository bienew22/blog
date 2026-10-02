DELETE FROM post_tags;

DELETE FROM posts;

DELETE FROM tags;

INSERT INTO posts (id, title, slug, content_md, content_html, author, create_at, update_at)
VALUES
    (1, 'test1', 't1', '# First Post', '<h1>First Post</h1>', 'bienew22', NOW(), NOW()),
    (2, 'test2', 't2', '# First Post', '<h1>First Post</h1>', 'bienew22', NOW(), NOW()),
    (3, 'test3', 't3', '# First Post', '<h1>First Post</h1>', 'bienew22', NOW(), NOW()),
    (4, 'test4', 't4', '# First Post', '<h1>First Post</h1>', 'bienew22', NOW(), NOW());

INSERT INTO tags (id, tag_name, tag_desc, post_cnt, create_at)
VALUES
    (1, 'Java', 'Java programming language', 2, NOW()),
    (2, 'Spring', 'Spring Framework', 2, NOW()),
    (3, 'PostgreSQL', 'PostgreSQL database', 3, NOW()),
    (4, 'Docker', 'Docker containerization', 3, NOW());


INSERT INTO post_tags (post_id, tag_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
    (2, 2),
    (2, 3),
    (3, 1),
    (3, 4),
    (4, 3),
    (4, 4);