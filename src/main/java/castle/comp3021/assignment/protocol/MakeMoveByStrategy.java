package castle.comp3021.assignment.protocol;

//import castle.comp3021.assignment.piece.Knight;
//import castle.comp3021.assignment.textversion.SmartPlayerFactory;

import java.util.Arrays;
import java.util.Random;

public class MakeMoveByStrategy {
    private final Strategy strategy;
    private final Game game;
    private final Move[] availableMoves;

    public MakeMoveByStrategy(Game game, Move[] availableMoves, Strategy strategy){
        this.game = game;
        this.availableMoves = availableMoves;
        this.strategy = strategy;
    }

    /**
     * Return next move according to different strategies made by {@link castle.comp3021.assignment.player.ComputerPlayer}
     * You can add helper method if needed, as long as this method returns a next move.
     * - {@link Strategy#RANDOM}: select a random move from the proposed moves by all pieces
     * - {@link Strategy#SMART}: come up with some strategy to select a next move from the proposed moves by all pieces
     *
     * @return a next move
     */
    public Move getNextMove(){
        // TODO-DOING
        if(strategy == Strategy.RANDOM){
            int index = new Random().nextInt(availableMoves.length);
            return availableMoves[index];
        }

        if(strategy == Strategy.SMART){
            Place centralPlace = game.getCentralPlace();
            int cx = centralPlace.x();
            int cy = centralPlace.y();
            int protectedMove = game.getConfiguration().getNumMovesProtection();
            int curNumMove = game.getNumMoves();
            int size = game.getConfiguration().getSize();

            int index = new Random().nextInt(availableMoves.length);
            Move nearestCentral = availableMoves[index];

            Move[] choice = new Move[7];
            Arrays.fill(choice, null);

            for(var m : availableMoves){
                Piece p = game.getPiece(m.getSource());
                int sx = m.getSource().x();
                int sy = m.getSource().y();
                int dx = m.getDestination().x();
                int dy = m.getDestination().y();
/////////////////////////////////////////////////////////////////////////////////////////////////////
                if (p.getLabel() == 'K') {
                    if(game.getPiece(m.getDestination()) == null) {
                        int sourceCentralDistance = Math.abs(sx - cx) + Math.abs(sx - cy);
                        int desCentralDistance = Math.abs(dx - cx) + Math.abs(dy - cy);
                        if(sourceCentralDistance < 3 && sourceCentralDistance != 0) {
                            if (sourceCentralDistance <= desCentralDistance) {
                                continue;//this knight got into greedy dead loop
                            }
                        }
                    }
                }
/////////////////////////////////////////////////////////////////////////////////////////////////////
                if(sx == cx && sy == cy){
                    if(p.getLabel() == 'K'){
                        if(curNumMove > protectedMove){
                            choice[0] = m;//win
                        }else {
                            continue;//don't move this piece
                        }
                    }else {//it is archer
                        //copy from skeleton
                        var steps = new int[]{1, -1, 2, -2};
                        for (var stepX :
                                steps) {
                            for (var stepY :
                                    steps) {
                                var destination = new Place(sx + stepX, sy + stepY);
                                if (Math.abs(destination.x() - sx) + Math.abs(destination.y() - sy) == 3) {
                                    if(game.getPiece(destination) != null) {
                                        if (game.getPiece(destination).getLabel() == 'K' &&
                                                game.getPiece(destination).getPlayer().equals(game.getCurrentPlayer())){
                                            //this archer is blocking your knight
                                            if(curNumMove > protectedMove){
                                                choice[1] = m;//move this archer
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
/////////////////////////////////////////////////////////////////////////////////////////////////////
                if(dx == cx && dy == cy && p.getLabel() == 'K'){//if this knight can go to central
                    choice[2] = m;
                }
/////////////////////////////////////////////////////////////////////////////////////////////////////
                if(game.getPiece(m.getDestination()) != null && dx == cx && dy == cy){
                    if(game.getPiece(m.getDestination()).getLabel() == 'K'){
                        //if next player can win and you can eat that piece
                        choice[3] = m;
                    }
                }
/////////////////////////////////////////////////////////////////////////////////////////////////////
                var steps = new int[]{1, -1, 2, -2};
                for (var stepX :
                        steps) {
                    for (var stepY :
                            steps) {
                        var loc = new Place(cx + stepX, cy + stepY);
                        if (Math.abs(loc.x() - cx) + Math.abs(loc.y() - cy) == 3) {
                            if(p.getLabel() == 'K' && dx == loc.x() && dy == loc.y()){
                                //if your knight can move to the point that close to central
                                choice[4] = m;
                            }
                        }
                    }
                }
/////////////////////////////////////////////////////////////////////////////////////////////////////
                if(game.getPiece(m.getDestination()) != null){
                    if(game.getPiece(m.getDestination()).getLabel() == 'A'){
                        //if you can eat a archer
                        choice[5] = m;
                    }else{
                        //if you can eat a knight
                        choice[6] = m;
                    }
                }
/////////////////////////////////////////////////////////////////////////////////////////////////////
                if(p.getLabel() == 'K') {
                    int nearestCentralDistance = Math.abs(nearestCentral.getDestination().x() - cx) +
                            Math.abs(nearestCentral.getDestination().y() - cy);
                    int currentMoveDistance = Math.abs(m.getDestination().x() - cx) +
                            Math.abs(m.getDestination().y() - cy);

                    if (currentMoveDistance < nearestCentralDistance) {
                        nearestCentral = m;//get the nearest central move
                    }
                }
            }

            for(int i=0;i<choice.length;i++){//return the best choice
                if(choice[i] != null){
                    return choice[i];
                }
            }

            return nearestCentral;//no chosen move then choose near central, no knight then random
        }

        //never reach
        return null;
    }
}
