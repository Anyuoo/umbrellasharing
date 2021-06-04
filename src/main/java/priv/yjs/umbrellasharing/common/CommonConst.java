package priv.yjs.umbrellasharing.common;

/**
 * 全局常量
 *
 * @since 2021/4/8
 */
public final class CommonConst {
    //收款方名
    public static final String PAYEE_NAME = "yuan-lan-ban";
    //租借时间（分）
    public static final long BORROW_MINUTE = 60 * 2;
    //租借超时，计算费用（3 RMB/Hour）
    public static final int BORROW_EXPIRE_PRICE = 3;

    private CommonConst() {
    }
}
