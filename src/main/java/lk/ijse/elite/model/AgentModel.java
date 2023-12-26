package lk.ijse.elite.model;

import lk.ijse.elite.model.dto.AgentDTO;
import lk.ijse.elite.util.SQLUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AgentModel {
    public static List<AgentDTO> loadAllAgents() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.sql("SELECT * FROM agent");
        List<AgentDTO> agentList = new ArrayList<>();

        while (resultSet.next()) {
            var dto = new AgentDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            agentList.add(dto);
        }
        return agentList;
    }

    public static AgentDTO searchAgent(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.sql("SELECT * FROM agent WHERE agent_id = ?", id);

        if (resultSet.next()) {
            return new AgentDTO(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5));
        }
        return null;
    }

    public boolean saveAgent(AgentDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.sql("INSERT INTO agent VALUES (?,?,?,?,?)",dto.getAgent_id(),dto.getName(),dto.getAddress(),dto.getMobile(),dto.getEmail());
    }

    public boolean updateAgent(AgentDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.sql("UPDATE agent SET Name = ?, Address = ?, Mobile = ?, Email = ? WHERE agent_id = ?",dto.getName(),dto.getAddress(),dto.getMobile(),dto.getEmail(),dto.getAgent_id());
    }

    public boolean deleteAgent(String agentid) throws SQLException, ClassNotFoundException {
        return SQLUtil.sql("DELETE FROM agent WHERE agent_id = ?", agentid);
    }

    public String generateAgentId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.sql("SELECT agent_id FROM agent ORDER BY agent_id DESC LIMIT 1");
        if (resultSet.next()) {
            String id = resultSet.getString("agent_id");
            String numericPart = id.replaceAll("\\D", "");
            int newAgentId = Integer.parseInt(numericPart) + 1;
            return String.format("Agent%03d", newAgentId);
        } else {
            return "Agent001";
        }
    }
}
