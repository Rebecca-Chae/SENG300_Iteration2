package testcases;
import org.lsmr.selfcheckout.Item;

//Stub class to cover cases that need the Item class
class ItemToTest extends Item{
	private final double weight;

    /**
     * Constructs an item with the indicated weight.
     *
     * @param weightInGrams The weight of the item.
     * @throws SimulationException If the weight is &le;0.
     */
    protected ItemToTest(double weightInGrams) {
        super(weightInGrams);
        this.weight = weightInGrams;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}
