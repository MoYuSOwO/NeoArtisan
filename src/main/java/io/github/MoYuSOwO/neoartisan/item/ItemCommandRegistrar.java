package io.github.moyusowo.neoartisan.item;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.ComponentLike;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")
final class ItemCommandRegistrar {
    private static final SuggestionProvider<CommandSourceStack> REGISTRY_ID_SUGGESTIONS =
            (ctx, builder) -> {
                String currentInput = builder.getRemaining().toLowerCase();
                ItemRegistryImpl.getInstance().getAllIds().stream()
                        .filter(id -> id.toLowerCase().startsWith(currentInput))
                        .forEach(builder::suggest);
                return builder.buildFuture();
            };

    private static final LiteralArgumentBuilder<CommandSourceStack> command =
            Commands.literal("item").requires(ctx -> ctx.getSender().isOp()).then(
                    Commands.literal("get").then(
                            Commands.argument("registryId", ArgumentTypes.namespacedKey())
                                    .suggests(REGISTRY_ID_SUGGESTIONS)
                                    .executes(
                                            ctx -> {
                                                if (ctx.getSource().getSender() instanceof Player player) {
                                                    NamespacedKey registryId = ctx.getArgument("registryId", NamespacedKey.class);
                                                    player.give(((ArtisanItemImpl) ItemRegistryImpl.getInstance().getArtisanItem(registryId)).getItemStack());
                                                    return 1;
                                                }
                                                ctx.getSource().getSender().sendMessage(
                                                        (ComponentLike) Component.literal("你必须是一名玩家！").withStyle(ChatFormatting.RED)
                                                );
                                                return 0;
                                            }
                                    )
                    ).then(
                            Commands.argument("registryId", ArgumentTypes.namespacedKey()).then(
                                    Commands.argument("count", IntegerArgumentType.integer(1))
                                            .executes(
                                                    ctx -> {
                                                        if (ctx.getSource().getSender() instanceof Player player) {
                                                            NamespacedKey registryId = ctx.getArgument("registryId", NamespacedKey.class);
                                                            int count = IntegerArgumentType.getInteger(ctx, "count");
                                                            player.give(((ArtisanItemImpl) ItemRegistryImpl.getInstance().getArtisanItem(registryId)).getItemStack(count));
                                                            return 1;
                                                        }
                                                        ctx.getSource().getSender().sendMessage(
                                                                (ComponentLike) Component.literal("你必须是一名玩家！").withStyle(ChatFormatting.RED)
                                                        );
                                                        return 0;
                                                    }
                                            )
                            )
                    )
            );
    private static final LiteralCommandNode<CommandSourceStack> buildCommand = command.build();

    private ItemCommandRegistrar() {}

    public static void registerCommands() {
        NeoArtisan.instance().getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> commands.registrar().register(buildCommand));
    }
}
