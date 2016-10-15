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

    public void processMessage(String args, EntityPlayerMP player) {
        String patternStr = "(Predator Bow|P *B|p *b)( +)(Change *Arrow|C *A|CA|ca)( +)(\\w+)";
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(args);
        if (m.find()) {
            currentMode = IntMode.INIT;
            if (m.group(5).equals("normal") || m.group(5).equals("n")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.NORMAL);
                this.DisplayMessage(msgCA_N, player);
            } else if (m.group(5).equals("split") || m.group(5).equals("s")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.SPLIT);
                this.DisplayMessage(msgCA_S, player);
            } else if (m.group(5).equals("hiimpact") || m.group(5).equals("h")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.HIIMPACT);
                this.DisplayMessage(msgCA_H, player);
            } else if (m.group(5).equals("airburst") || m.group(5).equals("a")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.AIRBURST);
                this.DisplayMessage(msgCA_A, player);
            } else if (m.group(5).equals("napalm") || m.group(5).equals("n")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.NAPALM);
                this.DisplayMessage(msgCA_NP, player);
            } else if (m.group(5).equals("lockin") || m.group(5).equals("l")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.LOCKIN);
                this.DisplayMessage(msgCA_L, player);
            } else {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.NORMAL);
                this.DisplayMessage(msgCA_N, player);
            }
        }
    }

    private void DisplayMessage(String message, EntityPlayerMP player) {
        TextComponentString tcs = new TextComponentString("");
        tcs.appendText(message);
        player.addChatMessage(tcs);
    }

    private void ProcessMessageInteractiveINIT(String args, EntityPlayerMP player) {
        currentMode = IntMode.STARTED;
        this.DisplayMessage(msgSTARTED_1, player);
        this.DisplayMessage(msgSTARTED_2, player);
    }

    private void ProcessMessageInteractiveSTARTED(String args, EntityPlayerMP player) {
        String patternStr = "(Predator *Bow|P *B|p *b)";
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(args);
        if (m.find()) {
            currentMode = IntMode.SETPB;
            this.DisplayMessage(msgSETPB_1, player);
            this.DisplayMessage(msgSETPB_2, player);
            this.DisplayMessage(msgSETPB_3, player);
        }
    }

    private void ProcessMessageInteractiveSETPB(String args, EntityPlayerMP player) {
        String patternChangeArrow = "(Change *arrow|C *A|C *a|c *a)";
        Pattern pCA = Pattern.compile(patternChangeArrow);
        Matcher mCA = pCA.matcher(args);
        if (mCA.find()) {
            currentMode = IntMode.PBCA;
            this.DisplayMessage(msgPBCA_1, player);
            this.DisplayMessage(msgPBCA_2, player);
            return;
        }
        String patternChangeDrawWeight = "(Change *draw *weight|C *D *W|C *D *w|C *d *w|c *d *w)";
        Pattern pCDW = Pattern.compile(patternChangeDrawWeight);
        Matcher mCDW = pCDW.matcher(args);
        if (mCDW.find()) {
            currentMode = IntMode.PBCDW;
            this.DisplayMessage(msgPBCDW_1, player);
            this.DisplayMessage(msgPBCDW_2, player);
            this.DisplayMessage(msgPBCDW_3, player);
            return;
        }
    }

    private void ProcessMessageInteractivePBCA(String args, EntityPlayerMP player) {
        String patternStr = "( *)(\\w+)( *)";
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(args);
        if (m.find()) {
            currentMode = IntMode.INIT;
            if (m.group(2).equals("Normal") || m.group(2).equals("n")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.NORMAL);
                this.DisplayMessage(msgCA_N, player);
            } else if (m.group(2).equals("split") || m.group(2).equals("s")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.SPLIT);
                this.DisplayMessage(msgCA_S, player);
            } else if (m.group(2).equals("hiimpact") || m.group(2).equals("h")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.HIIMPACT);
                this.DisplayMessage(msgCA_H, player);
            } else if (m.group(2).equals("airburst") || m.group(2).equals("a")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.AIRBURST);
                this.DisplayMessage(msgCA_A, player);
            } else if (m.group(2).equals("napalm") || m.group(2).equals("n")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.NAPALM);
                this.DisplayMessage(msgCA_NP, player);
            } else if (m.group(2).equals("lockin") || m.group(2).equals("l")) {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.LOCKIN);
                this.DisplayMessage(msgCA_L, player);
            } else {
                AntiEndermanEquipments.thisMod.m_PBOW.SetArrowType(ArrowType.NORMAL);
                this.DisplayMessage(msgCA_N, player);
            }
        }
    }

    private void ProcessMessageInteractivePBCDW(String args, EntityPlayerMP player) {
        String patternStr = "( *)(\\w+)( *)";
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(args);
        if (m.find()) {
            if (m.group(2).equals("Attack") || m.group(2).equals("A") || m.group(2).equals("a")) {
                currentMode = IntMode.PBA;
                this.DisplayMessage(msgPBA_1, player);
            } else if (m.group(2).equals("Velocity") || m.group(2).equals("V") || m.group(2).equals("v")) {
                currentMode = IntMode.PBV;
                this.DisplayMessage(msgPBV_1, player);
            } else if (m.group(2).equals("Knock") || m.group(2).equals("K") || m.group(2).equals("k")) {
                currentMode = IntMode.PBK;
                this.DisplayMessage(msgPBK_1, player);
            }
        }
    }

    private void ProcessMessageInteractivePBA(String args, EntityPlayerMP player) {
        String patternStr = "( *)(\\d+)( *)";
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(args);
        if (m.find()) {
            int power = Integer.parseInt(m.group(2));
            if (1 <= power && power <= 255) {
                currentMode = IntMode.INIT;
                AntiEndermanEquipments.thisMod.m_PBOW.SetOverriddenAttackPower(power);
                String msg = msgPBA_2;
                msg += Integer.toString(power) + ".";
                this.DisplayMessage(msg, player);
            }
        }
    }

    private void ProcessMessageInteractivePBV(String args, EntityPlayerMP player) {
        String patternStr = "( *)(\\d+)( *)";
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(args);
        if (m.find()) {
            int power = Integer.parseInt(m.group(2));
            if (1 <= power && power <= 30) {
                currentMode = IntMode.INIT;
                AntiEndermanEquipments.thisMod.m_PBOW.SetOverriddenArrowVelocity(power);
                String msg = msgPBV_2;
                msg += Integer.toString(power) + ".";
                this.DisplayMessage(msg, player);
            }
        }
    }

    private void ProcessMessageInteractivePBK(String args, EntityPlayerMP player) {
        String patternStr = "( *)(\\d+)( *)";
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(args);
        if (m.find()) {
            int power = Integer.parseInt(m.group(2));
            if (3 <= power && power <= 40) {
                currentMode = IntMode.INIT;
                AntiEndermanEquipments.thisMod.m_PBOW.SetOverriddenKnockBackWeight(power);
                String msg = msgPBK_2;
                msg += Integer.toString(power);
                this.DisplayMessage(msg, player);
            }
        }
    }

    private void CancelInt(String args, EntityPlayerMP player) {
        String patternStr = "( *)(\\w+)( *)";
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(args);
        if (m.find()) {
            if (m.group(2).equals("Cancel") || m.group(2).equals("C") || m.group(2).equals("c")) {
                currentMode = IntMode.INIT;
                this.DisplayMessage(msgCANCEL, player);
            }
        }
    }

    public void processMessageInteractive(String args, EntityPlayerMP player) {
        if (currentMode == IntMode.INIT) {
            ProcessMessageInteractiveINIT(args, player);
        } else {
            if (currentMode == IntMode.STARTED) {
                ProcessMessageInteractiveSTARTED(args, player);
            } else if (currentMode == IntMode.SETPB) {
                ProcessMessageInteractiveSETPB(args, player);
            } else if (currentMode == IntMode.PBCA) {
                ProcessMessageInteractivePBCA(args, player);
            } else if (currentMode == IntMode.PBCDW) {
                ProcessMessageInteractivePBCDW(args, player);
            } else if (currentMode == IntMode.PBA) {
                ProcessMessageInteractivePBA(args, player);
            } else if (currentMode == IntMode.PBV) {
                ProcessMessageInteractivePBV(args, player);
            } else if (currentMode == IntMode.PBV) {
                ProcessMessageInteractivePBV(args, player);
            }
            CancelInt(args, player);
        }
    }

    private enum IntMode {
        INIT, STARTED, SETPB, PBCA, PBCDW, PBA, PBV, PBK,
    }

    private IntMode currentMode;

    public boolean IsIntMode() {
        if (currentMode != IntMode.INIT) {
            return true;
        } else {
            return false;
        }
    }

    public void InitIntMode() {
        currentMode = IntMode.INIT;
    }

    private static final String msgSTARTED_1 = "Interactive mode started.";
    private static final String msgSTARTED_2 = "to setup the Bow type \"Predator Bow\". ( shortcut = pb )";
    private static final String msgSETPB_1 = "Predator Bow setup.";
    private static final String msgSETPB_2 = "\"Change arrow\" type. ( shortcut = ca )";
    private static final String msgSETPB_3 = "\"Change draw weight\". ( shortcut = cdw )";
    private static final String msgPBCA_1 = "to change arrow type \"Normal\"( n ), \"Split\"( s ), \"HI Impact\"( h ),";
    private static final String msgPBCA_2 = "\"Air Burst\"( a ), \"Napalm\"( n ), \"Lock-in\"( l )";
    private static final String msgPBCDW_1 = "to change Attack power \"Attack\"( a )";
    private static final String msgPBCDW_2 = "to change Arrow velocity \"Velocity\"( v )";
    private static final String msgPBCDW_3 = "to change Knock back weight \"Knock\"( k )";
    private static final String msgPBA_1 = "type Attack power ( 1 - 255 )";
    private static final String msgPBA_2 = "Attack power set to ";
    private static final String msgPBV_1 = "type Arrow velocity ( 1 - 30 )";
    private static final String msgPBV_2 = "Arrow velocity set to ";
    private static final String msgPBK_1 = "type Knock back weight ( 3 - 40 )";
    private static final String msgPBK_2 = "Knock back weight set to ";
    private static final String msgCANCEL = "Interactive mode canceled.";

    private static final String msgCA_N = "[Predator Bow] : Using arrow changed to Normal.";
    private static final String msgCA_S = "[Predator Bow] : Using arrow changed to Split.";
    private static final String msgCA_H = "[Predator Bow] : Using arrow changed to HI Impact.";
    private static final String msgCA_A = "[Predator Bow] : Using arrow changed to Air Burst.";
    private static final String msgCA_NP = "[Predator Bow] : Using arrow changed to Napalm.";
    private static final String msgCA_L = "[Predator Bow] : Using arrow changed to Lock-in.";

}
