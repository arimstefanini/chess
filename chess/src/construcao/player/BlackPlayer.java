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

public class BlackPlayer extends Player{

    public BlackPlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces(){
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public WhitePlayer getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            //black king side castle
            if(!this.board.getTile(5).isTileOccupied() &&
               !this.board.getTile(6).isTileOccupied()){

                final Tile rookTile = this.board.getTile(7);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.claculateAttackOnTile(5, opponentsLegals).isEmpty() &&
                            Player.claculateAttackOnTile(6, opponentsLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {

                        kingCastles.add(new Move.KingSideCastleMove(this.board,
                                                                    this.playerKing,
                                                                    6,
                                                                    (Rook) rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),
                                                                    5));
                    }
                }
            }
            if(!this.board.getTile(1).isTileOccupied() &&
                    !this.board.getTile(2).isTileOccupied() &&
                    !this.board.getTile(3).isTileOccupied()){

                final Tile rookTile = this.board.getTile(0);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    kingCastles.add(new Move.QueenSideCastleMove(this.board,
                                                                this.playerKing,
                                                                2,
                                                                (Rook) rookTile.getPiece(),
                                                                rookTile.getTileCoordinate(),
                                                                3));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
