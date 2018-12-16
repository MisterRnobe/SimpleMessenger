package server.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupData extends DialogCommonData {
    private String groupName;
    private String creator;
    private String avatarPath;
    private Integer usersCount;
}
