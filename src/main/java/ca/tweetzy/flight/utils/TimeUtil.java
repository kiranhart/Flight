/*
 * Flight
 * Copyright 2023 Kiran Hart
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

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@UtilityClass
public final class TimeUtil {

    public String convertToReadableDate(long timeInMilliseconds, final String format) {
        final Date date = new Date(timeInMilliseconds);
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public String convertToReadableDate(long timeInMilliseconds) {
       return convertToReadableDate(timeInMilliseconds, "MMMM/dd/yyyy - hh:mm a");
    }

    public Map<TimeUnit, Long> getTimeByLong(final long milliseconds) {
        final Map<TimeUnit, Long> times = new HashMap<>();

        times.put(TimeUnit.DAYS, TimeUnit.SECONDS.toDays(milliseconds));
        times.put(TimeUnit.HOURS, TimeUnit.SECONDS.toHours(milliseconds) - (TimeUnit.SECONDS.toDays(milliseconds) * 24L));
        times.put(TimeUnit.MINUTES, TimeUnit.SECONDS.toMinutes(milliseconds) - (TimeUnit.SECONDS.toHours(milliseconds) * 60));
        times.put(TimeUnit.SECONDS, TimeUnit.SECONDS.toSeconds(milliseconds) - (TimeUnit.SECONDS.toMinutes(milliseconds) * 60));

        return times;
    }
}
