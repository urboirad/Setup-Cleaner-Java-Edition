module urboirad.xyz.scje.setupcleanerje {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens urboirad.xyz.scje.setupcleanerje to javafx.fxml;
    exports urboirad.xyz.scje.setupcleanerje;
}