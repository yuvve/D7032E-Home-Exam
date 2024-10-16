package assets;

import common.IRepresentable;

import java.util.ArrayList;

public interface IPile extends IRepresentable {
    ArrayList<ICard> getCards();
    void shuffle();
    ICard drawTop();
    ICard drawBottom();
    ICriteriaStrategy viewTop();
}
