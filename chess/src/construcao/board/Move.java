package construcao.board;

import construcao.pieces.Pawn;
import construcao.pieces.Piece;
import construcao.pieces.Rook;

import javax.swing.*;
import java.net.PortUnreachableException;
import java.security.PublicKey;

public abstract class Move {

    final Board board;
    final Piece movePiece;
    final int destinationCoordinate;

    public static  final Move NULL_MOVE = new NullMove();

    public Move(final Board board, final Piece movePiece, final int destinationCoordinate) {
        this.board = board;
        this.movePiece = movePiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movePiece.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;
        return getDestinationCoodinate() == otherMove.getDestinationCoodinate() &&
                getMovePiece().equals(otherMove.getMovePiece());
    }

    public int getCurrentCoodinate(){
        return this.getMovePiece().getPiecePosition();
    }

    public int getDestinationCoodinate(){

        return this.destinationCoordinate;
    }

    public Piece getMovePiece(){
        return this.movePiece;
    }

    public boolean isAttack(){
        return false;
    }

    public boolean isCastlingMove(){
        return false;
    }

    public Piece getAttackPiece(){
        return null;
    }

    public Board execute() {
        final Board.Builder builder = new Board.Builder();

        for(final Piece piece: this.board.currentPlayer().getActivePieces()){
            if(!this.movePiece.equals(piece)){
                builder.setPiece(piece);
            }
        }
        for (final Piece piece: this.board.currentPlayer().getOpponent(). getActivePieces()){
            builder.setPiece(piece);
        }
        builder.setPiece(this.movePiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    public static class MajorMove extends Move{

        public MajorMove(final Board board, final Piece movePiece, final int destinationCoodinate){
            super(board, movePiece, destinationCoodinate);
        }
    }

    public static class AttackMove extends Move{

        final Piece attackPiece;

        public AttackMove(final Board board,
                          final Piece movePiece,
                          final int destinationCoordinate,
                          final Piece attackPiece) {
            super(board, movePiece, destinationCoordinate);
            this.attackPiece = attackPiece;
        }

        @Override
        public int hashCode(){
            return this.attackPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }
            final AttackMove otherAttackMove = ( AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackPiece());
        }
        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack(){
            return true;
        }

        public Piece getAttackedPiece(){
            return this.attackPiece;
        }
    }

    public static final class PawnMove extends Move {

        public PawnMove(final Board board,
                        final Piece movePiece,
                        final int destinationCoordinate) {
            super(board, movePiece, destinationCoordinate);
        }
    }

    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(final Board board,
                              final Piece movePiece,
                              final int destinationCoordinate,
                              final Piece attackPiece) {
            super(board, movePiece, destinationCoordinate, attackPiece);
        }
    }

    public static final class PawnEnPassantAttackMove extends AttackMove {
        public PawnEnPassantAttackMove(final Board board,
                                       final Piece movePiece,
                                       final int destinationCoordinate,
                                       final Piece attackPiece) {
            super(board, movePiece, destinationCoordinate, attackPiece);
        }
    }

    public static final class PawnJump extends Move {
        public PawnJump(final Board board,
                        final Piece movePiece,
                        final int destinationCoordinate) {
            super(board, movePiece, destinationCoordinate);
        }

        @Override
        public Board execute(){
            final Board.Builder builder = new Board.Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if (!this.movePiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movePiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

    }

    static abstract class CastleMove extends Move{

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board,
                          final Piece movePiece,
                          final int destinationCoodinate,
                          final Rook castleRook,
                          final int castleRookStart,
                          final int castleRookDestination){
            super(board, movePiece, destinationCoodinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook(){
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board execute(){

            final Board.Builder builder = new Board.Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if (!this.movePiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movePiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board,
                                  final Piece movePiece,
                                  final int destinationCoordinate,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(board, movePiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString(){
            return "O-O";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board,
                                   final Piece movePiece,
                                   final int destinationCoordinate,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDestination) {
            super(board, movePiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString(){
            return "O-O-O";
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("cannot execute the null move!");
        }
    }

    public static class MoveFactory {
        private MoveFactory(){
            throw  new RuntimeException("Not instatiable!");
        }

        public static  Move createMove(final Board board, final int currentCoodinate,
                                       final int destinationCoordinate){
            for(final Move move: board.getAllLegalMoves()){
                if(move.getCurrentCoodinate() == currentCoodinate &&
                    move.getDestinationCoodinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
