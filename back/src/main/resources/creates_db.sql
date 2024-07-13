CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       user_name VARCHAR(64) NOT NULL,
                       email VARCHAR(248) NOT NULL,
                       password VARCHAR(64) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE topics (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        title VARCHAR(254) NOT NULL,
                        description VARCHAR(254) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE posts (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          title VARCHAR(254) NOT NULL,
                          content LONGTEXT NOT NULL,
                          user_id BIGINT NOT NULL,
                          topic_id BIGINT NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (user_id) REFERENCES users(id),
                          FOREIGN KEY (topic_id) REFERENCES topics(id)
);

CREATE TABLE comments (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          comment TEXT NOT NULL,
                          user_id BIGINT NOT NULL,
                          post_id BIGINT NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (user_id) REFERENCES users(id),
                          FOREIGN KEY (post_id) REFERENCES posts(id)
);
