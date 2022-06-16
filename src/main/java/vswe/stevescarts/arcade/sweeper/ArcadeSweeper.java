package vswe.stevescarts.arcade.sweeper;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.arcade.ArcadeGame;
import vswe.stevescarts.arcade.tracks.TrackStory;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.handlers.SoundHandler;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.realtimers.ModuleArcade;

public class ArcadeSweeper extends ArcadeGame
{
    private Tile[][] tiles;
    protected boolean isPlaying;
    protected boolean hasFinished;
    private int currentGameType;
    private int ticks;
    protected int creepersLeft;
    protected int emptyLeft;
    private boolean hasStarted;
    private int[] highscore;
    private int highscoreTicks;
    private static String textureMenu;

    public ArcadeSweeper(final ModuleArcade module)
    {
        super(module, Localization.ARCADE.CREEPER);
        highscore = new int[]{999, 999, 999};
        newGame(currentGameType);
    }

    private void newGame(final int size)
    {
        switch (size)
        {
            case 0:
            {
                newGame(9, 9, 10);
                break;
            }
            case 1:
            {
                newGame(16, 16, 40);
                break;
            }
            case 2:
            {
                newGame(30, 16, 99);
                break;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void update()
    {
        super.update();
        if (hasStarted && isPlaying && !hasFinished && ticks < 19980)
        {
            ++ticks;
        }
        if (highscoreTicks > 0)
        {
            ++highscoreTicks;
            if (highscoreTicks == 78)
            {
                highscoreTicks = 0;
                ArcadeGame.playSound(SoundHandler.HIGH_SCORE, 1.0f, 1.0f);
            }
        }
    }

    private void newGame(final int width, final int height, final int totalCreepers)
    {
        isPlaying = true;
        ticks = 0;
        creepersLeft = totalCreepers;
        emptyLeft = width * height - totalCreepers;
        hasStarted = false;
        hasFinished = false;
        highscoreTicks = 0;
        tiles = new Tile[width][height];
        for (int x = 0; x < width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                tiles[x][y] = new Tile(this);
            }
        }
        for (int creepers = 0; creepers < totalCreepers; ++creepers)
        {
            final int x2 = getModule().getCart().random.nextInt(width);
            final int y2 = getModule().getCart().random.nextInt(height);
            if (!tiles[x2][y2].isCreeper())
            {
                tiles[x2][y2].setCreeper();
            }
        }
        for (int x2 = 0; x2 < width; ++x2)
        {
            for (int y2 = 0; y2 < height; ++y2)
            {
                if (!tiles[x2][y2].isCreeper())
                {
                    int count = 0;
                    for (int i = -1; i <= 1; ++i)
                    {
                        for (int j = -1; j <= 1; ++j)
                        {
                            if (i != 0 || j != 0)
                            {
                                final int x3 = x2 + i;
                                final int y3 = y2 + j;
                                if (x3 >= 0 && y3 >= 0 && x3 < width && y3 < height && tiles[x3][y3].isCreeper())
                                {
                                    ++count;
                                }
                            }
                        }
                    }
                    tiles[x2][y2].setNearbyCreepers(count);
                }
            }
        }
    }

    private int getMarginLeft()
    {
        return (443 - tiles.length * 10) / 2;
    }

    private int getMarginTop()
    {
        return (168 - tiles[0].length * 10) / 2;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource(ArcadeSweeper.textureMenu);
        for (int i = 0; i < tiles.length; ++i)
        {
            for (int j = 0; j < tiles[0].length; ++j)
            {
                tiles[i][j].draw(matrixStack, this, gui, getMarginLeft() + i * 10, getMarginTop() + j * 10, x, y);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void mouseClicked(final GuiMinecart gui, int x, int y, final int button)
    {
        if (!isPlaying)
        {
            return;
        }
        x -= getMarginLeft();
        y -= getMarginTop();
        final int xc = x / 10;
        final int yc = y / 10;
        if (button == 0)
        {
            openTile(xc, yc, true);
        }
        else if (button == 1 && isValidCoordinate(xc, yc))
        {
            hasStarted = true;
            ArcadeGame.playSound(SoundHandler.FLAG_CLICK, 1.0f, 1.0f);
            tiles[xc][yc].mark();
        }
        else if (button == 2 && isValidCoordinate(xc, yc) && tiles[xc][yc].getState() == Tile.TILE_STATE.OPENED)
        {
            ArcadeGame.playSound(SoundHandler.CLICK, 1.0f, 1.0f);
            int nearby = tiles[xc][yc].getNearbyCreepers();
            if (nearby != 0)
            {
                for (int i = -1; i <= 1; ++i)
                {
                    for (int j = -1; j <= 1; ++j)
                    {
                        if ((i != 0 || j != 0) && isValidCoordinate(xc + i, yc + j) && tiles[xc + i][yc + j].getState() == Tile.TILE_STATE.FLAGGED)
                        {
                            --nearby;
                        }
                    }
                }
                if (nearby == 0)
                {
                    for (int i = -1; i <= 1; ++i)
                    {
                        for (int j = -1; j <= 1; ++j)
                        {
                            if (i != 0 || j != 0)
                            {
                                openTile(xc + i, yc + j, false);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isValidCoordinate(final int x, final int y)
    {
        return x >= 0 && y >= 0 && x < tiles.length && y < tiles[0].length;
    }

    private void openTile(final int x, final int y, final boolean first)
    {
        if (isValidCoordinate(x, y))
        {
            hasStarted = true;
            final Tile.TILE_OPEN_RESULT result = tiles[x][y].open();
            if (emptyLeft == 0)
            {
                hasFinished = true;
                isPlaying = false;
                ArcadeGame.playSound(SoundHandler.GOOD_JOB, 1.0f, 1.0f);
                if (highscore[currentGameType] > ticks / 20)
                {
                    highscoreTicks = 1;
                    final int val = ticks / 20;
                    final byte byte1 = (byte) (val & 0xFF);
                    final byte byte2 = (byte) ((val & 0xFF00) >> 8);
                    getModule().sendPacket(3, new byte[]{(byte) currentGameType, byte1, byte2});
                }
            }
            else if (result == Tile.TILE_OPEN_RESULT.BLOB)
            {
                if (first)
                {
                    ArcadeGame.playSound(SoundHandler.BLOB_CLICK, 1.0f, 1.0f);
                }
                for (int i = -1; i <= 1; ++i)
                {
                    for (int j = -1; j <= 1; ++j)
                    {
                        openTile(x + i, y + j, false);
                    }
                }
            }
            else if (result == Tile.TILE_OPEN_RESULT.DEAD)
            {
                isPlaying = false;
                ArcadeGame.playSound(SoundEvents.GENERIC_EXPLODE, 1.0f, (1.0f + (getModule().getCart().random.nextFloat() - getModule().getCart().random.nextFloat()) * 0.2f) * 0.7f);
            }
            else if (result == Tile.TILE_OPEN_RESULT.OK && first)
            {
                ArcadeGame.playSound(SoundHandler.CLICK, 1.0f, 1.0f);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void keyPress(final GuiMinecart gui, final int character, final int extraInformation)
    {
        if (character == 19)
        {
            newGame(currentGameType);
        }
        else if (character == 20)
        {
            newGame(currentGameType = (currentGameType + 1) % 3);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
    {
        final String[] mapnames = {Localization.ARCADE.MAP_1.translate(), Localization.ARCADE.MAP_2.translate(), Localization.ARCADE.MAP_3.translate()};
        getModule().drawString(matrixStack, gui, Localization.ARCADE.LEFT.translate(String.valueOf(creepersLeft)), 10, 180, 4210752);
        getModule().drawString(matrixStack, gui, Localization.ARCADE.TIME.translate(String.valueOf(ticks / 20)), 10, 190, 4210752);
        getModule().drawString(matrixStack, gui, "R - " + Localization.ARCADE.INSTRUCTION_RESTART.translate(), 10, 210, 4210752);
        getModule().drawString(matrixStack, gui, "T - " + Localization.ARCADE.INSTRUCTION_CHANGE_MAP.translate(), 10, 230, 4210752);
        getModule().drawString(matrixStack, gui, Localization.ARCADE.MAP.translate(mapnames[currentGameType]), 10, 240, 4210752);
        getModule().drawString(matrixStack, gui, Localization.ARCADE.HIGH_SCORES.translate(), 330, 180, 4210752);
        for (int i = 0; i < 3; ++i)
        {
            getModule().drawString(matrixStack, gui, Localization.ARCADE.HIGH_SCORE_ENTRY.translate(mapnames[i], String.valueOf(highscore[i])), 330, 190 + i * 10, 4210752);
        }
    }

    @Override
    public void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 3)
        {
            short data2 = data[1];
            short data3 = data[2];
            if (data2 < 0)
            {
                data2 += 256;
            }
            if (data3 < 0)
            {
                data3 += 256;
            }
            highscore[data[0]] = (data2 | data3 << 8);
        }
    }

    @Override
    public void checkGuiData(final Object[] info)
    {
        for (int i = 0; i < 3; ++i)
        {
            getModule().updateGuiData(info, TrackStory.stories.size() + 2 + i, (short) highscore[i]);
        }
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        if (id >= TrackStory.stories.size() + 2 && id < TrackStory.stories.size() + 5)
        {
            highscore[id - (TrackStory.stories.size() + 2)] = data;
        }
    }

    @Override
    public void Save(final CompoundTag tagCompound, final int id)
    {
        for (int i = 0; i < 3; ++i)
        {
            tagCompound.putShort(getModule().generateNBTName("HighscoreSweeper" + i, id), (short) highscore[i]);
        }
    }

    @Override
    public void Load(final CompoundTag tagCompound, final int id)
    {
        for (int i = 0; i < 3; ++i)
        {
            highscore[i] = tagCompound.getShort(getModule().generateNBTName("HighscoreSweeper" + i, id));
        }
    }

    static
    {
        ArcadeSweeper.textureMenu = "/gui/sweeper.png";
    }
}
