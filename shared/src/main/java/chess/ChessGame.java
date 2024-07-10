package chess;

import java.util.ArrayList;
import java.util.Collection;

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
        currTeamTurn = TeamColor.WHITE;
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
        if (team == TeamColor.WHITE){
            currTeamTurn = TeamColor.BLACK;
        }
        else{
            currTeamTurn = TeamColor.WHITE;
        }
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
            Collection<ChessMove> potentialMoves = p.pieceMoves(b, startPosition);
            //don't leave the king in danger
            Collection<ChessMove> validmoves = new ArrayList<>();
            TeamColor tcolor = p.getTeamColor();
            ChessPosition WkingPos = currBoard.getWhiteKingPosition();
            ChessPosition BkingPos = currBoard.getBlackKingPosition();
            if (tcolor.equals(TeamColor.WHITE)){
                //check that no black piece can get the king
                for (ChessMove item : potentialMoves){
                    //figure out which are good and add to valid moves

                }
            }
            else{
                //check that no white piece can get the king
                for (ChessMove item : potentialMoves){
                    //figure out which are good and add to validmoves
                }
            }
            return validmoves;
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
        for (ChessMove item : validMoves(start)){
            ChessPosition maybe = item.getEndPosition();
            if (end.equals(maybe)){
                illegal = false;
                break;
            }
            else{
                illegal = true;
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
            currBoard.addPiece(start, null);
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
}
