function subtract(val1, val2) {
  if (!angular.isNumber(val1) || !angular.isNumber(val2)) return;
  return val1 - val2;
}

/**
 * Determine whether an object is null/undefined. In the
 * event of a String, ensure the length > 0.
 *
 * @param object
 * @returns {boolean}
 */
function hasValue(object) {
    if (object !== null && angular.isDefined(object)) {
        if (angular.isString(object)) {
            return !_.isEmpty(object);
        }
    } else {
        return false;
    }
}