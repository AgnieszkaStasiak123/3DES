module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    //exports ;
    //opens to;
    exports com.example.demo.UI;
    opens com.example.demo.UI to javafx.fxml;
    exports com.example.demo.src;
    opens com.example.demo.src to javafx.fxml;
}