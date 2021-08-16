package construcao.player;

public enum MoveStatus {

    DONE{
        @Override
        boolean isDone() {
            return false;
        }
    },
    ILLEGAL_MOVE{
        @Override
        boolean isDone() {
            return false;
        }
    },
    LEAVES_PLAYER_IN_CHECK{
        @Override
        boolean isDone() {
            return false;
        }
    };

    abstract boolean isDone();
}
