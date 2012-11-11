/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Last modified: 30.10.12 16:23
 */

package com.p000ison.dev.simpleclans2.commands.clan.bank;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a DepositCommand
 */
public class DepositCommand extends GenericPlayerCommand {

    public DepositCommand(SimpleClans plugin)
    {
        super("DepositCommand", plugin);
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.bank.deposit"), plugin.getSettingsManager().getBankCommand()));
        setIdentifiers(Language.getTranslation("bank.deposit.command"));
        setPermission("simpleclans.member.bank.deposit");
        setType(Type.BANK);
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null && cp.getClan().isVerified() && cp.isTrusted()) {
            return MessageFormat.format(Language.getTranslation("menu.bank.deposit"), plugin.getSettingsManager().getBankCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

         if (cp.isRankPermissionNegative("bank.deposit")) {
             player.sendMessage(ChatColor.DARK_RED + Language.getTranslation("no.reank.permissions"));
             return;
         }

        Clan clan = cp.getClan();

        if (!clan.isVerified()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            return;
        }

        if (!cp.isTrusted()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("only.trusted.players.can.access.clan.bank"));
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            ChatBlock.sendMessage(player, ChatColor.DARK_RED + Language.getTranslation("number.format"));
            return;
        }

        if (amount < 0.0D) {
            ChatBlock.sendMessage(player, ChatColor.DARK_RED + Language.getTranslation("amount.can.not.be.negative"));
            return;
        }

        if (!cp.transfer(clan, amount)) {
            ChatBlock.sendMessage(player, ChatColor.DARK_RED + Language.getTranslation("not.sufficient.money"));
            return;
        }


        ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("transaction.successfully"));
        ChatBlock.sendBlank(player);
        ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("now.clan.balance", clan.getBalance()));
        ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("now.player.balance", cp.getBalance()));

        clan.update();

    }
}
