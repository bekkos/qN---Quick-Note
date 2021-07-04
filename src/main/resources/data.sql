CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255),
    passwordHash VARCHAR(255)
);

INSERT INTO user (username, email, passwordHash) VALUES ('Admin', 'bekkosm@gmail.com', 'testpass');