package io.broadcast.engine.record;

import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Function;

/**
 * Represents a generic record that contains an identifier and an associated entity.
 *
 * <p>The {@code Record} class is designed to work with various types of identifiers and entities,
 * offering static factory methods to easily create instances for specific identifier types such as
 * {@link Integer}, {@link Long}, {@link String}, and {@link UUID}.</p>
 *
 * @param <I> The type of the identifier.
 * @param <T> The type of the entity.
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Record<I, T> {

    /**
     * Creates a new {@code Record} instance with the specified entity and identifier extractor.
     *
     * @param entity   The entity to associate with the record. Must not be {@code null}.
     * @param idGetter A function that extracts the identifier from the entity. Must not be {@code null}.
     * @param <I>      The type of the identifier.
     * @param <T>      The type of the entity.
     * @return A new {@code Record} instance containing the extracted identifier and the entity.
     * @throws NullPointerException if {@code entity} or {@code idGetter} is {@code null}.
     */
    @Contract("_, _ -> new")
    public static <I, T> @NotNull Record<I, T> of(@NotNull T entity, @NotNull Function<T, I> idGetter) {
        return new Record<>(idGetter.apply(entity), entity);
    }

    /**
     * Creates a new {@code Record} instance with an {@link Integer} identifier.
     *
     * @param entity   The entity to associate with the record. Must not be {@code null}.
     * @param idGetter A function that extracts the {@link Integer} identifier from the entity.
     * @param <T>      The type of the entity.
     * @return A new {@code Record} instance containing the extracted {@link Integer} identifier and the entity.
     * @throws NullPointerException if {@code entity} or {@code idGetter} is {@code null}.
     */
    @Contract("_, _ -> new")
    public static <T> @NotNull Record<Integer, T> ofInt(@NotNull T entity, @NotNull Function<T, Integer> idGetter) {
        return of(entity, idGetter);
    }

    /**
     * Creates a new {@code Record} instance with a {@link Long} identifier.
     *
     * @param entity   The entity to associate with the record. Must not be {@code null}.
     * @param idGetter A function that extracts the {@link Long} identifier from the entity.
     * @param <T>      The type of the entity.
     * @return A new {@code Record} instance containing the extracted {@link Long} identifier and the entity.
     * @throws NullPointerException if {@code entity} or {@code idGetter} is {@code null}.
     */
    @Contract("_, _ -> new")
    public static <T> @NotNull Record<Long, T> ofLong(@NotNull T entity, @NotNull Function<T, Long> idGetter) {
        return of(entity, idGetter);
    }

    /**
     * Creates a new {@code Record} instance with a {@link String} identifier.
     *
     * @param entity   The entity to associate with the record. Must not be {@code null}.
     * @param idGetter A function that extracts the {@link String} identifier from the entity.
     * @param <T>      The type of the entity.
     * @return A new {@code Record} instance containing the extracted {@link String} identifier and the entity.
     * @throws NullPointerException if {@code entity} or {@code idGetter} is {@code null}.
     */
    @Contract("_, _ -> new")
    public static <T> @NotNull Record<String, T> ofString(@NotNull T entity, @NotNull Function<T, String> idGetter) {
        return of(entity, idGetter);
    }

    /**
     * Creates a new {@code Record} instance with a {@link UUID} identifier.
     *
     * @param entity   The entity to associate with the record. Must not be {@code null}.
     * @param idGetter A function that extracts the {@link UUID} identifier from the entity.
     * @param <T>      The type of the entity.
     * @return A new {@code Record} instance containing the extracted {@link UUID} identifier and the entity.
     * @throws NullPointerException if {@code entity} or {@code idGetter} is {@code null}.
     */
    @Contract("_, _ -> new")
    public static <T> @NotNull Record<UUID, T> ofUUID(@NotNull T entity, @NotNull Function<T, UUID> idGetter) {
        return of(entity, idGetter);
    }

    /**
     * The identifier of the record, uniquely representing the associated entity.
     */
    private final I id;

    /**
     * The entity associated with the record.
     */
    private final T entity;
}
