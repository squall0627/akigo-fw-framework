package com.akigo.core.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class DateTimes {
    private DateTimes() {
    }

    /**
     * 現在のシステム日付を返却します。
     *
     * @return LocalDate 現在日付
     */
    public static LocalDate currentDate() {
        return LocalDate.now();
    }

    /**
     * 現在のシステム日時を返却します。
     *
     * @return LocalDateTime 現在日時
     */
    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 現在のシステム時間を返却します。
     *
     * @return LocalTime 現在時間
     */
    public static LocalTime currentTime() {
        return LocalTime.now();
    }

    /**
     * 文字列の日付をLocalDate型へ変換し返却します。
     *
     * @param date 文字列日付 「2007-12-03」など
     * @return LocalDate 日付
     */
    public static Optional<LocalDate> toDateFrom(String date) {
        if (date == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalDate.parse(date));
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException(date + "の解析中にエラーが発生しました", date, 0);
        }
    }

    /**
     * 文字列の日時をLocalDateTime型へ変換し返却します。
     *
     * @param dateTime 文字列日時 「2007-12-03T10:15:30」など
     * @return LocalDateTime 日時
     */
    public static Optional<LocalDateTime> toDateTimeFrom(String dateTime) {
        if (dateTime == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalDateTime.parse(dateTime));
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException(dateTime + "の解析中にエラーが発生しました", dateTime, 0);
        }
    }

    /**
     * 文字列の時間をLocalTime型へ変換し返却します。
     *
     * @param time 文字列時間 「10:15:30」など
     * @return LocalTime 時間
     */
    public static Optional<LocalTime> toTimeFrom(String time) {
        if (time == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalTime.parse(time));
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException(time + "の解析中にエラーが発生しました", time, 0);
        }
    }

    /**
     * 特殊な形式の文字列の日付をLocalDate型へ変換し返却します。
     *
     * @param date   文字列日付 「2007-12-03」
     * @param format 日付のフォーマット 「yyyy-MM-dd」など
     * @return LocalDate 日付
     */
    public static Optional<LocalDate> toDateFrom(String date, String format) {
        if (date == null) {
            return Optional.empty();
        }
        if (isValidFormat(date, format)) {
            return Optional.of(LocalDate.parse(date, DateTimeFormatter.ofPattern(format)));
        }
        return toDateFrom(date);
    }

    /**
     * 特殊な形式の文字列の日時をLocalDateTime型へ変換し返却します。
     *
     * @param dateTime 文字列日時　「2007-12-03 10:15:30」など
     * @param format   日時のフォーマット 「yyyy-MM-dd hh:mm:ss」など
     * @return LocalDateTime 日時
     */
    public static Optional<LocalDateTime> toDateTimeFrom(String dateTime, String format) {
        if (dateTime == null) {
            return Optional.empty();
        }
        if (isValidFormat(dateTime, format)) {
            return Optional.of(LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(format)));
        }
        return toDateTimeFrom(dateTime);
    }

    /**
     * 特殊な形式の文字列の時間をLocalTimeへ変換し返却します。
     *
     * @param time   文字列時間 「10:15:30」など
     * @param format 時間のフォーマット「hh:mm:ss」など
     * @return LocalTime 時間
     */
    public static Optional<LocalTime> toTimeFrom(String time, String format) {
        if (time == null) {
            return Optional.empty();
        }
        if (isValidFormat(time, format)) {
            return Optional.of(LocalTime.parse(time, DateTimeFormatter.ofPattern(format)));
        }
        return toTimeFrom(time);
    }

    /**
     * LocalDateの日付を文字列へ変換し返却します。
     *
     * @param date 日付
     * @return String 文字列日付 「2007-12-03」など
     */
    public static Optional<String> toStringFrom(LocalDate date) {
        if (date == null) {
            return Optional.empty();
        }
        return Optional.of(date.toString());
    }

    /**
     * LocalDateTimeの日時を文字列へ変換し返却します。
     *
     * @param dateTime 日時
     * @return String 文字列日時 「"2016-04-26T23:30:30"」など
     */
    public static Optional<String> toStringFrom(LocalDateTime dateTime) {
        if (dateTime == null) {
            return Optional.empty();
        }
        return Optional.of(dateTime.toString());
    }

    /**
     * LocalTimeの時間を文字列へ変換し返却します。
     *
     * @param time 時間
     * @return String 文字列時間 「10:15:30」など
     */
    public static Optional<String> toStringFrom(LocalTime time) {
        if (time == null) {
            return Optional.empty();
        }
        return Optional.of(time.toString());
    }

    /**
     * LocalDateの日付を和暦へ変換し返却します。
     *
     * @param date 日付
     * @return String 和暦 例：平成28年4月1日
     */
    public static Optional<String> toJapaneseString(LocalDate date) {
        if (date == null) {
            return Optional.empty();
        }
        JapaneseDate jpdate = JapaneseDate.from(date);
        String wareki = jpdate.format(DateTimeFormatter.ofPattern("Gy年M月d日"));
        return Optional.of(wareki);
    }

    /**
     * LocalDateの日付を年号がアルファベットの和暦へ変換し返却します。
     *
     * @param date 日付
     * @return String 和暦 例：H28/4/1
     */
    public static Optional<String> toJapaneseShortString(LocalDate date) {
        if (date == null) {
            return Optional.empty();
        }
        JapaneseDate jpdate = JapaneseDate.from(date);
        String wareki = jpdate.format(DateTimeFormatter.ofPattern("GGGGGy/M/d"));
        return Optional.of(wareki);
    }

    /**
     * LocalDateTimeの日時を和暦へ変換し返却します。
     *
     * @param dateTime 日時
     * @return String 和暦 例：平成28年4月1日午前0時0分0秒
     */
    public static Optional<String> toJapaneseString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return Optional.empty();
        }
        JapaneseDate japanDate = JapaneseDate.from(dateTime);
        String wareki = japanDate.format(DateTimeFormatter.ofPattern("GGGGy年M月d日"));
        String time = dateTime.format(DateTimeFormatter.ofPattern("ah時m分s秒"));
        return Optional.of(new StringBuilder().append(wareki).append(time).toString());
    }

    /**
     * LocalDateTimeの日時を年号がアルファベットの和暦へ変換し返却します。
     *
     * @param dateTime 日時
     * @return String 和暦 例：H28/4/1 00:00:00
     */
    public static Optional<String> toJapaneseShortString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return Optional.empty();
        }
        JapaneseDate japanDate = JapaneseDate.from(dateTime);
        String wareki = japanDate.format(DateTimeFormatter.ofPattern("GGGGGy/M/d"));
        String blank = " ";
        String time = dateTime.format(DateTimeFormatter.ofPattern("H:m:s"));
        return Optional.of(new StringBuilder().append(wareki).append(blank).append(time).toString());
    }

    /**
     * LocalTimeの時間を時分秒表記へ変換し返却します。
     *
     * @param time 時間
     * @return String 和暦 例：午前0時0分0秒
     */
    public static Optional<String> toJapaneseString(LocalTime time) {
        if (time == null) {
            return Optional.empty();
        }
        return Optional.of(time.format(DateTimeFormatter.ofPattern("ah時m分s秒")));
    }

    /**
     * 指定されたフォーマットで変換が可能か判定します。
     *
     * @param dateTime 文字列日時
     * @param format   フォーマット
     * @return boolean dateのparseに成功した場合、true、引数のどれかがnullの場合はfalse、dateのparseに失敗した場合は例外がスローされます。
     */
    public static boolean isValidFormat(String dateTime, String format) {
        if (dateTime == null || format == null) {
            return false;
        }
        try {
            DateTimeFormatter f = DateTimeFormatter.ofPattern(format);
            f.parse(dateTime);
            return true;
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException(dateTime + "の解析中にエラーが発生しました", dateTime, 0);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(format + "は不正な引数です");
        }
    }

    /**
     * 対象のListから最も新しい日付を返却します。
     *
     * @param dates 複数の日付
     * @return LocalDate 最も新しい日付
     */
    public static Optional<LocalDate> newestDate(List<LocalDate> dates) {
        if (dates == null || dates.isEmpty()) {
            return Optional.empty();
        }
        return dates.stream().max(Comparator.naturalOrder());
    }

    /**
     * 対象のListから最も新しい日時を返却します。
     *
     * @param dateTimes 複数の日時
     * @return LocalDateTime 最も新しい日時
     */
    public static Optional<LocalDateTime> newestDateTime(List<LocalDateTime> dateTimes) {
        if (dateTimes == null || dateTimes.isEmpty()) {
            return Optional.empty();
        }
        return dateTimes.stream().max(Comparator.naturalOrder());
    }

    /**
     * 対象のListから最も新しい時間を返却します。
     *
     * @param times 複数の時間
     * @return LocalTime 最も新しい時間
     */
    public static Optional<LocalTime> newestTime(List<LocalTime> times) {
        if (times == null || times.isEmpty()) {
            return Optional.empty();
        }
        return times.stream().max(Comparator.naturalOrder());
    }

    /**
     * 対象のListから最も古い日付を返却します。
     *
     * @param dates 複数の日付
     * @return LocalDate 最も古い日付
     */
    public static Optional<LocalDate> oldestDate(List<LocalDate> dates) {
        if (dates == null || dates.isEmpty()) {
            return Optional.empty();
        }
        return dates.stream().min(Comparator.naturalOrder());
    }

    /**
     * 対象のListから最も古い日時を返却します。
     *
     * @param dateTimes 複数の日時
     * @return LocalDateTime 最も古い日時
     */
    public static Optional<LocalDateTime> oldestDateTime(List<LocalDateTime> dateTimes) {
        if (dateTimes == null || dateTimes.isEmpty()) {
            return Optional.empty();
        }
        return dateTimes.stream().min(Comparator.naturalOrder());
    }

    /**
     * 対象のListから最も古い時間を返却します。
     *
     * @param times 複数の時間
     * @return LocalTime 最も古い時間
     */
    public static Optional<LocalTime> oldestTime(List<LocalTime> times) {
        if (times == null || times.isEmpty()) {
            return Optional.empty();
        }
        return times.stream().min(Comparator.naturalOrder());
    }

    /**
     * 対象の日付が365日中の何日目かを返却します。
     *
     * @param date 日時
     * @return Integer xxx日目
     */
    public static OptionalInt dayInYear(LocalDate date) {
        if (date == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(date.getDayOfYear());
    }

    /**
     * 対象の日付が365日中の何日目かを返却します。
     *
     * @param dateTime 日時
     * @return Integer xxx日目
     */
    public static OptionalInt dayInYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(dateTime.getDayOfYear());
    }

    /**
     * 第一引数の日付が第二引数の日付と第三引数の日付の間の日付か判定します。
     *
     * @param date 対象の日付
     * @param from 未来日
     * @param to   過去日
     * @return boolean dateがfrom,toの間の日付ならtrue、引数のどれかがnullなら例外をスロー、それ以外はfalse
     */
    public static boolean between(LocalDate date, LocalDate from, LocalDate to) {
        if (date == null || from == null || to == null) {
            throw new NullPointerException();
        }
        int difffromDate = date.compareTo(from);
        int difftoDate = date.compareTo(to);
        if (difffromDate <= 0 && difftoDate >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 第一引数の日時が第二引数の日時と第三引数の日時の間の日時か判定します。
     *
     * @param dateTime 対象の日時
     * @param from     未来日時
     * @param to       過去日時
     * @return boolean dateTimeがfrom,toの間の日時ならtrue、引数のどれかがnullなら例外をスロー、それ以外はfalse
     */
    public static boolean between(LocalDateTime dateTime, LocalDateTime from, LocalDateTime to) {
        if (dateTime == null || from == null || to == null) {
            throw new NullPointerException();
        }
        int diffFromDateTime = dateTime.compareTo(from);
        int diffToDateTime = dateTime.compareTo(to);
        if (diffFromDateTime <= 0 && diffToDateTime >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 第一引数の時間が第二引数の時間と第三引数の時間の間の時間か判定します。
     *
     * @param time 対象の時間
     * @param from 未来時間
     * @param to   過去時間
     * @return boolean timeがfrom,toの間の時間ならtrue、引数のどれかがnullなら例外をスロー、それ以外はfalse
     */
    public static boolean between(LocalTime time, LocalTime from, LocalTime to) {
        if (time == null || from == null || to == null) {
            throw new NullPointerException();
        }
        int diffFromTime = time.compareTo(from);
        int diffToTime = time.compareTo(to);
        if (diffFromTime <= 0 && diffToTime >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 対象の日付が平日か判定します。
     *
     * @param date 対象の日付
     * @return boolean dateが平日ならtrue、nullなら例外をスロー、それ以外はfalse
     */
    public static boolean isWeekday(LocalDate date) {
        if (date == null) {
            throw new NullPointerException();
        }
        switch (date.getDayOfWeek()) {
            case SUNDAY:
            case SATURDAY:
                return false;
            default:
                return true;
        }
    }

    /**
     * 対象の日時が平日か判定します。
     *
     * @param dateTime 対象の日時
     * @return boolean dateTimeが平日ならtrue、nullなら例外をスロー、それ以外はfalse
     */
    public static boolean isWeekday(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new NullPointerException();
        }
        switch (dateTime.getDayOfWeek()) {
            case SUNDAY:
            case SATURDAY:
                return false;
            default:
                return true;
        }
    }

    /**
     * 対象の日付が休日か判定します。
     *
     * @param date 対象の日付
     * @return boolean dateが休日ならtrue、nullなら例外をスロー、それ以外はfalse
     */
    public static boolean isHoliday(LocalDate date) {
        if (date == null) {
            throw new NullPointerException();
        }
        switch (date.getDayOfWeek()) {
            case SUNDAY:
            case SATURDAY:
                return true;
            default:
                return false;
        }
    }

    /**
     * 対象の日付が休日か判定します。
     *
     * @param dateTime 対象の日付
     * @return boolean dateTimeが休日ならtrue、nullなら例外をスロー、それ以外はfalse
     */
    public static boolean isHoliday(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new NullPointerException();
        }
        switch (dateTime.getDayOfWeek()) {
            case SUNDAY:
            case SATURDAY:
                return true;
            default:
                return false;
        }
    }

    /**
     * 第一引数の日付が第二引数の日付より前の日付か判定します。
     *
     * @param targetDate  対象の日付
     * @param compareDate 比較する日付
     * @return boolean targetDateがcompareDateより前の日付ならtrue、引数のどれかがnullなら例外をスロー、それ以外はfalse
     */
    public static boolean isBefore(LocalDate targetDate, LocalDate compareDate) {
        if (targetDate == null || compareDate == null) {
            throw new NullPointerException();
        }
        return targetDate.isBefore(compareDate);
    }

    /**
     * 第一引数の日時が第二引数の日時より前の日時か判定します。
     *
     * @param targetDateTime  対象の日時
     * @param compareDateTime 比較する日時
     * @return boolean targetDateTimeがcompareDateTimeより前の日時ならtrue、引数のどれかがnullなら例外をスロー、それ以外はfalse
     */
    public static boolean isBefore(LocalDateTime targetDateTime, LocalDateTime compareDateTime) {
        if (targetDateTime == null || compareDateTime == null) {
            throw new NullPointerException();
        }
        return targetDateTime.isBefore(compareDateTime);
    }

    /**
     * 第一引数の時間が第二引数の時間より前の時間か判定します。
     *
     * @param targetTime  対象の時間
     * @param compareTime 比較する時間
     * @return boolean targetTimeがcompareTimeより前の時間ならtrue、引数のどれかがnullなら例外をスロー、それ以外はfalse
     */
    public static boolean isBefore(LocalTime targetTime, LocalTime compareTime) {
        if (targetTime == null || compareTime == null) {
            throw new NullPointerException();
        }
        return targetTime.isBefore(compareTime);
    }

    /**
     * 第一引数の日付が第二引数の日付より後の日付か判定します。
     *
     * @param targetDate  対象の日付
     * @param compareDate 比較する日付
     * @return boolean targetDateがcompareDateより後の日付ならtrue、引数のどれかがnullなら例外をスロー、それ以外はfalse
     */
    public static boolean isAfter(LocalDate targetDate, LocalDate compareDate) {
        if (targetDate == null || compareDate == null) {
            throw new NullPointerException();
        }
        return targetDate.isAfter(compareDate);
    }

    /**
     * 第一引数の日時が第二引数の日時より後の日時か判定します。
     *
     * @param targetDateTime  対象の日時
     * @param compareDateTime 比較する日時
     * @return boolean targetDateTimeがcompareDateTimeより後の日時ならtrue、引数のどれかがnullなら例外をスロー、それ以外はfalse
     */
    public static boolean isAfter(LocalDateTime targetDateTime, LocalDateTime compareDateTime) {
        if (targetDateTime == null || compareDateTime == null) {
            throw new NullPointerException();
        }
        return targetDateTime.isAfter(compareDateTime);
    }

    /**
     * 第一引数の時間が第二引数の時間より後の時間か判定します。
     *
     * @param targetTime  対象の時間
     * @param compareTime 比較する時間
     * @return boolean targetTimeがcompareTimeより後の時間ならtrue、引数のどれかがnullなら例外をスロー、それ以外はfalse
     */
    public static boolean isAfter(LocalTime targetTime, LocalTime compareTime) {
        if (targetTime == null || compareTime == null) {
            throw new NullPointerException();
        }
        return targetTime.isAfter(compareTime);
    }

    /**
     * 対象の日付がうるう年か判定します。
     *
     * @param targetDate 対象の日付
     * @return boolean targetDateがうるう年ならtrue、nullなら例外をスロー、それ以外はfalse
     */
    public static boolean isLeapYear(LocalDate targetDate) {
        if (targetDate == null) {
            throw new NullPointerException();
        }
        return targetDate.isLeapYear();
    }

    /**
     * 対象の日付がうるう年か判定します。
     *
     * @param targetDateTime 対象の日時
     * @return boolean targetDateTimeがうるう年ならtrue、nullなら例外をスロー、それ以外はfalse
     */
    public static boolean isLeapYear(LocalDateTime targetDateTime) {
        if (targetDateTime == null) {
            throw new NullPointerException();
        }
        return targetDateTime.toLocalDate().isLeapYear();
    }

    /**
     * 現在のシステム時間が午前０時から午前11時59分59秒の間かを判定します。
     *
     * @return boolean 現在のシステム時間が午前０時から午前11時59分59秒の間ならtrue、それ以外はfalse
     */
    public static boolean isMorning() {
        return LocalTime.now().isBefore(LocalTime.NOON);
    }

    /**
     * 任意の時間が午前０時から午前11時59分59秒の間かを判定します。
     *
     * @param time 対象の時間
     * @return boolean timeが午前０時から午前11時59分59秒の間ならtrue、nullなら例外をスロー、それ以外はfalse
     */
    public static boolean isMorning(LocalTime time) {
        if (time == null) {
            throw new NullPointerException();
        }
        return time.isBefore(LocalTime.NOON);
    }

    /**
     * 任意の日時が午前０時から午前11時59分59秒の間かを判定します。
     *
     * @param dateTime 対象の時間
     * @return boolean dateTimeが午前０時から午前11時59分59秒の間ならtrue、nullなら例外をスロー、それ以外はfalse
     */
    public static boolean isMorning(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new NullPointerException();
        }
        return dateTime.isBefore(dateTime.withHour(12));
    }

    /**
     * 現在のシステム時間が午後12時から午後23時59分59秒の間かを判定します。
     *
     * @return boolean 現在のシステム時間が午後12時から午後23時59分59秒の間ならtrue、それ以外はfalse
     */
    public static boolean isAfternoon() {
        LocalTime time = LocalTime.now();
        if (time.isAfter(LocalTime.NOON) || time.equals(LocalTime.NOON)) {
            return true;
        }
        return false;
    }

    /**
     * 任意の時間が午後12時から午後23時59分59秒の間かを判定します。
     *
     * @param time 対象の時間
     * @return boolean timeが午後12時から午後23時59分59秒の間ならtrue、nullなら例外をスロー、それ以外はfalse
     */
    public static boolean isAfternoon(LocalTime time) {
        if (time == null) {
            throw new NullPointerException();
        }
        if (time.isAfter(LocalTime.NOON) || time.equals(LocalTime.NOON)) {
            return true;
        }
        return false;
    }

    /**
     * 任意の日時が午後12時から午後23時59分59秒の間かを判定します。
     *
     * @param dateTime 対象の日時
     * @return boolean dateTimeが午後12時から午後23時59分59秒の間ならtrue、nullなら例外をスロー、それ以外はfalse
     */
    public static boolean isAfternoon(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new NullPointerException();
        }
        if (dateTime.isAfter(dateTime.withHour(12)) || dateTime.isEqual(dateTime.withHour(12))) {
            return true;
        }
        return false;
    }

    /**
     * 指定した2つの日付の間の平日の日数を返却します。
     *
     * @param from 開始日付
     * @param to   終了日付
     * @return Integer 営業日数
     */
    public static OptionalInt getWeekdaysBetween(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            return OptionalInt.empty();
        }
        int weekday = 0;
        LocalDate fromDate = from;
        if (!from.equals(to)) {
            while (!fromDate.equals(to.plusDays(1))) {
                if (DateTimes.isWeekday(fromDate)) {
                    weekday++;
                }
                fromDate = fromDate.plusDays(1);
            }
            return OptionalInt.of(weekday);
        } else if (DateTimes.isWeekday(from)) {
            weekday++;
        }
        return OptionalInt.of(weekday);
    }

    /**
     * 指定した2つの日時の間の平日の日数を返却します。
     *
     * @param from 開始日時
     * @param to   終了日時
     * @return Integer 営業日数
     */
    public static OptionalInt getWeekdaysBetween(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            return OptionalInt.empty();
        }
        int weekday = 0;
        LocalDate fromDate = from.toLocalDate();
        if (!from.equals(to)) {
            while (!fromDate.equals(to.toLocalDate().plusDays(1))) {
                if (DateTimes.isWeekday(fromDate)) {
                    weekday++;
                }
                fromDate = fromDate.plusDays(1);
            }
            return OptionalInt.of(weekday);
        } else if (DateTimes.isWeekday(from)) {
            weekday++;
        }
        return OptionalInt.of(weekday);
    }

    /**
     * 指定した2つの日付の間の休日の日数を返却します。
     *
     * @param from 開始日付
     * @param to   終了日付
     * @return Integer 休日日数
     */
    public static OptionalInt getHolidaysBetween(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            return OptionalInt.empty();
        }
        int holiday = 0;
        LocalDate fromDate = from;
        if (!from.equals(to)) {
            while (!fromDate.equals(to.plusDays(1))) {
                if (DateTimes.isHoliday(fromDate)) {
                    holiday++;
                }
                fromDate = fromDate.plusDays(1);
            }
            return OptionalInt.of(holiday);
        } else if (DateTimes.isHoliday(from)) {
            holiday++;
        }
        return OptionalInt.of(holiday);
    }

    /**
     * 指定した2つの日時の間の休日の日数を返却します。
     *
     * @param from 開始日時
     * @param to   終了日時
     * @return Integer 休日日数
     */
    public static OptionalInt getHolidaysBetween(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            return OptionalInt.empty();
        }
        int holiday = 0;
        LocalDate fromDate = from.toLocalDate();
        if (!from.equals(to)) {
            while (!fromDate.equals(to.toLocalDate().plusDays(1))) {
                if (DateTimes.isHoliday(fromDate)) {
                    holiday++;
                }
                fromDate = fromDate.plusDays(1);
            }
            return OptionalInt.of(holiday);
        } else if (DateTimes.isHoliday(from)) {
            holiday++;
        }
        return OptionalInt.of(holiday);
    }

    /**
     * 受け取った日付からその月の最終日を返却します。
     *
     * @param date 対象の日付
     * @return date 最終日
     */
    public static OptionalInt getLastDayOfMonth(LocalDate date) {
        if (date == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(date.lengthOfMonth());
    }

    /**
     * 受け取った日時からその月の最終日を返却します。
     *
     * @param dateTime 対象の日時
     * @return date 最終日
     */
    public static OptionalInt getLastDayOfMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(dateTime.toLocalDate().lengthOfMonth());
    }

    /**
     * 第一引数の日付が第二引数の日付と同じ日付か判定します。
     *
     * @param targetDate  対象の日付
     * @param compareDate 比較する日付
     * @return boolean targetDateとcompareDateが等しい場合はtrue、引数のどれかがnullなら例外をスロー、それ以外はfalse
     */
    public static boolean equals(LocalDate targetDate, LocalDate compareDate) {
        if (targetDate == null || compareDate == null) {
            throw new NullPointerException();
        }
        return targetDate.equals(compareDate);
    }

    /**
     * 第一引数の日時が第二引数の日時と同じ日時か判定します。
     *
     * @param targetDateTime  対象の日時
     * @param compareDateTime 比較する日時
     * @return boolean targetDateTimeとcompareDateTimeが等しい場合はtrue、引数のどれかがnullなら例外をスロー、それ以外はfalse
     */
    public static boolean equals(LocalDateTime targetDateTime, LocalDateTime compareDateTime) {
        if (targetDateTime == null || compareDateTime == null) {
            throw new NullPointerException();
        }
        return targetDateTime.equals(compareDateTime);
    }

    /**
     * 第一引数の時間が第二引数の時間と同じ時間か判定します。
     *
     * @param targetTime  対象の時間
     * @param compareTime 比較する時間
     * @return boolean targetTimeとcompareTimeが等しい場合はtrue、引数のどれかがnullなら例外をスロー、それ以外はfalse
     */
    public static boolean equals(LocalTime targetTime, LocalTime compareTime) {
        if (targetTime == null || compareTime == null) {
            throw new NullPointerException();
        }
        return targetTime.equals(compareTime);
    }

    /**
     * LocalDate型をDate型へ変換し返却します。
     *
     * @param localDate 日付
     * @return Date型の日付
     */
    public static Date toDateFrom(LocalDate localDate) {
        ZoneId id = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(id);
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    /**
     * LocalTime型をDate型へ変換し返却します。
     *
     * @param localTime LocalTime型時間
     * @return Date型の日付
     */
    public static Date toDateFrom(LocalTime localTime) {
        ZoneId id = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDate.ofEpochDay(0), localTime, id);
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    /**
     * LocalDateTime型をDate型へ変換し返却します。
     *
     * @param localDateTime LocalDateTime型の日時
     * @return Date型の日付
     */
    public static Date toDateFrom(LocalDateTime localDateTime) {
        ZoneId id = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, id);
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    /**
     * Date型をLocalDate型へ変換し返却します。
     *
     * @param date Date型の日付
     * @return LocalDate型の日付
     */
    public static LocalDate toLocalDateFrom(Date date) {
        ZoneId id = ZoneId.systemDefault();
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, id).toLocalDate();
    }

    /**
     * Date型をLocalTime型へ変換し返却します。
     *
     * @param date Date型の日付
     * @return localTime をLocalTime型の時間
     */
    public static LocalTime toLocalTimeFrom(Date date) {
        ZoneId id = ZoneId.systemDefault();
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, id).toLocalTime();
    }

    /**
     * Date型をLocalDateTime型へ変換し返却します。
     *
     * @param date Date型の日付
     * @return Date LocalDateTime型の日付
     */
    public static LocalDateTime toLocalDateTimeFrom(Date date) {
        Instant instant = date.toInstant();
        ZoneId id = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, id);
    }
}
