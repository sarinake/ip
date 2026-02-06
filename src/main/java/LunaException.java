public class LunaException extends Exception {
    public LunaException(String message) {
        super(message);
    }
}

/* Possible errors:
- User types something completely random
- The description, /from, or /to parts of a task are empty
- IGNORE: Creating an event where the start date is after the end date
- IGNORE: Creating deadline/event where the date or time doesn't make sense (e.g. Jan 32 or 25pm)
- Marking/unmarking a task that's not marked/unmark, or a task that doesn't exist (e.g. the number DNE/non-positive/too large/wrong datatype)
 */
