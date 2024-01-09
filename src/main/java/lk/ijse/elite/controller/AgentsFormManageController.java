package lk.ijse.elite.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lk.ijse.elite.bo.custom.AgentBO;
import lk.ijse.elite.bo.custom.impl.AgentBOImpl;
import lk.ijse.elite.dto.AgentDTO;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class AgentsFormManageController {
    public TextField txtAgentid;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtMobile;
    public TextField txtEmail;
    AgentBO agentBO = new AgentBOImpl();

    public void initialize() {
        try {
            autoGenerateId();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String agentid = txtAgentid.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String mobile = txtMobile.getText();
        String email = txtEmail.getText();

        boolean isAgentValidated = validateAgent();
        if (!isAgentValidated) {
            return;
        }

        var dto = new AgentDTO(agentid, name, address, mobile, email);

        try {
            boolean isSaved = agentBO.saveAgent(dto);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Agent Added Succesfull").show();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtAddress.setText("");
        txtName.setText("");
        txtMobile.setText("");
        txtEmail.setText("");
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String agentid = txtAgentid.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String mobile = txtMobile.getText();
        String email = txtEmail.getText();

        boolean isAgentValidated = validateAgent();
        if (!isAgentValidated) {
            return;
        }

        var dto = new AgentDTO(agentid, name, address, mobile, email);

        try {
            boolean isUpdated = agentBO.updateAgent(dto);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Agent Update Succesfull!!!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String agentid = txtAgentid.getText();

        try {
            boolean isDeleted = agentBO.deleteAgent(agentid);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Agent Deleted Succesfull").show();
                initialize();
            } else {
                new Alert(Alert.AlertType.ERROR,"Agent Not Found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void autoGenerateId() throws SQLException, ClassNotFoundException {
        txtAgentid.setText(agentBO.generateAgentId());
    }

    private boolean validateAgent(){
        String name = txtName.getText();
        boolean nameValidation = Pattern.compile("[A-Za-z]{3,}").matcher(name).matches();
        if (!nameValidation) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name").show();
            txtName.requestFocus();
            return false;
        }

        String address = txtAddress.getText();
        boolean addressValidation = Pattern.compile("[A-Za-z]{3,}").matcher(address).matches();
        if (!addressValidation) {
            new Alert(Alert.AlertType.ERROR, "Invalid Address").show();
            txtAddress.requestFocus();
            return false;
        }

        String mobile = txtMobile.getText();
        boolean mobileValidation = Pattern.compile("[0-9]{10}").matcher(mobile).matches();
        if (!mobileValidation) {
            new Alert(Alert.AlertType.ERROR, "Invalid Mobile").show();
            txtMobile.requestFocus();
            return false;
        }

        String email = txtEmail.getText();
        boolean emailValidation = Pattern.compile("[A-Za-z0-9@.]{3,}").matcher(email).matches();
        if (!emailValidation) {
            new Alert(Alert.AlertType.ERROR, "Invalid Email").show();
            txtEmail.requestFocus();
            return false;
        }
        return true;
    }
}
