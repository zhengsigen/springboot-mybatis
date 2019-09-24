package cn.zhengsigen.mybatis.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class SellBook {
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private boolean isFree;
    private boolean isOrder;
    private Date createDate;
    private Date updateDate;
}
