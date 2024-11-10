package org.example.demo.Controllers;

public class returnTableData {
    private String borrowedDate;
    private String user;
    private String book;
    private String dueDate;

    public returnTableData(  String user,String book, String borrowedDate, String dueDate) {
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.book = book;
        this.user = user;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
