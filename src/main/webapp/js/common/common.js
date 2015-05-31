/**
 * Check if an object is null or undefined
 * @param object
 * @returns {boolean|*}
 */
function hasValue(object) {
    return object !== null && angular.isDefined(object);
};

/**
 * Check if an object is empty:
 *  - null
 *  - undefined
 *  - empty string, array, object
 *
 * @param object
 * @returns {*}
 */
function isEmpty(object) {
    if (!hasValue(object)) return true;
    if (_.isBoolean(object)) return !object;
    return _.isEmpty(object);
}

/**
 * Copy the properties of one object onto another
 * @param original
 * @param copy
 * @returns {*}
 */
function copyObject(source, destination) {
    if (!angular.isObject(source)) return;
    if (!hasValue(destination)) {
        destination = {};
    }
    for (var key in source) {
        destination[key] = source[key];
    }
    return destination;
}