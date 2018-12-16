package server.dto.responses;

import common.objects.Body;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DialogListData extends Body {
    private List<DialogCommonData> dialogCommonData = new LinkedList<>();
}
