package priv.yjs.umbrellasharing.config;



import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * LocalDate 或 localDateTime 的格式解析器
 *
 * @author Anyu
 * @since 2021/3/9
 */
@Configuration
public class DateTimeFormatConfig {
    private final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 格式化前端的localDate时间
     */
    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<>() {

            @Override
            public LocalDate parse(String s, Locale locale) {
                return LocalDate.parse(s, localDateFormatter);
            }


            @Override
            public String print(LocalDate localDate, Locale locale) {
                return localDateFormatter.format(localDate);
            }
        };
    }

    /**
     * 格式化前端的localDateTime时间
     */
    @Bean
    public Formatter<LocalDateTime> LocalDateTimeFormatter() {
        return new Formatter<>() {

            @Override
            public LocalDateTime parse(String s, Locale locale) {
                return LocalDateTime.parse(s, localDateTimeFormatter);
            }


            @Override
            public String print(LocalDateTime dateTime, Locale locale) {
                return localDateTimeFormatter.format(dateTime);
            }
        };
    }


    /**
     * 序列化
     */
    @Bean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        return new LocalDateTimeSerializer(localDateTimeFormatter);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
    }
}


