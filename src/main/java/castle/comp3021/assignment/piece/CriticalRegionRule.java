package castle.comp3021.assignment.piece;

//import castle.comp3021.assignment.protocol.Configuration;
import castle.comp3021.assignment.protocol.Game;
import castle.comp3021.assignment.protocol.Move;
import castle.comp3021.assignment.protocol.Place;

public class CriticalRegionRule implements Rule {


    /**
     * Validate whether the proposed  move will violate the critical region rule
     * I.e., there are no more than {@link Configuration#getCriticalRegionCapacity()} in the critical region.
     * Determine whether the move is in critical region, using {@link this#isInCriticalRegion(Game, Place)}
     * @param game the current game object
     * @param move the move to be validated
     * @return whether the given move is valid or not
     */
    @Override
    public boolean validate(Game game, Move move) {
        //TODO-DONE
        //The capacity refers to the max number of Knights of each player that are allowed in the critical region.
        if(!isInCriticalRegion(game, move.getDestination())){
            return true;
        }

        int numOfKnights = 0;
        for(int i=0;i<game.getConfiguration().getSize();i++){
            for(int j=0;j<game.getConfiguration().getSize();j++){
                if(isInCriticalRegion(game, new Place(i, j))) {
                    if (game.getBoard()[i][j] != null) {
                        if (game.getBoard()[i][j].getPlayer().equals(game.getCurrentPlayer()) &&
                                game.getBoard()[i][j].getLabel() == 'K') {
                            numOfKnights++;
                        }
                    }
                }
            }
        }

        if(game.getBoard()[move.getSource().x()][move.getSource().y()].getLabel() == 'K'){
            numOfKnights++;
        }

        if(numOfKnights > game.getConfiguration().getCriticalRegionCapacity()){
            return false;
        }
        return true;
    }

    /**
     * Check whether the given move is in critical region
     * Critical region is {@link Configuration#getCriticalRegionSize()} of rows, centered around center place
     * Example:
     *      In a 5 * 5 board, which center place lies in the 3rd row
     *      Suppose critical region size = 3, then for row 1-5, the critical region include row 2-4.
     * @param game the current game object
     * @param place the move to be validated
     * @return whether the given move is in critical region
     */
    private boolean isInCriticalRegion(Game game, Place place) {
        //TODO-DONE
        int cSize = game.getConfiguration().getCriticalRegionSize();
        int centralY = game.getCentralPlace().y();
        int y = place.y();
        if(y >= centralY - (cSize-1)/2 && y <= centralY + (cSize-1)/2){
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "critical region is full";
    }
}
