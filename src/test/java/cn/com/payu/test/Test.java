package cn.com.payu.test;

import cn.com.payu.app.modules.utils.MTRandom;
import com.glsx.plat.common.utils.DateUtils;
import com.glsx.plat.common.utils.SnowFlake;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Test {

    public static void main(String[] args) {

        MTRandom mtRandom = new MTRandom();
        System.out.println(SnowFlake.nextId());
        System.out.println(mtRandom.nextInt());


        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now();

        ZonedDateTime zdtStart = today.atStartOfDay(zoneId);
        ZonedDateTime zdtStop = today.plusDays(1).atStartOfDay(zoneId);

        Instant start = zdtStart.toInstant();
        Instant stop = zdtStop.toInstant();

//        Interval interval = Interval.of(start, stop);

        int seconds = DateUtils.oddSecondOfDay();
        System.out.println(seconds);
    }


}
