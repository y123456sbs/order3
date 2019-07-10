package com.b4pay.order.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName CustomDateConverter
 * @Description
 * @Version 2.1
 **/
public class CustomDateConverter implements Converter<String, Date> {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public Date convert(String value) {
        try {
            //进行日期转换
            if (StringUtils.isBlank(value)) {
                return null;
            }
            if(value.contains("-")) {
                // 表示采用的日期格式是以"-"表示的。eg: 2010-09-09
                SimpleDateFormat formatter;
                if (value.contains(":")) {
                    // 表示传入的时间是有时分秒的
                    formatter = new SimpleDateFormat(DATE_FORMAT);
                } else {
                    // 表示传入的时间是没有时分秒的
                    formatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
                }
                Date dtDate = formatter.parse(value);
                return dtDate;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
