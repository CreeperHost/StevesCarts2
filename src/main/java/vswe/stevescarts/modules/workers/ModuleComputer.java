package vswe.stevescarts.modules.workers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.client.guis.buttons.*;
import vswe.stevescarts.computer.*;
import vswe.stevescarts.entitys.EntityMinecartModular;

import java.util.ArrayList;

public class ModuleComputer extends ModuleWorker
{
    private IWriting writing;
    private short info;
    private ArrayList<ComputerProg> programs;
    private ComputerProg editProg;
    private ArrayList<ComputerTask> editTasks;
    private ComputerProg activeProg;
    private static final int headerSize = 1;
    private static final int programHeaderSize = 3;
    private static final int taskMaxCount = 256;
    private static final int varMaxCount = 63;
    private static final int taskSize = 2;
    private static final int varSize = 5;

    public ModuleComputer(final EntityMinecartModular cart)
    {
        super(cart);
        programs = new ArrayList<>();
        editTasks = new ArrayList<>();
    }

    @Override
    public byte getWorkPriority()
    {
        return 5;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    public boolean hasSlots()
    {
        return false;
    }

    @Override
    public int guiWidth()
    {
        return 443;
    }

    @Override
    public int guiHeight()
    {
        return 250;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(MatrixStack matrixStack, GuiMinecart gui)
    {
        if (isWriting())
        {
            drawString(matrixStack, gui, getWriting().getText(), 100, 6, 4210752);
            drawString(matrixStack, gui, "Max Length: " + getWriting().getMaxLength(), 100, 18, 4210752);
        }
    }

    @Override
    protected void loadButtons()
    {
        new ButtonProgramAdd(this, ButtonBase.LOCATION.OVERVIEW);
        new ButtonProgramStart(this, ButtonBase.LOCATION.OVERVIEW);
        for (int i = 0; i < 7; ++i)
        {
            new ButtonTaskType(this, ButtonBase.LOCATION.PROGRAM, i);
        }
        new ButtonVarAdd(this, ButtonBase.LOCATION.PROGRAM);
        for (int i = 0; i < 11; ++i)
        {
            new ButtonFlowType(this, ButtonBase.LOCATION.TASK, i);
        }
        new ButtonLabelId(this, ButtonBase.LOCATION.TASK, true);
        new ButtonLabelId(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowConditionVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowConditionVar(this, ButtonBase.LOCATION.TASK, true);
        for (int i = 0; i < 6; ++i)
        {
            new ButtonFlowConditionOperator(this, ButtonBase.LOCATION.TASK, i);
        }
        new ButtonFlowConditionUseSecondVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowConditionUseSecondVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowConditionInteger(this, ButtonBase.LOCATION.TASK, 1);
        new ButtonFlowConditionInteger(this, ButtonBase.LOCATION.TASK, -1);
        new ButtonFlowConditionInteger(this, ButtonBase.LOCATION.TASK, 10);
        new ButtonFlowConditionInteger(this, ButtonBase.LOCATION.TASK, -10);
        new ButtonFlowConditionSecondVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowConditionSecondVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowForVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowForVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowForUseStartVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowForUseStartVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowForStartInteger(this, ButtonBase.LOCATION.TASK, 1);
        new ButtonFlowForStartInteger(this, ButtonBase.LOCATION.TASK, -1);
        new ButtonFlowForStartInteger(this, ButtonBase.LOCATION.TASK, 10);
        new ButtonFlowForStartInteger(this, ButtonBase.LOCATION.TASK, -10);
        new ButtonFlowForStartVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowForStartVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowForUseEndVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowForUseEndVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowForEndInteger(this, ButtonBase.LOCATION.TASK, 1);
        new ButtonFlowForEndInteger(this, ButtonBase.LOCATION.TASK, -1);
        new ButtonFlowForEndInteger(this, ButtonBase.LOCATION.TASK, 10);
        new ButtonFlowForEndInteger(this, ButtonBase.LOCATION.TASK, -10);
        new ButtonFlowForEndVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowForEndVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonFlowForStep(this, ButtonBase.LOCATION.TASK, false);
        new ButtonFlowForStep(this, ButtonBase.LOCATION.TASK, true);
        for (int i = 0; i < 4; ++i)
        {
            new ButtonFlowEndType(this, ButtonBase.LOCATION.TASK, i);
        }
        for (int i = 0; i < 18; ++i)
        {
            new ButtonVarType(this, ButtonBase.LOCATION.TASK, i);
        }
        new ButtonVarVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonVarVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonVarUseFirstVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonVarUseFirstVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonVarFirstInteger(this, ButtonBase.LOCATION.TASK, 1);
        new ButtonVarFirstInteger(this, ButtonBase.LOCATION.TASK, -1);
        new ButtonVarFirstInteger(this, ButtonBase.LOCATION.TASK, 10);
        new ButtonVarFirstInteger(this, ButtonBase.LOCATION.TASK, -10);
        new ButtonVarFirstVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonVarFirstVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonVarUseSecondVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonVarUseSecondVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonVarSecondInteger(this, ButtonBase.LOCATION.TASK, 1);
        new ButtonVarSecondInteger(this, ButtonBase.LOCATION.TASK, -1);
        new ButtonVarSecondInteger(this, ButtonBase.LOCATION.TASK, 10);
        new ButtonVarSecondInteger(this, ButtonBase.LOCATION.TASK, -10);
        new ButtonVarSecondVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonVarSecondVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonControlType(this, ButtonBase.LOCATION.TASK, 0);
        ComputerControl.createButtons(getCart(), this);
        new ButtonControlUseVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonControlUseVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonControlInteger(this, ButtonBase.LOCATION.TASK, 1);
        new ButtonControlInteger(this, ButtonBase.LOCATION.TASK, -1);
        new ButtonControlInteger(this, ButtonBase.LOCATION.TASK, 10);
        new ButtonControlInteger(this, ButtonBase.LOCATION.TASK, -10);
        new ButtonControlVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonControlVar(this, ButtonBase.LOCATION.TASK, true);
        new ButtonInfoType(this, ButtonBase.LOCATION.TASK, 0);
        ComputerInfo.createButtons(getCart(), this);
        new ButtonInfoVar(this, ButtonBase.LOCATION.TASK, false);
        new ButtonInfoVar(this, ButtonBase.LOCATION.TASK, true);
        for (int i = 0; i < 21; ++i)
        {
            new ButtonTask(this, ButtonBase.LOCATION.FLOATING, i);
        }
        ButtonKeyboard.generateKeyboard(this);
    }

    @Override
    public boolean useButtons()
    {
        return true;
    }

    public boolean isWriting()
    {
        return writing != null;
    }

    public IWriting getWriting()
    {
        return writing;
    }

    public void setWriting(final IWriting val)
    {
        writing = val;
    }

    public void flipShift()
    {
        info ^= 0x1;
    }

    public void flipCaps()
    {
        info ^= 0x2;
    }

    public boolean getShift()
    {
        return (info & 0x1) != 0x0;
    }

    public boolean getCaps()
    {
        return (info & 0x2) != 0x0;
    }

    public boolean isLower()
    {
        return getShift() == getCaps();
    }

    public void disableShift()
    {
        info &= 0xFFFFFFFE;
    }

    public ComputerProg getCurrentProg()
    {
        return editProg;
    }

    public ArrayList<ComputerTask> getSelectedTasks()
    {
        return editTasks;
    }

    public void setCurrentProg(final ComputerProg prog)
    {
        editProg = prog;
    }

    public void setActiveProgram(final ComputerProg prog)
    {
        activeProg = prog;
    }

    public ComputerProg getActiveProgram()
    {
        return activeProg;
    }

    @Override
    public boolean work()
    {
        if (activeProg != null)
        {
            if (doPreWork())
            {
                startWorking(activeProg.getRunTime());
            }
            else
            {
                if (!activeProg.run())
                {
                    activeProg = null;
                }
                stopWorking();
            }
        }
        return true;
    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final PlayerEntity player)
    {
    }

    @Override
    public int numberOfPackets()
    {
        return 0;
    }

    @Override
    public int numberOfGuiData()
    {
        return 831;
    }

    public void activationChanged()
    {
        editTasks.clear();
        if (editProg != null)
        {
            for (final ComputerTask task : editProg.getTasks())
            {
                if (task.getIsActivated())
                {
                    editTasks.add(task);
                }
            }
        }
    }

    @Override
    protected void checkGuiData(final Object[] info)
    {
        updateGuiData(info, 0, this.info);
        if (editProg != null)
        {
            updateGuiData(info, 1, editProg.getInfo());
            final int tasks = editProg.getTasks().size();
            final int vars = editProg.getVars().size();
            updateGuiData(info, 2, (short) (tasks << 8 | vars));
            if (editProg == activeProg)
            {
                updateGuiData(info, 3, (short) activeProg.getActiveId());
            }
            else
            {
                updateGuiData(info, 3, (short) 256);
            }
            for (int taskId = 0; taskId < tasks; ++taskId)
            {
                final ComputerTask theTask = editProg.getTasks().get(taskId);
                for (int internalId = 0; internalId < 2; ++internalId)
                {
                    updateGuiData(info, 4 + taskId * 2 + internalId, theTask.getInfo(internalId));
                }
            }
            for (int varId = 0; varId < vars; ++varId)
            {
                final ComputerVar theVar = editProg.getVars().get(varId);
                for (int internalId = 0; internalId < 5; ++internalId)
                {
                    updateGuiData(info, 516 + varId * 5 + internalId, theVar.getInfo(internalId));
                }
            }
        }
        else
        {
            updateGuiData(info, 1, (short) 0);
        }
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        System.out.println("ID " + id + " Data " + data);
        if (id == 0)
        {
            info = data;
        }
        else if (id == 1)
        {
            if (data == 0)
            {
                editProg = null;
            }
            else
            {
                if (editProg == null)
                {
                    editProg = new ComputerProg(this);
                }
                editProg.setInfo(data);
            }
        }
        else if (editProg != null)
        {
            if (id == 2)
            {
                final int tasks = data >> 8 & 0xFF;
                final int vars = data & 0xFF;
                editProg.setTaskCount(tasks);
                editProg.setVarCount(vars);
            }
            else if (id == 3)
            {
                if (data >= 0 && data < 256)
                {
                    activeProg = editProg;
                    editProg.setActiveId(data);
                }
                else
                {
                    activeProg = null;
                    editProg.setActiveId(0);
                }
            }
            else
            {
                final int taskId = id - 1 - 3;
                if (taskId < 512)
                {
                    final int task = taskId / 2;
                    final int taskInternalPos = taskId % 2;
                    if (task >= 0 && task < editProg.getTasks().size())
                    {
                        final ComputerTask theTask = editProg.getTasks().get(task);
                        theTask.setInfo(taskInternalPos, data);
                    }
                }
                else
                {
                    final int varId = taskId - 512;
                    final int var = varId / 5;
                    final int varInternalPos = varId % 5;
                    if (var >= 0 && var < editProg.getVars().size())
                    {
                        final ComputerVar theVar = editProg.getVars().get(var);
                        theVar.setInfo(varInternalPos, data);
                    }
                }
            }
        }
    }
}
