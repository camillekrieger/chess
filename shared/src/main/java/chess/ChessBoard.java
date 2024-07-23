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
    ChessPosition wKingPos;
    ChessPosition bKingPos;

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
        if (piece != null && piece.getTeamColor() == ChessGame.TeamColor.WHITE && piece.getPieceType() == ChessPiece.PieceType.KING){
            wKingPos = position;
        }
        if (piece != null && piece.getTeamColor() == ChessGame.TeamColor.BLACK && piece.getPieceType() == ChessPiece.PieceType.KING){
            bKingPos = position;
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
        return wKingPos;
    }

    public ChessPosition getBKingPos() {
        return bKingPos;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */

    public void resetBoard() {
        squares = new ChessPiece[8][8];
        //place white
        ChessPosition lRook = new ChessPosition(1, 1);
        ChessPiece newLRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(lRook, newLRook);
        ChessPosition rRook = new ChessPosition(1, 8);
        ChessPiece newRRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(rRook, newRRook);
        ChessPosition lKnight = new ChessPosition(1, 2);
        ChessPiece newLKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(lKnight, newLKnight);
        ChessPosition rKnight = new ChessPosition(1, 7);
        ChessPiece newRKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(rKnight, newRKnight);
        ChessPosition lBishop = new ChessPosition(1, 3);
        ChessPiece newLBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(lBishop, newLBishop);
        ChessPosition rBishop = new ChessPosition(1, 6);
        ChessPiece newRBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(rBishop, newRBishop);
        ChessPosition queen = new ChessPosition(1, 4);
        ChessPiece newQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        addPiece(queen, newQueen);
        ChessPosition king = new ChessPosition(1, 5);
        ChessPiece newKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        addPiece(king, newKing);
        //white pawns
        ChessPosition pawnOne = new ChessPosition(2, 1);
        ChessPiece newPawnOne = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(pawnOne, newPawnOne);
        ChessPosition pawnTwo = new ChessPosition(2, 2);
        ChessPiece newPawnTwo = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(pawnTwo, newPawnTwo);
        ChessPosition pawnThree = new ChessPosition(2, 3);
        ChessPiece newPawnThree = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(pawnThree, newPawnThree);
        ChessPosition pawnFour = new ChessPosition(2, 4);
        ChessPiece newPawnFour = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(pawnFour, newPawnFour);
        ChessPosition pawnFive = new ChessPosition(2, 5);
        ChessPiece newPawnFive = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(pawnFive, newPawnFive);
        ChessPosition pawnSix = new ChessPosition(2, 6);
        ChessPiece newPawnSix = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(pawnSix, newPawnSix);
        ChessPosition pawnSeven = new ChessPosition(2, 7);
        ChessPiece newPawnSeven = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(pawnSeven, newPawnSeven);
        ChessPosition pawnEight = new ChessPosition(2, 8);
        ChessPiece newPawnEight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPiece(pawnEight, newPawnEight);
        //reset black
        ChessPosition blackLRook = new ChessPosition(8, 1);
        ChessPiece newBlackLRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(blackLRook, newBlackLRook);
        ChessPosition blackRRook = new ChessPosition(8, 8);
        ChessPiece newBlackRRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(blackRRook, newBlackRRook);
        ChessPosition blackLKnight = new ChessPosition(8, 2);
        ChessPiece newBlackLKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(blackLKnight, newBlackLKnight);
        ChessPosition blackRKnight = new ChessPosition(8, 7);
        ChessPiece newBlackRKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(blackRKnight, newBlackRKnight);
        ChessPosition blackLBishop = new ChessPosition(8, 3);
        ChessPiece newBlackLBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(blackLBishop, newBlackLBishop);
        ChessPosition blackRBishop = new ChessPosition(8, 6);
        ChessPiece newBlackRBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(blackRBishop, newBlackRBishop);
        ChessPosition blackQueen = new ChessPosition(8, 4);
        ChessPiece newBlackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        addPiece(blackQueen, newBlackQueen);
        ChessPosition blackKing = new ChessPosition(8, 5);
        ChessPiece newBlackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        addPiece(blackKing, newBlackKing);
        //black pawns
        ChessPosition blackPawnOne = new ChessPosition(7, 1);
        ChessPiece newBlackPawnOne = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(blackPawnOne, newBlackPawnOne);
        ChessPosition blackPawnTwo = new ChessPosition(7, 2);
        ChessPiece newBlackPawnTwo = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(blackPawnTwo, newBlackPawnTwo);
        ChessPosition blackPawnThree = new ChessPosition(7, 3);
        ChessPiece newBlackPawnThree = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(blackPawnThree, newBlackPawnThree);
        ChessPosition blackPawnFour = new ChessPosition(7, 4);
        ChessPiece newBlackPawnFour = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(blackPawnFour, newBlackPawnFour);
        ChessPosition blackPawnFive = new ChessPosition(7, 5);
        ChessPiece newBlackPawnFive = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(blackPawnFive, newBlackPawnFive);
        ChessPosition blackPawnSix = new ChessPosition(7, 6);
        ChessPiece newBlackPawnSix = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(blackPawnSix, newBlackPawnSix);
        ChessPosition blackPawnSeven = new ChessPosition(7, 7);
        ChessPiece newBlackPawnSeven = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(blackPawnSeven, newBlackPawnSeven);
        ChessPosition blackPawnEight = new ChessPosition(7, 8);
        ChessPiece newBlackPawnEight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPiece(blackPawnEight, newBlackPawnEight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares) && Objects.equals(wKingPos, that.wKingPos) && Objects.equals(bKingPos, that.bKingPos);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(wKingPos, bKingPos);
        result = 31 * result + Arrays.deepHashCode(squares);
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <=8; j++){
                result += '|';
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece p = getPiece(pos);
                if (p == null){
                    result += ' ';
                }
                else {
                    char c = 'T';
                    if (p.getPieceType() == ChessPiece.PieceType.KING) {
                        c = 'K';
                    }
                    if (p.getPieceType() == ChessPiece.PieceType.QUEEN) {
                        c = 'Q';
                    }
                    if (p.getPieceType() == ChessPiece.PieceType.BISHOP) {
                        c = 'B';
                    }
                    if (p.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                        c = 'N';
                    }
                    if (p.getPieceType() == ChessPiece.PieceType.ROOK) {
                        c = 'R';
                    }
                    if (p.getPieceType() == ChessPiece.PieceType.PAWN) {
                        c = 'P';
                    }
                    if (p.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        c = Character.toLowerCase(c);
                    }
                    result += c;
                }
            }
            result += '\n';
        }
        return result;
    }
}
