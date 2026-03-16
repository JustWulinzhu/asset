package com.wlz.asset.common.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestTask {

    // CRON格式：秒 分 时 日 月 周 年（可选）
    @Scheduled(cron = "0 0 13 * * ?")
    public void test() {
        log.info("测试定时任务执行");
    }

    // fixedRate 固定频率 每10秒执行一次
    @Scheduled(fixedRate = 10000) //  单位毫秒
    public void test2() {
//        log.info("测试定时任务执行2");
    }

}
