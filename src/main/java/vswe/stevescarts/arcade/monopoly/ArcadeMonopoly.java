package vswe.stevescarts.arcade.monopoly;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.arcade.ArcadeGame;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.realtimers.ModuleArcade;

import java.util.ArrayList;
import java.util.EnumSet;

public class ArcadeMonopoly extends ArcadeGame {
	private Die die;
	private Die die2;
	private ArrayList<Piece> pieces;
	private int currentPiece;
	private Place[] places;
	private int selectedPlace;
	private int diceTimer;
	private int diceCount;
	private int diceDelay;
	private ArrayList<Button> buttons;
	private Button roll;
	private Button end;
	private Button purchase;
	private Button rent;
	private Button bankrupt;
	private Button bed;
	private Button card;
	private Button jail;
	private Button mortgage;
	private Button unmortgage;
	private Button sellbed;
	private boolean rolled;
	private boolean controllable;
	private boolean endable;
	private boolean openedCard;
	private Card currentCard;
	private float cardScale;
	private int cardRotation;
	public static final int PLACE_WIDTH = 76;
	public static final int PLACE_HEIGHT = 122;
	public static final int BOARD_WIDTH = 14;
	public static final int BOARD_HEIGHT = 10;
	public static final float SCALE = 0.17f;
	private static final int CARD_WIDTH = 142;
	private static final int CARD_HEIGHT = 80;
	private static String[] textures;

	protected Place getSelectedPlace() {
		return (selectedPlace == -1) ? null : places[selectedPlace];
	}

	protected Piece getCurrentPiece() {
		return pieces.get(currentPiece);
	}

	public ArcadeMonopoly(final ModuleArcade module) {
		super(module, Localization.ARCADE.MADNESS);
		selectedPlace = -1;
		(pieces = new ArrayList<>()).add(new Piece(this, 0, Piece.CONTROLLED_BY.PLAYER));
		pieces.add(new Piece(this, 1, Piece.CONTROLLED_BY.COMPUTER));
		pieces.add(new Piece(this, 2, Piece.CONTROLLED_BY.COMPUTER));
		pieces.add(new Piece(this, 3, Piece.CONTROLLED_BY.COMPUTER));
		pieces.add(new Piece(this, 4, Piece.CONTROLLED_BY.COMPUTER));
		final StreetGroup streetGroup1 = new StreetGroup(50, new int[] { 89, 12, 56 });
		final StreetGroup streetGroup2 = new StreetGroup(50, new int[] { 102, 45, 145 });
		final StreetGroup streetGroup3 = new StreetGroup(50, new int[] { 135, 166, 213 });
		final StreetGroup streetGroup4 = new StreetGroup(100, new int[] { 239, 56, 120 });
		final StreetGroup streetGroup5 = new StreetGroup(100, new int[] { 245, 128, 45 });
		final StreetGroup streetGroup6 = new StreetGroup(150, new int[] { 238, 58, 35 });
		final StreetGroup streetGroup7 = new StreetGroup(150, new int[] { 252, 231, 4 });
		final StreetGroup streetGroup8 = new StreetGroup(200, new int[] { 19, 165, 92 });
		final StreetGroup streetGroup9 = new StreetGroup(200, new int[] { 40, 78, 161 });
		final PropertyGroup stationGroup = new PropertyGroup();
		final PropertyGroup utilGroup = new PropertyGroup();
		places = new Place[] { new Go(this), new Street(this, streetGroup1, "Soaryn Chest", 30, 2), new Community(this), new Street(this, streetGroup1, "Eddie's Cobble Stairs", 30, 2),
			new Place(this), new Utility(this, utilGroup, 0, "Test"), new Street(this, streetGroup2, "Ecu's Eco Escape", 60, 4), new Station(this, stationGroup, 0, "Wooden Station"),
			new Street(this, streetGroup2, "Test", 60, 4), new Villager(this), new Street(this, streetGroup3, "Direwolf's 9x9", 100, 6), new Chance(this),
			new Street(this, streetGroup3, "Greg's Forest", 100, 6), new Street(this, streetGroup3, "Alice's Tunnel", 110, 8), new Jail(this),
			new Street(this, streetGroup4, "Flora's Alveary", 140, 10), new Utility(this, utilGroup, 1, "Test"), new Street(this, streetGroup4, "Sengir's Greenhouse", 140, 10),
			new Street(this, streetGroup4, "Test", 160, 12), new Station(this, stationGroup, 1, "Standard Station"), new Street(this, streetGroup5, "Muse's Moon Base", 200, 14), new Community(this),
			new Street(this, streetGroup5, "Algorithm's Crafting CPU", 200, 14), new Street(this, streetGroup5, "Pink Lemmingaide Stand", 240, 16), new CornerPlace(this, 2),
			new Street(this, streetGroup6, "Covert's Railyard", 250, 18), new Chance(this), new Street(this, streetGroup6, "Test", 250, 18), new Street(this, streetGroup6, "Test", 270, 20),
			new Community(this), new Street(this, streetGroup6, "Test", 270, 20), new Station(this, stationGroup, 2, "Reinforced Station"),
			new Street(this, streetGroup7, "Player's Industrial Warehouse", 320, 22), new Villager(this), new Street(this, streetGroup7, "Dan's Computer Repair", 320, 22),
			new Street(this, streetGroup7, "iChun's Hat Shop", 350, 24), new Utility(this, utilGroup, 2, "Test"), new Street(this, streetGroup7, "Lex's Forge", 350, 24), new GoToJail(this),
			new Street(this, streetGroup8, "Morvelaira's Pretty Wall", 400, 26), new Street(this, streetGroup8, "Rorax's Tower of Doom", 400, 26), new Community(this),
			new Street(this, streetGroup8, "Jaded's Crash Lab", 440, 30), new Station(this, stationGroup, 3, "Galgadorian Station"), new Chance(this), new Street(this, streetGroup9, "Test", 500, 40),
			new Place(this), new Street(this, streetGroup9, "Vswe's Redstone Tower", 600, 50) };
		((Property) places[1]).setOwner(pieces.get(0));
		((Property) places[3]).setOwner(pieces.get(0));
		die = new Die(this, 0);
		die2 = new Die(this, 1);
		(buttons = new ArrayList<>()).add(roll = new Button() {
			@Override
			public String getName() {
				return "Roll";
			}

			@Override
			public boolean isVisible() {
				return true;
			}

			@Override
			public boolean isEnabled() {
				return diceCount == 0 && diceTimer == 0 && !rolled;
			}

			@Override
			public void onClick() {
				rolled = true;
				throwDice();
			}
		});
		buttons.add(end = new Button() {
			@Override
			public String getName() {
				return "End Turn";
			}

			@Override
			public boolean isVisible() {
				return true;
			}

			@Override
			public boolean isEnabled() {
				return controllable && endable;
			}

			@Override
			public void onClick() {
				rolled = false;
				controllable = false;
				nextPiece();
				endable = false;
				openedCard = false;
				if (useAI()) {
					roll.onClick();
				}
			}
		});
		buttons.add(purchase = new Button() {
			@Override
			public String getName() {
				return "Purchase";
			}

			@Override
			public boolean isVisible() {
				return controllable && places[getCurrentPiece().getPosition()] instanceof Property && !((Property) places[getCurrentPiece().getPosition()]).hasOwner();
			}

			@Override
			public boolean isEnabled() {
				final Property property = (Property) places[getCurrentPiece().getPosition()];
				return getCurrentPiece().canAffordProperty(property);
			}

			@Override
			public boolean isVisibleForPlayer() {
				return getSelectedPlace() == null;
			}

			@Override
			public void onClick() {
				getCurrentPiece().purchaseProperty((Property) places[getCurrentPiece().getPosition()]);
			}
		});
		buttons.add(rent = new Button() {
			@Override
			public String getName() {
				return "Pay Rent";
			}

			@Override
			public boolean isVisible() {
				if (controllable && places[getCurrentPiece().getPosition()] instanceof Property) {
					final Property property = (Property) places[getCurrentPiece().getPosition()];
					return property.hasOwner() && property.getOwner() != getCurrentPiece() && !property.isMortgaged();
				}
				return false;
			}

			@Override
			public boolean isEnabled() {
				final Property property = (Property) places[getCurrentPiece().getPosition()];
				return !endable && getCurrentPiece().canAffordRent(property);
			}

			@Override
			public boolean isVisibleForPlayer() {
				return getSelectedPlace() == null;
			}

			@Override
			public void onClick() {
				getCurrentPiece().payPropertyRent((Property) places[getCurrentPiece().getPosition()]);
				endable = true;
			}
		});
		buttons.add(bankrupt = new Button() {
			@Override
			public String getName() {
				return "Go Bankrupt";
			}

			@Override
			public boolean isVisible() {
				return !endable && rent.isVisible() && !rent.isEnabled();
			}

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			public boolean isVisibleForPlayer() {
				return getSelectedPlace() == null;
			}

			@Override
			public void onClick() {
				getCurrentPiece().bankrupt(((Property) places[getCurrentPiece().getPosition()]).getOwner());
				endable = true;
			}
		});
		buttons.add(bed = new Button() {
			@Override
			public String getName() {
				return "Buy Bed";
			}

			@Override
			public boolean isVisible() {
				return getSelectedPlace() != null && getSelectedPlace() instanceof Street;
			}

			@Override
			public boolean isEnabled() {
				final Street street = (Street) getSelectedPlace();
				return controllable && street.ownsAllInGroup(getCurrentPiece()) && street.getStructureCount() < 5 && getCurrentPiece().canAffordStructure(street) && !street.isMortgaged();
			}

			@Override
			public void onClick() {
				getCurrentPiece().buyStructure((Street) getSelectedPlace());
			}
		});
		buttons.add(card = new Button() {
			@Override
			public String getName() {
				return "Pick a Card";
			}

			@Override
			public boolean isVisible() {
				return controllable && places[getCurrentPiece().getPosition()] instanceof CardPlace && (!openedCard || currentCard != null);
			}

			@Override
			public boolean isEnabled() {
				return !openedCard;
			}

			@Override
			public boolean isVisibleForPlayer() {
				return getSelectedPlace() == null;
			}

			@Override
			public void onClick() {
				openCard(((CardPlace) places[getCurrentPiece().getPosition()]).getCard());
			}
		});
		buttons.add(jail = new Button() {
			@Override
			public String getName() {
				return "Pay for /tpx";
			}

			@Override
			public boolean isVisible() {
				return controllable && getCurrentPiece().isInJail();
			}

			@Override
			public boolean isEnabled() {
				return getCurrentPiece().canAffordFine();
			}

			@Override
			public boolean isVisibleForPlayer() {
				return getSelectedPlace() == null;
			}

			@Override
			public void onClick() {
				getCurrentPiece().payFine();
				endable = true;
			}
		});
		buttons.add(mortgage = new Button() {
			@Override
			public String getName() {
				return "Mortgage";
			}

			@Override
			public boolean isVisible() {
				return controllable && getSelectedPlace() != null && getSelectedPlace() instanceof Property && ((Property) getSelectedPlace()).getOwner() == getCurrentPiece() && !((Property) getSelectedPlace()).isMortgaged();
			}

			@Override
			public boolean isEnabled() {
				return ((Property) getSelectedPlace()).canMortgage();
			}

			@Override
			public void onClick() {
				getCurrentPiece().getMoneyFromMortgage((Property) getSelectedPlace());
			}
		});
		buttons.add(unmortgage = new Button() {
			@Override
			public String getName() {
				return "Unmortage";
			}

			@Override
			public boolean isVisible() {
				return controllable && getSelectedPlace() != null && getSelectedPlace() instanceof Property && ((Property) getSelectedPlace()).getOwner() == getCurrentPiece() && ((Property) getSelectedPlace()).isMortgaged();
			}

			@Override
			public boolean isEnabled() {
				return getCurrentPiece().canAffordUnMortgage((Property) getSelectedPlace());
			}

			@Override
			public void onClick() {
				getCurrentPiece().payUnMortgage((Property) getSelectedPlace());
			}
		});
		buttons.add(sellbed = new Button() {
			@Override
			public String getName() {
				return "Sell Bed";
			}

			@Override
			public boolean isVisible() {
				return getSelectedPlace() != null && getSelectedPlace() instanceof Street;
			}

			@Override
			public boolean isEnabled() {
				final Street street = (Street) getSelectedPlace();
				return controllable && street.getStructureCount() > 0;
			}

			@Override
			public void onClick() {
				getCurrentPiece().sellStructure((Street) getSelectedPlace());
			}
		});
		if (getCurrentPiece().getController() == Piece.CONTROLLED_BY.COMPUTER) {
			roll.onClick();
		}
	}

	private boolean useAI() {
		return getCurrentPiece().getController() == Piece.CONTROLLED_BY.COMPUTER;
	}

	private void nextPiece() {
		currentPiece = (currentPiece + 1) % pieces.size();
		if (getCurrentPiece().isBankrupt()) {
			nextPiece();
		}
	}

	private void throwDice() {
		if (diceCount == 0) {
			if (diceTimer == 0) {
				diceTimer = 20;
			}
			die.randomize();
			die2.randomize();
		}
	}

	public void movePiece(final int steps) {
		diceCount = steps;
	}

	public int getTotalDieEyes() {
		return die.getNumber() + die2.getNumber();
	}

	public boolean hasDoubleDice() {
		return die.getNumber() == die2.getNumber();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void update() {
		super.update();
		if (diceDelay == 0) {
			if (diceTimer > 0) {
				throwDice();
				if (--diceTimer == 0) {
					if (getCurrentPiece().isInJail()) {
						controllable = true;
						if (hasDoubleDice()) {
							getCurrentPiece().releaseFromJail();
							endable = true;
							if (useAI()) {
								end.onClick();
							}
						} else {
							getCurrentPiece().increaseTurnsInJail();
							if (getCurrentPiece().getTurnsInJail() < 3) {
								endable = true;
								if (useAI()) {
									end.onClick();
								}
							} else if (useAI()) {
								if (jail.isVisible() && jail.isEnabled()) {
									jail.onClick();
								} else {
									bankrupt.onClick();
								}
								end.onClick();
							}
						}
					} else {
						movePiece(getTotalDieEyes());
					}
				}
			} else if (diceCount != 0) {
				if (diceCount > 0) {
					getCurrentPiece().move(1);
					places[getCurrentPiece().getPosition()].onPiecePass(getCurrentPiece());
					--diceCount;
				} else {
					getCurrentPiece().move(-1);
					++diceCount;
				}
				if (diceCount == 0) {
					if (places[getCurrentPiece().getPosition()].onPieceStop(getCurrentPiece())) {
						endable = true;
					}
					controllable = true;
					if (useAI()) {
						if (purchase.isVisible() && purchase.isEnabled()) {
							purchase.onClick();
						} else if (card.isVisible() && card.isEnabled()) {
							card.onClick();
						} else if (rent.isVisible()) {
							if (rent.isEnabled()) {
								rent.onClick();
							} else {
								bankrupt.onClick();
							}
						}
						if (end.isVisible() && end.isEnabled()) {
							end.onClick();
						}
					}
				}
			}
			diceDelay = 3;
		} else {
			--diceDelay;
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void drawBackground(PoseStack matrixStack, GuiMinecart gui, final int x, final int y) {
		loadTexture(gui, 1);
		die.draw(matrixStack, gui, 20, 20);
		die2.draw(matrixStack, gui, 50, 20);
		final float smallgridX = x / 0.17f - 686.94116f;
		final float smallgridY = y / 0.17f - 30.117645f;
		boolean foundHover = false;
		if (selectedPlace != -1) {
			drawPropertyOnBoardWithPositionRotationAndScale(matrixStack, gui, places[selectedPlace], selectedPlace, true, false, (int) ((590.6666666666666 - (
				(getId(selectedPlace) == 0) ? 122 : 76)) / 2.0), 51, 0, 0.75f);
		}
		for (int i = 0; i < places.length; ++i) {
			if (!foundHover && getModule().inRect((int) smallgridX, (int) smallgridY, getSmallgridPlaceArea(i))) {
				if (selectedPlace == -1) {
					drawPropertyOnBoardWithPositionRotationAndScale(matrixStack, gui, places[i], i, true, false, (int) ((590.6666666666666 - ((getId(i) == 0) ? 122 : 76)) / 2.0), 51, 0, 0.75f);
				}
				foundHover = true;
				drawPropertyOnBoard(matrixStack, gui, places[i], i, getSide(i), getId(i), true);
			} else {
				drawPropertyOnBoard(matrixStack, gui, places[i], i, getSide(i), getId(i), false);
			}
		}
		for (int i = 0; i < pieces.size(); ++i) {
			final Piece piece = pieces.get(i);
			loadTexture(gui, 1);
			final int[] menu = piece.getMenuRect(i);
			getModule().drawImage(matrixStack, gui, menu, 0, 122);
			for (int j = 0; j < 3; ++j) {
				int v = 0;
				switch (j) {
					case 0: {
						v = ((piece.getController() == Piece.CONTROLLED_BY.PLAYER) ? 0 : ((piece.getController() == Piece.CONTROLLED_BY.COMPUTER) ? 1 : 2));
						break;
					}
					case 1: {
						v = (pieces.get(i).isBankrupt() ? 4 : ((currentPiece == i) ? ((diceCount == 0) ? ((diceTimer > 0) ? 3 : 2) : 1) : 0));
						break;
					}
					case 2: {
						v = ((getSelectedPlace() != null && getSelectedPlace() instanceof Property && ((Property) getSelectedPlace()).getOwner() == pieces.get(i)) ? (
							((Property) getSelectedPlace()).isMortgaged() ? 2 : 1) : 0);
						break;
					}
				}
				getModule().drawImage(matrixStack, gui, menu[0] + 3, menu[1] + 3 + j * 9, j * 12, 152 + 6 * v, 12, 6);
			}
			final int[] player = piece.getPlayerMenuRect(i);
			getModule().drawImage(matrixStack, gui, player, 232, 24 * piece.getV());
			Note.drawPlayerValue(matrixStack, this, gui, menu[0] + 50, menu[1] + 2, piece.getNoteCount());
			for (int k = piece.getAnimationNotes().size() - 1; k >= 0; --k) {
				final NoteAnimation animation = piece.getAnimationNotes().get(k);
				int animX = menu[0] + 50 + (6 - animation.getNote().getId()) * 20;
				if (animX + 16 > 443) {
					animX = player[0] + (player[2] - 16) / 2;
				}
				if (animation.draw(matrixStack, this, gui, animX, menu[1] + 2)) {
					piece.removeNewNoteAnimation(k);
				}
			}
			piece.updateExtending(getModule().inRect(x, y, menu));
		}
		loadTexture(gui, 1);
		int id = 0;
		for (final Button button : buttons) {
			if (button.isReallyVisible(this)) {
				final int[] rect = getButtonRect(id++);
				int v = 0;
				if (!button.isReallyEnabled(this)) {
					v = 1;
				} else if (getModule().inRect(x, y, rect)) {
					v = 2;
				}
				getModule().drawImage(matrixStack,gui, rect, 152, v * 18);
			}
		}
		if (getSelectedPlace() != null) {
			if (getSelectedPlace() instanceof Street) {
				final Street street = (Street) getSelectedPlace();
				getModule().drawImage(matrixStack, gui, 32, 185, 76, 22, 16, 16);
				if (street.getOwner() != null && !street.isMortgaged()) {
					if (street.getStructureCount() == 0) {
						getModule().drawImage(matrixStack, gui, 7, street.ownsAllInGroup(street.getOwner()) ? 241 : 226, 124, 22, 5, 10);
					} else {
						getModule().drawImage(matrixStack, gui, 323, 172 + (street.getStructureCount() - 1) * 17, 124, 22, 5, 10);
					}
				}
				for (int l = 1; l <= 5; ++l) {
					drawStreetRent(matrixStack, gui, street, l);
				}
				Note.drawValue(matrixStack, this, gui, 62, 170, 3, street.getMortgageValue());
				Note.drawValue(matrixStack, this, gui, 62, 185, 3, street.getStructureCost());
				Note.drawValue(matrixStack, this, gui, 62, 222, 3, street.getRentCost(false));
				Note.drawValue(matrixStack, this, gui, 62, 237, 3, street.getRentCost(true));
			} else if (getSelectedPlace() instanceof Station) {
				final Station station = (Station) getSelectedPlace();
				if (station.getOwner() != null && !station.isMortgaged()) {
					getModule().drawImage(matrixStack, gui, 323, 184 + (station.getOwnedInGroup() - 1) * 17, 124, 22, 5, 10);
				}
				Note.drawValue(matrixStack, this, gui, 62, 170, 3, station.getMortgageValue());
				for (int l = 1; l <= 4; ++l) {
					drawStationRent(matrixStack, gui, station, l);
				}
			} else if (getSelectedPlace() instanceof Utility) {
				final Utility utility = (Utility) getSelectedPlace();
				if (utility.getOwner() != null && !utility.isMortgaged()) {
					getModule().drawImage(matrixStack, gui, 323, 184 + (utility.getOwnedInGroup() - 1) * 17, 124, 22, 5, 10);
				}
				Note.drawValue(matrixStack,this, gui, 62, 170, 3, utility.getMortgageValue());
				for (int l = 1; l <= 3; ++l) {
					drawUtilityRent(matrixStack, gui, utility, l);
				}
			}
		}
		if (currentCard != null) {
			cardScale = Math.min(cardScale + 0.02f, 1.0f);
			cardRotation = Math.max(0, cardRotation - 6);
			drawCard(matrixStack, gui, true);
			drawCard(matrixStack, gui, false);
			if (cardScale == 1.0f && useAI()) {
				removeCard();
			}
		}
	}

	private void openCard(final Card card) {
		openedCard = true;
		currentCard = card;
		cardScale = 0.0f;
		cardRotation = 540;
	}

	private void drawCard(PoseStack matrixStack,  GuiMinecart gui, final boolean isFront)
	{
		//TODO
//		GlStateManager._pushMatrix();
		final int x = 150;
		final int y = 44;
		final float s = cardScale;
		final float posX = gui.getGuiLeft() + 71;
		final float posY = gui.getGuiTop() + 40;
//		GlStateManager._translatef(0.0f, 0.0f, 100.0f);
//		GlStateManager._translatef(posX + x, posY + y, 0.0f);
//		GlStateManager._scalef(s, s, 1.0f);
//		GlStateManager._rotatef(cardRotation + (isFront ? 0 : 180), 0.0f, 1.0f, 0.0f);
//		GlStateManager._translatef(-posX, -posY, 0.0f);
		loadTexture(gui, 0);
		final int[] rect = { 0, 0, 142, 80 };
		currentCard.render(this, matrixStack, gui, rect, isFront);
//		GlStateManager._popMatrix();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void drawForeground(PoseStack matrixStack, GuiMinecart gui) {
		int id = 0;
		for (final Button button : buttons) {
			if (button.isReallyVisible(this)) {
				getModule().drawString(matrixStack, gui, button.getName(), getButtonRect(id++), 4210752);
			}
		}
		if (getSelectedPlace() != null) {
			if (getSelectedPlace() instanceof Street) {
				getModule().drawString(matrixStack, gui, "Mortgage", 10, 175, 4210752);
				getModule().drawString(matrixStack,gui, "Buy", 10, 190, 4210752);
				getModule().drawString(matrixStack, gui, "Rents", 10, 215, 4210752);
				getModule().drawString(matrixStack, gui, "Normal", 14, 227, 4210752);
				getModule().drawString(matrixStack, gui, "Group", 14, 242, 4210752);
			} else if (getSelectedPlace() instanceof Station) {
				getModule().drawString(matrixStack, gui, "Mortgage", 10, 175, 4210752);
				getModule().drawString(matrixStack, gui, "Rents", 330, 170, 4210752);
			} else if (getSelectedPlace() instanceof Utility) {
				getModule().drawString(matrixStack, gui, "Mortgage", 10, 175, 4210752);
				getModule().drawSplitString(gui, "The rent depends on the eye count of the dice, if you own one Utility it's " + Utility.getMultiplier(1) + "x the eye count, if you own two it's " + Utility.getMultiplier(2) + "x and if you own them all it's " + Utility.getMultiplier(3) + "x.", 10, 195, 145, 4210752);
				getModule().drawString(matrixStack, gui, "Rents", 330, 170, 4210752);
			}
		}
	}

	private void drawStreetRent(PoseStack matrixStack, GuiMinecart gui, final Street street, final int structures) {
		loadTexture(gui, 1);
		int graphicalStructures = structures;
		int u = 0;
		if (graphicalStructures == 5) {
			graphicalStructures = 1;
			u = 1;
		}
		final int yPos = 169 + (structures - 1) * 17;
		for (int i = 0; i < graphicalStructures; ++i) {
			getModule().drawImage(matrixStack, gui, 330 + i * 6, yPos, 76 + u * 16, 22, 16, 16);
		}
		Note.drawValue(matrixStack,this, gui, 370, yPos, 3, street.getRentCost(structures));
	}

	private void drawStationRent(PoseStack matrixStack, GuiMinecart gui, final Station station, final int ownedStations) {
		loadTexture(gui, 1);
		final int yPos = 181 + (ownedStations - 1) * 17;
		for (int i = 0; i < ownedStations; ++i) {
			getModule().drawImage(matrixStack, gui, 330 + i * 16, yPos, 76 + i * 16, 70, 16, 16);
		}
		Note.drawValue(matrixStack, this, gui, 410, yPos, 2, station.getRentCost(ownedStations));
	}

	private void drawUtilityRent(PoseStack matrixStack,  GuiMinecart gui, final Utility utility, final int utils) {
		loadTexture(gui, 1);
		final int yPos = 181 + (utils - 1) * 17;
		for (int i = 0; i < utils; ++i) {
			getModule().drawImage(matrixStack, gui, 330 + i * 16, yPos, 76 + i * 16, 86, 16, 16);
		}
		Note.drawValue(matrixStack, this, gui, 400, yPos, 2, utility.getRentCost(utils));
	}

	private int[] getButtonRect(final int i) {
		return new int[] { 10, 50 + i * 22, 80, 18 };
	}

	private int getSide(final int i) {
		if (i < 14) {
			return 0;
		}
		if (i < 24) {
			return 1;
		}
		if (i < 38) {
			return 2;
		}
		return 3;
	}

	private int getId(final int i) {
		if (i < 14) {
			return i;
		}
		if (i < 24) {
			return i - 14;
		}
		if (i < 38) {
			return i - 24;
		}
		return i - 38;
	}

	private int[] getSmallgridPlaceArea(final int id) {
		final int side = getSide(id);
		int i = getId(id);
		if (i == 0) {
			switch (side) {
				case 0: {
					return new int[] { 1110, 806, 122, 122 };
				}
				case 1: {
					return new int[] { 0, 806, 122, 122 };
				}
				case 2: {
					return new int[] { 0, 0, 122, 122 };
				}
				default: {
					return new int[] { 1110, 0, 122, 122 };
				}
			}
		} else {
			--i;
			switch (side) {
				case 0: {
					return new int[] { 122 + (13 - i) * 76 - 76, 806, 76, 122 };
				}
				case 1: {
					return new int[] { 0, 122 + (9 - i) * 76 - 76, 122, 76 };
				}
				case 2: {
					return new int[] { 122 + i * 76, 0, 76, 122 };
				}
				default: {
					return new int[] { 1110, 122 + i * 76, 122, 76 };
				}
			}
		}
	}

	private void drawPropertyOnBoard(PoseStack matrixStack,  GuiMinecart gui, final Place place, final int id, final int side, int i, final boolean hover) {
		int offX = 0;
		int offY = 0;
		int rotation = 0;
		if (i == 0) {
			switch (side) {
				case 0: {
					offX = 1110;
					offY = 806;
					rotation = 0;
					break;
				}
				case 1: {
					offX = 122;
					offY = 806;
					rotation = 90;
					break;
				}
				case 2: {
					offX = 122;
					offY = 122;
					rotation = 180;
					break;
				}
				default: {
					offX = 1110;
					offY = 122;
					rotation = 270;
					break;
				}
			}
		} else {
			--i;
			switch (side) {
				case 0: {
					offX = 122 + (13 - i) * 76 - 76;
					offY = 806;
					rotation = 0;
					break;
				}
				case 1: {
					offX = 122;
					offY = 122 + (9 - i) * 76 - 76;
					rotation = 90;
					break;
				}
				case 2: {
					offX = 122 + i * 76 + 76;
					offY = 122;
					rotation = 180;
					break;
				}
				default: {
					offX = 1110;
					offY = 122 + i * 76 + 76;
					rotation = 270;
					break;
				}
			}
		}
		offX += 686;
		offY += 30;
		drawPropertyOnBoardWithPositionRotationAndScale(matrixStack,  gui, place, id, false, hover, offX, offY, rotation, 0.17f);
	}

	private void drawPropertyOnBoardWithPositionRotationAndScale(PoseStack matrixStack, GuiMinecart gui, final Place place, final int id, final boolean zoom, final boolean hover, final int x, final int y, final int r, final float s) {
		//TODO
//		GlStateManager._pushMatrix();
		final EnumSet<Place.PLACE_STATE> states = EnumSet.noneOf(Place.PLACE_STATE.class);
		if (zoom) {
			states.add(Place.PLACE_STATE.ZOOMED);
		} else if (hover) {
			states.add(Place.PLACE_STATE.HOVER);
		}
		if (selectedPlace == id) {
			states.add(Place.PLACE_STATE.SELECTED);
		}
		if (place instanceof Property) {
			final Property property = (Property) place;
			if (property.hasOwner() && property.getOwner().showProperties()) {
				states.add(Place.PLACE_STATE.MARKED);
			}
		}
		final float posX = gui.getGuiLeft();
		final float posY = gui.getGuiTop();
//		GlStateManager._translatef(posX + x * s, posY + y * s, 0.0f);
//		GlStateManager._scalef(s, s, 1.0f);
//		GlStateManager._rotatef(r, 0.0f, 0.0f, 1.0f);
//		GlStateManager._translatef(-posX, -posY, 0.0f);
		place.draw(matrixStack, gui, states);
		final int[] total = new int[place.getPieceAreaCount()];
		for (int i = 0; i < pieces.size(); ++i) {
			if (!pieces.get(i).isBankrupt() && pieces.get(i).getPosition() == id) {
				final int[] array = total;
				final int pieceAreaForPiece = place.getPieceAreaForPiece(pieces.get(i));
				++array[pieceAreaForPiece];
			}
		}
		final int[] pos = new int[place.getPieceAreaCount()];
		for (int j = 0; j < pieces.size(); ++j) {
			if (!pieces.get(j).isBankrupt() && pieces.get(j).getPosition() == id) {
				loadTexture(gui, 1);
				final int area = place.getPieceAreaForPiece(pieces.get(j));
				place.drawPiece(matrixStack, gui, pieces.get(j), total[area], pos[area]++, area, states);
			}
		}
		place.drawText(matrixStack, gui, states);
//		GlStateManager._popMatrix();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int b) {
		final float smallgridX = x / 0.17f - 686.94116f;
		final float smallgridY = y / 0.17f - 30.117645f;
		int i = 0;
		while (i < places.length) {
			if (getModule().inRect((int) smallgridX, (int) smallgridY, getSmallgridPlaceArea(i))) {
				if (places[i] instanceof Property) {
					if (i == selectedPlace) {
						selectedPlace = -1;
					} else {
						selectedPlace = i;
					}
					return;
				}
				break;
			} else {
				++i;
			}
		}
		int id = 0;
		for (final Button button : buttons) {
			if (button.isReallyVisible(this) && getModule().inRect(x, y, getButtonRect(id++))) {
				if (button.isReallyEnabled(this)) {
					button.onClick();
				}
				return;
			}
		}
		if (currentCard != null && cardScale == 1.0f) {
			final int[] rect = { 150, 44, 142, 80 };
			if (getModule().inRect(x, y, rect)) {
				removeCard();
			}
		}
		selectedPlace = -1;
	}

	public void loadTexture(final GuiMinecart gui, final int number) {
		ResourceHelper.bindResource(ArcadeMonopoly.textures[number]);
		//TODO
//		GlStateManager._color4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public Place[] getPlaces() {
		return places;
	}

	private void removeCard() {
		currentCard.doStuff(this, getCurrentPiece());
		currentCard = null;
		endable = true;
		if (diceCount == 0 && useAI()) {
			end.onClick();
		}
	}

	static {
		ArcadeMonopoly.textures = new String[5];
		for (int i = 0; i < ArcadeMonopoly.textures.length; ++i) {
			ArcadeMonopoly.textures[i] = "/gui/monopoly_" + i + ".png";
		}
	}
}
