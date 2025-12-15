package com.ryanbester.shfa.neoforge;

import com.ryanbester.shfa.ConfigScreen;
import com.ryanbester.shfa.SHFAState;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

import java.awt.event.KeyEvent;

@Mod("shfa")
public class SHFANeoForge {
    // キー設定のインスタンス
    public static KeyMapping toggleKey;

    public SHFANeoForge(IEventBus modEventBus, ModContainer modContainer) {
        // 1. 設定ファイルの登録
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC, "shfa-config.toml");

        // 2. 設定画面 (Config Screen) の登録
        // commonにある ConfigScreen を利用し、保存時のコールバックを定義します
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (client, parent) ->
                new ConfigScreen(() -> {
                    // 保存ボタンが押されたときの処理
                    Config.BLOCKS.set(SHFAState.enabledBlocks.stream().toList());
                    Config.SPEC.save();
                })
        );

        // 3. イベントリスナーの登録
        // Modライフサイクル用 (キー登録、設定読み込み)
        modEventBus.addListener(this::registerKeys);
        modEventBus.addListener(this::onLoadConfig);
        modEventBus.addListener(this::onReloadConfig);

        // ゲームプレイ用 (Tickイベントなど)
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
    }

    // --- キー登録 ---
    private void registerKeys(RegisterKeyMappingsEvent event) {
        toggleKey = new KeyMapping(
                "shfa.toggle_shfa",
                KeyEvent.VK_H,
                "shfa.shfa_category"
        );
        event.register(toggleKey);
    }

    // --- 毎フレーム処理 (Fabric版と同じロジック) ---
    private void onClientTick(ClientTickEvent.Post event) {
        // キーが押された瞬間を検知
        while (toggleKey.consumeClick()) {
            SHFAState.toggleSHFA();
        }
    }

    // --- コンフィグ読み込みイベント ---
    private void onLoadConfig(final ModConfigEvent.Loading event) {
        updateConfigState();
    }

    private void onReloadConfig(final ModConfigEvent.Reloading event) {
        updateConfigState();
    }

    private void updateConfigState() {
        // 設定ファイルの内容をメモリ(SHFAState)に反映
        SHFAState.enabledBlocks.clear();
        SHFAState.enabledBlocks.addAll(Config.BLOCKS.get());
    }
}