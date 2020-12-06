package castle.comp3021.assignment.protocol;

import castle.comp3021.assignment.piece.Knight;
import castle.comp3021.assignment.textversion.SmartPlayerFactory;

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
            return availableMoves[0];//this is wong but don't know how to do now
        }

        return null;
    }
}
