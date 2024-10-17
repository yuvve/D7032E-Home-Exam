package assets.impl;

import assets.ICard;
import assets.ICriteriaStrategy;
import assets.IResource;
import exceptions.CardFlippingForbiddenDueToGameLogic;
import exceptions.CardSidePeekingForbiddenDueToGameLogic;

public class PointSaladCard implements ICard {

    private final ICriteriaStrategy criteriaStrategy;
    private final IResource resource;
    private boolean criteriaSideActive;

    public PointSaladCard(ICriteriaStrategy criteriaStrategy, IResource resource){
        this.criteriaStrategy = criteriaStrategy;
        this.resource = resource;
        this.criteriaSideActive = false;
    }

    @Override
    public ICriteriaStrategy getCriteriaStrategy() throws CardSidePeekingForbiddenDueToGameLogic {
        if (!criteriaSideActive){
            throw new CardSidePeekingForbiddenDueToGameLogic("Criteria side is not active");
        }
        return criteriaStrategy;
    }

    @Override
    public IResource getResource() throws CardSidePeekingForbiddenDueToGameLogic{
        return resource;
    }

    @Override
    public boolean isCriteriaSideActive() {
        return criteriaSideActive;
    }

    @Override
    public boolean canFlip(){
        return criteriaSideActive;
    }

    @Override
    public void flip() throws CardFlippingForbiddenDueToGameLogic {
        if (!canFlip()){
            throw new CardFlippingForbiddenDueToGameLogic("Card cannot be flipped");
        }
        criteriaSideActive = !criteriaSideActive;
    }

    @Override
    public String represent() {
        if (criteriaSideActive){
            return "A card with criteria: "
                    + criteriaStrategy.represent() + " and resource: "
                    + resource.represent();
        }
        return "A card with resource: " + resource.represent();
    }
}
