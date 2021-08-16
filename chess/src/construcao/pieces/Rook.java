package construcao.pieces;

import construcao.Alliance;
import construcao.board.Board;
import construcao.board.BoardUtils;
import construcao.board.Move;
import static construcao.board.Move.*;
import construcao.board.Tile;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8, -1, 1, 8};

    public Rook(final Alliance pieceAlliance,
                final int piecePosition) {

        super(pieceType.ROOK, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoodinate = this.piecePosition;

            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoodinate)) {

                if(isFirstColumnExclusion(candidateDestinationCoodinate, candidateCoordinateOffset) ||
                        isEightColumnExclusion(candidateDestinationCoodinate, candidateCoordinateOffset)) {
                    break;
                }
                candidateDestinationCoodinate += candidateCoordinateOffset;

                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoodinate)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoodinate);
                    if(!candidateDestinationTile.isTileOccupied()){
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoodinate));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if(this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new AttackMove(board, this, candidateDestinationCoodinate, pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Rook movePiece(final Move move) {
        return new Rook(move.getMovePiece().getPieceAlliance(), move.getDestinationCoodinate());
    }

    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == 1);
    }
}
