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
INSERT INTO user (username, email, passwordHash) VALUES ('Admin2', 'bekkosm2@gmail.com', 'testpass');
INSERT INTO notebook (name, owner_id) VALUES ('Test Notebook 1', 1);
INSERT INTO notebook (name, owner_id) VALUES ('Test Notebook 2', 1);
INSERT INTO notebook (name, owner_id) VALUES ('Test Notebook 2', 2);
INSERT INTO notebook (name, owner_id) VALUES ('Test Notebook 3', 1);
INSERT INTO note (name, content, notebook_id) VALUES ('Test Note 1', 'ABCDEFG', 1);
