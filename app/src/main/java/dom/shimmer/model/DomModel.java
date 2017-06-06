package dom.shimmer.model;

import dom.shimmer.adapter.IDomModel;

/**
 * Created by kevindom on 2/06/17.
 */

public class DomModel implements IDomModel {
    private boolean isOpening;
    private boolean isOpen;

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void toggle() {
        this.isOpen = !this.isOpen;
        this.isOpening = !this.isOpening;
    }

    @Override
    public boolean isOpening() {
        return this.isOpening;
    }
}
