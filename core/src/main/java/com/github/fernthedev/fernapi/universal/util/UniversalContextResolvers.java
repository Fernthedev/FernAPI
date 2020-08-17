package com.github.fernthedev.fernapi.universal.util;

import co.aikar.commands.*;
import co.aikar.commands.contexts.ContextResolver;
import co.aikar.commands.contexts.IssuerAwareContextResolver;
import co.aikar.commands.contexts.IssuerOnlyContextResolver;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.api.OfflineFPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UniversalContextResolvers {



    public static class FernCommandIssuerContextResolver implements IssuerOnlyContextResolver<FernCommandIssuer, CommandExecutionContext<?, ?>>, ContextResolver<FernCommandIssuer, CommandExecutionContext<?, ?>> {
        /**
         * Parses the context of type {@link C} into {@link T}, or throws an exception.
         *
         * @param commandExecutionContext The context to parse from.
         * @return The parsed instance of the wanted type.
         * @throws InvalidCommandArgument In case the context contains any discrepancies, it will throw this exception.
         */
        @Override
        public FernCommandIssuer getContext(CommandExecutionContext<?, ?> commandExecutionContext) throws InvalidCommandArgument {
            return Universal.getMethods().convertCommandSenderToAPISender(commandExecutionContext.getIssuer().getIssuer());
        }
    }

    public static class IFConsoleIssuerContextResolver implements IssuerOnlyContextResolver<IFConsole<?>, CommandExecutionContext<?, ?>>,ContextResolver<IFConsole<?>, CommandExecutionContext<?, ?>> {
        /**
         * Parses the context of type {@link C} into {@link T}, or throws an exception.
         *
         * @param commandExecutionContext The context to parse from.
         * @return The parsed instance of the wanted type.
         * @throws InvalidCommandArgument In case the context contains any discrepancies, it will throw this exception.
         */
        @Override
        public IFConsole<?> getContext(CommandExecutionContext<?, ?> commandExecutionContext) throws InvalidCommandArgument {
            return Universal.getMethods().convertConsoleToAPISender(commandExecutionContext.getIssuer().getIssuer());
        }
    }


    public static class SenderIFPlayerContextResolver implements IssuerAwareContextResolver<IFPlayer<?>, CommandExecutionContext<?, ?>> {
        /**
         * Parses the context of type {@link C} into {@link T}, or throws an exception.
         *
         * @param c The context to parse from.
         * @return The parsed instance of the wanted type.
         * @throws InvalidCommandArgument In case the context contains any discrepancies, it will throw this exception.
         */
        @Override
        public IFPlayer<?> getContext(CommandExecutionContext<?, ?> c) throws InvalidCommandArgument {
            boolean isOptional = c.isOptional();
            CommandIssuer sender = c.getIssuer();

            UUID uuid;

            Universal.debug("Flags for parsing player: " + c.getFlags());

            String name = null;

            if(c.hasFlag("other")) {
                String arg = c.popFirstArg();

                Universal.debug("The arg is " + arg);

                if (arg == null || arg.isEmpty()) {

                    if (c.hasFlag("defaultself")) {
                        Universal.debug("Default self is on");

                        if (!sender.isPlayer() && !isOptional)
                            throw new InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE);

                        if (!sender.isPlayer() && isOptional) return null;

                        uuid = sender.getUniqueId();
                    } else {
                        if (isOptional) {
                            return null;
                        } else {
                            throw new InvalidCommandArgument("Must specify player.");
                        }
                    }

                } else {
                    Universal.debug("Checking player arg " + arg);
                    if (c.hasFlag("uuid")) {
                        Universal.debug("Checking player uuid arg " + arg);
                        uuid = UUID.fromString(Objects.requireNonNull(arg));
                    } else {
                        Universal.debug("Checking player name arg " + arg);
                        uuid = UUIDFetcher.getUUID(arg);
                    }
                }
                name = arg;
            } else {
                if (!sender.isPlayer() && !isOptional)
                    throw new InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE);

                uuid = sender.getUniqueId();
            }

            if (uuid == null) {
                if (isOptional) {
                    return null;
                } else {
                    throw new InvalidCommandArgument("Unable to find player", false);
                }
            }

            OfflineFPlayer<?> player = Universal.getMethods().getPlayerFromUUID(uuid);

            if (player == null) throw new NullPointerException();

            if (player.getName() == null && name != null) player.setName(name);

            IFPlayer<?> player1 = player.getPlayer();

            if (player1 == null) {
                if (isOptional || c.hasFlag("offline")) {
                    return player;
                } else throw new InvalidCommandArgument("Unable to find player " + player.getName(), false);
            }

            return player.getPlayer();

//
//            boolean isPlayerSender = sender.isPlayer();
//            if (!c.hasFlag("other")) {
//                IFPlayer<?> player = isPlayerSender ? (IFPlayer<?>) sender : null;
//                if (player == null && !isOptional) {
//                    throw new InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE, false);
//                }
//                return player;
//            } else {
//                String arg = c.popFirstArg();
//                if (arg == null && isOptional) {
//                    if (c.hasFlag("defaultself")) {
//                        if (isPlayerSender) {
//                            return (IFPlayer<?>) sender;
//                        } else {
//                            throw new InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE, false);
//                        }
//                    } else {
//                        return null;
//                    }
//                } else if (arg == null) {
//                    throw new InvalidCommandArgument();
//                }
//
//                IFPlayer<?> player = getOnlinePlayer(c.getIssuer(), arg, isOptional);
//
//                return player.isNull() ? null : player;
//            }

        }
    }


    public static class OnlineIFPlayerArrayCommandResolver implements ContextResolver<IFPlayer<?>[], CommandExecutionContext<?, ?>> {


        /**
         * Parses the context of type {@link C} into {@link T}, or throws an exception.
         *
         * @param c The context to parse from.
         * @return The parsed instance of the wanted type.
         * @throws InvalidCommandArgument In case the context contains any discrepancies, it will throw this exception.
         */
        @Override
        public IFPlayer<?>[] getContext(CommandExecutionContext<?, ?> c) throws InvalidCommandArgument {

            CommandIssuer issuer = c.getIssuer();
            final String search = c.popFirstArg();
            boolean allowMissing = c.hasFlag("allowmissing");
            Set<IFPlayer<?>> players = new HashSet<>();
            Pattern split = COMMA;
            String splitter = c.getFlagValue("splitter", (String) null);
            if (splitter != null) {
                split = Pattern.compile(Pattern.quote(splitter));
            }
            for (String lookup : split.split(search)) {
                IFPlayer<?> player = getOnlinePlayer(issuer, lookup, allowMissing);
                if (player != null) {
                    players.add(player);
                }
            }
            if (players.isEmpty() && !c.hasFlag("allowempty")) {
                issuer.sendError(UniversalMinecraftMessageKeys.NO_PLAYER_FOUND_SERVER,
                        "{search}", search);

                throw new InvalidCommandArgument(false);
            }
            return players.toArray(new IFPlayer[0]);

        }
    }


    public static class SingularIFPlayerContextResolver implements ContextResolver<OfflineFPlayer<?>, CommandExecutionContext<?, ?>> {
        /**
         * Parses the context of type {@link C} into {@link T}, or throws an exception.
         *
         * @param c The context to parse from.
         * @return The parsed instance of the wanted type.
         * @throws InvalidCommandArgument In case the context contains any discrepancies, it will throw this exception.
         */
        @Override
        public OfflineFPlayer<?> getContext(CommandExecutionContext<?, ?> c) throws InvalidCommandArgument {
            String name = c.popFirstArg();
            UUID uuid = null;
            if (c.hasFlag("uuid")) {
                uuid = UUID.fromString(name);
            }
            OfflineFPlayer<?> offlinePlayer = uuid != null ? Universal.getMethods().getPlayerFromUUID(uuid) : Universal.getMethods().getPlayerFromName(name);
            if (offlinePlayer.isPlayerNull() || !offlinePlayer.isOnline()) {
                throw new InvalidCommandArgument(UniversalMinecraftMessageKeys.NO_PLAYER_FOUND_OFFLINE,
                        "{search}", name);
            }
            return offlinePlayer;
        }
    }



    @Nullable
    static IFPlayer<?> getOnlinePlayer(CommandIssuer issuer, String lookup, boolean allowMissing) throws InvalidCommandArgument {
        IFPlayer<?> player = findPlayerSmart(issuer, lookup);
        //noinspection Duplicates

        if (player == null) {
            if (allowMissing) {
                return null;
            }
            throw new InvalidCommandArgument(false);
        }

        if (player.isPlayerNull()) {
            if (allowMissing) {
                return player;
            }
            throw new InvalidCommandArgument(false);
        }

        return player;
    }

    public static IFPlayer<?> findPlayerSmart(CommandIssuer issuer, String search) {
        CommandIssuer requester = issuer.getIssuer();
        if (search == null) {
            return null;
        }
        String name = ACFUtil.replace(search, ":confirm", "");

        if (!isValidName(name)) {
            issuer.sendError(UniversalMinecraftMessageKeys.IS_NOT_A_VALID_NAME, "{name}", name);
            return null;
        }

        List<? extends IFPlayer<?>> matches = Universal.getMethods().matchPlayerName(name);
        List<IFPlayer<?>> confirmList = new ArrayList<>();
        findMatches(search, requester, matches, confirmList);


        if (matches.size() > 1 || confirmList.size() > 1) {
            String allMatches = matches.stream().map(IFPlayer::getName).collect(Collectors.joining(", "));
            issuer.sendError(UniversalMinecraftMessageKeys.MULTIPLE_PLAYERS_MATCH,
                    "{search}", name, "{all}", allMatches);
            return null;
        }

        //noinspection Duplicates
        if (matches.isEmpty()) {
            IFPlayer<?> player = ACFUtil.getFirstElement(confirmList);
            if (player == null) {
                issuer.sendError(UniversalMinecraftMessageKeys.NO_PLAYER_FOUND_SERVER, "{search}", name);
            } else {
                issuer.sendInfo(UniversalMinecraftMessageKeys.PLAYER_IS_VANISHED_CONFIRM, "{vanished}", player.getName());
            }
            return null;
        }

        return Universal.getMethods().getPlayerFromName(name);
    }

    private static void findMatches(String search, CommandIssuer requester, List<? extends IFPlayer<?>> matches, List<IFPlayer<?>> confirmList) {
        // Remove vanished players from smart matching.
        Iterator<? extends IFPlayer<?>> iter = matches.iterator();
        //noinspection Duplicates
        while (iter.hasNext()) {
            IFPlayer<?> player = iter.next();
            if (requester instanceof IFPlayer<?> && !((IFPlayer<?>) requester).canSee(player)) {
                if (requester.hasPermission("acf.seevanish")) {
                    if (!search.endsWith(":confirm")) {
                        confirmList.add(player);
                        iter.remove();
                    }
                } else {
                    iter.remove();
                }
            }
        }
    }

    public static final Pattern COMMA = Pattern.compile(",");
    public static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_$]{1,16}$");

    public static boolean isValidName(String name) {

        return name != null && !name.isEmpty() && VALID_NAME_PATTERN.matcher(name).matches();
    }

}
