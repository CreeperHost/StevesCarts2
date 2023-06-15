package vswe.stevescarts.arcade.tetris;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.arcade.ArcadeGame;
import vswe.stevescarts.arcade.tracks.TrackStory;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.realtimers.ModuleArcade;

public class ArcadeTetris extends ArcadeGame
{
    private TetrisBlock[][] board;
    private TetrisPiece piece;
    private static SoundEvent[] removalSounds;
    private int ticks;
    private boolean isPlaying;
    private boolean quickMove;
    private int gameOverTicks;
    private int highscore;
    private int score;
    private int removed;
    private int[] removedByAmount;
    private int delay;
    private int piecesSinceDelayChange;
    private boolean newHighScore;
    public static final int BOARD_START_X = 189;
    public static final int BOARD_START_Y = 9;
    private static String texture;

    public ArcadeTetris(final ModuleArcade module)
    {
        super(module, Localization.ARCADE.STACKER);
        ticks = 0;
        isPlaying = true;
        quickMove = false;
        delay = 10;
        newgame();
    }

    private void newgame()
    {
        board = new TetrisBlock[10][15];
        generatePiece();
        isPlaying = true;
        ticks = 0;
        quickMove = false;
        score = 0;
        removed = 0;
        removedByAmount = new int[4];
        delay = 10;
        piecesSinceDelayChange = 0;
        newHighScore = false;
    }

    private void generatePiece()
    {
        piece = TetrisPiece.createPiece(getModule().getCart().random.nextInt(7));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void update()
    {
        super.update();
        if (isPlaying)
        {
            if (ticks == 0 || quickMove)
            {
                if (piece != null)
                {
                    final TetrisPiece.MOVE_RESULT result = piece.move(this, board, 0, 1, true);
                    if (result == TetrisPiece.MOVE_RESULT.FAIL)
                    {
                        piece = null;
                        int removedCount = 0;
                        for (int y = 0; y < board[0].length; ++y)
                        {
                            boolean valid = true;
                            for (int x = 0; x < board.length; ++x)
                            {
                                if (board[x][y] == null)
                                {
                                    valid = false;
                                    break;
                                }
                            }
                            if (valid)
                            {
                                for (int y2 = y; y2 >= 0; --y2)
                                {
                                    for (int x2 = 0; x2 < board.length; ++x2)
                                    {
                                        final TetrisBlock value = (y2 == 0) ? null : board[x2][y2 - 1];
                                        board[x2][y2] = value;
                                    }
                                }
                                ++removedCount;
                            }
                        }
                        if (removedCount > 0)
                        {
                            removed += removedCount;
                            final int[] removedByAmount = this.removedByAmount;
                            final int n = removedCount - 1;
                            ++removedByAmount[n];
                            score += removedCount * removedCount * 100;
                            ArcadeGame.playSound(ArcadeTetris.removalSounds[removedCount - 1], 1.0f, 1.0f);
                        }
                        quickMove = false;
                        ++piecesSinceDelayChange;
                        if (piecesSinceDelayChange == 8)
                        {
                            piecesSinceDelayChange = 0;
                            if (delay > 0)
                            {
                                --delay;
                            }
                        }
                    }
                    else if (result == TetrisPiece.MOVE_RESULT.GAME_OVER)
                    {
                        piece = null;
                        isPlaying = false;
                        quickMove = false;
                        gameOverTicks = 0;
                        newHighScore();
                        //TODO bring back sounds
//                        ArcadeGame.playSound(SoundHandler.GAME_OVER, 1.0f, 1.0f);
                    }
                }
                else
                {
                    generatePiece();
                }
                ticks = delay;
            }
            else
            {
                --ticks;
            }
        }
        else if (gameOverTicks < 170)
        {
            gameOverTicks = Math.min(170, gameOverTicks + 5);
        }
        else if (newHighScore)
        {
            //TODO bring back sounds
//            ArcadeGame.playSound(SoundHandler.HIGH_SCORE, 1.0f, 1.0f);
            newHighScore = false;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource(ArcadeTetris.texture);
        getModule().drawImage(guiGraphics, gui, 187, 7, 0, 40, 104, 154);
        for (int i = 0; i < board.length; ++i)
        {
            for (int j = 0; j < board[0].length; ++j)
            {
                final TetrisBlock b = board[i][j];
                if (b != null)
                {
                    b.render(guiGraphics, this, gui, i, j);
                }
            }
        }
        if (piece != null)
        {
            piece.render(guiGraphics, this, gui);
        }
        if (!isPlaying)
        {
            final int graphicalValue = Math.min(gameOverTicks, 150);
            getModule().drawImage(guiGraphics, gui, 189, 159 - graphicalValue, 104, 40, 100, graphicalValue);
            if (graphicalValue == 150 && getModule().inRect(x, y, new int[]{189, 9, 100, 150}))
            {
                getModule().drawImage(guiGraphics, gui, 213, 107, 0, 194, 54, 34);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void keyPress(final GuiMinecart gui, final int character, final int extraInformation)
    {
        if (piece != null)
        {
            if (character == 19)
            {
                piece.rotate(board);
            }
            else if (character == 30)
            {
                piece.move(this, board, -1, 0, false);
            }
            else if (character == 32)
            {
                piece.move(this, board, 1, 0, false);
            }
            else if (character == 31)
            {
                quickMove = true;
            }
        }
        if (character == 19)
        {
            newgame();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button == 0 && !isPlaying && gameOverTicks >= 150 && getModule().inRect(x, y, new int[]{189, 9, 100, 150}))
        {
            newgame();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        getModule().drawString(guiGraphics, gui, Localization.ARCADE.HIGH_SCORE.translate(String.valueOf(highscore)), 10, 20, 4210752);
        getModule().drawString(guiGraphics, gui, Localization.ARCADE.SCORE.translate(String.valueOf(score)), 10, 40, 4210752);
        getModule().drawString(guiGraphics, gui, Localization.ARCADE.REMOVED_LINES.translate(String.valueOf(removed)), 10, 60, 4210752);
        for (int i = 0; i < 4; ++i)
        {
            getModule().drawString(guiGraphics, gui, Localization.ARCADE.REMOVED_LINES_COMBO.translate(String.valueOf(i), String.valueOf(removedByAmount[i])), 10, 80 + i * 10, 4210752);
        }
        getModule().drawString(guiGraphics, gui, "W - " + Localization.ARCADE.INSTRUCTION_ROTATE.translate(), 340, 20, 4210752);
        getModule().drawString(guiGraphics, gui, "A - " + Localization.ARCADE.INSTRUCTION_LEFT.translate(), 340, 30, 4210752);
        getModule().drawString(guiGraphics, gui, "S - " + Localization.ARCADE.INSTRUCTION_DROP.translate(), 340, 40, 4210752);
        getModule().drawString(guiGraphics, gui, "D - " + Localization.ARCADE.INSTRUCTION_RIGHT.translate(), 340, 50, 4210752);
        getModule().drawString(guiGraphics, gui, "R - " + Localization.ARCADE.INSTRUCTION_RESTART.translate(), 340, 70, 4210752);
    }

    private void newHighScore()
    {
        if (score > highscore)
        {
            final int val = score / 100;
            final byte byte1 = (byte) (val & 0xFF);
            final byte byte2 = (byte) ((val & 0xFF00) >> 8);
            getModule().sendPacket(1, new byte[]{byte1, byte2});
            newHighScore = true;
        }
    }

    @Override
    public void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 1)
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
            highscore = (data2 | data3 << 8) * 100;
        }
    }

    @Override
    public void checkGuiData(final Object[] info)
    {
        getModule().updateGuiData(info, TrackStory.stories.size(), (short) (highscore / 100));
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        if (id == TrackStory.stories.size())
        {
            highscore = data * 100;
        }
    }

    @Override
    public void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putShort(getModule().generateNBTName("Highscore", id), (short) highscore);
    }

    @Override
    public void Load(final CompoundTag tagCompound, final int id)
    {
        highscore = tagCompound.getShort(getModule().generateNBTName("Highscore", id));
    }

    static
    {
        //TODO bring back sounds
        //        ArcadeTetris.removalSounds = new SoundEvent[]{SoundHandler.LINES_1, SoundHandler.LINES_2, SoundHandler.LINES_3, SoundHandler.LINES_4};
        ArcadeTetris.texture = "/gui/tetris.png";
    }
}
