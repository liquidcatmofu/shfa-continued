package com.ryanbester.shfa.neoforge;

import com.ryanbester.shfa.SHFAState;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<List<String>> BLOCKS;

    static {
        BLOCKS = BUILDER
                .comment("List of blocks that are included with SHFA")
                .define("Blocks", new java.util.ArrayList<>(SHFAState.enabledBlocksDefault));
        SPEC = BUILDER.build();
    }
}