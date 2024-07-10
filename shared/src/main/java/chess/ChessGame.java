package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor currTeamTurn;
    ChessBoard currBoard;

    public ChessGame() {
        currBoard = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currTeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currTeamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard b = getBoard();
        ChessPiece p = b.getPiece(startPosition);
        if (p == null){
            return null;
        }
        else {
            return p.pieceMoves(b, startPosition);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     * A move is illegal if the chess piece cannot move there,
     * if the move leaves the team’s king in danger,
     * or if it’s not the corresponding team's turn.
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        boolean illegal = false;
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promo = move.getPromotionPiece();
        ChessPiece pp = null;
        if (currBoard == null){
            illegal = true;
        }
        else {
            pp = currBoard.getPiece(start);
        }
        if (pp == null){
            illegal = true;
        }
        else {
            for (ChessMove item : validMoves(start)) {
                ChessPosition maybe = item.getEndPosition();
                if (end.equals(maybe)) {
                    if (isInCheck(pp.getTeamColor())){
                        illegal = true;
                    }
                    else {
                        illegal = false;
                        break;
                    }
                } else {
                    illegal = true;
                }
            }
        }
        if (illegal) {
            throw new InvalidMoveException("Invalid Move");
        }
        else{
            //execute move
            ChessBoard b = getBoard();
            ChessPiece p = b.getPiece(start);
            TeamColor t = p.getTeamColor();
            ChessPiece nullPiece = new ChessPiece(t, null);
            currBoard.addPiece(start, nullPiece);
            if (promo != null){
                ChessPiece cp = new ChessPiece(t, promo);
                currBoard.addPiece(end, cp);

            }
            else{
                currBoard.addPiece(end, p);
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingstart;
        if (teamColor == TeamColor.WHITE){
            kingstart = currBoard.getWKingPos();
        }
        else{
            kingstart = currBoard.getBKingPos();
        }
        //for each black piece, see if it can hit the king's position
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition start;
        if (teamColor == TeamColor.WHITE){
            start = currBoard.getWKingPos();
        }
        else{
            start = currBoard.getBKingPos();
        }
//        return validMoves(start).isEmpty();
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return currTeamTurn == chessGame.currTeamTurn && Objects.equals(currBoard, chessGame.currBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currTeamTurn, currBoard);
    }
}
