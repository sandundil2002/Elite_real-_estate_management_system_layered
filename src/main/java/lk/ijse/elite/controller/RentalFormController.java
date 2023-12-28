package lk.ijse.elite.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.elite.bo.custom.RentingBO;
import lk.ijse.elite.bo.custom.impl.RentingBOImpl;
import lk.ijse.elite.model.dto.RentingDTO;
import lk.ijse.elite.model.dto.tm.RentingTM;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class RentalFormController {
    public TableColumn colRentId;
    public TableColumn colPropertyId;
    public TableColumn colAgentId;
    public TableColumn colDate;
    public TableColumn colDuration;
    public AnchorPane rentPane;
    public TableView tblRent;
    public TableColumn colMaintain;
    public TableColumn colDelete;
    RentingBO rentingBO = new RentingBOImpl();

    public void initialize() throws SQLException {
        setCellValueFactories();
        loadAllRentals();
    }

    public void btnViewPropertyOnAction(ActionEvent actionEvent) {
        rentPane.getChildren().clear();
        try {
            AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/PropertyForm.fxml"));
            rentPane.getChildren().add(anchorPane);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setCellValueFactories() {
        colRentId.setCellValueFactory(new PropertyValueFactory<>("rent_id"));
        colPropertyId.setCellValueFactory(new PropertyValueFactory<>("property_id"));
        colAgentId.setCellValueFactory(new PropertyValueFactory<>("agent_id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colMaintain.setCellValueFactory(new PropertyValueFactory<>("btnMaintain"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("btnDelete"));
    }

    private void loadAllRentals() {
        ObservableList<RentingTM> obList = FXCollections.observableArrayList();

        try {
            List<RentingDTO> dtoList = rentingBO.loadAllRenting();
            for (RentingDTO dto : dtoList) {
                Button Maintain = new Button("Maintain");
                Button Delete = new Button("Delete");
                Maintain.setCursor(Cursor.HAND);
                Delete.setCursor(Cursor.HAND);

                Maintain.setOnAction((e) ->{
                    ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to maintain this Renting?", ok, no).showAndWait();
                    if (result.orElse(no) == ok) {
                        try {
                            URL resource = MaintainmanageFormController.class.getResource("/view/maintainmanage_form.fxml");
                            Parent parent = FXMLLoader.load(resource);
                            Scene scene = new Scene(parent);
                            Stage stage = new Stage();
                            stage.setTitle("Property Maintain Form");
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                Delete.setOnAction((e) ->{
                    ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this Renting?", ok, no).showAndWait();
                    if (result.orElse(no) == ok) {
                        try {
                            boolean isDeleted = rentingBO.deleteRenting(dto.getRent_id());
                            if (isDeleted){
                                new Alert(Alert.AlertType.CONFIRMATION, "Renting has been deleted successfully").show();
                                initialize();
                            }else{
                                new Alert(Alert.AlertType.ERROR, "Failed to delete the Renting").show();
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
                        }
                    }
                });

               obList.add(new RentingTM(
                        dto.getRent_id(),
                        dto.getProperty_id(),
                        dto.getAgent_id(),
                        dto.getDate(),
                        dto.getDuration(),
                        Maintain,
                        Delete
                ));
            }
            tblRent.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnViewMaintainOnAction(ActionEvent actionEvent) {
        rentPane.getChildren().clear();
        try {
            AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/maintain_Form.fxml"));
            rentPane.getChildren().add(anchorPane);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
