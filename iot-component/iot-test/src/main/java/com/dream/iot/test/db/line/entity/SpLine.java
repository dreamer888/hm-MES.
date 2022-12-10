package com.dream.iot.test.db.line.entity;

import com.dream.iot.test.db.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 线体表
 * </p>
 *
 * @author 75039960@qq.com
 * @since 2022-07-01
 */
@ApiModel(value="SpLine对象", description="线体表")
public class SpLine extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "线体")
    private String line;

    @ApiModelProperty(value = "线体描述")
    private String lineDesc;

    @ApiModelProperty(value = "工序段代号")
    private String processSection;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
    public String getLineDesc() {
        return lineDesc;
    }

    public void setLineDesc(String lineDesc) {
        this.lineDesc = lineDesc;
    }
    public String getProcessSection() {
        return processSection;
    }

    public void setProcessSection(String processSection) {
        this.processSection = processSection;
    }

    //@Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "SpLine{" +
            "line=" + line +
            ", lineDesc=" + lineDesc +
            ", processSection=" + processSection +
        "}";
    }
}
