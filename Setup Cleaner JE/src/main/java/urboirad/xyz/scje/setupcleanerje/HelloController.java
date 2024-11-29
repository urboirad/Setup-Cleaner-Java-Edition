package urboirad.xyz.scje.setupcleanerje;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelloController {

    @FXML
    private Button fileDirButton;
    @FXML
    private Label fileDirLabel;
    @FXML
    private ComboBox<String> extSelectBox;
    @FXML
    private Slider dateSlider;
    @FXML
    private Label fileListLabel;
    @FXML
    private Label sizeLabel;
    @FXML
    private ImageView logoImage;

    private File currentDirectory;
    private List<String> selectedExtensions = new ArrayList<>();

    @FXML
    public void initialize() {
        // Populate the combo box
        extSelectBox.getItems().addAll("Installation/Setup Files", ".zip", ".rar", ".png", ".jpg", ".jpeg", ".gif",
                ".bmp", ".tiff", ".tif", ".webp", ".mp4", ".mkv", ".avi", ".mov", ".wmv", ".flv", ".webm",
                ".mp3", ".wav", ".flac", ".ogg", ".wma", ".aac", ".m4a", ".opus", ".pdf", ".docx", ".doc",
                ".xlsx", ".xls", ".pptx", ".ppt", ".txt", ".html", ".css", ".js");

        // Logo
        Image logo = new Image(HelloController.class.getResource("scpLogo.png").toString());
        logoImage.setImage(logo);
    }

    @FXML
    private void onSelectFileDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select File Directory");
        File selectedDirectory = directoryChooser.showDialog(fileDirButton.getScene().getWindow());
        if (selectedDirectory != null) {
            currentDirectory = selectedDirectory;
            fileDirLabel.setText(currentDirectory.getAbsolutePath());
            refreshFileList();
        }
    }

    @FXML
    private void onSelectExtension() {
        String selectedText = extSelectBox.getValue();
        if ("Installation/Setup Files".equals(selectedText)) {
            selectedExtensions = Arrays.asList("setup", "installer", ".msi", ".msm", ".msp", ".mst", ".msu",
                    ".idt", ".cub", ".pcp", "-x64", "-x86", "-amd64", "-x32", "-amd32", "-win64", "-64-bit",
                    "-32-bit", ".exe");
        } else {
            selectedExtensions = List.of(selectedText);
        }
        refreshFileList();
    }

    private void refreshFileList() {
        if (currentDirectory == null) {
            fileListLabel.setText("No directory selected.");
            return;
        }

        // Filter files based on extensions
        File[] files = currentDirectory.listFiles(file -> {
            for (String ext : selectedExtensions) {
                if (file.getName().toLowerCase().endsWith(ext.toLowerCase())) {
                    return true;
                }
            }
            return false;
        });

        if (files != null) {
            StringBuilder fileNames = new StringBuilder();
            long totalSize = 0;
            for (File file : files) {
                fileNames.append(file.getName()).append("\n");
                totalSize += file.length();
            }

            fileListLabel.setText(fileNames.toString());
            sizeLabel.setText(String.format("Combined Size: %.2f MB", totalSize / (1024.0 * 1024)));
        }
    }

    @FXML
    private void onRefreshFileList() {
        if (currentDirectory == null) {
            fileListLabel.setText("No directory selected.");
            return;
        }

        // Filter files based on extensions
        File[] files = currentDirectory.listFiles(file -> {
            for (String ext : selectedExtensions) {
                if (file.getName().toLowerCase().endsWith(ext.toLowerCase())) {
                    return true;
                }
            }
            return false;
        });

        if (files != null) {
            StringBuilder fileNames = new StringBuilder();
            long totalSize = 0;
            for (File file : files) {
                fileNames.append(file.getName()).append("\n");
                totalSize += file.length();
            }

            fileListLabel.setText(fileNames.toString());
            sizeLabel.setText(String.format("Combined Size: %.2f MB", totalSize / (1024.0 * 1024)));
        }
    }

    @FXML
    private void onDeleteSelectedFiles() {
        if (currentDirectory == null) return;

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to delete the selected files?");
        confirmationAlert.setContentText("This action cannot be undone.");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                File[] files = currentDirectory.listFiles(file -> {
                    for (String ext : selectedExtensions) {
                        if (file.getName().toLowerCase().endsWith(ext.toLowerCase())) {
                            return true;
                        }
                    }
                    return false;
                });

                if (files != null) {
                    for (File file : files) {
                        file.delete();
                    }
                }
                refreshFileList();
            }
        });
    }
}