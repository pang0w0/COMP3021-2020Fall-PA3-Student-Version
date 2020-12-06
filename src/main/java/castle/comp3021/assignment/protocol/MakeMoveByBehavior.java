package castle.comp3021.assignment.protocol;

import castle.comp3021.assignment.piece.Knight;

import java.util.ArrayList;
import java.util.Random;

public class MakeMoveByBehavior {
    private final Behavior behavior;
    private final Game game;
    private final Move[] availableMoves;

    public MakeMoveByBehavior(Game game, Move[] availableMoves, Behavior behavior){
        this.game = game;
        this.availableMoves = availableMoves;
        this.behavior = behavior;
    }

    /**
     * Return next move according to different strategies made by each piece.
     * You can add helper method if needed, as long as this method returns a next move.
     * - {@link Behavior#RANDOM}: return a random move from {@link this#availableMoves}
     * - {@link Behavior#GREEDY}: prefer the moves towards central place, the closer, the better
     * - {@link Behavior#CAPTURING}: prefer the moves that captures the enemies, killing the more, the better.
     *                               when there are many pieces that can captures, randomly select one of them
     * - {@link Behavior#BLOCKING}: prefer the moves that block enemy's {@link Knight}.
     *                              See how to block a knight here: https://en.wikipedia.org/wiki/Xiangqi (see `Horse`)
     *
     * @return a selected move adopting strategy specified by {@link this#behavior}
     */
    public Move getNextMove(){
        // TODO-DONE?

        if(behavior == Behavior.RANDOM){
            int index = new Random().nextInt(availableMoves.length);
            return availableMoves[index];
        }




        if(behavior == Behavior.GREEDY) {
            Move nearestCentral = availableMoves[0];
            Place central = game.getCentralPlace();

            for (var m : availableMoves) {
                if (m.getDestination().equals(central)) {
                    return m;
                }
                int nearestCentralDistance = Math.abs(nearestCentral.getDestination().x() - central.x()) +
                        Math.abs(nearestCentral.getDestination().y() - central.y());
                int currentMoveDistance = Math.abs(m.getDestination().x() - central.x()) +
                        Math.abs(m.getDestination().y() - central.y());

                if (currentMoveDistance < nearestCentralDistance) {
                    nearestCentral = m;//get the nearest central move
                }
            }
            return nearestCentral;
        }




        if(behavior == Behavior.CAPTURING){
            var capMoves = new ArrayList<Move>();
            for(var m : availableMoves){
                Piece p = game.getBoard()[m.getDestination().x()][m.getDestination().y()];
                if(p != null){
                    if(!p.getPlayer().equals(game.getCurrentPlayer())){
                        capMoves.add(m);
                    }
                }
            }

            if(capMoves.size() == 0){//no move then random
                int index = new Random().nextInt(availableMoves.length);
                return availableMoves[index];
            }

            int index = new Random().nextInt(capMoves.size());
            return availableMoves[index];
        }




        if(behavior == Behavior.BLOCKING){
            Move bestBlock = availableMoves[0];
            int maxNumOfBocking = 0;
            for(var m : availableMoves){
                int numOfBocking = 0;
                int x = m.getDestination().x();
                int y = m.getDestination().y();
                Piece pTop = game.getBoard()[x][y+1];
                Piece pDown = game.getBoard()[x][y-1];
                Piece pLeft = game.getBoard()[x-1][y];
                Piece pRight = game.getBoard()[x+1][y];

                if(pTop != null && pTop.getLabel() == 'K'){
                    numOfBocking++;
                }
                if(pDown != null && pDown.getLabel() == 'K'){
                    numOfBocking++;
                }
                if(pLeft != null && pLeft.getLabel() == 'K'){
                    numOfBocking++;
                }
                if(pRight != null && pRight.getLabel() == 'K'){
                    numOfBocking++;
                }

                if(numOfBocking > maxNumOfBocking){
                    bestBlock = m;
                    maxNumOfBocking = numOfBocking;
                }
            }

            return bestBlock;
        }

        return null;
    }
}

