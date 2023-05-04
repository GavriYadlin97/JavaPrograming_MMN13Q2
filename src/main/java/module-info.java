module com.example.maman13q2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.maman13q2 to javafx.fxml;
    exports com.maman13q2;
}