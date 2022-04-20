package ma.cdgk.integration.normalizer;

public interface EventNormalizer<T, R> {

    R normalize(T event);
}
