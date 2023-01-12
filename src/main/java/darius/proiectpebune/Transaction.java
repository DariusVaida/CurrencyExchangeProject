package darius.proiectpebune;

public class Transaction {
    private String username;
    private String from_currency;
    private double value_from;
    private String to_currency;
    private double value_to;
    private double amount;

    public Transaction(String username, String from_currency, double value_from, String to_currency, double value_to, double amount) {
        this.username = username;
        this.from_currency = from_currency;
        this.value_from = value_from;
        this.to_currency = to_currency;
        this.value_to = value_to;
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFrom_currency() {
        return from_currency;
    }

    public void setFrom_currency(String from_currency) {
        this.from_currency = from_currency;
    }

    public double getValue_from() {
        return value_from;
    }

    public void setValue_from(double value_from) {
        this.value_from = value_from;
    }

    public String getTo_currency() {
        return to_currency;
    }

    public void setTo_currency(String to_currency) {
        this.to_currency = to_currency;
    }

    public double getValue_to() {
        return value_to;
    }

    public void setValue_to(double value_to) {
        this.value_to = value_to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}