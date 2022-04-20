package ma.cdgk.integration.camel.mapping;

public interface EventMapping<T, R> {
    R map(T t);
}
