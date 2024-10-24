package assets.impl;

import assets.ICard;
import assets.ICriteriaStrategy;
import assets.IResource;
import exceptions.CardFlippingException;
import exceptions.CardFlippingException;

public class PointSaladCard implements ICard {

    private final ICriteriaStrategy criteriaStrategy;
    private final IResource resource;
    private boolean criteriaSideActive;

    public PointSaladCard(ICriteriaStrategy criteriaStrategy, IResource resource){
        this.criteriaStrategy = criteriaStrategy;
        this.resource = resource;
        this.criteriaSideActive = true;
    }

    @Override
    public ICriteriaStrategy getCriteriaStrategy() throws CardFlippingException {
        if (!criteriaSideActive){
            throw new CardFlippingException("Criteria side is not active");
        }
        return criteriaStrategy;
    }

    @Override
    public IResource getResource() throws CardFlippingException {
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
    public void flip() throws CardFlippingException {
        if (!canFlip()){
            throw new CardFlippingException("Card cannot be flipped");
        }
        criteriaSideActive = !criteriaSideActive;
    }

    @Override
    public String represent() {
        if (criteriaSideActive){
            return
                    "Criteria: " + criteriaStrategy.represent() + "\n"
                    + "Resource: " + resource.represent();
        }
        return resource.represent();
    }
}
