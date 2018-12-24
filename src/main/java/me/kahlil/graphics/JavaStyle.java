package me.kahlil.graphics;

import org.immutables.value.Value;

/** Immutables style for traditional java isFoo(), getFoo() and setFoo() method naming patterns. */
@Value.Style(get = {"get*", "is*"}, init = "set*")
public @interface JavaStyle {}
