package fuzzyset;

public class GaussianFuzzySet extends FuzzySet {
    private double m, sigma;

    public GaussianFuzzySet(double m, double sigma) {
        this.m = m;
        this.sigma = sigma;
    }

    @Override
    public double getMembership(double x) {
        return Math.exp(-Math.pow(x - m, 2) / (2 * Math.pow(sigma, 2)));
    }
}
