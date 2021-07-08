CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255),
    passwordHash VARCHAR(255)
);

CREATE TABLE notebook (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    owner_id INT,
    FOREIGN KEY (owner_id) REFERENCES user(id)
);

CREATE TABLE note (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    content VARCHAR,
    notebook_id INT,
    FOREIGN KEY (notebook_id) REFERENCES notebook(id)
);

INSERT INTO user (username, email, passwordHash) VALUES ('Admin', 'bekkosm@gmail.com', 'testpass');