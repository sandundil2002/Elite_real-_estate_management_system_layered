package lk.ijse.elite.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import lk.ijse.elite.db.DbConnection;
import lk.ijse.elite.model.dto.CustomerDTO;
import lk.ijse.elite.model.dto.PaymentDTO;
import lk.ijse.elite.model.dto.PaymentdetailDTO;
import lk.ijse.elite.model.dto.PropertyDTO;
import lk.ijse.elite.model.CustomerModel;
import lk.ijse.elite.model.PaymentModel;
import lk.ijse.elite.model.PlaceOrderModel;
import lk.ijse.elite.model.PropertyModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class PlaceorderFromController {
    public TextField txtCustomerName;
    public TextField txtPropertyPrice;
    public ComboBox comProid;
    public ComboBox comCusid;
    public DatePicker txtDate;
    public TextField txtPaymentid;
    public ChoiceBox cmdPaymethod;

    public void initialize(){
        try {
            autoGenarateId();
            loadAllProperty();
            loadAllCustomer();
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
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        });

        cmdPaymethod.getItems().addAll("Cash","Card","Cheque","Online");
        cmdPaymethod.setValue("Cash");
    }

    public void btnBuyOnAction(ActionEvent actionEvent) {
        String paymentId = txtPaymentid.getText();
        String customerId = String.valueOf(comCusid.getValue());
        String propertyId = String.valueOf(comProid.getValue());
        String name = txtCustomerName.getText();
        String price = txtPropertyPrice.getText();
        String method = String.valueOf(cmdPaymethod.getValue());
        String date = txtDate.getValue().toString();

        var paymentDto = new PaymentDTO(paymentId,customerId,propertyId,date,price,method);
        var paymentDetailDto = new PaymentdetailDTO(propertyId,paymentId,method);

        try {
            boolean isSuccess = PaymentModel.isPaymentSuccess(paymentDto,paymentDetailDto);
            if (isSuccess) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment Successfull", ButtonType.OK).show();
                jasperReport();
                btnClearOnAction();
                autoGenarateId();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnClearOnAction() {
        txtPaymentid.clear();
        txtCustomerName.clear();
        txtPropertyPrice.clear();
        txtDate.getEditor().clear();
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
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void jasperReport(){
        HashMap buyProperty = new HashMap<>();
        buyProperty.put("Payment_id",txtPaymentid.getText());

        InputStream resourceAsStream = getClass().getResourceAsStream("/reports/buyProperty.jrxml");
        try {
            JasperDesign load = JRXmlLoader.load(resourceAsStream);
            JasperReport compileReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(
                            compileReport,
                            buyProperty,
                            DbConnection.getInstance().getConnection()
                    );
        JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }


    private void autoGenarateId() throws SQLException, ClassNotFoundException {
        txtPaymentid.setText(new PaymentModel().generatePaymentId());
    }
}
