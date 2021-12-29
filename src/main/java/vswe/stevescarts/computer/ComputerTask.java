package vswe.stevescarts.computer;

import vswe.stevescarts.modules.workers.ModuleComputer;

import java.util.Random;

public class ComputerTask
{
    private static Random rand;
    private ModuleComputer module;
    private ComputerProg prog;
    private int info;

    public ComputerTask(final ModuleComputer module, final ComputerProg prog)
    {
        this.module = module;
        this.prog = prog;
    }

    public int getTime()
    {
        return 5;
    }

    public int run(final ComputerProg prog, final int id)
    {
        if (isFlowGoto())
        {
            final int labelId = getFlowLabelId();
            for (int i = 0; i < prog.getTasks().size(); ++i)
            {
                final ComputerTask task = prog.getTasks().get(i);
                if (task.isFlowLabel() && task.getFlowLabelId() == labelId)
                {
                    return i;
                }
            }
        }
        else if (isFlowCondition())
        {
            final boolean condition = evalFlowCondition();
            int nested = 0;
            if (!condition)
            {
                if (isFlowIf() || isFlowElseif())
                {
                    for (int j = id + 1; j < prog.getTasks().size(); ++j)
                    {
                        final ComputerTask task2 = prog.getTasks().get(j);
                        if (task2.isFlowIf())
                        {
                            ++nested;
                        }
                        else if (task2.isFlowElseif() || task2.isFlowElse() || task2.isFlowEndif())
                        {
                            if (nested == 0)
                            {
                                return j;
                            }
                            if (task2.isFlowEndif())
                            {
                                --nested;
                            }
                        }
                    }
                }
                else if (isFlowWhile())
                {
                    for (int j = id + 1; j < prog.getTasks().size(); ++j)
                    {
                        final ComputerTask task2 = prog.getTasks().get(j);
                        if (task2.isFlowWhile())
                        {
                            ++nested;
                        }
                        else if (task2.isFlowEndwhile())
                        {
                            if (nested == 0)
                            {
                                return j;
                            }
                            --nested;
                        }
                    }
                }
            }
        }
        else if (isFlowFor())
        {
            final boolean condition = evalFlowFor();
            if (!condition)
            {
                int nested = 0;
                for (int j = id + 1; j < prog.getTasks().size(); ++j)
                {
                    final ComputerTask task2 = prog.getTasks().get(j);
                    if (task2.isFlowFor())
                    {
                        ++nested;
                    }
                    else if (task2.isFlowEndfor())
                    {
                        if (nested == 0)
                        {
                            return j;
                        }
                        --nested;
                    }
                }
            }
        }
        else if (isFlowContinue() || isFlowBreak())
        {
            int nested2 = 0;
            for (int i = id + 1; i < prog.getTasks().size(); ++i)
            {
                final ComputerTask task = prog.getTasks().get(i);
                if (task.isFlowWhile() || task.isFlowFor())
                {
                    ++nested2;
                }
                else if (task.isFlowEndwhile() || task.isFlowEndfor())
                {
                    if (nested2 == 0)
                    {
                        if (isFlowContinue())
                        {
                            return task.preload(prog, i);
                        }
                        return i;
                    }
                    else
                    {
                        --nested2;
                    }
                }
            }
        }
        else if (isVar(getType()) && !isVarEmpty())
        {
            final ComputerVar var = getVarVar();
            if (var != null)
            {
                int value1;
                if (getVarUseFirstVar())
                {
                    final ComputerVar var2 = getVarFirstVar();
                    if (var2 == null)
                    {
                        return -1;
                    }
                    value1 = var2.getByteValue();
                }
                else
                {
                    value1 = getVarFirstInteger();
                }
                int value2;
                if (hasTwoValues())
                {
                    if (getVarUseSecondVar())
                    {
                        final ComputerVar var3 = getVarSecondVar();
                        if (var3 == null)
                        {
                            return -1;
                        }
                        value2 = var3.getByteValue();
                    }
                    else
                    {
                        value2 = getVarSecondInteger();
                    }
                }
                else
                {
                    value2 = 0;
                }
                var.setByteValue(calcVarValue(value1, value2));
            }
        }
        else if (isControl(getType()) && !isControlEmpty())
        {
            final ComputerControl control = ComputerControl.getMap().get((byte) getControlType());
            if (control != null && control.isControlValid(module.getCart()))
            {
                int value3;
                if (getControlUseVar())
                {
                    final ComputerVar var4 = getControlVar();
                    if (var4 == null)
                    {
                        return -1;
                    }
                    value3 = var4.getByteValue();
                }
                else
                {
                    value3 = getControlInteger();
                }
                control.runHandler(module.getCart(), (byte) value3);
            }
        }
        else if (isInfo(getType()) && !isInfoEmpty())
        {
            final ComputerInfo info = ComputerInfo.getMap().get((byte) getControlType());
            if (info != null && info.isInfoValid(module.getCart()))
            {
                final ComputerVar var5 = getInfoVar();
                if (var5 != null)
                {
                    info.getHandler(module.getCart(), var5);
                }
            }
        }
        else if (isVar(getType()))
        {
            for (final ComputerVar var5 : prog.getVars())
            {
                System.out.println(var5.getFullInfo());
            }
        }
        return -1;
    }

    public int preload(final ComputerProg prog, final int id)
    {
        if (isFlowElseif() || isFlowElse())
        {
            int nested = 0;
            for (int i = id + 1; i < prog.getTasks().size(); ++i)
            {
                final ComputerTask task = prog.getTasks().get(i);
                if (task.isFlowIf())
                {
                    ++nested;
                }
                else if (task.isFlowEndif())
                {
                    if (nested == 0)
                    {
                        return i;
                    }
                    --nested;
                }
            }
        }
        else if (isFlowEndwhile())
        {
            int nested = 0;
            for (int i = id - 1; i >= 0; --i)
            {
                final ComputerTask task = prog.getTasks().get(i);
                if (task.isFlowEndwhile())
                {
                    ++nested;
                }
                else if (task.isFlowWhile())
                {
                    if (nested == 0)
                    {
                        return i;
                    }
                    --nested;
                }
            }
        }
        else if (isFlowFor())
        {
            final ComputerVar var = getFlowForVar();
            if (var != null)
            {
                if (getFlowForUseStartVar())
                {
                    final ComputerVar var2 = getFlowForStartVar();
                    if (var2 != null)
                    {
                        var.setByteValue(var2.getByteValue());
                    }
                }
                else
                {
                    var.setByteValue(getFlowForStartInteger());
                }
            }
        }
        else if (isFlowEndfor())
        {
            System.out.println("End for");
            int nested = 0;
            for (int i = id - 1; i >= 0; --i)
            {
                final ComputerTask task = prog.getTasks().get(i);
                if (task.isFlowEndfor())
                {
                    ++nested;
                }
                else if (task.isFlowFor())
                {
                    if (nested == 0)
                    {
                        final ComputerVar var3 = task.getFlowForVar();
                        if (var3 != null)
                        {
                            final int dif = task.getFlowForDecrease() ? -1 : 1;
                            var3.setByteValue(var3.getByteValue() + dif);
                        }
                        return i;
                    }
                    --nested;
                }
            }
        }
        return id;
    }

    @Override
    public ComputerTask clone()
    {
        final ComputerTask clone = new ComputerTask(module, prog);
        clone.info = info;
        return clone;
    }

    public ComputerProg getProgram()
    {
        return prog;
    }

    public void setInfo(final int id, final short val)
    {
        int iVal = val;
        if (iVal < 0)
        {
            iVal += 65536;
        }
        final boolean oldVal = getIsActivated();
        info &= ~(65535 << id * 16);
        info |= iVal << id * 16;
        if (oldVal != getIsActivated())
        {
            module.activationChanged();
        }
    }

    public short getInfo(final int id)
    {
        return (short) ((info & 65535 << id * 16) >> id * 16);
    }

    public void setIsActivated(final boolean val)
    {
        final boolean oldVal = getIsActivated();
        info &= 0xFFFFFFFE;
        info |= (val ? 1 : 0);
        if (oldVal != val)
        {
            module.activationChanged();
        }
    }

    public boolean getIsActivated()
    {
        return (info & 0x1) != 0x0;
    }

    public void setType(final int type)
    {
        final int oldType = getType();
        final boolean flag = isBuild(oldType);
        info &= 0xFFFFFFF1;
        info |= type << 1;
        if (oldType != type && (!flag || !isBuild(type)))
        {
            info &= 0xF;
        }
    }

    public int getType()
    {
        return (info & 0xE) >> 1;
    }

    public static boolean isEmpty(final int type)
    {
        return type == 0;
    }

    public static boolean isFlow(final int type)
    {
        return type == 1;
    }

    public static boolean isVar(final int type)
    {
        return type == 2;
    }

    public static boolean isControl(final int type)
    {
        return type == 3;
    }

    public static boolean isInfo(final int type)
    {
        return type == 4;
    }

    public static boolean isBuild(final int type)
    {
        return type == 5 || isAddon(type);
    }

    public static boolean isAddon(final int type)
    {
        return type == 6;
    }

    public int getImage()
    {
        if (isEmpty(getType()))
        {
            return -1;
        }
        if (isFlow(getType()))
        {
            return getFlowImageForTask();
        }
        if (isVar(getType()))
        {
            return getVarImage(getVarType());
        }
        if (isControl(getType()))
        {
            return getControlImage(getControlType());
        }
        if (isInfo(getType()))
        {
            return getInfoImage(getInfoType());
        }
        return -1;
    }

    public static String getTypeName(final int type)
    {
        switch (type)
        {
            default:
            {
                return "Empty";
            }
            case 1:
            {
                return "Flow Control";
            }
            case 2:
            {
                return "Variable Control";
            }
            case 3:
            {
                return "Module Control";
            }
            case 4:
            {
                return "Module Info";
            }
            case 5:
            {
                return "Builder";
            }
            case 6:
            {
                return "Addon";
            }
        }
    }

    @Override
    public String toString()
    {
        if (isEmpty(getType()))
        {
            return "Empty";
        }
        if (isFlow(getType()))
        {
            return getFlowTypeName(getFlowType()) + " " + getFlowText();
        }
        if (isVar(getType()))
        {
            return getVarTypeName(getVarType()) + ": " + getVarText();
        }
        if (isControl(getType()))
        {
            return "Set " + getControlTypeName(getControlType()) + " to " + getControlText();
        }
        if (isInfo(getType()))
        {
            return "Set " + getVarName(getInfoVar()) + " to " + getInfoTypeName(getInfoType());
        }
        return "Unknown";
    }

    public int getFlowType()
    {
        return (info & 0xF0) >> 4;
    }

    public void setFlowType(final int type)
    {
        final int oldType = getFlowType();
        if (oldType == type)
        {
            return;
        }
        final boolean conditionFlag = isFlowCondition();
        info &= 0xFFFFFF0F;
        info |= type << 4;
        if (!conditionFlag || !isFlowCondition())
        {
            info &= 0xFF;
        }
    }

    public boolean isFlowEmpty()
    {
        return isFlow(getType()) && getFlowType() == 0;
    }

    public boolean isFlowLabel()
    {
        return isFlow(getType()) && getFlowType() == 1;
    }

    public boolean isFlowGoto()
    {
        return isFlow(getType()) && getFlowType() == 2;
    }

    public boolean isFlowIf()
    {
        return isFlow(getType()) && getFlowType() == 3;
    }

    public boolean isFlowElseif()
    {
        return isFlow(getType()) && getFlowType() == 4;
    }

    public boolean isFlowElse()
    {
        return isFlow(getType()) && getFlowType() == 5;
    }

    public boolean isFlowWhile()
    {
        return isFlow(getType()) && getFlowType() == 6;
    }

    public boolean isFlowFor()
    {
        return isFlow(getType()) && getFlowType() == 7;
    }

    public boolean isFlowEnd()
    {
        return isFlow(getType()) && getFlowType() == 8;
    }

    public boolean isFlowBreak()
    {
        return isFlow(getType()) && getFlowType() == 9;
    }

    public boolean isFlowContinue()
    {
        return isFlow(getType()) && getFlowType() == 10;
    }

    public boolean isFlowCondition()
    {
        return isFlowIf() || isFlowElseif() || isFlowWhile();
    }

    public static int getFlowImage(final int type)
    {
        return 12 + type;
    }

    public int getFlowImageForTask()
    {
        if (isFlowEnd())
        {
            return getEndImage(getFlowEndType());
        }
        return getFlowImage(getFlowType());
    }

    public static String getFlowTypeName(final int type)
    {
        switch (type)
        {
            default:
            {
                return "Empty";
            }
            case 1:
            {
                return "Label";
            }
            case 2:
            {
                return "GoTo";
            }
            case 3:
            {
                return "If";
            }
            case 4:
            {
                return "Else if";
            }
            case 5:
            {
                return "Else";
            }
            case 6:
            {
                return "While";
            }
            case 7:
            {
                return "For";
            }
            case 8:
            {
                return "End";
            }
            case 9:
            {
                return "Break";
            }
            case 10:
            {
                return "Continue";
            }
        }
    }

    public String getFlowText()
    {
        if (isFlowLabel() || isFlowGoto())
        {
            return "[" + getFlowLabelId() + "]";
        }
        if (isFlowCondition())
        {
            final ComputerVar var = getFlowConditionVar();
            String str = getVarName(var);
            str += " ";
            str += getFlowOperatorName(getFlowConditionOperator(), false);
            str += " ";
            if (getFlowConditionUseSecondVar())
            {
                final ComputerVar var2 = getFlowConditionSecondVar();
                str += getVarName(var2);
            }
            else
            {
                str += getFlowConditionInteger();
            }
            return str;
        }
        if (isFlowFor())
        {
            String str2 = getVarName(getFlowForVar());
            str2 += " = ";
            if (getFlowForUseStartVar())
            {
                str2 += getVarName(getFlowForStartVar());
            }
            else
            {
                str2 += getFlowForStartInteger();
            }
            str2 += " to ";
            if (getFlowForUseEndVar())
            {
                str2 += getVarName(getFlowForEndVar());
            }
            else
            {
                str2 += getFlowForEndInteger();
            }
            str2 = str2 + "  step " + (getFlowForDecrease() ? "-" : "+") + "1";
            return str2;
        }
        if (isFlowEnd())
        {
            return getEndTypeName(getFlowEndType());
        }
        return "(Not set)";
    }

    public int getFlowLabelId()
    {
        return (info & 0x1F00) >> 8;
    }

    public void setFlowLabelId(int id)
    {
        if (id < 0)
        {
            id = 0;
        }
        else if (id > 31)
        {
            id = 31;
        }
        info &= 0xFFFFE0FF;
        info |= id << 8;
    }

    public int getFlowConditionVarIndex()
    {
        return getVarIndex(8);
    }

    public ComputerVar getFlowConditionVar()
    {
        return getVar(8);
    }

    public void setFlowConditionVar(final int val)
    {
        setVar(8, val);
    }

    public int getFlowConditionOperator()
    {
        return (info & 0xE000) >> 13;
    }

    public void setFlowConditionOperator(final int val)
    {
        info &= 0xFFFF1FFF;
        info |= val << 13;
    }

    public boolean isFlowConditionOperatorEquals()
    {
        return getFlowConditionOperator() == 0;
    }

    public boolean isFlowConditionOperatorNotequals()
    {
        return getFlowConditionOperator() == 1;
    }

    public boolean isFlowConditionOperatorGreaterequals()
    {
        return getFlowConditionOperator() == 2;
    }

    public boolean isFlowConditionOperatorGreater()
    {
        return getFlowConditionOperator() == 3;
    }

    public boolean isFlowConditionOperatorLesserequals()
    {
        return getFlowConditionOperator() == 4;
    }

    public boolean isFlowConditionOperatorLesser()
    {
        return getFlowConditionOperator() == 5;
    }

    public boolean getFlowConditionUseSecondVar()
    {
        return getUseOptionalVar(16);
    }

    public void setFlowConditionUseSecondVar(final boolean val)
    {
        setUseOptionalVar(16, val);
    }

    public int getFlowConditionInteger()
    {
        return getInteger(17);
    }

    public void setFlowConditionInteger(final int val)
    {
        setInteger(17, val);
    }

    public int getFlowConditionSecondVarIndex()
    {
        return getVarIndex(17);
    }

    public ComputerVar getFlowConditionSecondVar()
    {
        return getVar(17);
    }

    public void setFlowConditionSecondVar(final int val)
    {
        setVar(17, val);
    }

    public boolean evalFlowCondition()
    {
        if (!isFlowCondition())
        {
            return false;
        }
        final ComputerVar var = getFlowConditionVar();
        if (var == null)
        {
            return false;
        }
        final int varValue = var.getByteValue();
        int compareWith;
        if (getFlowConditionUseSecondVar())
        {
            final ComputerVar var2 = getFlowConditionVar();
            if (var2 == null)
            {
                return false;
            }
            compareWith = var2.getByteValue();
        }
        else
        {
            compareWith = getFlowConditionInteger();
        }
        if (isFlowConditionOperatorEquals())
        {
            return varValue == compareWith;
        }
        if (isFlowConditionOperatorNotequals())
        {
            return varValue != compareWith;
        }
        if (isFlowConditionOperatorGreaterequals())
        {
            return varValue >= compareWith;
        }
        if (isFlowConditionOperatorGreater())
        {
            return varValue > compareWith;
        }
        if (isFlowConditionOperatorLesserequals())
        {
            return varValue <= compareWith;
        }
        return isFlowConditionOperatorLesser() && varValue < compareWith;
    }

    public static String getFlowOperatorName(final int type, final boolean isLong)
    {
        switch (type)
        {
            default:
            {
                return isLong ? "Unknown" : "?";
            }
            case 0:
            {
                return isLong ? "Equals to" : "=";
            }
            case 1:
            {
                return isLong ? "Not equals to" : "!=";
            }
            case 2:
            {
                return isLong ? "Greater than or equals to" : ">=";
            }
            case 3:
            {
                return isLong ? "Greater than" : ">";
            }
            case 4:
            {
                return isLong ? "Smaller than or equals to" : "<=";
            }
            case 5:
            {
                return isLong ? "Smaller than" : "<";
            }
        }
    }

    public int getFlowForVarIndex()
    {
        return getVarIndex(8);
    }

    public ComputerVar getFlowForVar()
    {
        return getVar(8);
    }

    public void setFlowForVar(final int val)
    {
        setVar(8, val);
    }

    public boolean getFlowForUseStartVar()
    {
        return getUseOptionalVar(13);
    }

    public void setFlowForUseStartVar(final boolean val)
    {
        setUseOptionalVar(13, val);
    }

    public int getFlowForStartInteger()
    {
        return getInteger(14);
    }

    public void setFlowForStartInteger(final int val)
    {
        setInteger(14, val);
    }

    public int getFlowForStartVarIndex()
    {
        return getVarIndex(14);
    }

    public ComputerVar getFlowForStartVar()
    {
        return getVar(14);
    }

    public void setFlowForStartVar(final int val)
    {
        setVar(14, val);
    }

    public boolean getFlowForUseEndVar()
    {
        return getUseOptionalVar(22);
    }

    public void setFlowForUseEndVar(final boolean val)
    {
        setUseOptionalVar(22, val);
    }

    public int getFlowForEndInteger()
    {
        return getInteger(23);
    }

    public void setFlowForEndInteger(final int val)
    {
        setInteger(23, val);
    }

    public int getFlowForEndVarIndex()
    {
        return getVarIndex(23);
    }

    public ComputerVar getFlowForEndVar()
    {
        return getVar(23);
    }

    public void setFlowForEndVar(final int val)
    {
        setVar(23, val);
    }

    public boolean getFlowForDecrease()
    {
        return (info & Integer.MIN_VALUE) != 0x0;
    }

    public void setFlowForDecrease(final boolean val)
    {
        info &= Integer.MAX_VALUE;
        info |= (val ? 1 : 0) << 31;
    }

    public boolean evalFlowFor()
    {
        if (!isFlowFor())
        {
            return false;
        }
        final ComputerVar var = getFlowForVar();
        if (var == null)
        {
            return false;
        }
        final int varValue = var.getByteValue();
        int compareWith;
        if (getFlowForUseEndVar())
        {
            final ComputerVar var2 = getFlowForEndVar();
            if (var2 == null)
            {
                return false;
            }
            compareWith = var2.getByteValue();
        }
        else
        {
            compareWith = getFlowForEndInteger();
        }
        return varValue != compareWith;
    }

    public int getFlowEndType()
    {
        return (info & 0x300) >> 8;
    }

    public void setFlowEndType(int val)
    {
        if (val < 0)
        {
            val = 0;
        }
        else if (val > 3)
        {
            val = 3;
        }
        info &= 0xFFFFFCFF;
        info |= val << 8;
    }

    public boolean isFlowEndif()
    {
        return isFlowEnd() && getFlowEndType() == 1;
    }

    public boolean isFlowEndwhile()
    {
        return isFlowEnd() && getFlowEndType() == 2;
    }

    public boolean isFlowEndfor()
    {
        return isFlowEnd() && getFlowEndType() == 3;
    }

    public static String getEndTypeName(final int type)
    {
        switch (type)
        {
            default:
            {
                return "(not set)";
            }
            case 1:
            {
                return "If";
            }
            case 2:
            {
                return "While";
            }
            case 3:
            {
                return "For";
            }
        }
    }

    public static int getEndImage(final int type)
    {
        if (type == 0)
        {
            return 20;
        }
        return 45 + type;
    }

    public int getVarType()
    {
        return (info & 0x1F0) >> 4;
    }

    public void setVarType(final int val)
    {
        info &= 0xFFFFFE0F;
        info |= val << 4;
    }

    public boolean isVarEmpty()
    {
        return isVar(getType()) && getVarType() == 0;
    }

    public boolean isVarSet()
    {
        return isVar(getType()) && getVarType() == 1;
    }

    public boolean isVarAdd()
    {
        return isVar(getType()) && getVarType() == 2;
    }

    public boolean isVarSub()
    {
        return isVar(getType()) && getVarType() == 3;
    }

    public boolean isVarMult()
    {
        return isVar(getType()) && getVarType() == 4;
    }

    public boolean isVarDiv()
    {
        return isVar(getType()) && getVarType() == 5;
    }

    public boolean isVarMod()
    {
        return isVar(getType()) && getVarType() == 6;
    }

    public boolean isVarAnd()
    {
        return isVar(getType()) && getVarType() == 7;
    }

    public boolean isVarOr()
    {
        return isVar(getType()) && getVarType() == 8;
    }

    public boolean isVarXor()
    {
        return isVar(getType()) && getVarType() == 9;
    }

    public boolean isVarNot()
    {
        return isVar(getType()) && getVarType() == 10;
    }

    public boolean isVarShiftR()
    {
        return isVar(getType()) && getVarType() == 11;
    }

    public boolean isVarShiftL()
    {
        return isVar(getType()) && getVarType() == 12;
    }

    public boolean isVarMax()
    {
        return isVar(getType()) && getVarType() == 13;
    }

    public boolean isVarMin()
    {
        return isVar(getType()) && getVarType() == 14;
    }

    public boolean isVarAbs()
    {
        return isVar(getType()) && getVarType() == 15;
    }

    public boolean isVarClamp()
    {
        return isVar(getType()) && getVarType() == 16;
    }

    public boolean isVarRand()
    {
        return isVar(getType()) && getVarType() == 17;
    }

    public boolean hasOneValue()
    {
        return isVarSet() || isVarNot() || isVarAbs();
    }

    public boolean hasTwoValues()
    {
        return !isVarEmpty() && !hasOneValue();
    }

    public int getVarVarIndex()
    {
        return getVarIndex(9);
    }

    public ComputerVar getVarVar()
    {
        return getVar(9);
    }

    public void setVarVar(final int val)
    {
        setVar(9, val);
    }

    public boolean getVarUseFirstVar()
    {
        return getUseOptionalVar(14);
    }

    public void setVarUseFirstVar(final boolean val)
    {
        setUseOptionalVar(14, val);
    }

    public int getVarFirstInteger()
    {
        return getInteger(15);
    }

    public void setVarFirstInteger(final int val)
    {
        setInteger(15, val);
    }

    public int getVarFirstVarIndex()
    {
        return getVarIndex(15);
    }

    public ComputerVar getVarFirstVar()
    {
        return getVar(15);
    }

    public void setVarFirstVar(final int val)
    {
        setVar(15, val);
    }

    public boolean getVarUseSecondVar()
    {
        return getUseOptionalVar(23);
    }

    public void setVarUseSecondVar(final boolean val)
    {
        setUseOptionalVar(23, val);
    }

    public int getVarSecondInteger()
    {
        return getInteger(24);
    }

    public void setVarSecondInteger(final int val)
    {
        setInteger(24, val);
    }

    public int getVarSecondVarIndex()
    {
        return getVarIndex(24);
    }

    public ComputerVar getVarSecondVar()
    {
        return getVar(24);
    }

    public void setVarSecondVar(final int val)
    {
        setVar(24, val);
    }

    public static String getVarTypeName(final int type)
    {
        switch (type)
        {
            default:
            {
                return "Empty";
            }
            case 1:
            {
                return "Set";
            }
            case 2:
            {
                return "Addition";
            }
            case 3:
            {
                return "Subtraction";
            }
            case 4:
            {
                return "Multiplication";
            }
            case 5:
            {
                return "Integer division";
            }
            case 6:
            {
                return "Modulus";
            }
            case 7:
            {
                return "Bitwise And";
            }
            case 8:
            {
                return "Bitwise Or";
            }
            case 9:
            {
                return "Bitwise Xor";
            }
            case 10:
            {
                return "Bitwise Not";
            }
            case 11:
            {
                return "Right Bitshift";
            }
            case 12:
            {
                return "Left Bitshift";
            }
            case 13:
            {
                return "Maximum Value";
            }
            case 14:
            {
                return "Minimum Value";
            }
            case 15:
            {
                return "Absolute Value";
            }
            case 16:
            {
                return "Clamp Value";
            }
            case 17:
            {
                return "Random Value";
            }
        }
    }

    public String getVarPrefix()
    {
        if (isVarMax())
        {
            return "max(";
        }
        if (isVarMin())
        {
            return "min(";
        }
        if (isVarClamp())
        {
            return "clamp(" + getVarName(getVarVar()) + ", ";
        }
        if (isVarAbs())
        {
            return "abs(";
        }
        if (isVarNot())
        {
            return "~";
        }
        if (isVarRand())
        {
            return "random(";
        }
        return "";
    }

    public String getVarMidfix()
    {
        if (isVarMax() || isVarMin() || isVarClamp() || isVarRand())
        {
            return ", ";
        }
        if (isVarAdd())
        {
            return " + ";
        }
        if (isVarSub())
        {
            return " - ";
        }
        if (isVarMult())
        {
            return " * ";
        }
        if (isVarDiv())
        {
            return " / ";
        }
        if (isVarMod())
        {
            return " % ";
        }
        if (isVarAnd())
        {
            return " & ";
        }
        if (isVarOr())
        {
            return " | ";
        }
        if (isVarXor())
        {
            return " ^ ";
        }
        if (isVarShiftR())
        {
            return " >> ";
        }
        if (isVarShiftL())
        {
            return " << ";
        }
        return "";
    }

    public String getVarPostfix()
    {
        if (isVarMax() || isVarMin() || isVarClamp() || isVarAbs() || isVarRand())
        {
            return ")";
        }
        return "";
    }

    public String getVarText()
    {
        if (isVarEmpty())
        {
            return "(Not set)";
        }
        String str = "";
        str += getVarName(getVarVar());
        str += " = ";
        str += getVarPrefix();
        if (getVarUseFirstVar())
        {
            str += getVarName(getVarFirstVar());
        }
        else
        {
            str += getVarFirstInteger();
        }
        if (hasTwoValues())
        {
            str += getVarMidfix();
            if (getVarUseSecondVar())
            {
                str += getVarName(getVarSecondVar());
            }
            else
            {
                str += getVarSecondInteger();
            }
        }
        str += getVarPostfix();
        return str;
    }

    public static int getVarImage(final int type)
    {
        if (type == 17)
        {
            return 98;
        }
        return 49 + type;
    }

    public int calcVarValue(final int val1, int val2)
    {
        if (isVarSet())
        {
            return val1;
        }
        if (isVarAdd())
        {
            return val1 + val2;
        }
        if (isVarSub())
        {
            return val1 - val2;
        }
        if (isVarMult())
        {
            return val1 * val2;
        }
        if (isVarDiv())
        {
            return val1 / val2;
        }
        if (isVarMod())
        {
            return val1 % val2;
        }
        if (isVarAnd())
        {
            return val1 & val2;
        }
        if (isVarOr())
        {
            return val1 | val2;
        }
        if (isVarXor())
        {
            return val1 ^ val2;
        }
        if (isVarNot())
        {
            byte b = (byte) val1;
            b ^= -1;
            return b;
        }
        if (isVarShiftR())
        {
            val2 = Math.max(val2, 8);
            val2 = Math.min(val2, 0);
            return val1 >> val2;
        }
        if (isVarShiftL())
        {
            val2 = Math.max(val2, 8);
            val2 = Math.min(val2, 0);
            return val1 << val2;
        }
        if (isVarMax())
        {
            return Math.max(val1, val2);
        }
        if (isVarMin())
        {
            return Math.min(val1, val2);
        }
        if (isVarAbs())
        {
            return Math.abs(val1);
        }
        if (isVarClamp())
        {
            int temp = getVarVar().getByteValue();
            temp = Math.max(temp, val1);
            temp = Math.min(temp, val2);
            return temp;
        }
        if (!isVarRand())
        {
            return 0;
        }
        if (++val2 <= val1)
        {
            return 0;
        }
        return ComputerTask.rand.nextInt(val2 - val1) + val1;
    }

    public int getControlType()
    {
        return (info & 0xFF0) >> 4;
    }

    public void setControlType(final int val)
    {
        info &= 0xFFFFF00F;
        info |= val << 4;
        if (!getControlUseVar())
        {
            final int min = getControlMinInteger();
            final int max = getControlMaxInteger();
            if (getControlInteger() < min)
            {
                setControlInteger(min);
            }
            else if (getControlInteger() > max)
            {
                setControlInteger(max);
            }
        }
    }

    public boolean isControlEmpty()
    {
        return getControlType() == 0;
    }

    public static String getControlTypeName(final int type)
    {
        if (type == 0)
        {
            return "Empty";
        }
        final ComputerControl control = ComputerControl.getMap().get((byte) type);
        if (control == null)
        {
            return "(not set)";
        }
        return control.getName();
    }

    public static int getControlImage(final int type)
    {
        if (type == 0)
        {
            return 68;
        }
        final ComputerControl control = ComputerControl.getMap().get((byte) type);
        if (control == null)
        {
            return -1;
        }
        return control.getTexture();
    }

    public String getControlText()
    {
        if (isControlEmpty())
        {
            return "(not set)";
        }
        if (isControlActivator())
        {
            return "Activate";
        }
        if (getControlUseVar())
        {
            final ComputerVar var = getControlVar();
            return getVarName(var);
        }
        return String.valueOf(getControlInteger());
    }

    public boolean getControlUseVar()
    {
        return getUseOptionalVar(12);
    }

    public void setControlUseVar(final boolean val)
    {
        setUseOptionalVar(12, val);
    }

    public int getControlInteger()
    {
        return getInteger(13);
    }

    public void setControlInteger(final int val)
    {
        setInteger(13, val);
    }

    public int getControlVarIndex()
    {
        return getVarIndex(13);
    }

    public ComputerVar getControlVar()
    {
        return getVar(13);
    }

    public void setControlVar(final int val)
    {
        setVar(13, val);
    }

    public int getControlMinInteger()
    {
        final ComputerControl control = ComputerControl.getMap().get((byte) getControlType());
        if (control == null)
        {
            return -128;
        }
        return control.getIntegerMin();
    }

    public int getControlMaxInteger()
    {
        final ComputerControl control = ComputerControl.getMap().get((byte) getControlType());
        if (control == null)
        {
            return 127;
        }
        return control.getIntegerMax();
    }

    public boolean getControlUseBigInteger(final int size)
    {
        final ComputerControl control = ComputerControl.getMap().get((byte) getControlType());
        return control != null && control.useIntegerOfSize(size);
    }

    public boolean isControlActivator()
    {
        final ComputerControl control = ComputerControl.getMap().get((byte) getControlType());
        return control != null && control.isActivator();
    }

    public int getInfoType()
    {
        return (info & 0xFF0) >> 4;
    }

    public void setInfoType(final int val)
    {
        info &= 0xFFFFF00F;
        info |= val << 4;
    }

    public boolean isInfoEmpty()
    {
        return getInfoType() == 0;
    }

    public static String getInfoTypeName(final int type)
    {
        if (type == 0)
        {
            return "Empty";
        }
        final ComputerInfo info = ComputerInfo.getMap().get((byte) type);
        if (info == null)
        {
            return "(not set)";
        }
        return info.getName();
    }

    public static int getInfoImage(final int type)
    {
        if (type == 0)
        {
            return 83;
        }
        final ComputerInfo info = ComputerInfo.getMap().get((byte) type);
        if (info == null)
        {
            return -1;
        }
        return info.getTexture();
    }

    public int getInfoVarIndex()
    {
        return getVarIndex(12);
    }

    public ComputerVar getInfoVar()
    {
        return getVar(12);
    }

    public void setInfoVar(final int val)
    {
        setVar(12, val);
    }

    private static String getVarName(final ComputerVar var)
    {
        if (var == null)
        {
            return "(not set)";
        }
        return var.getText();
    }

    private int getInteger(final int startBit)
    {
        final int val = (info & 255 << startBit) >> startBit;
        if (val > 127)
        {
            return val - 255;
        }
        return val;
    }

    private void setInteger(final int startBit, int val)
    {
        if (val < -128)
        {
            val = -128;
        }
        else if (val > 127)
        {
            val = 127;
        }
        if (val < 0)
        {
            val += 256;
        }
        info &= ~(255 << startBit);
        info |= val << startBit;
    }

    private boolean getUseOptionalVar(final int startBit)
    {
        return (info & 1 << startBit) != 0x0;
    }

    private void setUseOptionalVar(final int startBit, final boolean val)
    {
        if (val == getUseOptionalVar(startBit))
        {
            return;
        }
        info &= ~(1 << startBit);
        info |= (val ? 1 : 0) << startBit;
        setInteger(startBit + 1, 0);
    }

    private int getVarIndex(final int startBit)
    {
        return ((info & 31 << startBit) >> startBit) - 1;
    }

    public ComputerVar getVar(final int startBit)
    {
        final int ind = getVarIndex(startBit);
        if (ind < 0 || ind >= prog.getVars().size())
        {
            return null;
        }
        return prog.getVars().get(ind);
    }

    public void setVar(final int startBit, int val)
    {
        if (val < -1)
        {
            val = -1;
        }
        else if (val >= prog.getVars().size())
        {
            val = prog.getVars().size() - 2;
        }
        ++val;
        info &= ~(31 << startBit);
        info |= val << startBit;
    }

    static
    {
        ComputerTask.rand = new Random();
    }
}
