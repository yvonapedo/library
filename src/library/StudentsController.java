/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author YVON
 */
public class StudentsController implements Initializable {

    @FXML
    private JFXButton btn1;

    @FXML
    private JFXButton btn2;

    @FXML
    private JFXButton btn3;

    @FXML
    private JFXButton btn4;

    @FXML
    private JFXButton btn5;

    @FXML
    private JFXButton btn_add_student;

    @FXML
    private JFXButton btn_home;

    @FXML
    private Label lbl_title;

    @FXML
    private Pane top_pane;

    @FXML
    private JFXComboBox<String> choiceBox;

    @FXML
    private JFXTextField textField;

    @FXML
    private Label showTime;

    @FXML
    private Button btn_close;

    @FXML
    private TableView tblData;

    @FXML
    private TableColumn<Students_Table, String> col_Student_id;

    @FXML
    private TableColumn<Students_Table, String> col_first_name;

    @FXML
    private TableColumn<Students_Table, String> col_Last_name;

    @FXML
    private TableColumn<Students_Table, String> col_Gender;

    @FXML
    private TableColumn<Students_Table, String> col_Dob;

    @FXML
    private TableColumn<Students_Table, String> col_Address;

    @FXML
    private TableColumn<Students_Table, String> col_Phone_Number;

    @FXML
    private TableColumn<Students_Table, String> col_Student_Reference;
    Connection con = null;
    ResultSet rs = null;
    PreparedStatement preparedStatement;
    Connection connection;

    public StudentsController() throws SQLException {
        connection = (Connection) ConnectDB.conDB();
    }

    @FXML
    private void StudentAction(ActionEvent event) {
        if (event.getSource() == btn_add_student) {
            try {
                //add you loading or delays - ;-)
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("NewStudent.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    public void STimeDisplay() {
        Date date = new Date();
        do {
            showTime.setText(date.toString());
        } while (0 == 0);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            int second = LocalDateTime.now().getSecond();
            int minute = LocalDateTime.now().getMinute();
            int hour = LocalDateTime.now().getHour();
            showTime.setText(hour + ":" + (minute) + ":" + second);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        // Student_id , First_name , Last_name , Gender, Dob, Address, Phone_number, Student_Reference
        tblData.setEditable(true);

        FilteredList<Students_Table> flStudents = new FilteredList(data, p -> true);//Pass the data to a filtered list
        tblData.setItems(flStudents);//Set the table's items using the filtered list

        choiceBox.getItems().addAll("Student id", "First name", "Last name", "Gender",
                "Date of Birth", "Address", "Phone number", "Student Reference");
        choiceBox.setValue("First name");

        textField.setOnKeyReleased(keyEvent -> {
            switch (choiceBox.getValue())//Switch on choiceBox value
            {
                case "Student id":
                    flStudents.setPredicate(p -> p.getStudent_id().toLowerCase().contains(textField.getText().toLowerCase().trim()));//filter table by first name
                    break;
                case "First name":
                    flStudents.setPredicate(p -> p.getFirst_name().toLowerCase().contains(textField.getText().toLowerCase().trim()));//filter table by first name
                    break;
                case "Last name":
                    flStudents.setPredicate(p -> p.getLast_name().toLowerCase().contains(textField.getText().toLowerCase().trim()));//filter table by first name
                    break;
                case "Gender":
                    flStudents.setPredicate(p -> p.getGender().toLowerCase().contains(textField.getText().toLowerCase().trim()));//filter table by first name
                    break;
                case "Date of Birth":
                    flStudents.setPredicate(p -> p.getDob().toLowerCase().contains(textField.getText().toLowerCase().trim()));//filter table by first name
                    break;
                case "Address":
                    flStudents.setPredicate(p -> p.getAddress().toLowerCase().contains(textField.getText().toLowerCase().trim()));//filter table by first name
                    break;
                case "Phone number":
                    flStudents.setPredicate(p -> p.getPhone_number().toLowerCase().contains(textField.getText().toLowerCase().trim()));//filter table by first name
                    break;
                case "Student Reference":
                    flStudents.setPredicate(p -> p.getStudent_Reference().toLowerCase().contains(textField.getText().toLowerCase().trim()));//filter table by first name
                    break;
            }

        });

        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)
                -> {//reset table and textfield when new choice is selected
            if (newVal != null) {
                textField.setText("");
                flStudents.setPredicate(null);
            }
        });

        col_Student_id.setCellValueFactory(new PropertyValueFactory<>("Student_id"));
        col_first_name.setCellValueFactory(new PropertyValueFactory<>("First_name"));
        col_Last_name.setCellValueFactory(new PropertyValueFactory<>("Last_name"));
        col_Gender.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        col_Dob.setCellValueFactory(new PropertyValueFactory<>("Dob"));
        col_Address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        col_Phone_Number.setCellValueFactory(new PropertyValueFactory<>("Phone_number"));
        col_Student_Reference.setCellValueFactory(new PropertyValueFactory<>("Student_Reference"));

        display();
        //table.setItems(data);
        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)
                -> {//reset table and textfield when new choice is selected
            if (newVal != null) {
                textField.setText("");
                flStudents.setPredicate(null);
            }
        });
    }

    ObservableList<Students_Table> data = FXCollections.observableArrayList();
    ;

    String SQL = "select * from students";

    private void display() {
        try {
            con = ConnectDB.conDB();

            Class.forName("com.mysql.cj.jdbc.Driver");

            rs = con.createStatement().executeQuery(SQL);

            while (rs.next()) {
                data.add(new Students_Table(rs.getString("Student_id"), rs.getString("First_name"),
                        rs.getString("Last_name"), rs.getString("Gender"), rs.getString("Dob"), rs.getString("Address"), rs.getString("Phone_number"), rs.getString("Student_Reference")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     @FXML
    private void StudentsMenu(ActionEvent event) {

        if (event.getSource() == btn_close) {
           System.exit(0);
        }
        
        if (event.getSource() == btn_home) {

            try {
                //add you loading or delays - ;-)
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("home.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
        
        //Books
        if (event.getSource() == btn1) {

            try {
                //add you loading or delays - ;-)
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Books.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }

        //Students
        if (event.getSource() == btn3) {

            try {
                //add you loading or delays - ;-)
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Students.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }

        //Categories
        if (event.getSource() == btn2) {

            try {
                //add you loading or delays - ;-)
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Categories.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }

        //Borrows
        if (event.getSource() == btn4) {

            try {
                //add you loading or delays - ;-)
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Borrows.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }

    }
}
