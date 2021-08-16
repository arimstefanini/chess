package construcao.pieces;

import construcao.Alliance;
import construcao.board.Board;
import construcao.board.BoardUtils;
import static construcao.board.Move.*;

import construcao.board.Move;
import construcao.board.Tile;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {

    private final  static int[] CANDIDATE_MOVE_COODINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final Alliance pieceAlliance, int piecePosition) {

        super(pieceType.KNIGHT, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset: CANDIDATE_MOVE_COODINATES){
            final int candidateDestinationCoodinate = this.piecePosition + currentCandidateOffset;

            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoodinate)){
                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSevenColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEightColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoodinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoodinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if(this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoodinate, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getMovePiece().getPieceAlliance(), move.getDestinationCoodinate());
    }

    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final  int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -17) || (candidateOffset == -10) ||
                candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusion(final  int currentPosition, final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currentPosition] && ((candidateOffset == -10) || candidateOffset == 6);
    }

    private static boolean isSevenColumnExclusion(final  int currentPosition, final int candidateOffset){
        return  BoardUtils.SEVEN_COLUMN[currentPosition] && ((candidateOffset == -6) || candidateOffset == 10);
    }

    private static boolean isEightColumnExclusion(final  int currentPosition, final int candidateOffset){
        return  BoardUtils.EIGHT_COLUMN[currentPosition] && ((candidateOffset == -15) || (candidateOffset == -6) ||
                candidateOffset == 10 || candidateOffset == 17);
    }
}
