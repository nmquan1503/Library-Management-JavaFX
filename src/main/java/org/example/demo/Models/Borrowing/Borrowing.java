package org.example.demo.Models.Borrowing;

import org.example.demo.Models.Users.Date;

public class Borrowing {
  private int idBorrowing;
  private int idBook;
  private int idUser;
  private Date borrowedDate;
  private Date dueDate;
  private Date returnedDate;
  public Borrowing(int idBorrowing,int idBook,int idUser,Date borrowedDate,Date dueDate,Date returnedDate){
    this.idBorrowing=idBorrowing;
    this.idBook = idBook;
    this.idUser = idUser;
    this.borrowedDate=borrowedDate;
    this.dueDate=dueDate;
    this.returnedDate=returnedDate;
  }

  
  public int getIdBorrowing(){
    return idBorrowing;
  }
  
  public void setIdBorrowing(int idBorrowing){
    this.idBorrowing=idBorrowing;
  }

  public int getIdBook() {
    return idBook;
  }

  public void setIdBook(int idBook) {
    this.idBook = idBook;
  }

  public int getIdUser() {
    return idUser;
  }

  public void setIdUser(int idUser) {
    this.idUser = idUser;
  }

  public Date getBorrowedDate() {
    return borrowedDate;
  }

  public void setBorrowedDate(Date borrowedDate) {
    this.borrowedDate = borrowedDate;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  public Date getReturnedDate() {
    return returnedDate;
  }

  public void setReturnedDate(Date returnedDate) {
    this.returnedDate = returnedDate;
  }

}
