package construcao;

import construcao.player.BlackPlayer;
import construcao.player.Player;
import construcao.player.WhitePlayer;

public enum Alliance {
    WHITE{
        @Override
        public int getDirection() {
            return -1;
        }

        public boolean isWhite(){
            return true;
        }
        public boolean isBlack(){
            return false;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return whitePlayer;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        public boolean isWhite(){
            return false;
        }
        public boolean isBlack(){
            return true;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };

    public abstract int getDirection();

    public abstract boolean isWhite();

    public abstract boolean isBlack();

    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
