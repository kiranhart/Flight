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

package ca.tweetzy.flight.files.configuration.comments;

public interface Commentable {

    /**
     * Set a comment to the section or value selected by path.
     * Comment will be indented automatically.
     * Multi-line comments can be provided using \n character.
     *
     * @param path    path of desired section or key
     * @param comment the comment to add, # symbol is not needed
     * @param type    either above (BLOCK) or SIDE
     */
    void setComment(String path, String comment, CommentType type);

    /**
     * Set a block comment above the section or value selected by path.
     * Comment will be indented automatically.
     * Multi-line comments can be provided using \n character.
     *
     * @param path    path of desired section or key
     * @param comment the comment to add, # symbol is not needed
     */
    default void setComment(final String path, final String comment) {
        this.setComment(path, comment, CommentType.BLOCK);
    }

    /**
     * Retrieve the comment of the section or value selected by path.
     *
     * @param path path of desired section or key
     * @param type either above (BLOCK) or SIDE
     *
     * @return the comment of the section or value selected by path,
     *         or null if that path does not have any comment of this type
     */
    String getComment(String path, CommentType type);

    /**
     * Retrieve the block comment of the section or value selected by path.
     *
     * @param path path of desired section or key
     *
     * @return the block comment of the section or value selected by path,
     *         or null if that path does not have any comment of type block
     */
    default String getComment(final String path) {
        return this.getComment(path, CommentType.BLOCK);
    }

}
