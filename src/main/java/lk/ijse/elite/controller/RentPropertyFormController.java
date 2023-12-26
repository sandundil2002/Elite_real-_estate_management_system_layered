package lk.ijse.elite.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lk.ijse.elite.model.CustomerModel;
import lk.ijse.elite.model.PaymentModel;
import lk.ijse.elite.model.PropertyModel;
import lk.ijse.elite.model.RentingModel;
import lk.ijse.elite.model.dto.*;

import java.sql.SQLException;
import java.util.List;

public class RentPropertyFormController {
    public TextField txtCustomerName;
    public TextField txtPropertyPrice;
    public DatePicker txtDate;
    public TextField txtRentid;
    public JFXComboBox comProid;
    public JFXComboBox comCusid;
    public TextField txtpayId;
    public ChoiceBox cmdPaymethod;
    public ChoiceBox cmdDuration;

    public void initialize(){
        loadAllProperty();
        loadAllCustomer();
        try {
            autoGenarateRentId();
            autoGenaratePaymentId();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        txtDate.setValue(java.time.LocalDate.now());

        comProid.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            try {
                PropertyDTO propertyDto = PropertyModel.searchProperty(t1.toString());
                txtPropertyPrice.setText(propertyDto.getPrice());
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        });

        comCusid.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            try {
                CustomerDTO customerDto = CustomerModel.searchCustomer(t1.toString());
                txtCustomerName.setText(customerDto.getName());
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        });

        cmdPaymethod.getItems().addAll("Cash","Card","Cheque","Online");
        cmdPaymethod.setValue("Cash");

        cmdDuration.getItems().addAll("3 Months","6 Months","1 Year","2 Years","5 Years");
        cmdDuration.setValue("3 Months");
    }

    public void btnRentOnAction(ActionEvent actionEvent) {
        String rentId = txtRentid.getText();
        String paymentId = txtpayId.getText();
        String customerId = String.valueOf(comCusid.getValue());
        String propertyId = String.valueOf(comProid.getValue());
        String name = txtCustomerName.getText();
        String price = txtPropertyPrice.getText();
        String method = String.valueOf(cmdPaymethod.getValue());
        String date = txtDate.getValue().toString();
        String duration = String.valueOf(cmdDuration.getValue());

        var rentDto = new RentDTO(rentId,propertyId,customerId,date,duration);
        var rentDetailDto = new RentingDetailDTO(rentId,propertyId,duration);
        var paymentDto = new PaymentDTO(paymentId,customerId,propertyId,date,price,method);
        var paymentDetailDto = new PaymentdetailDTO(propertyId,paymentId,method);

        try {
            boolean isSuccess = RentingModel.isUpdated(rentDto,rentDetailDto,paymentDto,paymentDetailDto);
            if (isSuccess){
                new Alert(Alert.AlertType.CONFIRMATION,"Rent Succesfull").show();
                btnRentClearOnAction();
                autoGenarateRentId();
            }
        } catch(SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public void btnRentClearOnAction() {
        txtCustomerName.clear();
        txtPropertyPrice.clear();
    }

    private void loadAllProperty() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<PropertyDTO> proList = PropertyModel.loadAllProperty();

            for (PropertyDTO propertyDto  : proList) {
                obList.add(propertyDto.getPropertyId());
            }
            comProid.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void loadAllCustomer() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<CustomerDTO> cusList = CustomerModel.getAllCustomers();

            for (CustomerDTO customerDto  : cusList) {
                obList.add(customerDto.getCustomer_id());
            }

            comCusid.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void autoGenarateRentId() throws SQLException, ClassNotFoundException {
        txtRentid.setText(new RentingModel().generateRentId());
    }

    private void autoGenaratePaymentId() throws SQLException, ClassNotFoundException {
        txtpayId.setText(new PaymentModel().generatePaymentId());
    }
}
