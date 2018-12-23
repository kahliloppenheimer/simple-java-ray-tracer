package me.kahlil.graphics;

import org.immutables.value.Value;

@Value.Style(get = {"get*", "is*"}, init = "set*")
public @interface JavaStyle {}
