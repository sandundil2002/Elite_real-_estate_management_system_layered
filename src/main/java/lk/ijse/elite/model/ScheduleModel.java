package lk.ijse.elite.model;

import lk.ijse.elite.model.dto.SheduleDTO;
import lk.ijse.elite.util.SQLUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleModel {
    public static List<SheduleDTO> loadAllSchedule() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.sql("SELECT * FROM schedule");
        List<SheduleDTO> ScheduleList = new ArrayList<>();

        while (resultSet.next()) {
            ScheduleList.add(new SheduleDTO(
                    resultSet.getString(1),
                    resultSet.getString(4),
                    resultSet.getString(3),
                    resultSet.getString(2),
                    resultSet.getString(5)
            ));
        }
        return ScheduleList;
    }

    public static int getScheduleCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.sql("SELECT COUNT(*) FROM schedule");
        return resultSet.next() ? resultSet.getInt(1) : 0;
    }

    public boolean saveShedule(SheduleDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.sql("INSERT INTO schedule VALUES(?,?,?,?,?)",
                dto.getScheduleId(),
                dto.getAdminId(),
                dto.getDate(),
                dto.getTime(),
                dto.getStatus()
        );
    }

    public boolean updateShedule(SheduleDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.sql("UPDATE schedule SET Admin_id=?, Date=?, Time=?, Status=? WHERE Shedule_id=?",
                dto.getAdminId(),
                dto.getDate(),
                dto.getTime(),
                dto.getStatus(),
                dto.getScheduleId());
    }

    public SheduleDTO searchShedule(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.sql("SELECT * FROM schedule WHERE Shedule_id=?", id);

        if (resultSet.next()) {
            String shedule_id = resultSet.getString(1);
            String admin_id = resultSet.getString(2);
            String date = resultSet.getString(3);
            String time = resultSet.getString(4);
            String status = resultSet.getString(5);
            return new SheduleDTO(shedule_id, admin_id, date, time,status);
        }
        return null;
    }

    public boolean deleteShedule(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.sql("DELETE FROM schedule WHERE Shedule_id=?", id);
    }

    public List<SheduleDTO> loadAllShedules() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.sql("SELECT * FROM schedule");
        List<SheduleDTO> sheduleList = new ArrayList<>();

        while (resultSet.next()) {
            sheduleList.add(new SheduleDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return sheduleList;
    }

    public boolean updateSheduleCompleted(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.sql("UPDATE schedule SET Status = ? WHERE Shedule_id = ?", "Completed", id);
    }

    public boolean updateSheduleCansel(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.sql("UPDATE schedule SET Status = ? WHERE Shedule_id = ?", "Cansel", id);
    }

    public String generateSheduleId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.sql("SELECT Shedule_id FROM schedule ORDER BY Shedule_id DESC LIMIT 1");
        if (resultSet.next()) {
            String id = resultSet.getString("Shedule_id");
            String numericPart = id.replaceAll("\\D+","");
            int newSheduleId = Integer.parseInt(numericPart) + 1;
            return String.format("S%03d",newSheduleId);
        } else {
            return "S001";
        }
    }
}