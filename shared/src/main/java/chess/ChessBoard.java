package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    ChessPosition WKingPos;
    ChessPosition BKingPos;

    public ChessBoard() {
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && piece.getPieceType() == ChessPiece.PieceType.KING){
            WKingPos = position;
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK && piece.getPieceType() == ChessPiece.PieceType.KING){
            BKingPos = position;
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    public ChessPosition getWKingPos() {
        return WKingPos;
    }

    public ChessPosition getBKingPos() {
        return BKingPos;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */

    public void resetBoard() {
        squares = new ChessPiece[8][8];
        //place white
        ChessPosition LRook = new ChessPosition(1, 1);
        ChessPiece newLRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(LRook, newLRook);
        ChessPosition RRook = new ChessPosition(1, 8);
        ChessPiece newRRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(RRook, newRRook);
        ChessPosition LKnight = new ChessPosition(1, 2);
        ChessPiece newLKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(LKnight, newLKnight);
        ChessPosition RKnight = new ChessPosition(1, 7);
        ChessPiece newRKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(RKnight, newRKnight);
        ChessPosition LBishop = new ChessPosition(1, 3);
        ChessPiece newLBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(LBishop, newLBishop);
        ChessPosition RBishop = new ChessPosition(1, 6);
        ChessPiece newRBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(RBishop, newRBishop);
        ChessPosition Queen = new ChessPosition(1, 4);
        ChessPiece newQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        addPiece(Queen, newQueen);
        ChessPosition King = new ChessPosition(1, 5);
        ChessPiece newKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        addPiece(King, newKing);
        //white pawns
        ChessPosition PawnOne = new ChessPosition(2, 1);
        ChessPiece newPawnOne = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(PawnOne, newPawnOne);
        ChessPosition PawnTwo = new ChessPosition(2, 2);
        ChessPiece newPawnTwo = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(PawnTwo, newPawnTwo);
        ChessPosition PawnThree = new ChessPosition(2, 3);
        ChessPiece newPawnThree = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(PawnThree, newPawnThree);
        ChessPosition PawnFour = new ChessPosition(2, 4);
        ChessPiece newPawnFour = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(PawnFour, newPawnFour);
        ChessPosition PawnFive = new ChessPosition(2, 5);
        ChessPiece newPawnFive = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(PawnFive, newPawnFive);
        ChessPosition PawnSix = new ChessPosition(2, 6);
        ChessPiece newPawnSix = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(PawnSix, newPawnSix);
        ChessPosition PawnSeven = new ChessPosition(2, 7);
        ChessPiece newPawnSeven = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(PawnSeven, newPawnSeven);
        ChessPosition PawnEight = new ChessPosition(2, 8);
        ChessPiece newPawnEight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(PawnEight, newPawnEight);
        //reset black
        ChessPosition BlackLRook = new ChessPosition(8, 1);
        ChessPiece newBlackLRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(BlackLRook, newBlackLRook);
        ChessPosition BlackRRook = new ChessPosition(8, 8);
        ChessPiece newBlackRRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(BlackRRook, newBlackRRook);
        ChessPosition BlackLKnight = new ChessPosition(8, 2);
        ChessPiece newBlackLKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(BlackLKnight, newBlackLKnight);
        ChessPosition BlackRKnight = new ChessPosition(8, 7);
        ChessPiece newBlackRKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(BlackRKnight, newBlackRKnight);
        ChessPosition BlackLBishop = new ChessPosition(8, 3);
        ChessPiece newBlackLBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(BlackLBishop, newBlackLBishop);
        ChessPosition BlackRBishop = new ChessPosition(8, 6);
        ChessPiece newBlackRBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(BlackRBishop, newBlackRBishop);
        ChessPosition BlackQueen = new ChessPosition(8, 4);
        ChessPiece newBlackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        addPiece(BlackQueen, newBlackQueen);
        ChessPosition BlackKing = new ChessPosition(8, 5);
        ChessPiece newBlackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        addPiece(BlackKing, newBlackKing);
        //black pawns
        ChessPosition BlackPawnOne = new ChessPosition(7, 1);
        ChessPiece newBlackPawnOne = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BlackPawnOne, newBlackPawnOne);
        ChessPosition BlackPawnTwo = new ChessPosition(7, 2);
        ChessPiece newBlackPawnTwo = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BlackPawnTwo, newBlackPawnTwo);
        ChessPosition BlackPawnThree = new ChessPosition(7, 3);
        ChessPiece newBlackPawnThree = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BlackPawnThree, newBlackPawnThree);
        ChessPosition BlackPawnFour = new ChessPosition(7, 4);
        ChessPiece newBlackPawnFour = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BlackPawnFour, newBlackPawnFour);
        ChessPosition BlackPawnFive = new ChessPosition(7, 5);
        ChessPiece newBlackPawnFive = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BlackPawnFive, newBlackPawnFive);
        ChessPosition BlackPawnSix = new ChessPosition(7, 6);
        ChessPiece newBlackPawnSix = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BlackPawnSix, newBlackPawnSix);
        ChessPosition BlackPawnSeven = new ChessPosition(7, 7);
        ChessPiece newBlackPawnSeven = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BlackPawnSeven, newBlackPawnSeven);
        ChessPosition BlackPawnEight = new ChessPosition(7, 8);
        ChessPiece newBlackPawnEight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(BlackPawnEight, newBlackPawnEight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares) && Objects.equals(WKingPos, that.WKingPos) && Objects.equals(BKingPos, that.BKingPos);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(WKingPos, BKingPos);
        result = 31 * result + Arrays.deepHashCode(squares);
        return result;
    }
}
