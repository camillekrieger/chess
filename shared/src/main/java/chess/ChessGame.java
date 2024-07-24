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
    ChessBoard currBoard = new ChessBoard();

    public ChessGame() {
        currBoard.resetBoard();
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
            Collection<ChessMove> posMoves = p.pieceMoves(b, startPosition);
            Collection<ChessMove> newMoves = new ArrayList<>();
            for (ChessMove i : posMoves){
                //make the move by hand and move it back if it needs to be moved back
                TeamColor t = p.getTeamColor();
                ChessPiece.PieceType promo = i.getPromotionPiece();
                ChessPosition end = i.getEndPosition();
                currBoard.addPiece(startPosition, null);
                ChessPiece pt = currBoard.getPiece(end);
                if (promo != null){
                    ChessPiece cp = new ChessPiece(t, promo);
                    currBoard.addPiece(end, cp);
                }
                else{
                    currBoard.addPiece(end, p);
                }
                if (!isInCheck(t)){
                    newMoves.add(i);
                }
                if (pt != null){
                    currBoard.addPiece(end, pt);
                }
                else{
                    currBoard.addPiece(end, null);
                }
                currBoard.addPiece(startPosition, p);
            }
            return newMoves;
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
    private boolean checkValidMoves(Collection<ChessMove> validMoves, ChessMove move){
        boolean illegal = false;
        for (ChessMove item : validMoves){
            if (move.equals(item)) {
                illegal = false;
                break;
            } else {
                illegal = true;
            }
        }
        return illegal;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        boolean illegal = false;
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promo = move.getPromotionPiece();
        ChessPiece pp = currBoard.getPiece(start);
        if (pp != null) {
            if (pp.getTeamColor() == currTeamTurn) {
                Collection<ChessMove> vM = validMoves(start);
                if (vM.isEmpty()){
                    illegal = true;
                }
                else {
                    illegal = checkValidMoves(vM, move);
                }
            }
            else {
                illegal = true;
            }
        }
        else {
            illegal = true;
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
            if (getTeamTurn() == TeamColor.WHITE){
                setTeamTurn(TeamColor.BLACK);
            }
            else{
                setTeamTurn(TeamColor.WHITE);
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */

    private boolean checkIfWhiteInCheck(ChessPosition maybePos, ChessPosition wKingStart){
        if (currBoard.getPiece(maybePos) != null) {
            if (currBoard.getPiece(maybePos).getTeamColor() == TeamColor.BLACK) {
                Collection<ChessMove> maybeMoves = currBoard.getPiece(maybePos).pieceMoves(currBoard, maybePos);
                for (ChessMove m : maybeMoves) {
                    if (wKingStart.equals(m.getEndPosition())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkIfBlackInCheck(ChessPosition maybePos, ChessPosition bKingStart){
        if (currBoard.getPiece(maybePos) != null) {
            if (currBoard.getPiece(maybePos).getTeamColor() == TeamColor.WHITE) {
                Collection<ChessMove> maybeMoves = currBoard.getPiece(maybePos).pieceMoves(currBoard, maybePos);
                for (ChessMove m : maybeMoves) {
                    if (bKingStart.equals(m.getEndPosition())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE){
            ChessPosition wKingStart = currBoard.getWKingPos();
            if (wKingStart == null){
                return false;
            }
            //see if each black piece can hit the king
            for (int i = 1; i <=8; i++){
                for (int j = 1; j <= 8; j++) {
                    ChessPosition maybePos = new ChessPosition(i, j);
                    if (checkIfWhiteInCheck(maybePos, wKingStart)){
                        return true;
                    }
                }
            }
        }
        else{
            ChessPosition bKingStart = currBoard.getBKingPos();
            if (bKingStart == null){
                return false;
            }
            //see if each white piece can hit the king
            for (int i = 1; i <=8; i++){
                for (int j = 1; j <= 8; j++) {
                    ChessPosition maybePos = new ChessPosition(i, j);
                    if (checkIfBlackInCheck(maybePos, bKingStart)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    private boolean whiteInCheckmate(){
        boolean checkmate = false;
        for (int i = 1; i <=8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition maybePos = new ChessPosition(i, j);
                if (currBoard.getPiece(maybePos) != null) {
                    if (currBoard.getPiece(maybePos).getTeamColor() == TeamColor.WHITE) {
                        Collection<ChessMove> potentialMoves = validMoves(maybePos);
                        checkmate = potentialMoves.isEmpty();
                    }
                }
            }
        }
        return checkmate;
    }

    private boolean blackInCheckmate(){
        boolean checkmate = false;
        for (int i = 1; i <=8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition maybePos = new ChessPosition(i, j);
                if (currBoard.getPiece(maybePos) != null) {
                    if (currBoard.getPiece(maybePos).getTeamColor() == TeamColor.BLACK) {
                        Collection<ChessMove> potentialMoves = validMoves(maybePos);
                        if (potentialMoves.isEmpty()){
                            checkmate = true;
                        }
                        else{
                            checkmate = false;
                            break;
                        }
                    }
                }
            }
        }
        return checkmate;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        boolean checkmate = false;
        if (teamColor == TeamColor.WHITE) {
            ChessPosition wKingStart = currBoard.getWKingPos();
            if (wKingStart == null) {
                return false;
            }
            checkmate = whiteInCheckmate();
        }
        else{
            ChessPosition bKingStart = currBoard.getBKingPos();
            if (bKingStart == null) {
                return false;
            }
            checkmate = blackInCheckmate();
        }
        return checkmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */

    private boolean ifInStalemate(TeamColor teamColor){
        boolean result = false;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition curr = new ChessPosition(i, j);
                if (currBoard.getPiece(curr) != null && currBoard.getPiece(curr).getTeamColor() == teamColor) {
                    Collection<ChessMove> posMoves = validMoves(curr);
                    if (posMoves.isEmpty()) {
                        result = true;
                    } else {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        boolean result = false;
        if (!isInCheck(teamColor) || !isInCheckmate(teamColor)) {
            result = ifInStalemate(teamColor);
        }
        return result;
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
