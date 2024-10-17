package assets.impl;

import assets.ICard;
import assets.ICriteriaStrategy;
import assets.IResource;
import exceptions.CardFlippingForbiddenDueToGameLogic;

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
    public ICriteriaStrategy getCriteriaStrategy() {
        return criteriaStrategy;
    }

    @Override
    public IResource getResource() {
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
        return "A card with resource: "
                + resource.represent() + " and criteria: "
                + criteriaStrategy.represent();
    }
}
