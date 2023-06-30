package vswe.stevescarts.arcade.invaders;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.arcade.ArcadeGame;
import vswe.stevescarts.arcade.tracks.TrackStory;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.realtimers.ModuleArcade;

import java.util.ArrayList;

public class ArcadeInvaders extends ArcadeGame
{
    protected ArrayList<Unit> invaders;
    private final ArrayList<Player> lives;
    private final ArrayList<Unit> buildings;
    protected ArrayList<Projectile> projectiles;
    private Player player;
    protected int moveDirection;
    protected int moveSpeed;
    protected int moveDown;
    private int fireDelay;
    private int score;
    private int highscore;
    protected boolean hasPahighast;
    protected boolean canSpawnPahighast;
    private boolean newHighscore;
    private int gameoverCounter;
    private static String texture;
    private static final String[][] numbers;

    public ArcadeInvaders(final ModuleArcade module)
    {
        super(module, Localization.ARCADE.GHAST);
        invaders = new ArrayList<>();
        buildings = new ArrayList<>();
        lives = new ArrayList<>();
        projectiles = new ArrayList<>();
        start();
    }

    private void start()
    {
        buildings.clear();
        lives.clear();
        projectiles.clear();
        player = new Player(this);
        for (int i = 0; i < 3; ++i)
        {
            lives.add(new Player(this, 10 + i * 20, 190));
        }
        for (int i = 0; i < 4; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                buildings.add(new Building(this, 48 + i * 96 + j * 16, 120));
            }
        }
        moveSpeed = 0;
        fireDelay = 0;
        score = 0;
        canSpawnPahighast = false;
        newHighscore = false;
        spawnInvaders();
    }

    private void spawnInvaders()
    {
        invaders.clear();
        hasPahighast = false;
        for (int j = 0; j < 3; ++j)
        {
            for (int i = 0; i < 14; ++i)
            {
                invaders.add(new InvaderGhast(this, 20 + i * 20, 10 + 25 * j));
            }
        }
        ++moveSpeed;
        moveDirection = 1;
        moveDown = 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void update()
    {
        super.update();
        if (player != null)
        {
            if (player.ready)
            {
                boolean flag = false;
                boolean flag2 = false;
                for (int i = invaders.size() - 1; i >= 0; --i)
                {
                    final Unit invader = invaders.get(i);
                    final Unit.UPDATE_RESULT result = invader.update();
                    if (result == Unit.UPDATE_RESULT.DEAD)
                    {
                        if (((InvaderGhast) invader).isPahighast)
                        {
                            hasPahighast = false;
                        }
                        ArcadeGame.playSound(SoundEvents.GHAST_HURT, 0.15f, 1.0f);
                        invaders.remove(i);
                        ++score;
                    }
                    else if (result == Unit.UPDATE_RESULT.TURN_BACK)
                    {
                        flag = true;
                    }
                    else if (result == Unit.UPDATE_RESULT.GAME_OVER)
                    {
                        flag2 = true;
                    }
                }
                if (moveDown > 0)
                {
                    --moveDown;
                }
                if (flag)
                {
                    moveDirection *= -1;
                    moveDown = 5;
                }
                if (invaders.size() == 0 || (hasPahighast && invaders.size() == 1))
                {
                    score += (hasPahighast ? 200 : 50);
                    canSpawnPahighast = true;
                    spawnInvaders();
                }
                if (flag2)
                {
                    lives.clear();
                    projectiles.clear();
                    player = null;
                    newHighScore();
                    return;
                }
                for (int i = buildings.size() - 1; i >= 0; --i)
                {
                    if (buildings.get(i).update() == Unit.UPDATE_RESULT.DEAD)
                    {
                        buildings.remove(i);
                    }
                }
                for (int i = projectiles.size() - 1; i >= 0; --i)
                {
                    if (projectiles.get(i).update() == Unit.UPDATE_RESULT.DEAD)
                    {
                        projectiles.remove(i);
                    }
                }
                //30
                if (isKeyDown(65))
                {
                    player.move(-1);
                }
                else if (isKeyDown(68))
                {
                    player.move(1);
                }
                if (fireDelay == 0 && isKeyDown(87))
                {
                    projectiles.add(new Projectile(this, player.x + 8 - 2, player.y - 15, true));
                    ArcadeGame.playSound(SoundEvents.ARROW_SHOOT, 0.8f, 1.0f / (getModule().getCart().random.nextFloat() * 0.4f + 1.2f) + 0.5f);
                    fireDelay = 10;
                }
                else if (fireDelay > 0)
                {
                    --fireDelay;
                }
            }
            if (player.update() == Unit.UPDATE_RESULT.DEAD)
            {
                projectiles.clear();
                if (lives.size() != 0)
                {
                    lives.get(0).setTarget(player.x, player.y);
                    player = lives.get(0);
                    lives.remove(0);
                }
                else
                {
                    player = null;
                    newHighScore();
                }
            }
        }
        else if (gameoverCounter == 0)
        {
            boolean flag = false;
            for (int j = invaders.size() - 1; j >= 0; --j)
            {
                final Unit invader2 = invaders.get(j);
                if (invader2.update() == Unit.UPDATE_RESULT.TARGET)
                {
                    flag = true;
                }
            }
            if (!flag)
            {
                gameoverCounter = 1;
            }
        }
        else if (newHighscore && gameoverCounter < 5)
        {
            ++gameoverCounter;
            if (gameoverCounter == 5)
            {
                //TODO reimplement sound
//                ArcadeGame.playSound(SoundHandler.HIGH_SCORE, 1.0f, 1.0f);
            }
        }
    }

    public boolean isKeyDown(int id)
    {

        //TODO
//        		return InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), id);
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource(ArcadeInvaders.texture);
        for (int i = 0; i < 27; ++i)
        {
            getModule().drawImage(guiGraphics, gui, 5 + i * 16, 150, 16, 32, 16, 16);
        }
        for (int i = 0; i < 5; ++i)
        {
            getModule().drawImage(guiGraphics, gui, 3 + i * 16, 190, 16, 32, 16, 16);
        }
        for (final Unit invader : invaders)
        {
            invader.draw(guiGraphics, gui);
        }
        if (player != null)
        {
            player.draw(guiGraphics, gui);
        }
        for (final Unit player : lives)
        {
            player.draw(guiGraphics, gui);
        }
        for (final Unit projectile : projectiles)
        {
            projectile.draw(guiGraphics, gui);
        }
        for (final Unit building : buildings)
        {
            building.draw(guiGraphics, gui);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        getModule().drawString(guiGraphics, gui, Localization.ARCADE.EXTRA_LIVES.translate() + ":", 10, 180, 4210752);
        getModule().drawString(guiGraphics, gui, Localization.ARCADE.HIGH_SCORE.translate(String.valueOf(highscore)), 10, 210, 4210752);
        getModule().drawString(guiGraphics, gui, Localization.ARCADE.SCORE.translate(String.valueOf(score)), 10, 220, 4210752);
        getModule().drawString(guiGraphics, gui, "W - " + Localization.ARCADE.INSTRUCTION_SHOOT.translate(), 330, 180, 4210752);
        getModule().drawString(guiGraphics, gui, "A - " + Localization.ARCADE.INSTRUCTION_LEFT.translate(), 330, 190, 4210752);
        getModule().drawString(guiGraphics, gui, "D - " + Localization.ARCADE.INSTRUCTION_RIGHT.translate(), 330, 200, 4210752);
        getModule().drawString(guiGraphics, gui, "R - " + Localization.ARCADE.INSTRUCTION_RESTART.translate(), 330, 220, 4210752);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void keyPress(final GuiMinecart gui, final int character, final int extraInformation)
    {
        if (character == 19)
        {
            start();
        }
    }

    private void newHighScore()
    {
        buildings.clear();
        int digits;
        if (score == 0)
        {
            digits = 1;
        }
        else
        {
            digits = (int) Math.floor(Math.log10(score)) + 1;
        }
        canSpawnPahighast = false;
        int currentGhast = 0;
        for (int i = 0; i < digits; ++i)
        {
            final int digit = score / (int) Math.pow(10.0, digits - i - 1) % 10;
            final String[] number = ArcadeInvaders.numbers[digit];
            for (int j = 0; j < number.length; ++j)
            {
                final String line = number[j];
                for (int k = 0; k < line.length(); ++k)
                {
                    if (line.charAt(k) == 'X')
                    {
                        final int x = (443 - (digits * 90 - 10)) / 2 + i * 90 + k * 20;
                        final int y = 5 + j * 20;
                        InvaderGhast ghast;
                        if (currentGhast >= invaders.size())
                        {
                            invaders.add(ghast = new InvaderGhast(this, x, -20));
                            ++currentGhast;
                        }
                        else
                        {
                            ghast = (InvaderGhast) invaders.get(currentGhast++);
                        }
                        ghast.setTarget(x, y);
                    }
                }
            }
        }
        for (int i = currentGhast; i < invaders.size(); ++i)
        {
            final InvaderGhast ghast2 = (InvaderGhast) invaders.get(i);
            ghast2.setTarget(ghast2.x, -25);
        }
        gameoverCounter = 0;
        if (score > highscore)
        {
            newHighscore = true;
            final int val = score;
            final byte byte1 = (byte) (val & 0xFF);
            final byte byte2 = (byte) ((val & 0xFF00) >> 8);
            getModule().sendPacket(2, new byte[]{byte1, byte2});
        }
    }

    @Override
    public void receivePacket(final int id, final byte[] data, final net.minecraft.world.entity.player.Player player)
    {
        if (id == 2)
        {
            short data2 = data[0];
            short data3 = data[1];
            if (data2 < 0)
            {
                data2 += 256;
            }
            if (data3 < 0)
            {
                data3 += 256;
            }
            highscore = (data2 | data3 << 8);
        }
    }

    @Override
    public void checkGuiData(final Object[] info)
    {
        getModule().updateGuiData(info, TrackStory.stories.size() + 1, (short) highscore);
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        if (id == TrackStory.stories.size() + 1)
        {
            highscore = data;
        }
    }

    @Override
    public void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putShort(getModule().generateNBTName("HighscoreGhast", id), (short) highscore);
    }

    @Override
    public void Load(final CompoundTag tagCompound, final int id)
    {
        highscore = tagCompound.getShort(getModule().generateNBTName("HighscoreGhast", id));
    }

    static
    {
        ArcadeInvaders.texture = "/gui/invaders.png";
        numbers = new String[][]{{"XXXX", "X  X", "X  X", "X  X", "X  X", "X  X", "XXXX"}, {"   X", "   X", "   X", "   X", "   X", "   X", "   X"}, {"XXXX", "   X", "   X", "XXXX", "X   ", "X   ", "XXXX"}, {"XXXX", "   X", "   X", "XXXX", "   X", "   X", "XXXX"}, {"X  X", "X  X", "X  X", "XXXX", "   X", "   X", "   X"}, {"XXXX", "X   ", "X   ", "XXXX", "   X", "   X", "XXXX"}, {"XXXX", "X   ", "X   ", "XXXX", "X  X", "X  X", "XXXX"}, {"XXXX", "   X", "   X", "   X", "   X", "   X", "   X"}, {"XXXX", "X  X", "X  X", "XXXX", "X  X", "X  X", "XXXX"}, {"XXXX", "X  X", "X  X", "XXXX", "   X", "   X", "XXXX"}};
    }
}
