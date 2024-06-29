package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator {
    private ChessPosition startpos;
    private ChessBoard current;
    private ChessGame.TeamColor color;
    public QueenMovesCalculator(ChessPosition pos, ChessBoard currBoard, ChessGame.TeamColor color){
        this.startpos = pos;
        this.current = currBoard;
        this.color = color;
    }

    public Collection<ChessMove> possibleMoves() {
        Collection<ChessMove> posMoves = new ArrayList<>();
        int row = startpos.getRow();
        int col = startpos.getColumn();
        //up left
        int UpLRow = row + 1;
        int UpLCol = col - 1;
        while (UpLRow <= 8 && UpLCol > 0){
            ChessPosition upLeft = new ChessPosition(UpLRow, UpLCol);
            if (current.getPiece(upLeft) == null){
                ChessMove newUpLeft = new ChessMove(startpos, upLeft, null);
                posMoves.add(newUpLeft);
                UpLRow++;
                UpLCol--;
            }
            else if(current.getPiece(upLeft).getTeamColor() != color){
                ChessMove newUpLeft = new ChessMove(startpos, upLeft, null);
                posMoves.add(newUpLeft);
                break;
            }
            else{
                break;
            }
        }
        //up right
        int UpRRow = row + 1;
        int UpRCol = col + 1;
        while (UpRRow <= 8 && UpRCol <= 8){
            ChessPosition upRight = new ChessPosition(UpRRow, UpRCol);
            if (current.getPiece(upRight) == null){
                ChessMove newUpRight = new ChessMove(startpos, upRight, null);
                posMoves.add(newUpRight);
                UpRRow++;
                UpRCol++;
            }
            else if(current.getPiece(upRight).getTeamColor() != color){
                ChessMove newUpRight = new ChessMove(startpos, upRight, null);
                posMoves.add(newUpRight);
                break;
            }
            else{
                break;
            }
        }
        //down left
        int DownLRow = row - 1;
        int DownLCol = col - 1;
        while (DownLRow > 0 && DownLCol > 0){
            ChessPosition downLeft = new ChessPosition(DownLRow, DownLCol);
            if (current.getPiece(downLeft) == null){
                ChessMove newDownLeft = new ChessMove(startpos, downLeft, null);
                posMoves.add(newDownLeft);
                DownLRow--;
                DownLCol--;
            }
            else if(current.getPiece(downLeft).getTeamColor() != color){
                ChessMove newDownLeft = new ChessMove(startpos, downLeft, null);
                posMoves.add(newDownLeft);
                break;
            }
            else{
                break;
            }
        }
        //down right
        int DownRRow = row - 1;
        int DownRCol = col + 1;
        while (DownRRow > 0 && DownRCol <= 8){
            ChessPosition downRight = new ChessPosition(DownRRow, DownRCol);
            if (current.getPiece(downRight) == null){
                ChessMove newDownRight = new ChessMove(startpos, downRight, null);
                posMoves.add(newDownRight);
                DownRRow--;
                DownRCol++;
            }
            else if(current.getPiece(downRight).getTeamColor() != color){
                ChessMove newDownRight = new ChessMove(startpos, downRight, null);
                posMoves.add(newDownRight);
                break;
            }
            else{
                break;
            }
        }
        //going up
        for (int i = row + 1; i <= 8; i++){
            ChessPosition up = new ChessPosition(i, col);
            if (current.getPiece(up) == null){
                ChessMove newUp = new ChessMove(startpos, up, null);
                posMoves.add(newUp);
            }
            else if(current.getPiece(up).getTeamColor() != color){
                ChessMove newUp = new ChessMove(startpos, up, null);
                posMoves.add(newUp);
                break;
            }
            else{
                break;
            }
        }
        //going down
        for (int i = row - 1; i > 0; i--){
            ChessPosition down = new ChessPosition(i, col);
            if (current.getPiece(down) == null){
                ChessMove newDown = new ChessMove(startpos, down, null);
                posMoves.add(newDown);
            }
            else if(current.getPiece(down).getTeamColor() != color){
                ChessMove newDown = new ChessMove(startpos, down, null);
                posMoves.add(newDown);
                break;
            }
            else{
                break;
            }
        }
        //going left
        for (int i = col - 1; i > 0; i--){
            ChessPosition left = new ChessPosition(row, i);
            if (current.getPiece(left) == null){
                ChessMove newLeft = new ChessMove(startpos, left, null);
                posMoves.add(newLeft);
            }
            else if(current.getPiece(left).getTeamColor() != color){
                ChessMove newLeft = new ChessMove(startpos, left, null);
                posMoves.add(newLeft);
                break;
            }
            else{
                break;
            }
        }
        //going right
        for (int i = col + 1; i <= 8; i++){
            ChessPosition right = new ChessPosition(row, i);
            if (current.getPiece(right) == null){
                ChessMove newRight = new ChessMove(startpos, right, null);
                posMoves.add(newRight);
            }
            else if(current.getPiece(right).getTeamColor() != color){
                ChessMove newRight = new ChessMove(startpos, right, null);
                posMoves.add(newRight);
                break;
            }
            else{
                break;
            }
        }
        return posMoves;
    }
}
