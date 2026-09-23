package vswe.stevescarts.api.modules.data;

import net.minecraft.network.chat.Component;


import java.util.ArrayList;

public class ModuleDataGroup {
    private final ArrayList<ModuleData> modules;
    private String name;
    private int count;

    public ModuleDataGroup(final String name) {
        this.name = name;
        count = 1;
        modules = new ArrayList<>();
    }

    public static ModuleDataGroup getCombinedGroup(final String name, final ModuleDataGroup group1, final ModuleDataGroup group2) {
        final ModuleDataGroup newgroup = group1.copy();
        newgroup.add(group2);
        newgroup.name = name;
        return newgroup;
    }

    public String getName() {
        return Component.translatable(name + (getCount() == 1 ? ".singular" : ".plural")).getString();
    }

    public ArrayList<ModuleData> getModules() {
        return modules;
    }

    public int getCount() {
        return count;
    }

    public ModuleDataGroup setCount(final int count) {
        this.count = count;
        return this;
    }

    public ModuleDataGroup add(final ModuleData module) {
        modules.add(module);
        return this;
    }

    public ModuleDataGroup copy() {
        final ModuleDataGroup newObj = new ModuleDataGroup(name).setCount(getCount());
        for (final ModuleData obj : getModules()) {
            newObj.add(obj);
        }
        return newObj;
    }

    public ModuleDataGroup copy(final int count) {
        final ModuleDataGroup newObj = new ModuleDataGroup(name).setCount(count);
        for (final ModuleData obj : getModules()) {
            newObj.add(obj);
        }
        return newObj;
    }

    public String getCountName() {
        switch (count) {
            case 1 -> {
                return Component.translatable("info.stevescarts.moduleCount1").getString();
            }
            case 2 -> {
                return Component.translatable("info.stevescarts.moduleCount2").getString();
            }
            case 3 -> {
                return Component.translatable("info.stevescarts.moduleCount3").getString();
            }
            default -> {
                return "???";
            }
        }
    }

    public void add(final ModuleDataGroup group) {
        for (final ModuleData obj : group.getModules()) {
            add(obj);
        }
    }
}
