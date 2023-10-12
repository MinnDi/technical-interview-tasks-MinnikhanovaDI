package ru.eagdort.interview.rate.limiter;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * TODO Необходимо реализовать оконный лимитер, который:
 * <p>
 * 1. способен пропускать заданное (rate) количество TPS (операций в секунду)
 * <p>
 * 2. не пропускать операции при превышении rate, т.е. меджу двумя моментами времени,
 * * отстоящими друг от друга на секунду, не должно быть больше операций, чем rate
 * <p>
 * 3. должен работать в многопоточном режиме
 */
public class WindowRateLimiter implements RateLimiter {

    private final int rate;
    private LocalTime startTime;
    private int count;

    public WindowRateLimiter(int rate) {
        this.rate = rate;
    }

    @Override
    public boolean accept() {
        if (rate == 0) {
            return false;
        }
        synchronized (this) {
            if (startTime == null) {
                startTime = LocalTime.now();
            }
            if (LocalTime.now().isBefore(startTime.plus(1, TimeUnit.SECONDS.toChronoUnit()))) {
                if (count < rate) {
                    count++;
                    return true;
                } else {
                    return false;
                }
            }else {
                startTime = startTime.plus(1, TimeUnit.SECONDS.toChronoUnit());
                count = 1;
                return true;
            }
        }
    }

}
