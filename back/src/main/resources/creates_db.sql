CREATE TABLE users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name  VARCHAR(64)  NOT NULL,
    email      VARCHAR(248) NOT NULL,
    password   VARCHAR(64)  NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE topics
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(254) NOT NULL,
    description VARCHAR(254) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE posts
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    title      VARCHAR(254) NOT NULL,
    content    LONGTEXT     NOT NULL,
    user_id    BIGINT       NOT NULL,
    topic_id   BIGINT       NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (topic_id) REFERENCES topics (id),
    INDEX(user_id),
    INDEX(topic_id)
);

CREATE TABLE comments
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    content    TEXT      NOT NULL,
    user_id    BIGINT    NOT NULL,
    post_id    BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (post_id) REFERENCES posts (id),
    INDEX(user_id),
    INDEX(post_id)
);

INSERT INTO topics (title, description)
VALUES
    ('Machine Learning', 'Discussions on machine learning algorithms and applications'),
    ('Cloud Computing', 'Topics on cloud platforms like AWS, Azure, and Google Cloud'),
    ('Blockchain', 'Everything related to blockchain technology and cryptocurrencies'),
    ('Cyber-security', 'Topics on cyber-security practices, tools, and news'),
    ('Artificial Intelligence', 'Discussions on AI techniques and advancements'),
    ('Data Science', 'Topics on data analysis, data mining, and big data'),
    ('Networking', 'All about computer networking and communication protocols'),
    ('Database Management', 'Discussions on database management systems like MySQL, PostgreSQL, etc.'),
    ('Microservices', 'Topics on microservices architecture and related tools'),
    ('Mobile Development', 'Discussions on iOS and Android app development'),
    ('Virtualization', 'Topics on virtualization technologies like VMware and Hyper-V'),
    ('Internet of Things (IoT)', 'Everything related to IoT devices and ecosystems'),
    ('Big Data', 'Discussions on big data technologies and frameworks like Hadoop and Spark'),
    ('DevSecOps', 'Integrating security practices within the DevOps process'),
    ('Kubernetes', 'All about Kubernetes and container orchestration'),
    ('AR/VR', 'Topics on augmented and virtual reality technologies'),
    ('Quantum Computing', 'Discussions on the advancements and implications of quantum computing'),
    ('Edge Computing', 'Topics on edge computing and its applications in IoT'),
    ('Ethical Hacking', 'Discussions on ethical hacking techniques and practices'),
    ('Software Testing', 'Everything related to software testing methodologies and tools');
