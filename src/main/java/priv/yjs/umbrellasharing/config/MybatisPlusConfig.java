package priv.yjs.umbrellasharing.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import priv.yjs.umbrellasharing.common.CommonConst;

import java.time.LocalDateTime;

@Configuration
@MapperScan("priv.yjs.umbrellasharing.mapper")
public class MybatisPlusConfig {

    /**
     * 自定义插入字段
     */
    @Bean
    public MetaObjectHandler customMetaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now())
                        .strictInsertFill(metaObject, "modifiedTime", LocalDateTime.class, LocalDateTime.now())
                        .strictInsertFill(metaObject, "status", Integer.class, 0)
                        .strictInsertFill(metaObject, "endTime", LocalDateTime.class, LocalDateTime.now().plusMinutes(CommonConst.BORROW_MINUTE))
                        .strictInsertFill(metaObject, "borrowTime", LocalDateTime.class, LocalDateTime.now());
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                strictUpdateFill(metaObject, "modifiedTime", LocalDateTime.class, LocalDateTime.now());
            }
        };
    }
}
