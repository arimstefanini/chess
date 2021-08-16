package construcao.pieces;

import construcao.Alliance;
import construcao.board.Board;
import construcao.board.BoardUtils;
import construcao.board.Move;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {8};

    public Pawn(final Alliance pieceAlliance, int piecePosition) {

        super(pieceType.PAWN, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATES){
            final int canditateDestinationCoodinate = this.piecePosition + (this.getPieceAlliance().getDirection() *currentCandidateOffset);

            if(!BoardUtils.isValidTileCoordinate(canditateDestinationCoodinate)){
                continue;
            }

            if(currentCandidateOffset == 8 && board.getTile(canditateDestinationCoodinate).isTileOccupied()){
                legalMoves.add(new Move.MajorMove(board, this, canditateDestinationCoodinate));

            } else if(currentCandidateOffset == 16 && this.isFirstMove() &&
                    (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack())||
                    (BoardUtils.SEVEN_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()) ){
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(canditateDestinationCoodinate).isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board, this, canditateDestinationCoodinate));
                }

            } else if (currentCandidateOffset == 7 &&
                      !(BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())){

                if(board.getTile(canditateDestinationCoodinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(canditateDestinationCoodinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        legalMoves.add(new Move.MajorMove(board, this, canditateDestinationCoodinate));
                    }
                }

            } else if (currentCandidateOffset == 9 &&
                      !(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                      (BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())) {

                if(board.getTile(canditateDestinationCoodinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(canditateDestinationCoodinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        legalMoves.add(new Move.MajorMove(board, this, canditateDestinationCoodinate));
                    }
                }
            }

        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getMovePiece().getPieceAlliance(), move.getDestinationCoodinate());
    }

    @Override
    public String toString(){

        return PieceType.PAWN.toString();
    }


}
