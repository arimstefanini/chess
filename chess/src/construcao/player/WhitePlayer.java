package construcao.player;

import construcao.Alliance;
import construcao.board.Board;
import construcao.board.Move;
import construcao.board.Tile;
import construcao.pieces.Piece;
import construcao.pieces.Rook;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player{

    public WhitePlayer(final Board board, final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public BlackPlayer getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            //white king side castle
            if(!this.board.getTile(61).isTileOccupied()
                    && !this.board.getTile(62).isTileOccupied()){

                final Tile rookTile = this.board.getTile(63);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.claculateAttackOnTile(61, opponentsLegals).isEmpty() &&
                        Player.claculateAttackOnTile(62, opponentsLegals).isEmpty() &&
                        rookTile.getPiece().getPieceType().isRook()) {

                        kingCastles.add(new Move.KingSideCastleMove(this.board,
                                                                    this.playerKing,
                                                                    62,
                                                                    (Rook) rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),
                                                                   61));
                    }
                }
            }
            if(!this.board.getTile(59).isTileOccupied() &&
                    !this.board.getTile(58).isTileOccupied() &&
                    !this.board.getTile(57).isTileOccupied()){

                final Tile rookTile = this.board.getTile(56);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    kingCastles.add(new Move.QueenSideCastleMove(this.board,
                                                                this.playerKing,
                                                                58,
                                                                (Rook) rookTile.getPiece(),
                                                                rookTile.getTileCoordinate(),
                                                                59));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
