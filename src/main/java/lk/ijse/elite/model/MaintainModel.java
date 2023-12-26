package lk.ijse.elite.model;

import lk.ijse.elite.model.dto.MaintainDTO;
import lk.ijse.elite.util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaintainModel {
    public boolean addMaintain(MaintainDTO dto) throws SQLException, ClassNotFoundException {
       return SQLUtil.sql("INSERT INTO maintain VALUES (?,?,?,?)",dto.getMaintain_id(),dto.getRent_id(),dto.getDate(),dto.getStatus());
    }

    public List<MaintainDTO> loadAllMaintenance() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.sql("SELECT * FROM maintain");
        List<MaintainDTO> maintainList = new ArrayList<>();

        while (resultSet.next()) {
            maintainList.add(new MaintainDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }
        return maintainList;
    }

    public boolean updateMaintainComplete(String maintainId) throws SQLException, ClassNotFoundException {
        return SQLUtil.sql("UPDATE maintain SET Status = ? WHERE Maintain_id = ?","Completed",maintainId);
    }

    public boolean updateMaintainCansel(String maintainId) throws SQLException, ClassNotFoundException {
        return SQLUtil.sql("UPDATE maintain SET Status = ? WHERE Maintain_id = ?","Canceled",maintainId);
    }

    public String generateMaintainId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.sql("SELECT Maintain_id FROM maintain ORDER BY Maintain_id DESC LIMIT 1");
        if (resultSet.next()) {
            String id = resultSet.getString("Maintain_id");
            String numericPart = id.replaceAll("\\D", "");
            int newmaintainId = Integer.parseInt(numericPart);
            return String.format("M%03d", newmaintainId) + 1 ;
        } else {
            return "M001";
        }
    }
}
