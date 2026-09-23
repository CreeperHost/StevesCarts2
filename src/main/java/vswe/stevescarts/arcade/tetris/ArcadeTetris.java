package vswe.stevescarts.arcade.tetris;

import net.minecraft.network.chat.Component;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import vswe.stevescarts.arcade.ArcadeGame;
import vswe.stevescarts.arcade.tracks.TrackStory;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.init.ModSounds;
import vswe.stevescarts.modules.realtimers.ModuleArcade;

public class ArcadeTetris extends ArcadeGame {
    public static final int BOARD_START_X = 189;
    public static final int BOARD_START_Y = 9;
    private static String texture;

    static {
        ArcadeTetris.texture = "/gui/tetris.png";
    }

    private TetrisBlock[][] board;
    private TetrisPiece piece;
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

    public ArcadeTetris(final ModuleArcade module) {
        super(module, "arcade.stevescarts.mobStacker");
        ticks = 0;
        isPlaying = true;
        quickMove = false;
        delay = 10;
        newgame();
    }

    private void newgame() {
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

    private void generatePiece() {
        piece = TetrisPiece.createPiece(getModule().getCart().getRandom().nextInt(7));
    }

    @Override
    public void update() {
        super.update();
        if (isPlaying) {
            if (ticks == 0 || quickMove) {
                if (piece != null) {
                    final TetrisPiece.MOVE_RESULT result = piece.move(this, board, 0, 1, true);
                    if (result == TetrisPiece.MOVE_RESULT.FAIL) {
                        piece = null;
                        int removedCount = 0;
                        for (int y = 0; y < board[0].length; ++y) {
                            boolean valid = true;
                            for (int x = 0; x < board.length; ++x) {
                                if (board[x][y] == null) {
                                    valid = false;
                                    break;
                                }
                            }
                            if (valid) {
                                for (int y2 = y; y2 >= 0; --y2) {
                                    for (int x2 = 0; x2 < board.length; ++x2) {
                                        final TetrisBlock value = (y2 == 0) ? null : board[x2][y2 - 1];
                                        board[x2][y2] = value;
                                    }
                                }
                                ++removedCount;
                            }
                        }
                        if (removedCount > 0) {
                            removed += removedCount;
                            final int[] removedByAmount = this.removedByAmount;
                            final int n = removedCount - 1;
                            ++removedByAmount[n];
                            score += removedCount * removedCount * 100;
                            ArcadeGame.playSound(switch (removedCount) {
                                case 1 -> ModSounds.LINES_1.get();
                                case 2 -> ModSounds.LINES_2.get();
                                case 3 -> ModSounds.LINES_3.get();
                                default -> ModSounds.LINES_4.get();
                            }, 1.0f, 1.0f);
                        }
                        quickMove = false;
                        ++piecesSinceDelayChange;
                        if (piecesSinceDelayChange == 8) {
                            piecesSinceDelayChange = 0;
                            if (delay > 0) {
                                --delay;
                            }
                        }
                    } else if (result == TetrisPiece.MOVE_RESULT.GAME_OVER) {
                        piece = null;
                        isPlaying = false;
                        quickMove = false;
                        gameOverTicks = 0;
                        newHighScore();
                        ArcadeGame.playSound(ModSounds.GAME_OVER.get(), 1.0f, 1.0f);
                    }
                } else {
                    generatePiece();
                }
                ticks = delay;
            } else {
                --ticks;
            }
        } else if (gameOverTicks < 170) {
            gameOverTicks = Math.min(170, gameOverTicks + 5);
        } else if (newHighScore) {
            ArcadeGame.playSound(ModSounds.HIGH_SCORE.get(), 1.0f, 1.0f);
            newHighScore = false;
        }
    }

    @Override
    public void drawBackground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final int x, final int y) {
        Identifier texture = ResourceHelper.getResource(ArcadeTetris.texture);
        getModule().drawImage(GuiGraphicsExtractor, texture, gui, 187, 7, 0, 40, 104, 154);
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                final TetrisBlock b = board[i][j];
                if (b != null) {
                    b.render(GuiGraphicsExtractor, texture, this, gui, i, j);
                }
            }
        }
        if (piece != null) {
            piece.render(GuiGraphicsExtractor, texture, this, gui);
        }
        if (!isPlaying) {
            final int graphicalValue = Math.min(gameOverTicks, 150);
            getModule().drawImage(GuiGraphicsExtractor, texture, gui, 189, 159 - graphicalValue, 104, 40, 100, graphicalValue);
            if (graphicalValue == 150 && getModule().inRect(x, y, new int[]{189, 9, 100, 150})) {
                getModule().drawImage(GuiGraphicsExtractor, texture, gui, 213, 107, 0, 194, 54, 34);
            }
        }
    }

    @Override
    public void keyPress(final GuiMinecart gui, final int character, final int extraInformation) {
        if (piece != null) {
            if (character == InputConstants.KEY_W) {
                piece.rotate(board);
            } else if (character == InputConstants.KEY_A) {
                piece.move(this, board, -1, 0, false);
            } else if (character == InputConstants.KEY_D) {
                piece.move(this, board, 1, 0, false);
            } else if (character == InputConstants.KEY_S) {
                quickMove = true;
            }
        }
        if (character == InputConstants.KEY_R) {
            newgame();
        }
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT && !isPlaying && gameOverTicks >= 150 && getModule().inRect(x, y, new int[]{189, 9, 100, 150})) {
            newgame();
        }
    }

    @Override
    public void drawForeground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui) {
        getModule().drawString(GuiGraphicsExtractor, gui, Component.translatable("arcade.stevescarts.highScore", String.valueOf(highscore)).getString(), 10, 20, 4210752);
        getModule().drawString(GuiGraphicsExtractor, gui, Component.translatable("arcade.stevescarts.score", String.valueOf(score)).getString(), 10, 40, 4210752);
        getModule().drawString(GuiGraphicsExtractor, gui, Component.translatable("arcade.stevescarts.stackerRemovedLines", String.valueOf(removed)).getString(), 10, 60, 4210752);
        for (int i = 0; i < 4; ++i) {
            getModule().drawString(GuiGraphicsExtractor, gui, Component.translatable("arcade.stevescarts.stackerRemovedLinesCombo." + i, removedByAmount[i]).getString(), 10, 80 + i * 10, 4210752);
        }
        getModule().drawString(GuiGraphicsExtractor, gui, "W - " + Component.translatable("arcade.stevescarts.instructionRotate").getString(), 340, 20, 4210752);
        getModule().drawString(GuiGraphicsExtractor, gui, "A - " + Component.translatable("arcade.stevescarts.instructionLeft").getString(), 340, 30, 4210752);
        getModule().drawString(GuiGraphicsExtractor, gui, "S - " + Component.translatable("arcade.stevescarts.instructionDrop").getString(), 340, 40, 4210752);
        getModule().drawString(GuiGraphicsExtractor, gui, "D - " + Component.translatable("arcade.stevescarts.instructionRight").getString(), 340, 50, 4210752);
        getModule().drawString(GuiGraphicsExtractor, gui, "R - " + Component.translatable("arcade.stevescarts.instructionRestart").getString(), 340, 70, 4210752);
    }

    private void newHighScore() {
        if (score > highscore) {
            final int val = score / 100;
            final byte byte1 = (byte) (val & 0xFF);
            final byte byte2 = (byte) ((val & 0xFF00) >> 8);
            getModule().sendPacket(1, new byte[]{byte1, byte2});
            newHighScore = true;
        }
    }

    @Override
    public void receivePacket(final int id, final byte[] data, final Player player) {
        if (id == 1) {
            short data2 = data[0];
            short data3 = data[1];
            if (data2 < 0) {
                data2 += 256;
            }
            if (data3 < 0) {
                data3 += 256;
            }
            highscore = (data2 | data3 << 8) * 100;
        }
    }

    @Override
    public void checkGuiData(final Object[] info) {
        getModule().updateGuiData(info, TrackStory.stories.size(), (short) (highscore / 100));
    }

    @Override
    public void receiveGuiData(final int id, final short data) {
        if (id == TrackStory.stories.size()) {
            highscore = data * 100;
        }
    }

    @Override
    public void Save(ValueOutput output, int id) {
        output.putShort(getModule().generateNBTName("Highscore", id), (short) highscore);
    }

    @Override
    public void Load(ValueInput input, int id) {
        highscore = input.getShortOr(getModule().generateNBTName("Highscore", id), (short) 0);
    }
}
