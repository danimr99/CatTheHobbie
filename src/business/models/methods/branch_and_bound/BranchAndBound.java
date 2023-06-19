package business.models.methods.branch_and_bound;

public abstract class BranchAndBound {
    public abstract void run();
    protected abstract CommonConfiguration configureRoot();
    protected abstract CommonConfiguration[] expand(CommonConfiguration configuration);
    protected abstract boolean solution(CommonConfiguration configuration);
    protected abstract boolean completable(CommonConfiguration configuration);
    protected abstract boolean feasible(CommonConfiguration configuration);
    protected abstract double value(CommonConfiguration configuration);
    protected abstract double partialValue(CommonConfiguration configuration);
    protected abstract double estimatedValue(CommonConfiguration configuration);
}
