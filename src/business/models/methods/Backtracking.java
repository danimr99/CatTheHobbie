package business.models.methods;

public abstract class Backtracking {
    public abstract void run();
    protected abstract void prepare(int[] x, int k);
    protected abstract boolean existsSuccessor(int[] x, int k);
    protected abstract void existsNext(int[] x, int k);
    protected abstract boolean solution(int[] x, int k);
    protected abstract boolean completable(int[] x, int k);
    protected abstract boolean feasible(int[] x);
    protected abstract void treatSolution(int[] x);
}
