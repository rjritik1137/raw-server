package rateLimit;

public class EmptyRateLimit implements RateLimiter{
    @Override
    public boolean isRateLimited() {
        return false;
    }

    @Override
    public Long increase() {
        return null;
    }

    @Override
    public Long decrease() {
        return null;
    }
}
