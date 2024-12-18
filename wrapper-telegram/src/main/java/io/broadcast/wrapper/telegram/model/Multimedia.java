package io.broadcast.wrapper.telegram.model;

import io.broadcast.wrapper.telegram.model.media.Media;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Multimedia<M extends Media> {

    @Contract("_ -> new")
    @SafeVarargs
    public static <M extends Media> @NotNull Multimedia<M> of(M... medias) {
        return new Multimedia<>(new ArrayList<>(Arrays.asList(medias)));
    }

    @Contract("_ -> new")
    public static <M extends Media> @NotNull Multimedia<M> of(Collection<M> medias) {
        return new Multimedia<>(new ArrayList<>(medias));
    }

    private final List<M> mediaList;

    public Multimedia<M> and(M media) {
        mediaList.add(media);
        return this;
    }

    public <T> List<T> mapToList(Function<M, T> mapping) {
        return mediaList.stream().map(mapping).collect(Collectors.toList());
    }

    public boolean hasContents() {
        return mediaList != null && !mediaList.isEmpty();
    }
}
