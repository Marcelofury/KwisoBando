package kwiso.bando.config;

public final class RoundResult {
    public enum Kind {
        WIN,
        LOSS
    }

    public final Kind kind;
    public final float elapsedSeconds;
    public final String reason;

    public RoundResult(Kind kind, float elapsedSeconds, String reason) {
        this.kind = kind;
        this.elapsedSeconds = elapsedSeconds;
        this.reason = reason;
    }
}
