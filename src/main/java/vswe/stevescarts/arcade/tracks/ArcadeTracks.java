package vswe.stevescarts.arcade.tracks;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import vswe.stevescarts.Constants;
import vswe.stevescarts.arcade.ArcadeGame;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.realtimers.ModuleArcade;

import java.util.ArrayList;

public class ArcadeTracks extends ArcadeGame
{
    private TrackLevel currentMap;
    private boolean isMenuOpen;
    private boolean isRunning;
    private int currentStory;
    private int currentLevel;
    private int[] unlockedLevels;
    ArrayList<Cart> carts;
    private Cart player;
    private Cart enderman;
    private int playerStartX;
    private int playerStartY;
    private TrackOrientation.DIRECTION playerStartDirection;
    private int itemX;
    private int itemY;
    private boolean isItemTaken;
    private ArrayList<Track> tracks;
    private Track[][] trackMap;
    private int tick;
    private int currentMenuTab;
    private ArrayList<ScrollableList> lists;
    private boolean storySelected;
    private ScrollableList storyList;
    private ScrollableList mapList;
    private ScrollableList userList;
    private ArrayList<TrackLevel> userMaps;
    private boolean isUsingEditor;
    private boolean isSaveMenuOpen;
    private boolean failedToSave;
    private String saveName;
    private String lastSavedName;
    public static final int LEFT_MARGIN = 5;
    public static final int TOP_MARGIN = 5;
    private static String textureMenu;
    private static String textureGame;
    private final int BUTTON_COUNT = 14;
    private TrackEditor editorTrack;
    private TrackDetector editorDetectorTrack;
    private Track hoveringTrack;
    private boolean isEditorTrackDraging;
    private String validSaveNameCharacters;

    public ArcadeTracks(final ModuleArcade module)
    {
        super(module, Localization.ARCADE.OPERATOR);
        isMenuOpen = true;
        isRunning = false;
        currentStory = -1;
        currentLevel = -1;
        currentMenuTab = 0;
        saveName = "";
        lastSavedName = "";
        validSaveNameCharacters = "abcdefghijklmnopqrstuvwxyz0123456789 ";
        (carts = new ArrayList<>()).add(player = new Cart(0)
        {
            @Override
            public void onItemPickUp()
            {
                completeLevel();
                //TODO bring back sounds
//                ArcadeGame.playSound(SoundHandler.WIN, 1.0f, 1.0f);
            }

            @Override
            public void onCrash()
            {
                if (isPlayingFinalLevel() && currentStory < unlockedLevels.length - 1 && unlockedLevels[currentStory + 1] == -1)
                {
                    getModule().sendPacket(0, new byte[]{(byte) (currentStory + 1), 0});
                }
            }
        });
        carts.add(enderman = new Cart(1));
        (lists = new ArrayList<>()).add(storyList = new ScrollableList(this, 5, 40)
        {
            @Override
            public boolean isVisible()
            {
                return currentMenuTab == 0 && !storySelected;
            }
        });
        lists.add(mapList = new ScrollableList(this, 5, 40)
        {
            @Override
            public boolean isVisible()
            {
                return currentMenuTab == 0 && storySelected;
            }
        });
        lists.add(userList = new ScrollableList(this, 5, 40)
        {
            @Override
            public boolean isVisible()
            {
                return currentMenuTab == 1;
            }
        });
        (unlockedLevels = new int[TrackStory.stories.size()])[0] = 0;
        for (int i = 1; i < unlockedLevels.length; ++i)
        {
            unlockedLevels[i] = -1;
        }
        loadStories();
        if (getModule().getCart().level().isClientSide)
        {
            loadUserMaps();
        }
    }

    private void loadStories()
    {
        storyList.clearList();
        for (int i = 0; i < TrackStory.stories.size(); ++i)
        {
            if (unlockedLevels[i] > -1)
            {
                storyList.add(TrackStory.stories.get(i).getName());
            }
            else
            {
                storyList.add(null);
            }
        }
    }

    private void loadMaps()
    {
        final int story = storyList.getSelectedIndex();
        if (story != -1)
        {
            final ArrayList<TrackLevel> levels = TrackStory.stories.get(story).getLevels();
            mapList.clearList();
            for (int i = 0; i < levels.size(); ++i)
            {
                if (unlockedLevels[story] >= i)
                {
                    mapList.add(levels.get(i).getName());
                }
                else
                {
                    mapList.add(null);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void loadUserMaps()
    {
        userList.clearList();
        userMaps = TrackLevel.loadMapsFromFolder();
        for (TrackLevel userMap : userMaps)
        {
            userList.add(userMap.getName());
        }
    }

    private void loadMap(final int story, final int level)
    {
        currentStory = story;
        currentLevel = level;
        loadMap(TrackStory.stories.get(story).getLevels().get(level));
    }

    private void loadMap(final TrackLevel map)
    {
        isUsingEditor = false;
        trackMap = new Track[27][10];
        tracks = new ArrayList<>();
        for (final Track track : map.getTracks())
        {
            final Track newtrack = track.copy();
            tracks.add(newtrack);
            if (newtrack.getX() >= 0 && newtrack.getX() < trackMap.length && newtrack.getY() >= 0 && newtrack.getY() < trackMap[0].length)
            {
                trackMap[newtrack.getX()][newtrack.getY()] = newtrack;
            }
        }
        hoveringTrack = null;
        editorTrack = null;
        editorDetectorTrack = null;
        currentMap = map;
        isRunning = false;
        playerStartX = currentMap.getPlayerStartX();
        playerStartY = currentMap.getPlayerStartY();
        playerStartDirection = currentMap.getPlayerStartDirection();
        itemX = currentMap.getItemX();
        itemY = currentMap.getItemY();
        resetPosition();
    }

    private void resetPosition()
    {
        tick = 0;
        player.setX(playerStartX);
        player.setY(playerStartY);
        isItemTaken = false;
        player.setDirection(TrackOrientation.DIRECTION.STILL);
        enderman.setAlive(false);
    }

    public Track[][] getTrackMap()
    {
        return trackMap;
    }

    public Cart getEnderman()
    {
        return enderman;
    }

    private boolean isPlayingFinalLevel()
    {
        return isPlayingNormalLevel() && currentLevel == TrackStory.stories.get(currentStory).getLevels().size() - 1;
    }

    private boolean isUsingEditor()
    {
        return isUsingEditor;
    }

    private boolean isPlayingUserLevel()
    {
        return currentStory == -1;
    }

    private boolean isPlayingNormalLevel()
    {
        return !isUsingEditor() && !isPlayingUserLevel();
    }

    @Override
    public void update()
    {
        super.update();
        if (isRunning)
        {
            if (tick == 3)
            {
                for (final Cart cart : carts)
                {
                    cart.move(this);
                }
                tick = 0;
            }
            else
            {
                ++tick;
            }
        }
    }

    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        if (isSaveMenuOpen)
        {
            final int[] menu = getSaveMenuArea();
            if (failedToSave)
            {
                getModule().drawString(guiGraphics, gui, Localization.ARCADE.SAVE_ERROR.translate(), menu[0] + 3, menu[1] + 3, 16711680);
            }
            else
            {
                getModule().drawString(guiGraphics, gui, Localization.ARCADE.SAVE.translate(), menu[0] + 3, menu[1] + 3, 4210752);
            }
            getModule().drawString(guiGraphics, gui, saveName + ((saveName.length() < 15 && getModule().getCart().level().getGameTime() % 20L < 10L) ? "|" : ""), menu[0] + 5, menu[1] + 16, 16777215);
        }
        else if (isMenuOpen)
        {
            for (final ScrollableList list : lists)
            {
                list.drawForeground(guiGraphics, gui);
            }
            if (currentMenuTab == 0 || currentMenuTab == 1)
            {
                final int[] menu = getMenuArea();
                String str;
                if (currentMenuTab == 1)
                {
                    str = Localization.ARCADE.USER_MAPS.translate();
                }
                else if (storySelected)
                {
                    str = TrackStory.stories.get(storyList.getSelectedIndex()).getName();
                }
                else
                {
                    str = Localization.ARCADE.STORIES.translate();
                }
                getModule().drawString(guiGraphics, gui, str, menu[0] + 5, menu[1] + 32, 4210752);
            }
            else
            {
                final int[] menu = getMenuArea();
                getModule().drawSplitString(guiGraphics, gui, Localization.ARCADE.HELP.translate(), menu[0] + 10, menu[1] + 20, menu[2] - 20, 4210752);
            }
        }
        else
        {
            for (final LevelMessage message : currentMap.getMessages())
            {
                if (message.isVisible(isRunning, isRunning && player.getDireciotn() == TrackOrientation.DIRECTION.STILL, isRunning && isItemTaken))
                {
                    getModule().drawSplitString(guiGraphics, gui, message.getMessage(), 9 + message.getX() * 16, 9 + message.getY() * 16, message.getW() * 16, 4210752);
                }
            }
            if (isUsingEditor())
            {
                getModule().drawString(guiGraphics, gui, "1-5 - " + Localization.ARCADE.INSTRUCTION_SHAPE.translate(), 10, 180, 4210752);
                getModule().drawString(guiGraphics, gui, "R - " + Localization.ARCADE.INSTRUCTION_ROTATE_TRACK.translate(), 10, 190, 4210752);
                getModule().drawString(guiGraphics, gui, "F - " + Localization.ARCADE.INSTRUCTION_FLIP_TRACK.translate(), 10, 200, 4210752);
                getModule().drawString(guiGraphics, gui, "A - " + Localization.ARCADE.INSTRUCTION_DEFAULT_DIRECTION.translate(), 10, 210, 4210752);
                getModule().drawString(guiGraphics, gui, "T - " + Localization.ARCADE.INSTRUCTION_TRACK_TYPE.translate(), 10, 220, 4210752);
                getModule().drawString(guiGraphics, gui, "D - " + Localization.ARCADE.INSTRUCTION_DELETE_TRACK.translate(), 10, 230, 4210752);
                getModule().drawString(guiGraphics, gui, "C - " + Localization.ARCADE.INSTRUCTION_COPY_TRACK.translate(), 10, 240, 4210752);
                getModule().drawString(guiGraphics, gui, "S - " + Localization.ARCADE.INSTRUCTION_STEVE.translate(), 330, 180, 4210752);
                getModule().drawString(guiGraphics, gui, "X - " + Localization.ARCADE.INSTRUCTION_MAP.translate(), 330, 190, 4210752);
                getModule().drawString(guiGraphics, gui, Localization.ARCADE.LEFT_MOUSE.translate() + " - " + Localization.ARCADE.INSTRUCTION_PLACE_TRACK.translate(), 330, 200, 4210752);
                getModule().drawString(guiGraphics, gui, Localization.ARCADE.RIGHT_MOUSE.translate() + " - " + Localization.ARCADE.INSTRUCTION_DESELECT_TRACK.translate(), 330, 210, 4210752);
            }
        }
    }

    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        if (!isSaveMenuOpen && isMenuOpen)
        {
            ResourceHelper.bindResource(ArcadeTracks.textureMenu);
            getModule().drawImage(guiGraphics, gui, getMenuArea(), 0, 0);
            for (int i = 0; i < 3; ++i)
            {
                final int[] rect = getMenuTabArea(i);
                final boolean active = getModule().inRect(x, y, rect);
                final boolean hidden = !active && i == currentMenuTab;
                if (!hidden)
                {
                    getModule().drawImage(guiGraphics, gui, rect[0], rect[1] + rect[3], 0, active ? 114 : 113, rect[2], 1);
                }
            }
            for (final ScrollableList list : lists)
            {
                list.drawBackground(guiGraphics, gui, x, y);
            }
        }
        else if (currentMap != null)
        {
            ResourceHelper.bindResource(ArcadeTracks.textureGame);
            if (isUsingEditor() && !isRunning)
            {
                for (int i = 0; i < trackMap.length; ++i)
                {
                    for (int j = 0; j < trackMap[0].length; ++j)
                    {
                        getModule().drawImage(guiGraphics, gui, 5 + i * 16, 5 + j * 16, 16, 128, 16, 16);
                    }
                }
            }
            for (final Track track : tracks)
            {
                getModule().drawImage(guiGraphics, gui, getTrackArea(track.getX(), track.getY()), 16 * track.getU(), 16 * track.getV(), track.getRotation());
            }
            if (isUsingEditor())
            {
                if (editorDetectorTrack != null && !isRunning)
                {
                    editorDetectorTrack.drawOverlay(guiGraphics, getModule(), gui, editorDetectorTrack.getX() * 16 + 8, editorDetectorTrack.getY() * 16 + 8, isRunning);
                    getModule().drawImage(guiGraphics, gui, 5 + editorDetectorTrack.getX() * 16, 5 + editorDetectorTrack.getY() * 16, 32, 128, 16, 16);
                }
            }
            else
            {
                for (final Track track : tracks)
                {
                    track.drawOverlay(guiGraphics, getModule(), gui, x, y, isRunning);
                }
            }
            if (!isItemTaken)
            {
                int itemIndex = 0;
                if (isPlayingFinalLevel())
                {
                    itemIndex = 1;
                }
                getModule().drawImage(guiGraphics, gui, 5 + itemX * 16, 5 + itemY * 16, 16 * itemIndex, 240, 16, 16);
            }
            for (final Cart cart : carts)
            {
                cart.render(guiGraphics, this, gui, tick);
            }
            if (isUsingEditor() && !isRunning)
            {
                getModule().drawImage(guiGraphics, gui, 5 + playerStartX * 16, 5 + playerStartY * 16, 162, 212, 8, 8, playerStartDirection.getRenderRotation());
            }
            if (!isMenuOpen && editorTrack != null)
            {
                getModule().drawImage(guiGraphics, gui, x - 8, y - 8, 16 * editorTrack.getU(), 16 * editorTrack.getV(), 16, 16, editorTrack.getRotation());
            }
            if (isSaveMenuOpen)
            {
                final int[] rect2 = getSaveMenuArea();
                getModule().drawImage(guiGraphics, gui, rect2, 0, 144);
            }
        }
        ResourceHelper.bindResource(ArcadeTracks.textureGame);
        for (int i = 0; i < 14; ++i)
        {
            if (isButtonVisible(i))
            {
                final int[] rect = getButtonArea(i);
                final int srcX = isButtonDisabled(i) ? 208 : (getModule().inRect(x, y, rect) ? 224 : 240);
                final int srcY = i * 16;
                getModule().drawImage(guiGraphics, gui, rect, srcX, srcY);
            }
        }
    }

    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        for (int i = 0; i < 14; ++i)
        {
            if (!isButtonDisabled(i) && isButtonVisible(i))
            {
                getModule().drawStringOnMouseOver(guiGraphics, gui, getButtonText(i), x, y, getButtonArea(i));
            }
        }
    }

    @Override
    public void mouseMovedOrUp(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (isSaveMenuOpen)
        {
            return;
        }
        if (isMenuOpen)
        {
            for (final ScrollableList list : lists)
            {
                list.mouseMovedOrUp(gui, x, y, button);
            }
        }
        if (currentMap != null && isUsingEditor())
        {
            final int x2 = x - 5;
            final int y2 = y - 5;
            final int gridX = x2 / 16;
            final int gridY = y2 / 16;
            if (gridX >= 0 && gridX < trackMap.length && gridY >= 0 && gridY < trackMap[0].length)
            {
                hoveringTrack = trackMap[gridX][gridY];
            }
            else
            {
                hoveringTrack = null;
            }
        }
        handleEditorTrack(x, y, button, false);
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (!isSaveMenuOpen)
        {
            if (isMenuOpen)
            {
                if (!getModule().inRect(x, y, getMenuArea()))
                {
                    if (currentMap != null)
                    {
                        isMenuOpen = false;
                    }
                }
                else
                {
                    for (int i = 0; i < 3; ++i)
                    {
                        if (i != currentMenuTab && getModule().inRect(x, y, getMenuTabArea(i)))
                        {
                            currentMenuTab = i;
                            break;
                        }
                    }
                    for (final ScrollableList list : lists)
                    {
                        list.mouseClicked(gui, x, y, button);
                    }
                }
            }
            else
            {
                if (!isRunning)
                {
                    for (final Track track : tracks)
                    {
                        if (getModule().inRect(x, y, getTrackArea(track.getX(), track.getY())))
                        {
                            if (isUsingEditor())
                            {
                                if (editorTrack != null)
                                {
                                    continue;
                                }
                                track.onEditorClick(this);
                            }
                            else
                            {
                                track.onClick(this);
                            }
                        }
                    }
                }
                handleEditorTrack(x, y, button, true);
            }
        }
        for (int i = 0; i < 14; ++i)
        {
            final int[] rect = getButtonArea(i);
            if (getModule().inRect(x, y, rect) && isButtonVisible(i) && !isButtonDisabled(i))
            {
                buttonClicked(i);
                break;
            }
        }
    }

    public void completeLevel()
    {
        if (isPlayingNormalLevel())
        {
            final int nextLevel = currentLevel + 1;
            if (nextLevel > unlockedLevels[currentStory])
            {
                getModule().sendPacket(0, new byte[]{(byte) currentStory, (byte) nextLevel});
            }
        }
    }

    public int[] getMenuArea()
    {
        return new int[]{93, 27, 256, 113};
    }

    private int[] getMenuTabArea(final int id)
    {
        final int[] menu = getMenuArea();
        return new int[]{menu[0] + 1 + id * 85, menu[1] + 1, 84, 12};
    }

    private int[] getSaveMenuArea()
    {
        return new int[]{172, 60, 99, 47};
    }

    private int[] getButtonArea(int id)
    {
        if (id == 4 || id == 5)
        {
            final int[] menu = getMenuArea();
            return new int[]{menu[0] + 235 - 18 * (id - 4), menu[1] + 20, 16, 16};
        }
        if (id > 5 && id < 10)
        {
            final int[] menu = getMenuArea();
            return new int[]{menu[0] + 235, menu[1] + 20 + (id - 6) * 18, 16, 16};
        }
        if (id >= 12 && id < 14)
        {
            final int[] menu = getSaveMenuArea();
            return new int[]{menu[0] + menu[2] - 18 * (id - 11) - 2, menu[1] + menu[3] - 18, 16, 16};
        }
        if (id >= 10 && id < 12)
        {
            id -= 6;
        }
        return new int[]{455, 26 + id * 18, 16, 16};
    }

    private boolean isButtonVisible(final int id)
    {
        if (id == 4 || id == 5)
        {
            return isMenuOpen && currentMenuTab == 0;
        }
        if (id > 5 && id < 10)
        {
            return isMenuOpen && currentMenuTab == 1;
        }
        if (id >= 10 && id < 12)
        {
            return isUsingEditor();
        }
        return id < 12 || id >= 14 || isSaveMenuOpen;
    }

    private boolean isButtonDisabled(final int id)
    {
        switch (id)
        {
            case 0:
            {
                return isRunning || isMenuOpen || isSaveMenuOpen;
            }
            case 1:
            {
                return isRunning || isMenuOpen || isSaveMenuOpen;
            }
            case 2:
            {
                return !isRunning || isSaveMenuOpen;
            }
            case 3:
            {
                return isMenuOpen || isSaveMenuOpen || !isPlayingNormalLevel() || currentLevel + 1 > unlockedLevels[currentStory];
            }
            case 4:
            {
                return (storySelected ? mapList : storyList).getSelectedIndex() == -1;
            }
            case 5:
            {
                return !storySelected;
            }
            case 6:
            case 8:
            {
                return userList.getSelectedIndex() == -1;
            }
            case 7:
            case 9:
            case 12:
            {
                return false;
            }
            case 10:
            case 11:
            {
                return isMenuOpen || isSaveMenuOpen || isRunning;
            }
            case 13:
            {
                return saveName.length() == 0;
            }
            default:
            {
                return true;
            }
        }
    }

    private void buttonClicked(final int id)
    {
        switch (id)
        {
            case 0:
            {
                for (final Track track : tracks)
                {
                    track.saveBackup();
                }
                player.setDirection(playerStartDirection);
                isRunning = true;
                break;
            }
            case 1:
            {
                isMenuOpen = true;
                editorTrack = null;
                break;
            }
            case 2:
            {
                for (final Track track : tracks)
                {
                    track.loadBackup();
                }
                resetPosition();
                isRunning = false;
                break;
            }
            case 3:
            {
                loadMap(currentStory, currentLevel + 1);
                break;
            }
            case 4:
            {
                if (storySelected)
                {
                    loadMap(storyList.getSelectedIndex(), mapList.getSelectedIndex());
                    isMenuOpen = false;
                    break;
                }
                storySelected = true;
                mapList.clear();
                loadMaps();
                break;
            }
            case 5:
            {
                storySelected = false;
                break;
            }
            case 6:
            {
                currentStory = -1;
                loadMap(userMaps.get(userList.getSelectedIndex()));
                isMenuOpen = false;
                break;
            }
            case 7:
            {
                loadMap(TrackLevel.editor);
                isMenuOpen = false;
                lastSavedName = "";
                isUsingEditor = true;
                break;
            }
            case 8:
            {
                final TrackLevel mapToEdit = userMaps.get(userList.getSelectedIndex());
                loadMap(mapToEdit);
                lastSavedName = mapToEdit.getName();
                isMenuOpen = false;
                isUsingEditor = true;
                break;
            }
            case 9:
            {
                userList.clear();
                if (getModule().getCart().level().isClientSide)
                {
                    loadUserMaps();
                    break;
                }
                break;
            }
            case 10:
            {
                if (lastSavedName.length() == 0)
                {
                    isSaveMenuOpen = true;
                    failedToSave = false;
                    break;
                }
                save(lastSavedName);
                break;
            }
            case 11:
            {
                isSaveMenuOpen = true;
                failedToSave = false;
                break;
            }
            case 13:
            {
                if (save(saveName))
                {
                    saveName = "";
                    isSaveMenuOpen = false;
                    break;
                }
                break;
            }
            case 12:
            {
                isSaveMenuOpen = false;
                break;
            }
        }
    }

    private String getButtonText(final int id)
    {
        switch (id)
        {
            case 0:
            {
                return Localization.ARCADE.BUTTON_START.translate();
            }
            case 1:
            {
                return Localization.ARCADE.BUTTON_MENU.translate();
            }
            case 2:
            {
                return Localization.ARCADE.BUTTON_STOP.translate();
            }
            case 3:
            {
                return Localization.ARCADE.BUTTON_NEXT.translate();
            }
            case 4:
            {
                return storySelected ? Localization.ARCADE.BUTTON_START_LEVEL.translate() : Localization.ARCADE.BUTTON_SELECT_STORY.translate();
            }
            case 5:
            {
                return Localization.ARCADE.BUTTON_SELECT_OTHER_STORY.translate();
            }
            case 6:
            {
                return Localization.ARCADE.BUTTON_START_LEVEL.translate();
            }
            case 7:
            {
                return Localization.ARCADE.BUTTON_CREATE_LEVEL.translate();
            }
            case 8:
            {
                return Localization.ARCADE.BUTTON_EDIT_LEVEL.translate();
            }
            case 9:
            {
                return Localization.ARCADE.BUTTON_REFRESH.translate();
            }
            case 10:
            {
                return Localization.ARCADE.BUTTON_SAVE.translate();
            }
            case 11:
            {
                return Localization.ARCADE.BUTTON_SAVE_AS.translate();
            }
            case 12:
            {
                return Localization.ARCADE.BUTTON_CANCEL.translate();
            }
            case 13:
            {
                return Localization.ARCADE.BUTTON_SAVE.translate();
            }
            default:
            {
                return "Hello, I'm a button";
            }
        }
    }

    public static int[] getTrackArea(final int x, final int y)
    {
        return new int[]{5 + 16 * x, 5 + 16 * y, 16, 16};
    }

    public boolean isItemOnGround()
    {
        return !isItemTaken;
    }

    public void pickItemUp()
    {
        isItemTaken = true;
    }

    public int getItemX()
    {
        return itemX;
    }

    public int getItemY()
    {
        return itemY;
    }

    @Override
    public void Save(final CompoundTag tagCompound, final int id)
    {
        for (int i = 0; i < unlockedLevels.length; ++i)
        {
            tagCompound.putByte(getModule().generateNBTName("Unlocked" + i, id), (byte) unlockedLevels[i]);
        }
    }

    @Override
    public void Load(final CompoundTag tagCompound, final int id)
    {
        for (int i = 0; i < unlockedLevels.length; ++i)
        {
            unlockedLevels[i] = tagCompound.getByte(getModule().generateNBTName("Unlocked" + i, id));
        }
        loadStories();
    }

    @Override
    public void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 0)
        {
            unlockedLevels[data[0]] = data[1];
            if (unlockedLevels[data[0]] > TrackStory.stories.get(data[0]).getLevels().size() - 1)
            {
                unlockedLevels[data[0]] = TrackStory.stories.get(data[0]).getLevels().size() - 1;
            }
        }
    }

    @Override
    public void checkGuiData(final Object[] info)
    {
        for (int i = 0; i < unlockedLevels.length; ++i)
        {
            getModule().updateGuiData(info, i, (short) unlockedLevels[i]);
        }
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        if (id >= 0 && id < unlockedLevels.length)
        {
            if ((unlockedLevels[id] = data) != 0)
            {
                loadMaps();
            }
            else
            {
                loadStories();
            }
        }
    }

    public void setEditorTrack(final TrackEditor track)
    {
        if (editorTrack != null)
        {
            track.setType(editorTrack.getType());
        }
        editorTrack = track;
    }

    public void setEditorDetectorTrack(final TrackDetector track)
    {
        if (track.equals(editorDetectorTrack))
        {
            editorDetectorTrack = null;
        }
        else
        {
            editorDetectorTrack = track;
        }
    }

    public TrackDetector getEditorDetectorTrack()
    {
        return editorDetectorTrack;
    }

    //TODO, Keys are now ints
    @Override
    public void keyPress(final GuiMinecart gui, final int character, final int extraInformation)
    {
        if (isSaveMenuOpen)
        {
            if (saveName.length() < 15 && validSaveNameCharacters.indexOf(Character.toLowerCase(character)) != -1)
            {
                saveName += character;
            }
            else if (extraInformation == 14 && saveName.length() > 0)
            {
                saveName = saveName.substring(0, saveName.length() - 1);
            }
        }
        else
        {
            if (!isUsingEditor() || isRunning)
            {
                return;
            }
            Track track;
            if (editorTrack != null)
            {
                track = editorTrack;
            }
            else
            {
                track = hoveringTrack;
            }
            switch (Character.toLowerCase(character))
            {
                case 'a':
                {
                    if (track != null && track.getOrientation().getOpposite() != null)
                    {
                        track.setOrientation(track.getOrientation().getOpposite());
                        break;
                    }
                    break;
                }
                case 'r':
                {
                    if (track != null)
                    {
                        for (final TrackOrientation orientation : TrackOrientation.ALL)
                        {
                            if (orientation.getV() == track.getV() && ((orientation.getV() == 1 && orientation.getRotation() != track.getRotation()) || orientation.getRotation() == track.getRotation().getNextRotation()))
                            {
                                track.setOrientation(orientation);
                                break;
                            }
                        }
                        break;
                    }
                    break;
                }
                case 'f':
                {
                    if (track != null)
                    {
                        for (final TrackOrientation orientation : TrackOrientation.ALL)
                        {
                            if (orientation.getV() == track.getV() && (orientation.getV() == 2 || orientation.getV() == 3) && orientation.getRotation() == track.getRotation().getFlippedRotation())
                            {
                                track.setOrientation(orientation);
                                break;
                            }
                        }
                        break;
                    }
                    break;
                }
                case 't':
                {
                    if (editorTrack != null)
                    {
                        editorTrack.nextType();
                        break;
                    }
                    break;
                }
                case '1':
                {
                    setEditorTrack(new TrackEditor(TrackOrientation.CORNER_DOWN_RIGHT));
                    break;
                }
                case '2':
                {
                    setEditorTrack(new TrackEditor(TrackOrientation.STRAIGHT_VERTICAL));
                    break;
                }
                case '3':
                {
                    setEditorTrack(new TrackEditor(TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_RIGHT));
                    break;
                }
                case '4':
                {
                    setEditorTrack(new TrackEditor(TrackOrientation.JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_DOWN));
                    break;
                }
                case '5':
                {
                    setEditorTrack(new TrackEditor(TrackOrientation.JUNCTION_4WAY));
                    break;
                }
                case 'd':
                {
                    if (hoveringTrack != null)
                    {
                        tracks.remove(hoveringTrack);
                        if (hoveringTrack.getX() >= 0 && hoveringTrack.getX() < trackMap.length && hoveringTrack.getY() >= 0 && hoveringTrack.getY() < trackMap[0].length)
                        {
                            trackMap[hoveringTrack.getX()][hoveringTrack.getY()] = null;
                        }
                        hoveringTrack = null;
                        break;
                    }
                    break;
                }
                case 'c':
                {
                    if (editorTrack == null && hoveringTrack != null)
                    {
                        setEditorTrack(new TrackEditor(hoveringTrack.getOrientation()));
                        editorTrack.setType(hoveringTrack.getU());
                        break;
                    }
                    break;
                }
                case 's':
                {
                    if (hoveringTrack != null)
                    {
                        if (playerStartX == hoveringTrack.getX() && playerStartY == hoveringTrack.getY())
                        {
                            playerStartDirection = playerStartDirection.getLeft();
                        }
                        else
                        {
                            playerStartX = hoveringTrack.getX();
                            playerStartY = hoveringTrack.getY();
                        }
                        resetPosition();
                        break;
                    }
                    break;
                }
                case 'x':
                {
                    if (hoveringTrack != null)
                    {
                        itemX = hoveringTrack.getX();
                        itemY = hoveringTrack.getY();
                        break;
                    }
                    break;
                }
            }
        }
    }

    private void handleEditorTrack(final int x, final int y, final int button, final boolean clicked)
    {
        if (isRunning)
        {
            isEditorTrackDraging = false;
            return;
        }
        if (editorTrack != null)
        {
            if ((clicked && button == 0) || (!clicked && button == -1 && isEditorTrackDraging))
            {
                final int x2 = x - 5;
                final int y2 = y - 5;
                final int gridX = x2 / 16;
                final int gridY = y2 / 16;
                if (gridX >= 0 && gridX < trackMap.length && gridY >= 0 && gridY < trackMap[0].length)
                {
                    if (trackMap[gridX][gridY] == null)
                    {
                        final Track newtrack = editorTrack.getRealTrack(gridX, gridY);
                        trackMap[gridX][gridY] = newtrack;
                        tracks.add(newtrack);
                    }
                    isEditorTrackDraging = true;
                }
            }
            else if (button == 1 || (!clicked && isEditorTrackDraging))
            {
                if (clicked)
                {
                    editorTrack = null;
                }
                isEditorTrackDraging = false;
            }
        }
    }

    @Override
    public boolean disableStandardKeyFunctionality()
    {
        return isSaveMenuOpen;
    }

    @OnlyIn(Dist.CLIENT)
    private boolean save(String name)
    {
        if (TrackLevel.saveMap(name, playerStartX, playerStartY, playerStartDirection, itemX, itemY, tracks))
        {
            lastSavedName = name;
            loadUserMaps();
            return true;
        }
        saveName = name;
        failedToSave = true;
        isSaveMenuOpen = true;
        return false;
    }

    static
    {
        ArcadeTracks.textureMenu = "/gui/trackgamemenu.png";
        ArcadeTracks.textureGame = "/gui/trackgame.png";
    }
}
