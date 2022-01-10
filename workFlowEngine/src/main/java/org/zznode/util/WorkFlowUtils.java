package org.zznode.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zznode.dao.TbWorkflowSerialMapper;
import org.zznode.entity.TbWorkflowSerial;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkFlowUtils {

    private final TbWorkflowSerialMapper tbWorkflowSerialMapper;

    public String nextCode(String firstText) {
        String dateText = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Integer maxSerialNo = tbWorkflowSerialMapper.getMaxSerialNo(dateText);
        if (maxSerialNo == null) {
            maxSerialNo = 0;
        }
        maxSerialNo = maxSerialNo + 1;

        TbWorkflowSerial tbWorkflowSerial = new TbWorkflowSerial();
        tbWorkflowSerial.setSerialno(maxSerialNo);
        tbWorkflowSerial.setCreatedate(dateText);
        tbWorkflowSerialMapper.insert(tbWorkflowSerial);

        String serialNo = String.format("%04d", maxSerialNo);
        return firstText + "-" + dateText + "-" + serialNo;
    }
}
