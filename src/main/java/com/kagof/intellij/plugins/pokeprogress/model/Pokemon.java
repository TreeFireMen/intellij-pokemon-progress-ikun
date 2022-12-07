package com.kagof.intellij.plugins.pokeprogress.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.util.text.StringUtil;

public enum Pokemon {
    // Gen I
    CAICAI(913, "caicai", -16, 0, 27, PokemonType.WATER),
    XUXU(914,  "xuxu", -24, 0, 27, PokemonType.GRASS),
    KUNKUN(915,  "kunkun", -16, 0, 27, PokemonType.DRAGON),
    // Secret
    MISSINGNO(-1, "missingNo.", -20, 0, 35, true, null, null, PokemonType.NORMAL);

    public static final Map<String, Pokemon> DEFAULT_POKEMON = Arrays.stream(values())
        .filter(p -> !p.secret)
        .collect(ImmutableMap.toImmutableMap(Pokemon::getId, Function.identity(), (u, v) -> {
            throw new IllegalStateException(String.format("Duplicate Pokemon ID %s", u));
        }));

    private final List<PokemonType> types;

    private final String name;
    private final int number;
    private final String id;

    private final int xShift;
    private final int yShift;
    private final int height;
    private final boolean secret;
    private final Generation generation;

    public static Pokemon getById(final String id) {
        return DEFAULT_POKEMON.get(id);
    }

    Pokemon(final int number, final String name, final int xShift, final int yShift, final int height,
        final PokemonType... types) {
        this(number, name, xShift, yShift, height, null, types);
    }

    Pokemon(final int number, final String name, final int xShift, final int yShift, final int height,
        final RegionalVariant regionalVariant, final PokemonType... types) {
        this(number,
            name,
            xShift,
            yShift,
            height,
            false,
            Optional.ofNullable(regionalVariant).map(Enum::toString).orElse(null),
            Optional.ofNullable(regionalVariant).map(RegionalVariant::getGeneration).orElse(null),
            types);
    }

    Pokemon(final int number, final String name, final int xShift, final int yShift, final int height,
        final boolean secret, final String idModifier, final Generation gen, final PokemonType... types) {
        if (types == null || types.length < 1) {
            throw new IllegalArgumentException("configuration for " + name + " invalid");
        }

        this.types = ImmutableList.copyOf(types);
        this.xShift = xShift;
        this.yShift = yShift;
        this.height = height;
        this.name = name;
        this.number = number;
        id = getNumberString() + Optional.ofNullable(idModifier).map(m -> "_" + m).orElse("");

        this.secret = secret;
        generation = Optional.ofNullable(gen).orElseGet(() -> Generation.getGeneration(number));
    }

    public List<PokemonType> getTypes() {
        return types;
    }

    public int getXShift() {
        return xShift;
    }

    public int getYShift() {
        return yShift;
    }

    public int getHeight() {
        return height;
    }

    public boolean isSecret() {
        return secret;
    }

    public Generation getGeneration() {
        return generation;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getNumberString() {
        return number > 0 ? String.format("%03d", number) : "???";
    }

    public String getId() {
        return id;
    }

    public String getNameWithNumber() {
        return StringUtil.capitalizeWords(name, true) + " (#" + getNumberString() + ")";
    }

    @Override
    public String toString() {
        return getNameWithNumber();
    }

}
