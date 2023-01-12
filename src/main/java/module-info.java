module darius.proiectpebune {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.postgresql.jdbc;
    requires java.sql;


    opens darius.proiectpebune to javafx.fxml;
    exports darius.proiectpebune;
}