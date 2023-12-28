package lk.ijse.elite.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lk.ijse.elite.bo.custom.ScheduleBO;
import lk.ijse.elite.bo.custom.impl.ScheduleBOImpl;
import lk.ijse.elite.model.dto.ScheduleDTO;
import lk.ijse.elite.model.dto.tm.SheduleTM;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ShedulemanageFormController {
    public AnchorPane shedule;
    public Pane bodyPane;
    public TableColumn colStatus;
    public TableColumn colComplete;
    public TableColumn conCansel;
    @FXML
    private TableColumn<?, ?> colAdminId;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colScheduleID;

    @FXML
    private TableColumn<?, ?> colTime;

    @FXML
    private TableView<SheduleTM> tblShedule;
    ScheduleBO scheduleBO = new ScheduleBOImpl();

    public void initialize(){
        setCellValueFactory();
        try {
            loadAllShedules();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colScheduleID.setCellValueFactory(new PropertyValueFactory<>("shedule_id"));
        colAdminId.setCellValueFactory(new PropertyValueFactory<>("admin_id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colComplete.setCellValueFactory(new PropertyValueFactory<>("btnCompleted"));
        conCansel.setCellValueFactory(new PropertyValueFactory<>("btnCansel"));
    }

    public void btnShedulemanageOnAction(ActionEvent actionEvent) throws IOException {
        try {
            URL resource = SheduleFormController.class.getResource("/view/sheduleManageForm.fxml");
            Parent parent = FXMLLoader.load(resource);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Shedule Form");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAllShedules() throws SQLException {
        ObservableList<SheduleTM> obList = FXCollections.observableArrayList();

        try{
            List<ScheduleDTO> dtoList = scheduleBO.loadAllSchedule();

            for(ScheduleDTO dto : dtoList){
                Button Completed = new Button("Complete");
                Button Cansel = new Button("Cansel");
                Completed.setCursor(Cursor.HAND);
                Cansel.setCursor(Cursor.HAND);

                Completed.setOnAction((e) -> {
                    ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to Completed this Schedule?", ok, no).showAndWait();
                    if (result.orElse(no) == ok) {
                        try {
                            boolean isUpdated = scheduleBO.updateScheduleCompleted(dto.getScheduleId());
                            if(isUpdated) {
                                new Alert(Alert.AlertType.CONFIRMATION, "Schedule Update Succesfull!!!").show();
                                initialize();
                            }
                        } catch (SQLException ex) {
                            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
                        } catch (ClassNotFoundException ex) {
                            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
                        }
                    }
                });

                Cansel.setOnAction((e) -> {
                    ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to Cansel this Schedule?", ok, no).showAndWait();
                    if (result.orElse(no) == ok) {
                        try {
                            boolean isUpdated = scheduleBO.updateScheduleCansel(dto.getScheduleId());
                            if (isUpdated) {
                                new Alert(Alert.AlertType.CONFIRMATION, "Schedule Update Succesfull!!!").show();
                                initialize();
                            }
                        } catch (SQLException ex) {
                            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
                        } catch (ClassNotFoundException ex) {
                            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
                        }
                    }
                }
                );

                obList.add(new SheduleTM(
                        dto.getScheduleId(),
                        dto.getAdminId(),
                        dto.getDate(),
                        dto.getTime(),
                        dto.getStatus(),
                        Completed,
                        Cansel
                ));
            }
            tblShedule.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnRefeshOnAction(ActionEvent actionEvent) {
            initialize();
    }

    public void btnCustomerOnAction(ActionEvent actionEvent) {
        bodyPane.getChildren().clear();
        try {
            AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/customerForm.fxml"));
            bodyPane.getChildren().add(anchorPane);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
