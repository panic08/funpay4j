/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.funpay4j.util;

import lombok.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Util for working with FunPay users
 *
 * @author panic08
 * @since 1.0.1
 */
public class FunPayUserUtil {

    /**
     * Converts a string representation of the user's registration date to a {@link Date} object
     *
     * @param registerDate the date of registration as a string that needs to be converted
     * @return {@link Date} object representing the user's registration date
     */
    public static Date convertRegisterDateStringToDate(@NonNull String registerDate) throws ParseException {
        if (registerDate.startsWith("сегодня")) {
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.forLanguageTag("ru"));

            String time = registerDate.split(", ")[1];
            Date parsedDate = dateFormat.parse(time);

            calendar.set(Calendar.HOUR_OF_DAY, parsedDate.getHours());
            calendar.set(Calendar.MINUTE, parsedDate.getMinutes());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            return calendar.getTime();

        } else if (registerDate.startsWith("вчера")) {
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.forLanguageTag("ru"));

            String time = registerDate.split(", ")[1];
            Date parsedDate = dateFormat.parse(time);

            calendar.add(Calendar.DAY_OF_MONTH, -1);
            calendar.set(Calendar.HOUR_OF_DAY, parsedDate.getHours());
            calendar.set(Calendar.MINUTE, parsedDate.getMinutes());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            return calendar.getTime();

        } else {
            if (registerDate.contains(",")) {
                Calendar calendar = Calendar.getInstance();

                if (registerDate.split(", ")[0].matches(".*\\d{4}.*")) {
                    //if the row contains a year

                    SimpleDateFormat dateFormatWithYear = new SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.forLanguageTag("ru"));

                    Date parsedDate = dateFormatWithYear.parse(registerDate);

                    calendar.setTime(parsedDate);
                } else {
                    SimpleDateFormat dateFormatWithoutYear = new SimpleDateFormat("d MMMM, HH:mm", Locale.forLanguageTag("ru"));

                    Date parsedDate = dateFormatWithoutYear.parse(registerDate);

                    calendar.setTime(parsedDate);
                    calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                }

                return calendar.getTime();
            }

            return null;
        }
    }

    /**
     * Converts a string representation of the user's last seen date to a {@link Date} object
     *
     * @param lastSeenAt the date of last seen as a string that needs to be converted
     * @return {@link Date} object representing the user's last seen date
     */
    public static Date convertLastSeenAtStringToDate(@NonNull String lastSeenAt) throws ParseException {
        //delete the “(X days/weeks/years ago)” part
        lastSeenAt = lastSeenAt.replaceFirst("\\(.*\\)", "").trim();

        if (lastSeenAt.contains("сегодня")) {
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.forLanguageTag("ru"));
            String time = lastSeenAt.split(" ")[3];

            Date parsedDate = dateFormat.parse(time);

            calendar.set(Calendar.HOUR_OF_DAY, parsedDate.getHours());
            calendar.set(Calendar.MINUTE, parsedDate.getMinutes());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            return calendar.getTime();

        } else if (lastSeenAt.contains("вчера")) {
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.forLanguageTag("ru"));
            String time = lastSeenAt.split(" ")[3];

            Date parsedDate = dateFormat.parse(time);

            calendar.add(Calendar.DAY_OF_MONTH, -1);
            calendar.set(Calendar.HOUR_OF_DAY, parsedDate.getHours());
            calendar.set(Calendar.MINUTE, parsedDate.getMinutes());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            return calendar.getTime();

        } else if (lastSeenAt.matches(".*\\d{1,2} .* в .*")) {
            Calendar calendar = Calendar.getInstance();

            if (lastSeenAt.matches(".*\\d{4}.*")) {
                //if the row contains a year

                SimpleDateFormat dateFormatWithYear = new SimpleDateFormat("Был d MMMM yyyy 'в' HH:mm", Locale.forLanguageTag("ru"));

                Date parsedDate = dateFormatWithYear.parse(lastSeenAt);

                calendar.setTime(parsedDate);
            } else {
                SimpleDateFormat dateFormatWithoutYear = new SimpleDateFormat("Был d MMMM 'в' HH:mm", Locale.forLanguageTag("ru"));

                Date parsedDate = dateFormatWithoutYear.parse(lastSeenAt);

                calendar.setTime(parsedDate);
                calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            }

            return calendar.getTime();
        }

        return null;
    }



}
