package com.yoclabo.minecraft;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yoclabo.minecraft.Entity_Sticky_Arrow.ArrowType;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class Command_PredatorBow extends CommandBase {

    @Override
    public String getCommandName() {
        return "PredatorBow";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    }

    public static void processMessage(String args, EntityPlayerMP player) {
        String patternStr = "(Predator Bow|P *B|p *b)( +)(Change *Arrow|C *A|CA|ca)( +)(\\w+)";
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(args);
        if (m.find()) {
            TextComponentString message = new TextComponentString("");
            if (m.group(5).equals("normal") || m.group(5).equals("n")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.NORMAL);
                message.appendText("[Predator Bow]: Using arrow changed to Normal.");
                player.addChatComponentMessage(message);
            } else if (m.group(5).equals("split") || m.group(5).equals("s")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.SPLIT);
                message.appendText("[Predator Bow]: Using arrow changed to Split.");
                player.addChatComponentMessage(message);
            } else if (m.group(5).equals("hiimpact") || m.group(5).equals("h")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.HIIMPACT);
                message.appendText("[Predator Bow]: Using arrow changed to HI Impact.");
                player.addChatComponentMessage(message);
            } else if (m.group(5).equals("airburst") || m.group(5).equals("a")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.AIRBURST);
                message.appendText("[Predator Bow]: Using arrow changed to Air Burst.");
                player.addChatComponentMessage(message);
            } else if (m.group(5).equals("napalm") || m.group(5).equals("n")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.NAPALM);
                message.appendText("[Predator Bow]: Using arrow changed to Napalm.");
                player.addChatComponentMessage(message);
            } else if (m.group(5).equals("lockin") || m.group(5).equals("l")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.LOCKIN);
                message.appendText("[Predator Bow]: Using arrow changed to Lock-in.");
                player.addChatComponentMessage(message);
            } else {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.NORMAL);
                message.appendText("[Predator Bow]: Using arrow changed to Normal.");
                player.addChatComponentMessage(message);
            }
        }
    }

}
