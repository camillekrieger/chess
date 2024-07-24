package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class PawnMovesCalculator {
    private ChessPosition startpos;
    private ChessBoard current;
    private ChessGame.TeamColor color;
    public PawnMovesCalculator(ChessPosition pos, ChessBoard currBoard, ChessGame.TeamColor color){
        this.startpos = pos;
        this.current = currBoard;
        this.color = color;
    }

    public Collection<ChessMove> possibleMoves() {
        Collection<ChessMove> posMoves = new ArrayList<>();
        int row = startpos.getRow();
        int col = startpos.getColumn();
        if (color == ChessGame.TeamColor.WHITE) {
            //move up
            Collection<ChessMove> whiteMovesList = whiteMoves(row, col);
            posMoves.addAll(whiteMovesList);
        }
        else{
            //move down
            Collection<ChessMove> blackMovesList = blackMoves(row, col);
            posMoves.addAll(blackMovesList);
        }
        return posMoves;
    }

    private Collection<ChessMove> whiteMoves(int row, int col) {
        Collection<ChessMove> posMoves = new ArrayList<>();
        //move up
        ChessPosition upTwo = new ChessPosition(row + 2, col);
        ChessPosition upOne = new ChessPosition(row + 1, col);
        if (row == 2) {
            if (current.getPiece(upOne) == null) {
                if (current.getPiece(upTwo) == null) {
                    ChessMove newTwo = new ChessMove(startpos, upTwo, null);
                    posMoves.add(newTwo);
                }
            }
        }
        int upOneRow = row + 1;
        if (upOneRow < 8) {
            if (current.getPiece(upOne) == null) {
                ChessMove newOne = new ChessMove(startpos, upOne, null);
                posMoves.add(newOne);
            }
        }
        //capture up left
        int upLRow = row + 1;
        int upLCol = col - 1;
        if (upLRow < 8 && upLCol > 0) {
            ChessPosition upLeft = new ChessPosition(upLRow, upLCol);
            if (current.getPiece(upLeft) != null && current.getPiece(upLeft).getTeamColor() != color) {
                ChessMove newUpLeft = new ChessMove(startpos, upLeft, null);
                posMoves.add(newUpLeft);
            }
        }
        //capture up right
        int upRRow = row + 1;
        int upRCol = col + 1;
        if (upRRow < 8 && upRCol <= 8) {
            ChessPosition upRight = new ChessPosition(upRRow, upRCol);
            if (current.getPiece(upRight) != null && current.getPiece(upRight).getTeamColor() != color) {
                ChessMove newUpRight = new ChessMove(startpos, upRight, null);
                posMoves.add(newUpRight);
            }
        }
        if (upOneRow == 8) {
            ChessPiece.PieceType[] posPromos = {ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK};
            if (current.getPiece(upOne) == null) {
                for (ChessPiece.PieceType item : posPromos) {
                    ChessMove newOne = new ChessMove(startpos, upOne, item);
                    posMoves.add(newOne);
                }
            }
            //capture up left
            if (upLCol > 0) {
                ChessPosition upLeft = new ChessPosition(upLRow, upLCol);
                if (current.getPiece(upLeft) != null && current.getPiece(upLeft).getTeamColor() != color) {
                    for (ChessPiece.PieceType item : posPromos) {
                        ChessMove newUpLeft = new ChessMove(startpos, upLeft, item);
                        posMoves.add(newUpLeft);
                    }
                }
            }
            //capture up right
            if (upRCol <= 8) {
                ChessPosition upRight = new ChessPosition(upRRow, upRCol);
                if (current.getPiece(upRight) != null && current.getPiece(upRight).getTeamColor() != color) {
                    for (ChessPiece.PieceType item : posPromos) {
                        ChessMove newUpRight = new ChessMove(startpos, upRight, item);
                        posMoves.add(newUpRight);
                    }
                }
            }
        }
        return posMoves;
    }

    private Collection<ChessMove> blackMoves(int row, int col){
        Collection<ChessMove> posMoves = new ArrayList<>();
        //move down
        ChessPosition downOne = new ChessPosition(row - 1, col);
        ChessPosition downTwo = new ChessPosition(row - 2, col);
        if(row == 7){
            if (current.getPiece(downOne) == null) {
                if (current.getPiece(downTwo) == null) {
                    ChessMove newDownTwo = new ChessMove(startpos, downTwo, null);
                    posMoves.add(newDownTwo);
                }
            }
        }
        int downOneRow = row - 1;
        if (downOneRow > 1){
            if (current.getPiece(downOne) == null) {
                ChessMove newDownOne = new ChessMove(startpos, downOne, null);
                posMoves.add(newDownOne);
            }
        }
        //capture down left
        int downLRow = row - 1;
        int downLCol = col - 1;
        if (downLRow > 1 && downLCol > 0){
            ChessPosition downLeft = new ChessPosition(downLRow, downLCol);
            if (current.getPiece(downLeft) != null && current.getPiece(downLeft).getTeamColor() != color){
                ChessMove newDownLeft = new ChessMove(startpos, downLeft, null);
                posMoves.add(newDownLeft);
            }
        }
        //capture down right
        int downRRow = row - 1;
        int downRCol = col + 1;
        if (downRRow > 1 && downRCol <= 8){
            ChessPosition downRight = new ChessPosition(downRRow, downRCol);
            if (current.getPiece(downRight) != null && current.getPiece(downRight).getTeamColor() != color){
                ChessMove newDownRight = new ChessMove(startpos, downRight, null);
                posMoves.add(newDownRight);
            }
        }
        if (downOneRow == 1){
            ChessPiece.PieceType[] posPromos = {ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK};
            if (current.getPiece(downOne) == null) {
                for (ChessPiece.PieceType item : posPromos){
                    ChessMove newOne = new ChessMove(startpos, downOne, item);
                    posMoves.add(newOne);
                }
            }
            //capture down left
            if (downLCol > 0){
                ChessPosition downLeft = new ChessPosition(downLRow, downLCol);
                if (current.getPiece(downLeft) != null && current.getPiece(downLeft).getTeamColor() != color){
                    for (ChessPiece.PieceType item : posPromos) {
                        ChessMove newDownLeft = new ChessMove(startpos, downLeft, item);
                        posMoves.add(newDownLeft);
                    }
                }
            }
            //capture down right
            if (downRCol <= 8){
                ChessPosition downRight = new ChessPosition(downRRow, downRCol);
                if (current.getPiece(downRight) != null && current.getPiece(downRight).getTeamColor() != color){
                    for (ChessPiece.PieceType item : posPromos) {
                        ChessMove newDownRight = new ChessMove(startpos, downRight, item);
                        posMoves.add(newDownRight);
                    }
                }
            }
        }
        return posMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PawnMovesCalculator that = (PawnMovesCalculator) o;
        return Objects.equals(startpos, that.startpos) && Objects.equals(current, that.current) && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startpos, current, color);
    }
}
