package com.example.iapp.models;

/**
 * Created by rahul on 10/04/17.
 */

public class FundTransfer
{
    private String payee_name;

    private String payee_id;

    private String status;

    private String referance_no;

    private String transaction_amount;

    private String transaction_date;

    private String destination_accountno;

    public String getPayee_name ()
    {
        return payee_name;
    }

    public void setPayee_name (String payee_name)
    {
        this.payee_name = payee_name;
    }

    public String getPayee_id ()
    {
        return payee_id;
    }

    public void setPayee_id (String payee_id)
    {
        this.payee_id = payee_id;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getReferance_no ()
    {
        return referance_no;
    }

    public void setReferance_no (String referance_no)
    {
        this.referance_no = referance_no;
    }

    public String getTransaction_amount ()
    {
        return transaction_amount;
    }

    public void setTransaction_amount (String transaction_amount)
    {
        this.transaction_amount = transaction_amount;
    }

    public String getTransaction_date ()
    {
        return transaction_date;
    }

    public void setTransaction_date (String transaction_date)
    {
        this.transaction_date = transaction_date;
    }

    public String getDestination_accountno ()
    {
        return destination_accountno;
    }

    public void setDestination_accountno (String destination_accountno)
    {
        this.destination_accountno = destination_accountno;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [payee_name = "+payee_name+", payee_id = "+payee_id+", status = "+status+", referance_no = "+referance_no+", transaction_amount = "+transaction_amount+", transaction_date = "+transaction_date+", destination_accountno = "+destination_accountno+"]";
    }
}
