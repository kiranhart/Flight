/*
 * Flight
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.flight.utils;


import com.google.common.base.Strings;

/**
 * Date Created: April 10 2022
 * Time Created: 12:20 p.m.
 *
 * @author Kiran Hart
 * @see <a href="https://www.spigotmc.org/threads/progress-bars-and-percentages.276020/"></a>
 */
public final class ProgressBar {

    /**
     * It takes in a current value, a max value, the total number of bars, a symbol, a color for the completed bars, and a color for the not completed bars, and returns a string with the
     * completed bars colored with the completed color and the not completed bars colored with the not completed color
     *
     * @param current           The current value
     * @param max               The maximum value of the bar.
     * @param totalBars         The total amount of bars you want to display.
     * @param symbol            The symbol to use for the bar.
     * @param completedColor    The color of the completed part of the bar.
     * @param notCompletedColor The color of the bar that hasn't been completed yet.
     *
     * @return A string of a certain length, with a certain amount of a certain character, with a certain color.
     */
    public static String make(final int current, int max, int totalBars, char symbol, String completedColor, String notCompletedColor) {
        final float percent = (float) current / max;
        final int bars = (int) (totalBars * percent);
        return Common.colorize(completedColor + Strings.repeat(String.valueOf(symbol), bars)) + Common.colorize(notCompletedColor + Strings.repeat(String.valueOf(symbol), totalBars - bars));
    }

    /**
     * It returns a string that represents a progress bar
     *
     * @param current   The current value
     * @param max       The maximum value of the bar.
     * @param totalBars The total number of bars you want to display.
     * @param symbol    The symbol to use for the progress bar.
     *
     * @return A string
     */
    public static String make(final int current, int max, int totalBars, char symbol) {
        return make(current, max, totalBars, symbol, "&e", "&7");
    }

    /**
     * It takes the current value, the max value, the total number of bars, the character to use for the bars, the color of the filled bars, and the color of the empty bars.
     *
     * @param current   The current value
     * @param max       The maximum value of the bar.
     * @param totalBars The total number of bars to display.
     *
     * @return A string
     */
    public static String make(final int current, int max, int totalBars) {
        return make(current, max, totalBars, '|', "&e", "&7");
    }
}
