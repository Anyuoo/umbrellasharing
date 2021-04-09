package priv.yjs.umbrellasharing.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@TableName("borrow")
@Accessors(chain = true)
public class Borrow {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 雨伞id
     */
    private Long umbrellaId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 是否归还
     */
    private Boolean returned;
    /**
     * 借伞开始时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime borrowTime;
    /**
     * 借伞结束时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime endTime;
    /**
     * 归还时间
     */
    private LocalDateTime returnTime;
}
