DROP DATABASE IF EXISTS library;
CREATE DATABASE IF NOT EXISTS library;
USE library;
CREATE TABLE IF NOT EXISTS books (
	id_book INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255),
    description TEXT,
    publisher TEXT,
    published_date INT,
    page_count INT,
    count_rating INT DEFAULT 0,
    average_rating REAL DEFAULT 0,
    link_image VARCHAR(255),
    quantity INT DEFAULT 0,
    UNIQUE (title, link_image)
);
CREATE TABLE IF NOT EXISTS authors (
	id_author INT PRIMARY KEY AUTO_INCREMENT,
    name_author VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS categories (
	id_category INT PRIMARY KEY AUTO_INCREMENT,
    name_category VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS book_author(
    id_book INT NOT NULL,
    id_author INT NOT NULL,
    PRIMARY KEY (id_book,id_author),
    FOREIGN KEY(id_book) REFERENCES books(id_book) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(id_author) REFERENCES authors(id_author) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS book_category(
    id_book INT NOT NULL,
    id_category INT NOT NULL,
    PRIMARY KEY(id_book,id_category),
    FOREIGN KEY(id_book) REFERENCES books(id_book) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(id_category) REFERENCES categories(id_category) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS address (
    id_address INT PRIMARY KEY AUTO_INCREMENT,
    name_address VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user (
	id_user INT PRIMARY KEY AUTO_INCREMENT,
    name_user VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL,
    phone_number_user VARCHAR(20) NOT NULL UNIQUE,
    email_user VARCHAR(50),
    id_address INT NOT NULL,
    ban_date DATE,
    avatar MEDIUMBLOB,
    FOREIGN KEY(id_address) REFERENCES address(id_address) ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS librarian (
	id_librarian INT PRIMARY KEY AUTO_INCREMENT,
    name_librarian VARCHAR(50) NOT NULL,
    birthday DATE NOT NULL,
    phone_number_librarian VARCHAR(20) NOT NULL,
    email_librarian VARCHAR(50),
    id_address INT NOT NULL,
    username_account VARCHAR(50) NOT NULL,
    password_account VARCHAR(50) NOT NULL,
    avatar MEDIUMBLOB,
    FOREIGN KEY(id_address) REFERENCES address(id_address) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS borrowing (
	id_borrowing INT PRIMARY KEY AUTO_INCREMENT,
    id_book INT NOT NULL,
    id_user INT NOT NULL,
    borrowed_date DATE NOT NULL,
    due_date DATE NOT NULL,
    returned_date DATE,
    name_first_reader VARCHAR(255),
    FOREIGN KEY(id_book) REFERENCES books(id_book) ON UPDATE CASCADE,
    FOREIGN KEY(id_user) REFERENCES user(id_user) ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS notifications (
	id_notification INT PRIMARY KEY AUTO_INCREMENT,
    title TEXT NOT NULL,
    content TEXT,
    id_user INT,
    is_seen BOOLEAN,
    FOREIGN KEY(id_user) REFERENCES user(id_user) ON UPDATE CASCADE
);

INSERT INTO address (name_address)
VALUES 
('123 Main Street'),
('456 Elm Street'),
('789 Maple Avenue'),
('101 Oak Lane'),
('202 Birch Road');

INSERT INTO user (name_user, birthday, phone_number_user, email_user, id_address, ban_date, avatar)
VALUES 
('John Doe', '1990-05-15', '1234567890', 'johndoe@example.com', 1, NULL, NULL),
('Jane Smith', '1985-10-20', '9876543210', 'janesmith@example.com', 2, NULL, NULL),
('Alice Johnson', '1995-03-12', '1112223333', 'alicej@example.com', 3, '2024-01-01', NULL),
('Bob Brown', '2000-07-08', '4445556666', 'bobbrown@example.com', 4, NULL, NULL),
('Emma Davis', '1992-09-25', '7778889999', 'emmadavis@example.com', 5, NULL, NULL);

INSERT INTO librarian (name_librarian, birthday, phone_number_librarian, email_librarian, id_address, username_account, password_account, avatar)
VALUES 
('Libby Adams', '1988-02-14', '0346399421', 'minhquan15032005@gmail.com', 1, 'admin', 'admin', NULL);

INSERT INTO books (title, description, publisher, published_date, page_count, count_rating, average_rating, link_image, quantity)
VALUES 
('To Kill a Mockingbird', 'A novel about racial injustice in the Deep South.', 'J.B. Lippincott & Co.', 1960, 281, 5000, 4.8, NULL, 10),
('1984', 'A dystopian novel set in a totalitarian regime.', 'Secker & Warburg', 1949, 328, 7000, 4.6, NULL, 15),
('The Great Gatsby', 'A critique of the American Dream in the 1920s.', 'Charles Scribner''s Sons', 1925, 180, 3000, 4.2, NULL, 7);

INSERT INTO authors (name_author)
VALUES 
('Harper Lee'),
('George Orwell'),
('F. Scott Fitzgerald');

INSERT INTO categories (name_category)
VALUES 
('Fiction'),
('Classic'),
('Dystopian'),
('Literature');

INSERT INTO book_author (id_book, id_author)
VALUES 
(1, 1), -- "To Kill a Mockingbird" by Harper Lee
(2, 2), -- "1984" by George Orwell
(3, 3); -- "The Great Gatsby" by F. Scott Fitzgerald

INSERT INTO book_category (id_book, id_category)
VALUES 
(1, 1), -- "To Kill a Mockingbird" - Fiction
(1, 2), -- "To Kill a Mockingbird" - Classic
(2, 1), -- "1984" - Fiction
(2, 3), -- "1984" - Dystopian
(3, 1), -- "The Great Gatsby" - Fiction
(3, 2); -- "The Great Gatsby" - Classic

INSERT INTO borrowing (id_book, id_user, borrowed_date, due_date, returned_date, name_first_reader)
VALUES 
(1, 1, '2024-12-01', '2024-12-15', NULL, 'John Doe'),
(2, 2, '2024-12-05', '2024-12-20', '2024-12-18', 'Jane Smith'),
(3, 3, '2024-12-10', '2024-12-25', NULL, 'Alice Johnson');

INSERT INTO notifications (title, content, id_user, is_seen)
VALUES 
('Overdue Book', 'Your borrowed book is overdue.', 1, FALSE),
('New Book Arrival', 'We have added new books to our collection.', 2, TRUE),
('Membership Renewal', 'Your membership is due for renewal.', 3, FALSE);
