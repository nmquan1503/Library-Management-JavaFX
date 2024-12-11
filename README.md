## **Library Management Application**

The **Library Management Application** is a JavaFX-based software designed to streamline and simplify library operations. This application aims to provide an intuitive and efficient solution for managing library resources and processes, including:

- **Book Management**: Keep track of detailed book information such as title, author, description, categories, publisher, published date, rating, and stock availability.
- **Borrower Management**: Store and manage borrower details, including names, birthday, contact information, and borrowing history.
- **Borrowing and Returning Management**: Handle book loans and returns, monitor due dates, and send reminders for overdue books.

The application is designed to cater to libraries of all sizes, offering a user-friendly interface with robust performance. Leveraging the power of JavaFX, the application ensures a seamless user experience with high responsiveness.


---


## **Class diagram**
![](image/diagram.png)


---


## **Interfaces**

- **Login screen**
*The Login Screen provides secure access to the Library Management System. It features fields for username and password, along with an option to save login details for future sessions.*
![](image/pic2.png)


- **Home screen**  
*The Home Screen provides an overview of the Library. It displays key summary information such as the total number of books, borrowers,...*
![](image/pic1.png)
![](image/pic7.png)  
*The system automatically generates overdue notifications for borrowers who exceed their return deadlines. Additionally, it includes a scanning feature to generate an email draft with borrower details when scanning a specific code.*
![](image/pic18.png)
![](image/pic19.png)


- **Books Screen**  
*The Book Screen displays a list of all the books available in the library. It includes a section showing the top 3 most borrowed books, helping users easily identify the most popular titles. Additionally, there is a search field that allows users to quickly search for books by title, making it easier to find specific books in the collection. This screen provides an intuitive way for users to browse and access books efficiently.The Book View integrates with Google's Text-to-Speech API to read aloud book details.*
![](image/pic12.png)
![](image/pic15.png)


- **Users Screen**  
*The Users Screen displays a list of all library borrowers, along with a section showing individuals who are currently banned from borrowing. It also includes a search field that allows users to quickly find a specific borrower by name or other details, making it easier to manage and view user information efficiently. This screen provides an organized view of both active and restricted users.The User View integrates with Google's Text-to-Speech API to read aloud user details.*
![](image/pic13.png)
![](image/pic16.png)


- **Borrowing screen**  
*The Borrow Screen displays a list of all borrowing transactions in the library, providing an overview of previous book loans. This screen allows users to borrow books by adding a new borrowing record to the database. The action of borrowing a book is recorded as a new transaction, updating the borrowing history. Users can select the book to borrow, and upon confirmation, the borrowing transaction is added to the system, ensuring accurate tracking of book loans.*
![](image/pic10.png)
![](image/pic9.png)


- **Return books screen**  
*The Return Book Screen allows users to complete the process of returning borrowed books.*
![](image/pic11.png)
![](image/pic17.png)


- **Edit Screen**  
*The Edit Screen allows administrators to manage and modify library records. It provides options to update book information, including title, author, and other details, as well as add or remove books from the system. Additionally, this screen enables the editing of borrower information, allowing updates to borrower details, such as contact information or status. The screen also integrates with the Google Books API, allowing administrators to fetch book details directly from Google Books to ensure accurate and up-to-date information for each book in the library's collection.*
![](image/pic4.png)
![](image/pic5.png)
![](image/pic6.png)


- **Librarian Account Screen**  
*The Librarian Account Screen allows the librarian to update the details of their current account.*
![](image/pic14.png)


- **Dark/Light Mode and Translate**  
*The application supports both Dark and Light modes, allowing users to switch between them for a personalized viewing experience. Additionally, it offers a language toggle to easily switch between English and Vietnamese, providing seamless access to content in both languages.*
![](image/pic8.png)


---


## **Technologies used**
This project utilizes the following technologies and libraries:

- **JavaFX**: For building the user interface (UI) of the application, providing a rich and interactive experience.
- **JFoenix**: A JavaFX library that offers Material Design components to enhance the UI with modern and aesthetically pleasing controls.
- **MySQL**: Used as the relational database management system (RDBMS) to store and manage library data such as books, borrowers, and transactions.
- **Multithreading**: For improving the application's performance, allowing tasks like book searching, data processing, and other background tasks to run concurrently.
- **JUnit**: For unit testing, ensuring the functionality of individual components and methods within the application.
- **Google Books API**: To retrieve book information such as titles, authors, and descriptions from an external source, making it easier to fetch book data.
- **Google Text-to-Speech (TTS) API**: To enable the application to read book information aloud using text-to-speech technology, providing an accessible feature for users.
- **Google Translate API**: For supporting multi-language translation, allowing users to switch between English and Vietnamese in the application interface.
- **ZXing Library**: Used for generating QR codes to encode borrower details, enabling quick access to email drafting functionality.
- **Network Connectivity Management**: Enables the application to seamlessly operate in both online and offline modes by checking and adapting to the network status.


---


## **How to run**
- **Install IntelliJ IDEA**  
Download and install IntelliJ IDEA from [IntelliJ official website](https://www.jetbrains.com/idea/download/)
- **Install MySQL**
Download and install MySQL from [MySQL official website](https://dev.mysql.com/downloads/installer/).
- **Set up the Database**
    - Locate the [database.sql](src/main/resources/org/example/demo/database/librarySQL.sql)
    - Import this file into MySQL to create the necessary database schema and tables.
    - You can use a MySQL management tool like [MySQL Workbench](https://dev.mysql.com/downloads/workbench/) or run the SQL script directly from the MySQL command line.
- **Open the Project in IntelliJ**  
After installing IntelliJ, open the project folder in IntelliJ IDEA. IntelliJ will automatically detect the project settings, and you can start coding and running the application.

