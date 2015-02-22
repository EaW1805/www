package com.eaw1805.www.controllers.remote;

import com.eaw1805.data.model.Engine;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;

import java.util.List;
import java.util.Random;

/**
 * Offline engine used for calculating the supply lines.
 */
public class OfflineEngine
        implements Engine {

    /**
     * The game object fetched from the database.
     */
    private transient Game game;

    /**
     * The random generator.
     */
    private final transient Random randomGen;

    /**
     * List of alive nations.
     */
    private final List<Nation> aliveNations;


    public OfflineEngine(final Game thisGame, final List<Nation> lstNations) {
        game = thisGame;
        aliveNations = lstNations;
        randomGen = new Random();
        randomGen.setSeed(game.getGameId() * 1000);
    }

    /**
     * Game object of the turn processed.
     *
     * @return the game object.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Get the random number generator.
     *
     * @return the random number generator.
     */
    public Random getRandomGen() {
        return randomGen;
    }

    /**
     * Get list of alive nations.
     *
     * @return a list of alive nations.
     */
    public List<Nation> getAliveNations() {
        return aliveNations;
    }

    /**
     * Engine name.
     *
     * @return the name of the engine.
     */
    @Override
    public String getName() {
        return "OfflineEngine";
    }

}
