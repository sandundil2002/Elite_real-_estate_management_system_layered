package lk.ijse.elite.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.elite.bo.custom.AgentBO;
import lk.ijse.elite.bo.custom.impl.AgentBOImpl;
import lk.ijse.elite.dto.AgentDTO;
import lk.ijse.elite.dto.tm.AgentTM;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class AgentFormController {
    public AnchorPane agent;
    public TableColumn colAgentid;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colMobile;
    public TableColumn colEmail;
    public TableView tblAgent;
    AgentBO agentBO = new AgentBOImpl();

    public void initialize() throws SQLException {
        setCellValueFactory();
        loadAllAgents();
    }

    public void btnAgentmanageOnAction(ActionEvent actionEvent) {
        try {
            URL resource = AgentFormController.class.getResource("/view/agentsManageForm.fxml");
            Parent parent = FXMLLoader.load(resource);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Agent Manage Form");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCellValueFactory() {
        colAgentid.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("agent_id"));
        colName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("address"));
        colMobile.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("mobile"));
        colEmail.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("email"));
    }

    private void loadAllAgents(){
        ObservableList<AgentTM> obList = FXCollections.observableArrayList();

        try{
            List<AgentDTO> dtoList = agentBO.loadAllAgent();

            for(AgentDTO dto : dtoList){
                obList.add(new AgentTM(
                        dto.getAgent_id(),
                        dto.getName(),
                        dto.getAddress(),
                        dto.getMobile(),
                        dto.getEmail()
                ));
            }
            tblAgent.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void btnBuyOnAction(ActionEvent actionEvent) {
        try {
            URL resource = PropertymanageFormcCntroller.class.getResource("/view/propertymanage_form.fxml");
            Parent parent = FXMLLoader.load(resource);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Property Buy Form");
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnRefeshOnAction(ActionEvent actionEvent) {
        try {
            initialize();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
