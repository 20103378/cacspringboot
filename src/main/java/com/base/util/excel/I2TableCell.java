package com.base.util.excel;
import lombok.Data;

/**
 *
 *
 * @author Lynch
 */
@Data
public class I2TableCell {

    @ExcelColumn(value = "远传点号", col = 1)
    private String i2id;  //i2id
    @ExcelColumn(value = "上传类型", col = 2)
    private String i1type;  //i1类型
    @ExcelColumn(value = "I1编号", col = 3)
    private String i1id;  //i1id
    @ExcelColumn(value = "名称", col = 4)
    private String i2_refname;//名称
    @ExcelColumn(value = "备注", col = 5)
    private String i2_desc;//备注
}