package priv.yjs.umbrellasharing.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonUtils {
    /**
     * 要求外部订单号必须唯一。
     * @return {String}
     */
    public  static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        key = key + System.currentTimeMillis();
        key = key.substring(0, 15);
        return key;
    }
}
