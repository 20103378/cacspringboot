package com.base.util.excel;
import lombok.Data;

/**
 *
 *
 * @author Lynch
 */
@Data
public class RefNameCell {
    @ExcelColumn(value = "测量点名", col = 1)
    private String refName;

    @ExcelColumn(value = "描述", col = 2)
    private String refDesc;

}