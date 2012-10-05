/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 10/4/12 8:12 PM
 */

package com.p000ison.dev.simpleclans2.util.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * A ChatBlock is used to send tables with rows and columns to a  @see{org.bukkit.command.CommandSender}.
 */
public class ChatBlock {

    private static final int COLUMN_SPACING = 12;
    private static final int MAX_LINE_LENGTH = 320;

    private static String prefix = null;

    private List<StringBuilder[]> rows = new ArrayList<StringBuilder[]>();
    private Align[] alignment = null;
    private double[] columnSizes = null;

    /**
     * Gets the align of the column.
     *
     * @param column The column.
     * @return The align of this column.
     */
    private Align getAlign(int column)
    {
        if (alignment == null) {
            return null;
        }

        return alignment[column];
    }

    /**
     * Gets the max-width of a column
     *
     * @param col The index of the column.
     * @return The width
     */
    public double getMaxWidth(double col)
    {
        double maxWidth = 0;

        for (StringBuilder[] row : rows) {
            if (col < row.length) {
                maxWidth = Math.max(maxWidth, msgLength(row[(int) col]));
            }
        }

        return maxWidth;
    }

    /**
     * Generates the sizes of each column.
     */
    private void generateColumnSizes()
    {
        if (columnSizes == null) {
            // generate columns sizes

            int col_count = rows.get(0).length;

            columnSizes = new double[col_count];

            for (int i = 0; i < col_count; i++) {
                // add custom column spacing if specified

                columnSizes[i] = getMaxWidth(i) + COLUMN_SPACING;
            }
        }
    }

    /**
     * Ads a row to this block.
     *
     * @param sections An array of the sections of this row.
     */
    public void addRow(String... sections)
    {
        StringBuilder[] builderSections = new StringBuilder[sections.length];

        for (int i = 0; i < sections.length; i++) {
            builderSections[i] = new StringBuilder(sections[i]);
        }

        rows.add(builderSections);
    }

    /**
     * Sends the complete block to a @see{org.bukkit.command.CommandSender}.
     *
     * @param sender The retriever.
     * @return Weather is was successfully.
     * @throws IllegalArgumentException If there are no rows added, or the alignment is miss-setted.
     */
    public boolean sendBlock(CommandSender sender)
    {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("No rows added!");
        }

        generateColumnSizes();

        int firstRowLength = rows.get(0).length;

        if (alignment.length != firstRowLength) {
            throw new IllegalArgumentException("The number of alignments must equal the number of sections!");
        }

        if (columnSizes.length != firstRowLength) {
            throw new IllegalArgumentException("The number of alignments must equal the number of sections!");
        }

        for (StringBuilder[] row : rows) {

            StringBuilder finalRow = new StringBuilder();

            for (int column = 0; column < row.length; column++) {
                StringBuilder section = row[column];
                double columnSize = columnSizes[column];
                Align align = getAlign(column);

                if (align == null) {
                    return false;
                }

                double sectionLength = msgLength(section);

                switch (align) {
                    case RIGHT:
                        if (sectionLength > columnSize) {
                            cropRight(section, columnSize);
                        } else if (sectionLength < columnSize) {
                            padLeft(section, columnSize);
                        }
                        break;
                    case LEFT:
                        if (sectionLength > columnSize) {
                            cropRight(section, columnSize);
                        } else if (sectionLength < columnSize) {
                            padRight(section, columnSize);
                        }
                    case CENTER:
                        if (sectionLength > columnSize) {
                            cropRight(section, columnSize);
                        } else if (sectionLength < columnSize) {
                            center(section, columnSize);
                        }
                        break;
                }

                finalRow.append(section);
            }

            cropRight(finalRow, MAX_LINE_LENGTH);

            sender.sendMessage(finalRow.toString());
        }
        return true;
    }

    /**
     * Sets the alignment of each row.
     * <p/>
     * <p>Example:</p><br>
     * <p/>
     * block.setAlignment(Align.LEFT, Align.RIGHT);
     * <p/>
     * <p>This will produce something like this:</p>
     * <p/>
     * |TestString    |    TextString|
     *
     * @param alignment An array of alignments
     * @see com.p000ison.dev.simpleclans2.util.chat.Align
     */
    public void setAlignment(Align... alignment)
    {
        this.alignment = alignment;
    }

    /**
     * Crops the string right in the @see{java.lang.StringBuilder}
     *
     * @param text   The message to crop right.
     * @param length The lenght it of the section it should be crop right.
     */
    public static void cropRight(StringBuilder text, double length)
    {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException("The text can not be null or empty!");
        }

        while (msgLength(text) >= length) {
            text.deleteCharAt(text.length() - 1);
        }
    }


    /**
     * Crops the string left in the @see{java.lang.StringBuilder}
     *
     * @param text   The message to crop left.
     * @param length The lenght it of the section it should be crop left.
     */
    public static void cropLeft(StringBuilder text, double length)
    {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException("The text can not be null or empty!");
        }

        while (msgLength(text) >= length) {
            text.deleteCharAt(0);
        }
    }

    /**
     * Pads the string right in the @see{java.lang.StringBuilder}
     *
     * @param text   The message to pad right.
     * @param length The lenght it of the section it should be pad right.
     */
    public static void padRight(StringBuilder text, double length)
    {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException("The text can not be null or empty!");
        }

        double msgLenght = msgLength(text);

        if (msgLenght > length) {
            return;
        }

        while (msgLenght < length) {
            msgLenght += 4;
            text.append(' ');
        }
    }

    /**
     * Pads the string left in the @see{java.lang.StringBuilder}
     *
     * @param text   The message to pad left.
     * @param length The lenght it of the section it should be pad left.
     */
    public static void padLeft(StringBuilder text, double length)
    {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException("The text can not be null or empty!");
        }

        double msgLength = msgLength(text);

        if (msgLength > length) {
            return;
        }

        StringBuilder empty = new StringBuilder();

        while (msgLength < length) {
            msgLength += 4;
            empty.append(' ');
        }

        text.insert(0, empty);
    }

    /**
     * Centers the string in the @see{java.lang.StringBuilder}
     *
     * @param text       The message to center.
     * @param lineLength The lenght it of the section it should be centered in.
     */
    public static void center(StringBuilder text, double lineLength)
    {
        double length = msgLength(text);
        double diff = lineLength - length;

        // if too big for line return it as is

        if (diff < 0) {
            return;
        }

        double sideSpace = diff / 2;

        // pad the left with space

        padLeft(text, length + sideSpace);

        // pad the right with space

        padRight(text, length + sideSpace + sideSpace);
    }

    /**
     * Returns the length of a string.
     *
     * @param text The text to check.
     * @return The length of the string.
     */
    public static int msgLength(StringBuilder text)
    {
        int length = 0;

        // Loop through all the characters, skipping any color characters and their following color codes

        int textLength = text.length() - 1;

        for (int x = 0; x < text.length(); x++) {
            char currentChar = text.charAt(x);

            //ignore colors, but only if there is enought space. A § at the end of the line will not be recognized
            if (currentChar == '\u00a7' && x < textLength) {
                char nextChar = text.charAt(x + 1);
                if (ChatColor.getByChar(nextChar) == null) {
                    continue;
                }
            }

            int len = charLength(currentChar);
            if (len > 0) {
                length += len;
            } else {
                x++;
            }
        }
        return length;
    }

    /**
     * Returns the length of a char
     *
     * @param character The character to check
     * @return The lenght of the char
     */
    public static int charLength(char character)
    {
        if ("i.:,;|!".indexOf(character) != -1) {
            return 2;
        } else if ("l'".indexOf(character) != -1) {
            return 3;
        } else if ("tI[]".indexOf(character) != -1) {
            return 4;
        } else if ("fk{}<>\"*()".indexOf(character) != -1) {
            return 5;
        } else if ("abcdeghjmnopqrsuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ1234567890\\/#?$%-=_+&^".indexOf(character) != -1) {
            return 6;
        } else if ("@~".indexOf(character) != -1) {
            return 7;
        } else if (character == ' ') {
            return 4;
        } else {
            return -1;
        }
    }

    /**
     * Sets the prefix for messages
     *
     * @param prefix The prefix to set or <strong>null</strong> if you do not like one
     */
    public static void setPrefix(String prefix)
    {
        ChatBlock.prefix = prefix;
    }

    /**
     * Convert color hex values
     *
     * @param msg The message to colorize
     * @return The colored string
     */
    public static String parseColors(String msg)
    {
        return msg.replace("&", "\u00a7");
    }

}

