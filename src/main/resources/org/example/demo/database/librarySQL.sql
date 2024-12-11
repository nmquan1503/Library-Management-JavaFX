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
('Emma Davis', '1992-09-25', '7778889999', 'emmadavis@example.com', 5, NULL, NULL),
('Right', '2000-01-01', '2223340865', 'right@gmail.com', 1, '2025-01-01', NULL),
('Left', '2000-02-02', '5325797432', 'left@gmail.com', 1, '2025-01-01', NULL);

INSERT INTO librarian (name_librarian, birthday, phone_number_librarian, email_librarian, id_address, username_account, password_account, avatar)
VALUES 
('Libby Adams', '1988-02-14', '0346399421', 'minhquan15032005@gmail.com', 1, 'admin', 'admin', NULL);

INSERT INTO books (title, description, publisher, published_date, page_count, count_rating, average_rating, link_image, quantity)
VALUES 
('To Kill a Mockingbird', 'A novel about racial injustice in the Deep South.', 'J.B. Lippincott & Co.', 1960, 281, 5000, 4.8, NULL, 10),
('1984', 'A dystopian novel set in a totalitarian regime.', 'Secker & Warburg', 1949, 328, 7000, 4.6, NULL, 15),
('The Great Gatsby', 'A critique of the American Dream in the 1920s.', 'Charles Scribner''s Sons', 1925, 180, 3000, 4.2, NULL, 7),
('Teaching with Harry Potter', 'The Harry Potter phenomenon created a surge in reading with a lasting effect on all areas of culture, especially education. Today, teachers across the world are harnessing the power of the series to teach history, gender studies, chemistry, religion, philosophy, sociology, architecture, Latin, medieval studies, astronomy, SAT skills, and much more. These essays discuss the diverse educational possibilities of J.K. Rowling''s books. Teachers of younger students use Harry and Hermione to encourage kids with disabilities or show girls the power of being brainy scientists. Students are reading fanfiction, splicing video clips, or exploring Rowling''s new website, Pottermore. Harry Potter continues to open new doors to learning.', 'McFarland', 0, 287, 0, 0, 'http://books.google.com/books/content?id=sKW7u4786yUC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', 0),
('The Cuckoo''s Calling', 'Vietnamese version of the very popular adult debut mystery/detective novel by Robert Galbraith, a.k.a J. K. Rowling, featuring PI Cormoran Strike. Veitnamese translation by Ho Thi Nhu Mai. In Vietnamese. Annotation copyright Tsai Fong Books, Inc. Distributed by Tsai Fong Books, Inc.', 'Tsai Fong Books', 0, 670, 0, 0,'http://books.google.com/books/content?id=BUTroQEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api', 10),
('Harry Potter', NULL, 'PediaPress', 0, 1011, 0, 0, 'http://books.google.com/books/content?id=n3vng7gyGCYC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', 10),
('The Harry Potter Generation', 'The generation of readers most heavily impacted by J.K. Rowling''s Harry Potter series--those who grew up alongside "the boy who lived"--have come of age. They are poised to become teachers, parents, critics and writers, and many of their views and choices will be influenced by the literary revolution in which they were immersed. This collection of new essays explores the many different ways in which Harry Potter has shaped this generation''s views on everything from politics to identity to pedagogical spaces online. It seeks to determine how the books have affected fans'' understanding of their place in the world and their capacity to create it anew.', 'McFarland', 0, 219, 0, 0, 'http://books.google.com/books/content?id=1nOPDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', 10),
('Harry Potter and the Other', 'Contributions by Christina M. Chica, Kathryn Coto, Sarah Park Dahlen, Preethi Gorecki, Tolonda Henderson, Marcia Hernandez, Jackie C. Horne, Susan E. Howard, Peter C. Kunze, Florence Maätita, Sridevi Rao, Kallie Schell, Jennifer Patrice Sims, Paul Spickard, Lily Anne Welty Tamai, Ebony Elizabeth Thomas, Jasmine Wade, Karin E. Westman, and Charles D. Wilson Race matters in the fictional Wizarding World of the Harry Potter series as much as it does in the real world. As J. K. Rowling continues to reveal details about the world she created, a growing number of fans, scholars, readers, and publics are conflicted and concerned about how the original Wizarding World—quintessentially white and British—depicts diverse and multicultural identities, social subjectivities, and communities. Harry Potter and the Other: Race, Justice, and Difference in the Wizarding World is a timely anthology that examines, interrogates, and critiques representations of race and difference across various Harry Potter media, including books, films, and official websites, as well as online forums and the classroom. As the contributors to this volume demonstrate, a deeper reading of the series reveals multiple ruptures in popular understandings of the liberatory potential of the Potter series. Young people who are progressive, liberal, and empowered to question authority may have believed they were reading something radical as children and young teens, but increasingly they have raised alarms about the series’ depiction of peoples of color, cultural appropriation in worldbuilding, and the author’s antitrans statements in the media. Included essays examine the failed wizarding justice system, the counterproductive portrayal of Nagini as an Asian woman, the liberation of Dobby the elf, and more, adding meaningful contributions to existing scholarship on the Harry Potter series. As we approach the twenty-fifth anniversary of the publication of Harry Potter and the Philosopher’s Stone, Harry Potter and the Other provides a smorgasbord of insights into the way that race and difference have shaped this story, its world, its author, and the generations who have come of age during the era of the Wizarding World.', 'Univ. Press of Mississippi', 0, 330, 0, 0, 'http://books.google.com/books/content?id=2YR0EAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', 10),
('Harry Potter', 'The Harry Potter books are the bestselling books of all time. In this fascinating study, Susan Gunelius analyzes every aspect of the brand phenomenon that is Harry Potter. Delving into price wars, box office revenue, and brand values, amongst other things, this is the story of the most incredible brand success there has ever been.', 'Springer', 0, 214, 8, 3, 'http://books.google.com/books/content?id=abYKXvCwEToC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', 10);

INSERT INTO authors (name_author)
VALUES 
('Harper Lee'),
('George Orwell'),
('F. Scott Fitzgerald'),
('Valerie Estelle Frankel'),
('Robert Galbraith'),
('Emily Lauer'),
('Balaka Basu'),
('Sarah Park Dahlen'),
('Ebony Elizabeth Thomas'),
('S. Gunelius');

INSERT INTO categories (name_category)
VALUES 
('Fiction'),
('Classic'),
('Dystopian'),
('Literature'),
('Literary Criticism'),
('Performing Arts'),
('Business & Economics');

INSERT INTO book_author (id_book, id_author)
VALUES 
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(7, 6),
(7, 7),
(8, 8),
(8, 9),
(9, 10);

INSERT INTO book_category (id_book, id_category)
VALUES 
(1, 1),
(1, 2),
(2, 1),
(2, 3),
(3, 1),
(3, 2),
(4, 5),
(5, 1),
(7, 6),
(8, 5),
(9, 7);

INSERT INTO borrowing (id_book, id_user, borrowed_date, due_date, returned_date, name_first_reader)
VALUES 
(1, 1, '2024-12-01', '2024-12-15', NULL, 'John Doe'),
(2, 2, '2024-12-05', '2024-12-20', '2024-12-18', 'Jane Smith'),
(3, 3, '2024-12-10', '2024-12-25', NULL, 'Alice Johnson'),
(7, 3, '2024-12-11', '2024-12-21', NULL, NULL),
(8, 3, '2024-12-11', '2024-12-10', NULL, NULL),
(8, 1, '2024-12-11', '2024-12-10', NULL, NULL),
(7, 1, '2024-12-11', '2024-12-10', NULL, NULL),
(9, 1, '2024-12-11', '2024-12-21', NULL, NULL),
(9, 3, '2024-12-11', '2024-12-21', NULL, NULL),
(5, 3, '2024-12-11', '2024-12-21', NULL, NULL),
(5, 1, '2024-12-11', '2024-12-21', NULL, NULL),
(5, 2, '2024-12-11', '2024-12-21', NULL, NULL),
(9, 2, '2024-12-11', '2024-12-21', NULL, NULL),
(7, 2, '2024-12-11', '2024-12-21', NULL, NULL);

-- INSERT INTO notifications (title, content, id_user, is_seen)
-- VALUES 
-- ('Overdue Book', 'Your borrowed book is overdue.', 1, FALSE),
-- ('New Book Arrival', 'We have added new books to our collection.', 2, TRUE),
-- ('Membership Renewal', 'Your membership is due for renewal.', 3, FALSE);
