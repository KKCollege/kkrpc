package cn.kimmking.kkrpc.core.registry.kk;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * schedule to check kk registry.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/2/1 06:18
 */

@Slf4j
public class KKHeathChecker {

    final int interval = 5_000;

    final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    public void check(Callback callback) {
        executor.scheduleWithFixedDelay(() -> {
            log.debug(" schedule to check kk registry ... [{}]", DTF.format(LocalDateTime.now()));
            try {
                callback.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, interval, interval, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        this.executor.shutdown();
    }

    public interface Callback {
        void call() throws Exception;
    }


}
