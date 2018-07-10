package net.runelite.client.plugins.raids;

import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import java.awt.Color;

import javax.inject.Inject;
import java.awt.*;

public class RaidsCombatOverlay extends Overlay{
    @Inject
    private Client client;

    @Inject
    private RaidsPlugin plugin;

    @Inject
    private RaidsConfig config;

    private final PanelComponent panel = new PanelComponent();

    @Setter
    private boolean overlayShown = false;

    @Inject
    public RaidsCombatOverlay()
    {
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.LOW);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!plugin.isInRaidChambers() || !config.averageCombatLevel() || !overlayShown)
        {
            return null;
        }

        double averageCB = 0;
        int totalCombat = 0;
        int playerCount = 0;

        for (Player player : client.getPlayers())
        {
            if (player == null || player.getName() == null)
            {
                continue;
            }
            playerCount++;
            totalCombat += player.getCombatLevel();

            averageCB = ((double)totalCombat) / playerCount;
        }

        Color textColor = Color.WHITE;

        if(averageCB < 115)
            textColor = Color.RED;

        panel.getChildren().clear();
        panel.getChildren().add(LineComponent.builder()
                .left("AVG Combat:")
                .right(String.format("%.1f", averageCB))
                .rightColor(textColor)
                .build());

        panel.getChildren().add(LineComponent.builder()
                .left("Total Combat:")
                .right(totalCombat+"")
                .build());

        panel.getChildren().add(LineComponent.builder()
                .left("Players:")
                .right(playerCount + "")
                .build());

        return panel.render(graphics);
    }
}