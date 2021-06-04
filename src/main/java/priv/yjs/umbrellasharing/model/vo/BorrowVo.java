package priv.yjs.umbrellasharing.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BorrowVo {

    private Long id;
    /**
     * 雨伞id
     */
    private Long umbrellaId;
    /**
     * 用户id
     */
    private Long userId;

    private String username;

    private String pmName;
    /**
     * 是否归还
     */
    private Boolean returned;
    /**
     * 借伞开始时间
     */

    private LocalDateTime borrowTime;
    /**
     * 借伞结束时间
     */
    private LocalDateTime endTime;
    /**
     * 归还时间
     */
    private LocalDateTime returnTime;
}
