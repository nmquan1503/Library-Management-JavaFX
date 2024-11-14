package org.example.demo.Controllers;

import java.time.LocalDate;

public class returnTableData {
    private String borrowedDate;
    private String user;
    private String book;
    private String dueDate;
    private int idUser;
    private int idBook;
    private LocalDate due;
    public returnTableData(  String user,String book, String borrowedDate, String dueDate) {
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.book = book;
        this.user = user;
    }

    public int getIdUser() {
        return idUser;
    }

    public LocalDate getDue() {
        return due;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
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
