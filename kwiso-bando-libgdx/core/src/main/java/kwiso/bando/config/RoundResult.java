package kwiso.bando.config;

public final class RoundResult {
    public enum Kind {
        WIN,
        LOSS
    }

    public final Kind kind;
    public final float elapsedSeconds;
    public final String reason;
    public final boolean spotted;

    public RoundResult(Kind kind, float elapsedSeconds, String reason) {
        this(kind, elapsedSeconds, reason, false);
    }

    public RoundResult(Kind kind, float elapsedSeconds, String reason, boolean spotted) {
        this.kind = kind;
        this.elapsedSeconds = elapsedSeconds;
        this.reason = reason;
        this.spotted = spotted;
    }
}
